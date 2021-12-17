package com.example.stripespoc.web.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddCardRequest {
    @Valid
    private CardInfo card;
    private String type = "card";
    private String customer;

    @JsonIgnore
    public String getCustomer() {
        return customer;
    }

    @JsonProperty
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class CardInfo {
        @NotBlank
        //TODO:// additional card validation
        private String number;

        @Min(1)
        @Max(12)
        @NotNull
        private Integer expMonth;

        //TODO:// may need to configure special validator to handle logically
        @NotNull
        private Integer expYear;

        @Pattern(regexp = "^[0-9]{3}$")
        private String cvc;
    }
}
