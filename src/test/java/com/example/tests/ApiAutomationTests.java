package com.example.tests;

import com.example.api.ApiClient;
import com.example.config.TestConfig;
import com.example.dto.*;
import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThat;

@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Timeout(value = 60, unit = TimeUnit.SECONDS)
public class ApiAutomationTests {

    private static final int NUM_PLAYERS = 12;

    private ApiClient apiClient;
    private final Faker faker = new Faker(new Locale("en"));
    private final List<PlayerResponseDTO> createdPlayers = new ArrayList<>();
    private final SoftAssertions softAssertions = new SoftAssertions();

    private static final Logger logger = LoggerFactory.getLogger(ApiAutomationTests.class);

    @BeforeAll
    public void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterAll
    public void teardown() {
        // Cleanup any remaining players in case of failure
        for (PlayerResponseDTO player : createdPlayers) {
            try {
                apiClient.deletePlayer(player.id());
            } catch (Exception e) {
                logger.error("Error deleting player {}: {}", player.id(), e.getMessage(), e);
            }
        }
        softAssertions.assertAll();
    }

    @Test
    @Order(1)
    @DisplayName("Get user token")
    public void testLogin() {
        CredentialsDTO credentials = new CredentialsDTO(TestConfig.getTesterEmail(), TestConfig.getTesterPassword());
        Response response = new ApiClient(TestConfig.getBaseUrl(), "").login(credentials); // Без token

        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        softAssertions.assertThat(response.jsonPath().getString("accessToken")).isNotNull();

        TokenDTO tokenDto = response.as(TokenDTO.class);
        apiClient = new ApiClient(TestConfig.getBaseUrl(), tokenDto.accessToken()); // Set for other tests
    }

    @Test
    @Order(2)
    @DisplayName("Register 12 players")
    public void testCreatePlayers() {
        for (int i = 0; i < NUM_PLAYERS; i++) {
            String email = faker.internet().emailAddress();
            String username = faker.name().username();
            String name = faker.name().firstName();
            String surname = faker.name().lastName();
            String password = faker.internet().password(4, 20);

            PlayerRequestDTO playerRequest = new PlayerRequestDTO(
                    "EUR",
                    email,
                    name,
                    password,
                    password,
                    surname,
                    username
            );

            Response response = apiClient.createPlayer(playerRequest);

            softAssertions.assertThat(response.getStatusCode()).isEqualTo(201);
            softAssertions.assertThatCode(() -> response.then().assertThat().body(matchesJsonSchemaInClasspath("player-response-schema.json")))
                    .as("Create player response schema validation").doesNotThrowAnyException();


            PlayerResponseDTO player = response.as(PlayerResponseDTO.class);
            softAssertions.assertThat(player.id()).isNotEmpty();
            softAssertions.assertThat(player.username()).isEqualTo(username);
            softAssertions.assertThat(player.email()).isEqualTo(email);
            softAssertions.assertThat(player.name()).isEqualTo(name);
            softAssertions.assertThat(player.surname()).isEqualTo(surname);

            createdPlayers.add(player);
        }
    }

    @Test
    @Order(3)
    @DisplayName("Get one player profile")
    public void testGetOnePlayer() {
        // assumeThat(createdPlayers).isNotEmpty();

        PlayerResponseDTO firstPlayer = createdPlayers.get(0);
        PlayerRequestOneDTO request = new PlayerRequestOneDTO(firstPlayer.email());

        Response response = apiClient.getOnePlayer(request);

        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);
        softAssertions.assertThatCode(() -> response.then().assertThat().body(matchesJsonSchemaInClasspath("player-response-schema.json")))
                .as("Create player response schema validation").doesNotThrowAnyException();

        PlayerResponseDTO player = response.as(PlayerResponseDTO.class);
        softAssertions.assertThat(player).usingRecursiveComparison().isEqualTo(firstPlayer);
    }

    @Test
    @Order(4)
    @DisplayName("Get all players and sort by name")
    public void testGetAllAndSort() {
        // assumeThat(createdPlayers).hasSize(NUM_PLAYERS);

        Response response = apiClient.getAllPlayers();

        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);

        List<PlayerResponseDTO> players = response.jsonPath().getList(".", PlayerResponseDTO.class);
        softAssertions.assertThat(players).hasSize(NUM_PLAYERS);

        // Sort by name
        List<PlayerResponseDTO> sortedPlayers = players.stream()
                .sorted(Comparator.comparing(PlayerResponseDTO::name))
                .toList();

        // Assert sorted
        softAssertions.assertThat(sortedPlayers).isSortedAccordingTo(Comparator.comparing(PlayerResponseDTO::name));
    }

    @Test
    @Order(5)
    @DisplayName("Delete all created players")
    public void testDeletePlayers() {
        assumeThat(createdPlayers).isNotEmpty();

        List<PlayerResponseDTO> playersToDelete = new ArrayList<>(createdPlayers);
        for (PlayerResponseDTO player : playersToDelete) {
            Response response = apiClient.deletePlayer(player.id());

            softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);

            PlayerResponseDTO deleted = response.as(PlayerResponseDTO.class);
            softAssertions.assertThat(deleted.id()).isEqualTo(player.id());

            createdPlayers.remove(player);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Verify all players are deleted")
    public void testGetAllEmpty() {
        Response response = apiClient.getAllPlayers();

        softAssertions.assertThat(response.getStatusCode()).isEqualTo(200);

        List<PlayerResponseDTO> players = response.jsonPath().getList(".", PlayerResponseDTO.class);
        softAssertions.assertThat(players).isEmpty();
    }
}