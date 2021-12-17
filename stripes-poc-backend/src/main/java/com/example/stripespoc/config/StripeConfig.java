package com.example.stripespoc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
@Data
public class StripeConfig {
    private String baseUrl = "https://api.stripe.com/v1";
    //TODO:// encrypt with jks and move cipher to application.yml (need to configure config server client)
    private String key = "sk_test_51K77U3GzhQNlFIKSoFeDZA81R1sCaP2ZqglO3kfZ1rqcrOwg8YXy4XFMjmZwJiSp6Re9q0BDeJQf6ZgLAjNogOIQ00MhKSweFy";
    private String paymentMethods = "/payment_methods";
    private String attach = "/{id}/attach";
    private String customers = "/customers";
}
