package com.example.stripespoc.web;

import com.example.stripespoc.service.StripesService;
import com.example.stripespoc.web.request.AddCardRequest;
import com.example.stripespoc.web.request.ChargeRequest;
import com.example.stripespoc.web.request.CreateConnectedAccountRequest;
import com.example.stripespoc.web.request.CreateCustomerRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/")
@AllArgsConstructor
//TODO:// create response objects
//TODO:// pagination
public class StripesController {
    private final StripesService stripeService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 path = "/paymentmethods/card")
    public Mono<String> addCard(@RequestBody AddCardRequest request) {
        return stripeService.addCard(request);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                path = "/paymentmethods/card/list")

    public Mono<String> listCards(@RequestParam("customerId") String customerId) {
        return stripeService.listCards(customerId);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 path = "/customer")
    public Mono<String> createCustomer(@RequestBody CreateCustomerRequest request) {
        return stripeService.createCustomer(request);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                path = "/customer/list")
    public Mono<String> listCustomers() {
        return stripeService.listCustomers();
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 path = "/charge")
    public Mono<String> charge(@RequestBody ChargeRequest request) {
        return stripeService.charge(request);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                path = "/charge/{id}")
    public Mono<String> getChargeDetail(@PathVariable("id") String chargeId) {
        return stripeService.getChargeDetails(chargeId);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                path = "/charge")
    public Mono<String> getCustomerCharges(@RequestParam("customerId") String customerId) {
        return stripeService.getCustomerCharges(customerId);
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                 path = "/connectedAccount")
    public Mono<String> createConnectedAccount(@RequestBody CreateConnectedAccountRequest request) {
        return stripeService.createConnectedAccount(request);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
                path = "/connectedAccount/list")
    public Mono<String> createConnectedAccount() {
        return stripeService.listConnectedAccounts();
    }
}
