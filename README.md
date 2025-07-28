# Java API Automation Tests

This repository contains an example of automated REST API tests written in Java for a senior QA automation engineer test task. The code demonstrates best practices: SOLID principles, DTO usage, configuration from YAML, soft assertions, schema validation, and more. It's designed to be modular, maintainable, and a showcase of high-quality test automation.

## Project Overview
- **Goal**: Automate API tests for user registration, retrieval, sorting, and deletion using RestAssured, JUnit5, and Maven.
- **Key Features**:
    - DTOs as Java records for immutability.
    - API client abstraction for reusability.
    - Test data generation with JavaFaker.
    - Fluent assertions with AssertJ.
    - Configuration loaded from YAML (no hardcoding).
    - Soft assertions and cleanup in teardown.
- **Why this code is a good example**:
    - Follows SOLID: Single Responsibility (separate API client, tests), Open-Closed (extensible), etc.
    - Uses modern Java 21 features (records).
    - Robust error handling and logging.
    - Ready for CI/CD integration.

## Tech Stack
- Java 21
- Maven (build tool)
- RestAssured 5.5.5 (API testing)
- JUnit 5.13.3 (testing framework)
- Jackson 2.19.2 (JSON handling)
- AssertJ 3.27.3 (assertions)
- JavaFaker 2.1.0 (test data)
- SnakeYAML 2.3 (config loading)

## How to Run
1. Clone the repo: `git clone https://github.com/ВАШ_ЮЗЕРНЕЙМ/java-api-automation-tests.git`
2. Navigate to the project: `cd java-api-automation-tests`
3. Update `src/test/resources/config.yaml` with your API base URL and credentials.
4. Run tests: `mvn clean test`
    - Tests are in `src/test/java/com/example/tests/ApiAutomationTests.java`
    - They run in order (creation → get → sort → delete → verify empty).

## Badges
[![Maven Build](https://img.shields.io/badge/Maven-Build-green)](https://maven.apache.org/)
[![Java Version](https://img.shields.io/badge/Java-21-blue)](https://www.oracle.com/java/)

## License
MIT License - see [LICENSE](LICENSE) for details.

## Contributions
Feel free to fork and improve! This is meant as a portfolio example.