package org.gunsugunaydin.DetailedCityReport.service;

import org.gunsugunaydin.DetailedCityReport.config.AppConfig;
import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CityService {

    private final CityApiService cityApiService;
    private final AppConfig config;

    /**
     * Fetches city data for all cities listed in the 'config.json' file asynchronously.
     *
     * @return List<CityData> the list of city details for all cities.
     */
    public List<CityData> fetchCityData() {
        List<CompletableFuture<List<CityData>>> futures = createCityDataFutures(config.getCityList());

        List<CityData> cityDataList = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        validateCityData(cityDataList);

        return cityDataList;
    }

    /**
     * Fetches city data for the provided list of cities asynchronously.
     *
     * @param cities List of city names to fetch data for.
     * @return List<CityData> the list of city details for the provided cities.
     */
    public List<CityData> fetchCityDataByList(List<String> cities) {
        List<String> invalidCities = new ArrayList<>();
        List<CompletableFuture<List<CityData>>> futures = createCityDataFutures(cities);

        List<CityData> cityDataList = futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        for (int i = 0; i < cities.size(); i++) {
            if (cityDataList.isEmpty() || cityDataList.size() <= i || cityDataList.get(i) == null) {
                invalidCities.add(cities.get(i));
            }
        }

        if (!invalidCities.isEmpty()) {
            throw new IllegalArgumentException("Invalid city names: " + String.join(", ", invalidCities));
        }

        validateCityData(cityDataList);

        return cityDataList;
    }

    /**
     * Creates a list of CompletableFutures for fetching city data asynchronously.
     *
     * @param cities List of city names to fetch data for.
     * @return List<CompletableFuture<List<CityData>>> the list of CompletableFutures.
     */
    private List<CompletableFuture<List<CityData>>> createCityDataFutures(List<String> cities) {
        List<CompletableFuture<List<CityData>>> futures = new ArrayList<>();

        for (String city : cities) {
            CompletableFuture<List<CityData>> future = CompletableFuture.supplyAsync(() -> fetchCityDataForCity(city));
            futures.add(future);
        }

        return futures;
    }

    /**
     * Fetches city data for a single city, logging any errors encountered.
     *
     * @param city Name of the city to fetch data for.
     * @return List<CityData> the fetched city data or an empty list in case of an error.
     */
    private List<CityData> fetchCityDataForCity(String city) {
        try {
            List<CityData> cityData = cityApiService.fetchCityData(city);

            if (cityData == null || cityData.isEmpty()) {
                throw new Exception("No data found for city: " + city);
            }

            return cityData;

        } catch (Exception e) {
            log.error("Error fetching data for city: " + city + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Validates the fetched city data, ensuring that at least 10 cities were fetched.
     *
     * @param cityDataList List of fetched city data.
     */
    private void validateCityData(List<CityData> cityDataList) {
        if (cityDataList.size() < 10) {
            throw new IllegalStateException("Error: Fewer than 10 cities were fetched.");
        }
    }
}