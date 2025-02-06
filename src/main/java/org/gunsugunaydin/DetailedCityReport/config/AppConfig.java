package org.gunsugunaydin.DetailedCityReport.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;

@Component
public class AppConfig {

    private List<String> cityList;
    private Map<String, String> apis;
    private Map<String, String> apiKeys;

    @PostConstruct
    public void loadConfigFirst() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config.json");

        // If the config.json is not found, use default values (bknz.1.1)
        if (inputStream == null) {
            setDefaultValues();
        } else {
            Map<String, Object> config = mapper.readValue(inputStream, new TypeReference<Map<String, Object>>() {});
            this.cityList = mapper.convertValue(config.get("cityList"), new TypeReference<List<String>>() {});
            this.apis = mapper.convertValue(config.get("apis"), new TypeReference<Map<String, String>>() {});
            this.apiKeys = mapper.convertValue(config.get("apiKeys"), new TypeReference<Map<String, String>>() {});
        }
    }

    // 1.1 Default values
    private void setDefaultValues() {
        this.cityList = List.of("Ankara", "Istanbul", "Florence", "Rome", "Marseille", "Seoul", "Madrid", "Tokyo", "Los Angeles", "New York");
        this.apis = Map.of(
            "cityApi", "https://api.api-ninjas.com/v1/city",
            "weatherApi", "https://api.openweathermap.org/data/2.5/weather",
            "countryApi", "https://restcountries.com/v3.1/alpha/"
        );
        this.apiKeys = Map.of(
            "cityApiKey", "AZnAAcG6NOAPGhVN5fM9Yg==oTND6x4XpIXHcUGi",
            "weatherApiKey", "45fc10f6036587ecf0459b50bf448295"
        );
    }

    public List<String> getCityList() {
        return cityList;
    }

    public Map<String, String> getApis() {
        return apis;
    }

    public Map<String, String> getApiKeys() {
        return apiKeys;
    }
}