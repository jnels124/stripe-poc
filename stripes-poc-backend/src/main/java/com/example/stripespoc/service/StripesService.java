package com.example.stripespoc.service;

import com.example.stripespoc.config.StripeConfig;
import com.example.stripespoc.web.request.AddCardRequest;
import com.example.stripespoc.web.request.CreateCustomerRequest;
import com.example.stripespoc.web.stripes.IdResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@EnableConfigurationProperties({StripeConfig.class})
public class StripesService {
    private final WebClient webClient;
    private final StripeConfig stripeConfig;
    private final ObjectMapper objectMapper;

    public StripesService(WebClient.Builder webClientBuilder, StripeConfig stripeConfig, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(stripeConfig.getBaseUrl())
                                         .filter(ExchangeFilterFunctions.basicAuthentication(stripeConfig.getKey(), ""))
                                         .build();
        this.stripeConfig = stripeConfig;
        this.objectMapper = objectMapper;
    }

    //TODO:// probably want to setup payment intent instead to make sure the card can actually be used in a future payment
    //https://stripe.com/docs/api/payment_methods/attach
    public Mono<String> addCard(AddCardRequest request) {
        return webClient.post()
                        .uri(stripeConfig.getPaymentMethods())
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(toMultiValueMap(request)))
                        .retrieve()
                        .bodyToMono(IdResponse.class)
                        .flatMap(idResponse -> webClient.post()
                                                        .uri(stripeConfig.getPaymentMethods() + stripeConfig.getAttach(), idResponse.getId())
                                                        .accept(MediaType.APPLICATION_JSON)
                                                        .body(BodyInserters.fromFormData("customer", request.getCustomerId()))
                                                        .retrieve()
                                                        .bodyToMono(String.class));
    }

    public Mono<String> listCards(String customerId) {
        return webClient.get()
                        .uri(builder -> builder.path(stripeConfig.getPaymentMethods())
                                               .queryParam("type", "card")
                                               .queryParam("customer", customerId)
                                               .build())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> createCustomer(CreateCustomerRequest request) {
        return webClient.post()
                        .uri(stripeConfig.getCustomers())
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(toMultiValueMap(request)))
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> listCustomers() {
        return webClient.get()
                        .uri(stripeConfig.getCustomers())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class);
    }

    //TODO:// move these to common http util
    @SuppressWarnings("unchecked")
    private MultiValueMap<String, String> toMultiValueMap(Object value) {
        HashMap<String, Object> mapValues = objectMapper.convertValue(value, HashMap.class);
        MultiValueMap<String, String> multiMapValues = new LinkedMultiValueMap<>();
        mapValues.forEach((key, value1) -> multiMapValues.putAll(valueToStrings(key, value1)));
        return multiMapValues;
    }

    private static Map<String, List<String>> valueToStrings(String key, Object value) {
        if (value == null) {
            return Collections.emptyMap();
        }

        if (value instanceof Map) {
            return ((Map<?, ?>) value).entrySet()
                                      .stream()
                                      .filter(entry -> entry.getValue() != null)
                                      .collect(Collectors.toMap(entry -> key + "[" + entry.getKey() + "]", entry -> Collections.singletonList(entry.getValue().toString()))); //TODO:// recursively handle nested maps..Just collect list of maps and flatten.
        }

        if (value instanceof Iterable) {
            return Collections.singletonMap(key, StreamSupport.stream(((Iterable<?>) value).spliterator(), false)
                                                              .filter(Objects::nonNull)
                                                              .map(Object::toString)
                                                              .collect(Collectors.toList()));
        }
        return Collections.singletonMap(key, Collections.singletonList(value.toString()));
    }
}
