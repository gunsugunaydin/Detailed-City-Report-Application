package org.gunsugunaydin.DetailedCityReport.service;

import org.gunsugunaydin.DetailedCityReport.config.AppConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryApiService {

    private final AppConfig config;
    private final RestTemplate restTemplate;

    /**
     * Fetches the country name for a given country code from the external RestCountries API.
     *
     * @param countryCode the country code for which the name is required (e.g., TR)
     * @return String the common name of the country, or null if not found
     */
    @Cacheable(value = "countryCache", key = "#countryCode")
    public String getCountryName(String countryCode) {
        String url = config.getApis().get("countryApi") + countryCode;
        log.info("Calling API with URL: {}", url);

        try {
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );
            
            List<Map<String, Object>> countries = response.getBody();

            if (countries != null && !countries.isEmpty()) {
                Map<String, Object> country = countries.get(0);
                return getCommonCountryName(country);
            }

        } catch (Exception e) {
            log.error("Failed to fetch country name for code {}: {}", countryCode, e.getMessage());
        }

        return null;
    }

    /**
     * Extracts the common country name from the API response data.
     *
     * @param country the map containing country data
     * @return String the common name of the country, or null if not found
     */
    private String getCommonCountryName(Map<String, Object> country) {
        if (country.containsKey("name")) {
            Object nameObj = country.get("name");

            if (nameObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> nameMap = (Map<String, Object>) nameObj;
                return (String) nameMap.get("common");
            }
        }

        return null;
    }
}