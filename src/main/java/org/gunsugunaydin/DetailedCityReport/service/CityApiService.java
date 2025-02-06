package org.gunsugunaydin.DetailedCityReport.service;

import org.gunsugunaydin.DetailedCityReport.config.AppConfig;
import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityApiService {

    private final RestTemplate restTemplate;
    private final AppConfig config;

    /**
     * Fetches city data from the external City API.
     *
     * @param cityName the name of the city to fetch data for
     * @return List<CityData> the list of city data retrieved from the API
     */
    public List<CityData> fetchCityData(String cityName) {
        try {
            String uri = UriComponentsBuilder.fromUriString(config.getApis().get("cityApi"))
                    .queryParam("name", cityName)
                    .build()
                    .toUriString();

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-api-key", config.getApiKeys().get("cityApiKey"));

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<List<CityData>> response = restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<CityData>>() {}
            );

            return response.getBody();

        } catch (Exception e) {
            log.error("Failed to fetch city data for {}: {}", cityName, e.getMessage());
            throw new RuntimeException("Error fetching city data", e);
        }
    }
}