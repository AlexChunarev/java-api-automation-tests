package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerRequestDTO(
        @JsonProperty("currency_code") String currencyCode,
        @JsonProperty("email") String email,
        @JsonProperty("name") String name,
        @JsonProperty("password_change") String passwordChange,
        @JsonProperty("password_repeat") String passwordRepeat,
        @JsonProperty("surname") String surname,
        @JsonProperty("username") String username
) {}