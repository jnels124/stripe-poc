package com.example.stripespoc.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "stripe")
@Data
public class StripeConfig {
    private String baseUrl = "https://api.stripe.com/v1";
    //TODO:// encrypt with jks and move cipher to application.yml (need to configure config server client)...or inject with env var
    private String key = "sk_test_51K77U3GzhQNlFIKSoFeDZA81R1sCaP2ZqglO3kfZ1rqcrOwg8YXy4XFMjmZwJiSp6Re9q0BDeJQf6ZgLAjNogOIQ00MhKSweFy";
    private String paymentMethods = "/payment_methods";
    private String attach = "/{id}/attach";
    private String customers = "/customers";
    private String paymentIntents = "/payment_intents";
    private String connectedAccounts = "/accounts";
    private String accountLinks = "/account_links";
    private String charges = "/charges";

    private StripeWebhooks webhooks = new StripeWebhooks();

//    TODO://
    private static class StripeWebhooks {
//        private String accountLinkReturnUrl = "http://"
    }
}
