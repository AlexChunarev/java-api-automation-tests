package com.example.api;

import com.example.dto.*;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiClient {
    private final String baseUrl;
    private final RequestSpecification authSpec;
    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);

    public ApiClient(String baseUrl, String token) {
        this.baseUrl = baseUrl;
        this.authSpec = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .addHeader("Authorization", "Bearer " + token)
                .setContentType(ContentType.JSON)
                .build();
    }

    public Response login(CredentialsDTO credentials) {
        logger.debug("Sending login request: {}", credentials);
        Response response = given()
                .baseUri(baseUrl)
                .contentType(ContentType.JSON)
                .body(credentials)
                .post("/api/tester/login");
        logger.debug("Received login response: status={}, body={}", response.getStatusCode(), response.getBody().asString());
        return response;
    }

    public Response createPlayer(PlayerRequestDTO player) {
        logger.debug("Sending create player request: {}", player);
        Response response = given()
                .spec(authSpec)
                .body(player)
                .post("/api/automationTask/create");
        logger.debug("Received create player response: status={}, body={}", response.getStatusCode(), response.getBody().asString());
        return response;
    }

    public Response getOnePlayer(PlayerRequestOneDTO request) {
        logger.debug("Sending get one player request: {}", request);
        Response response = given()
                .spec(authSpec)
                .body(request)
                .post("/api/automationTask/getOne");
        logger.debug("Received get one player response: status={}, body={}", response.getStatusCode(), response.getBody().asString());
        return response;
    }

    public Response getAllPlayers() {
        logger.debug("Sending get all players request");
        Response response = given()
                .spec(authSpec)
                .get("/api/automationTask/getAll");
        logger.debug("Received get all players response: status={}, body={}", response.getStatusCode(), response.getBody().asString());
        return response;
    }

    public Response deletePlayer(String id) {
        logger.debug("Sending delete player request for id: {}", id);
        Response response = given()
                .spec(authSpec)
                .delete("/api/automationTask/deleteOne/{id}", id);
        logger.debug("Received delete player response: status={}, body={}", response.getStatusCode(), response.getBody().asString());
        return response;
    }
}