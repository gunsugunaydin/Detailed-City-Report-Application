package org.gunsugunaydin.DetailedCityReport.service;

import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class TemperatureUnitService {

    private static final Map<String, String> countryTemperatureUnits = Map.ofEntries(
        Map.entry("US", "°F"),
        Map.entry("UK", "°C"),
        Map.entry("TR", "°C"),
        Map.entry("JP", "°C"),
        Map.entry("CA", "°C"),
        Map.entry("IN", "°C"),
        Map.entry("DE", "°C"),
        Map.entry("FR", "°C"),
        Map.entry("IT", "°C"),
        Map.entry("ES", "°C"),
        Map.entry("BR", "°C")
    );

    private static final String DEFAULT_UNIT = "K";
    
    // Determines the unit for temperature based on the country
    public String determineUnitForCountry(String country) {
        return countryTemperatureUnits.getOrDefault(country.toUpperCase(), DEFAULT_UNIT);
    }
}