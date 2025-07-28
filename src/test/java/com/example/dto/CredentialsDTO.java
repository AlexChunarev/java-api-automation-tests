package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CredentialsDTO(
        @JsonProperty("email") String email,
        @JsonProperty("password") String password
) {}