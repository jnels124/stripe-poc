package com.example.stripespoc.web.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeRequest {
    private String customer;
    private Integer amount;
    private String currency = "usd";
    private String paymentMethod;
    private boolean confirm = true;
    private TransferData transferData;
    private Integer applicationFeeAmount;
}
