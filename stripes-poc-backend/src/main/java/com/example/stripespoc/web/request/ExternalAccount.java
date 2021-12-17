package com.example.stripespoc.web.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ExternalAccount {
    private String object = "bank_account";
    private String country = "US";
    private String currency = "usd";
    private String routingNumber = "102001017";
    private String accountNumber = "000123456789";
}
