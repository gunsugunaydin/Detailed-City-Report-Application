package org.gunsugunaydin.DetailedCityReport.controller;

import java.util.List;
import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.gunsugunaydin.DetailedCityReport.payload.WeatherData;
import org.gunsugunaydin.DetailedCityReport.service.CityService;
import org.gunsugunaydin.DetailedCityReport.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/weather")
@RequiredArgsConstructor
public class WeatherController {
    
    private final CityService cityService;
    private final WeatherService weatherService;
    
    @GetMapping("/report-weather")
    public ResponseEntity<List<WeatherData>> getWeather() {
        List<CityData> cityDataList = cityService.fetchCityData();
        List<WeatherData> weatherDataList = weatherService.fetchWeatherData(cityDataList);
        return ResponseEntity.ok(weatherDataList);
    }
}