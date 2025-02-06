package org.gunsugunaydin.DetailedCityReport.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.gunsugunaydin.DetailedCityReport.payload.WeatherData;
import org.gunsugunaydin.DetailedCityReport.service.CityService;
import org.gunsugunaydin.DetailedCityReport.service.GoogleSheetsService;
import org.gunsugunaydin.DetailedCityReport.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CityService cityService;
    private final WeatherService weatherService;
    private final GoogleSheetsService googleSheetsService;

    @GetMapping("/")
    public String showHomePage(Model model) {
        model.addAttribute("message", "Welcome to Detailed City Report!");
        return "home_view/home";
    }

    @PostMapping("/generate-report")
    public ResponseEntity<Map<String, String>> generateReportFromUI(
            @RequestParam(required = false, defaultValue = "") String downloadLocation,
            @RequestParam List<String> cities) {

        Map<String, String> response = new HashMap<>();
        try {
            if (cities == null || cities.isEmpty()) {
                response.put("error", "City list cannot be empty.");
                return ResponseEntity.badRequest().body(response);
            }

            List<CityData> cityDataList = cityService.fetchCityDataByList(cities);
            List<WeatherData> weatherDataList = weatherService.fetchWeatherData(cityDataList);

            googleSheetsService.generateCityReport(cityDataList, weatherDataList, downloadLocation);

            String successMessage = downloadLocation.isEmpty()
                    ? "Report downloaded successfully to your default downloads folder."
                    : "Report downloaded successfully to: " + downloadLocation;

            response.put("message", successMessage);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.put("error", "Invalid city name: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response.put("error", "Error generating report: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}