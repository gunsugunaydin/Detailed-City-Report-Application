package org.gunsugunaydin.DetailedCityReport.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.gunsugunaydin.DetailedCityReport.payload.WeatherData;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherApiService weatherApiService;

    /**
     * Fetches weather data for a list of cities asynchronously.
     *
     * @param cityDataList the list of cities for which weather data is required
     * @return List<WeatherData> the list of weather data for each city
     */
    public List<WeatherData> fetchWeatherData(List<CityData> cityDataList) {
        List<CompletableFuture<WeatherData>> futures = new ArrayList<>();

        for (CityData city : cityDataList) {
            String unit = determineTemperatureUnitForCountry(city.getCountry());
            CompletableFuture<WeatherData> future = CompletableFuture.supplyAsync(() -> 
                weatherApiService.fetchWeatherData(
                    String.valueOf(city.getLatitude()),
                    String.valueOf(city.getLongitude()),
                    unit
                )
            );
            futures.add(future);
        }

        return futures.stream()
                      .map(CompletableFuture::join)
                      .collect(Collectors.toList());
    }

    /**
     * Determines the temperature unit based on the country.
     *
     * @param country the country code for which the unit is determined
     * @return String the temperature unit
     */
    private String determineTemperatureUnitForCountry(String country) {
        Locale locale = new Locale.Builder().setRegion(country).build();

        Map<String, String> countryTemperatureUnits = Map.of(
            "US", "imperial",
            "GB", "metric", 
            "TR", "metric",
            "JP", "metric",
            "CA", "metric"
        );

        return countryTemperatureUnits.getOrDefault(locale.getCountry().toUpperCase(), "standard");
    }
}