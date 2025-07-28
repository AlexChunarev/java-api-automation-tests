package com.example.config;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class TestConfig {
    private static final Map<String, Object> config;

    static {
        Yaml yaml = new Yaml();
        try (InputStream inputStream = TestConfig.class.getClassLoader().getResourceAsStream("config.yaml")) {
            config = yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load config.yaml", e);
        }
    }

    public static String getBaseUrl() {
        String envValue = System.getenv("BASE_URL");
        return envValue != null ? envValue : (String) config.get("baseUrl");
    }

    public static String getTesterEmail() {
        String envValue = System.getenv("TESTER_EMAIL");
        return envValue != null ? envValue : (String) config.get("testerEmail");
    }

    public static String getTesterPassword() {
        String envValue = System.getenv("TESTER_PASSWORD");
        return envValue != null ? envValue : (String) config.get("testerPassword");
    }
}