package com.example.stripespoc.service;

import com.example.stripespoc.config.StripeConfig;
import com.example.stripespoc.web.request.AddCardRequest;
import com.example.stripespoc.web.request.ChargeRequest;
import com.example.stripespoc.web.request.CreateConnectedAccountRequest;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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

    public Mono<String> addCard(AddCardRequest request) {
        return webClient.post()
                        .uri(stripeConfig.getPaymentMethods())
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(toMultiValueMap(request)))
                        .exchangeToMono(response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                return response.bodyToMono(IdResponse.class)
                                               .flatMap(idResponse -> webClient.post()
                                                                               .uri(stripeConfig.getPaymentMethods() + stripeConfig.getAttach(), idResponse.getId())
                                                                               .accept(MediaType.APPLICATION_JSON)
                                                                               .body(BodyInserters.fromFormData("customer", request.getCustomer()))
                                                                               .retrieve()
                                                                               .bodyToMono(String.class));
                            }
                            else {
                                return response.bodyToMono(String.class);
                            }
                        });
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

    public Mono<String> charge(ChargeRequest request) {
        request.getTransferData().setAmount(BigDecimal.valueOf(request.getAmount()).divide(BigDecimal.valueOf(2), RoundingMode.HALF_EVEN).intValue());
        return webClient.post()
                        .uri(stripeConfig.getPaymentIntents())
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(toMultiValueMap(request)))
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> getChargeDetails(String chargeId) {
        return webClient.get()
                        .uri(stripeConfig.getCharges() + "/{id}", chargeId)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> getCustomerCharges(String customerId) {
        return webClient.get()
                        .uri(builder -> builder.path(stripeConfig.getCharges())
                                               .queryParam("customer", customerId)
                                               .build())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class);
    }

    public Mono<String> createConnectedAccount(CreateConnectedAccountRequest request) {
        return webClient.post()
                        .uri(stripeConfig.getConnectedAccounts())
                        .accept(MediaType.APPLICATION_JSON)
                        .body(BodyInserters.fromFormData(toMultiValueMap(request)))
                        .retrieve()
                        .bodyToMono(IdResponse.class)
                        .map(IdResponse::getId)
                        .flatMap(this::linkAccount);
    }

    public Mono<String> listConnectedAccounts() {
        return webClient.get()
                        .uri(stripeConfig.getConnectedAccounts())
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(String.class);
    }

    //TODO:// account verification if needed
    public Mono<String> linkAccount(String accountId) {
        return Mono.empty();
    }

    //TODO:// move these to common http util
    @SuppressWarnings("unchecked")
    private MultiValueMap<String, String> toMultiValueMap(Object value) {
        HashMap<String, Object> mapValues = objectMapper.convertValue(value, HashMap.class);
        MultiValueMap<String, String> multiMapValues = new LinkedMultiValueMap<>();
        mapValues.entrySet()
                 .stream()
                 .flatMap(StripesService::flatten)
                 .forEach(entry -> multiMapValues.putAll(valueToStrings(entry)));

        return multiMapValues;
    }

    @SuppressWarnings("unchecked")
    private static Map<String, List<String>> valueToStrings(Map.Entry<String, Object> entry) {
        String key = entry.getKey();
        Object value = entry.getValue();

        if (value == null) {
            return Collections.emptyMap();
        }

        if (value instanceof Iterable) {
            return Collections.singletonMap(key, StreamSupport.stream(((Iterable<?>) value).spliterator(), false)
                                                              .filter(Objects::nonNull)
                                                              .map(Object::toString)
                                                              .collect(Collectors.toList()));
        }
        return Collections.singletonMap(key, Collections.singletonList(value.toString()));
    }

    @SuppressWarnings("unchecked")
    private static Stream<Map.Entry<String, Object>> flatten(Map.Entry<String, Object> entry) {
        if (entry.getValue() instanceof Map<?, ?>) {
            Map<String, Object> nested = (Map<String, Object>) entry.getValue();

            return nested.entrySet().stream()
                         .map(e -> new AbstractMap.SimpleEntry(entry.getKey() + "[" + e.getKey() + "]", e.getValue()))
                         .flatMap(StripesService::flatten);
        }
        return Stream.of(entry);
    }
}
