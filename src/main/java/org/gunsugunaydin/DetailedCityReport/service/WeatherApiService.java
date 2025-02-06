package org.gunsugunaydin.DetailedCityReport.service;

import org.gunsugunaydin.DetailedCityReport.config.AppConfig;
import org.gunsugunaydin.DetailedCityReport.payload.WeatherData;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherApiService {

    private final RestTemplate restTemplate;
    private final AppConfig config;

    /**
     * Fetches weather data for the given coordinates and unit
     *
     * @param lat  the latitude of the location from city API (check WeatherService -> fetchWeatherData method)
     * @param lon  the longitude of the location from city API
     * @param unit the unit for temperature (e.g., metric, imperial)
     * @return WeatherData the weather information for the location
     */
    public WeatherData fetchWeatherData(String lat, String lon, String unit) {
        try {
            String uri = UriComponentsBuilder.fromUriString(config.getApis().get("weatherApi"))
                    .queryParam("lat", lat)
                    .queryParam("lon", lon)
                    .queryParam("units", unit)
                    .queryParam("appid", config.getApiKeys().get("weatherApiKey"))
                    .build()
                    .toUriString();

            return restTemplate.getForObject(uri, WeatherData.class);

        } catch (Exception e) {
            log.error("Failed to fetch weather data for coordinates ({}, {}): {}", lat, lon, e.getMessage());
            throw new RuntimeException("Error fetching weather data", e);
        }
    }
}