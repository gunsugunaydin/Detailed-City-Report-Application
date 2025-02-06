package org.gunsugunaydin.DetailedCityReport.controller;

import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.gunsugunaydin.DetailedCityReport.service.CityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/report-cities")
    public ResponseEntity<?> getCities() {
        try {
            List<CityData> cityDataList = cityService.fetchCityData();
            if (cityDataList.isEmpty()) {
                return new ResponseEntity<>("No cities found.", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(cityDataList);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Error: Fewer than 10 cities were fetched.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/report-dynamic-cities")
    public ResponseEntity<?> getDynamicCities(@RequestBody List<String> cityNames) {
        try {
            if (cityNames == null || cityNames.isEmpty()) {
                return new ResponseEntity<>("City list cannot be empty.", HttpStatus.BAD_REQUEST);
            }

            List<CityData> cityDataList = cityService.fetchCityDataByList(cityNames);

            if (cityDataList.isEmpty()) {
                return new ResponseEntity<>("No data found for the provided cities.", HttpStatus.NOT_FOUND);
            }
            return ResponseEntity.ok(cityDataList);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>("Error: Fewer than 10 cities were fetched.", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}