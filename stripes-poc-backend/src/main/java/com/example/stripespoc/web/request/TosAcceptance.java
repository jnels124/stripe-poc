package com.example.stripespoc.web.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.time.Instant;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class TosAcceptance {
    private long date = Instant.now().getEpochSecond();
    private String ip = "192.168.1.1";
}
