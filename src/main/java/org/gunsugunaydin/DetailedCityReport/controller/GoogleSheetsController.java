package org.gunsugunaydin.DetailedCityReport.controller;

import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.gunsugunaydin.DetailedCityReport.payload.WeatherData;
import org.gunsugunaydin.DetailedCityReport.service.CityService;
import org.gunsugunaydin.DetailedCityReport.service.GoogleSheetsService;
import org.gunsugunaydin.DetailedCityReport.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/googlesheets")
@RequiredArgsConstructor
public class GoogleSheetsController {

    private final CityService cityService;
    private final WeatherService weatherService;
    private final GoogleSheetsService googleSheetsService;

    @PostMapping("/generate-report")
    public ResponseEntity<String> generateReport(
            @RequestParam(required = false, defaultValue = "") String downloadLocation) {

        log.info("Received request to generate report. Download location: {}", 
                downloadLocation.isEmpty() ? "default location" : downloadLocation);

        try {
            List<CityData> cityDataList = cityService.fetchCityData();
            List<WeatherData> weatherDataList = weatherService.fetchWeatherData(cityDataList);

            googleSheetsService.generateCityReport(cityDataList, weatherDataList, downloadLocation);

            String successMessage = downloadLocation.isEmpty() 
                    ? "Report generated and downloaded successfully to your default downloads folder." 
                    : "Report generated and downloaded successfully to: " + downloadLocation;

            log.info(successMessage);
            return ResponseEntity.ok(successMessage);

        } catch (Exception e) {
            String errorMessage = "Error generating report: " + e.getMessage();
            log.error(errorMessage, e);
            return ResponseEntity.status(500).body(errorMessage);
        }
    }
}