package com.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PlayerResponseDTO(
        @JsonProperty("id") int id,
        @JsonProperty("username") String username,
        @JsonProperty("email") String email,
        @JsonProperty("name") String name,
        @JsonProperty("surname") String surname
) {}