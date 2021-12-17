package com.example.stripespoc.web.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateConnectedAccountRequest {
    private String type = "custom";
    private String businessType = "individual";
    private ExternalAccount externalAccount = new ExternalAccount();
    private ConnectedAccountCapabilities capabilities = new ConnectedAccountCapabilities();
    private BusinessProfile businessProfile = new BusinessProfile();
    private TosAcceptance tosAcceptance = new TosAcceptance();
    private Individual individual = new Individual();
}
