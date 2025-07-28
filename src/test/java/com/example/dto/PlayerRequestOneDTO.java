package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PlayerRequestOneDTO(
        @JsonProperty("email") String email
) {}