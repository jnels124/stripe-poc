package com.example.stripespoc.web.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CreateCustomerRequest {
    @NotBlank
    @Size(min = 2, max = 300)
    private String name;
}
