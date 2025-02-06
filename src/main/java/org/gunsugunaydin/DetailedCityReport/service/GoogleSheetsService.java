package org.gunsugunaydin.DetailedCityReport.service;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AutoResizeDimensionsRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetResponse;
import com.google.api.services.sheets.v4.model.DimensionRange;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.sheets.v4.model.Request;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gunsugunaydin.DetailedCityReport.payload.CityData;
import org.gunsugunaydin.DetailedCityReport.payload.WeatherData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.*;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSheetsService {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String DEFAULT_FILE_EXTENSION = ".pdf";
    private static final String PDF_MIME_TYPE = "application/pdf";
    private static final int FIRST_SHEET_ID = 0;
    private static final String DIMENSION_COLUMNS = "COLUMNS";
    private static final int START_COLUMN_INDEX = 0;
    private static final int END_COLUMN_INDEX = 13;

    @Value("${google.application.name}")
    private String applicationName;

    @Value("${google.credentials.path}")
    private String credentialsPath;

    private final CountryApiService countryApiService;
    private final TemperatureUnitService unitService;

    private GoogleCredentials credentials;

    // Initializes Google credentials after bean construction
    @PostConstruct
    public void initializeCredentials() {
        try {
            credentials = loadCredentials();
            log.info("Google credentials successfully initialized.");
        } catch (IOException e) {
            log.error("Failed to initialize Google credentials: {}", e.getMessage());
            throw new IllegalStateException("Failed to initialize Google credentials", e);
        }
    }

    // Loads Google credentials from the specified path
    private GoogleCredentials loadCredentials() throws IOException {
        try (InputStream in = new FileInputStream(credentialsPath)) {
            return GoogleCredentials.fromStream(in)
                    .createScoped(Arrays.asList(SheetsScopes.SPREADSHEETS, DriveScopes.DRIVE));
        }
    }

    // Generates a detailed city report using provided city and weather data
    public void generateCityReport(List<CityData> cityDataList, List<WeatherData> weatherDataList, String downloadLocation) {
        try {
            Sheets sheetsService = createSheetsService();
            String spreadsheetId = createSpreadSheet(sheetsService);
            populateSpreadsheet(sheetsService, spreadsheetId, cityDataList, weatherDataList);
            autoResizeColumns(sheetsService, spreadsheetId);
            downloadSpreadsheetAsPdf(spreadsheetId, downloadLocation);
        } catch (Exception e) {
            log.error("Error generating city report: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate city report", e);
        }
    }

    // Creates and returns a Sheets service object
    private Sheets createSheetsService() throws GeneralSecurityException, IOException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(applicationName)
                .build();
    }

    // Creates a new spreadsheet with a timestamped title
    private String createSpreadSheet(Sheets sheetsService) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy h:mm:ss a", Locale.ENGLISH);
        String timestamp = ZonedDateTime.now().format(formatter);

        String safeTimestamp = timestamp.replaceAll("[^a-zA-Z0-9,\\s:]", "");
        String title = "Detailed City Report - " + safeTimestamp;

        Spreadsheet spreadsheet = new Spreadsheet()
                .setProperties(new SpreadsheetProperties().setTitle(title));
        return sheetsService.spreadsheets().create(spreadsheet).execute().getSpreadsheetId();
    }

    // Populates the spreadsheet with city and weather data
    private void populateSpreadsheet(Sheets sheetsService, String spreadsheetId,
                                     List<CityData> cityDataList, List<WeatherData> weatherDataList) throws IOException {
        List<List<Object>> rows = new ArrayList<>();
        rows.add(getSpreadsheetHeaders());

        for (int i = 0; i < cityDataList.size(); i++) {
            CityData city = cityDataList.get(i);
            WeatherData weather = weatherDataList.get(i);
            rows.add(formatCityWeatherRow(city, weather));
        }

        ValueRange data = new ValueRange().setValues(rows);
        sheetsService.spreadsheets().values()
                .update(spreadsheetId, "Sheet1", data)
                .setValueInputOption("RAW")
                .execute();
        log.info("Data successfully populated in spreadsheet with ID: {}", spreadsheetId);
    }

    // Returns the headers for the spreadsheet
    private List<Object> getSpreadsheetHeaders() {
        return Arrays.asList("Name", "Region", "Country", "Latitude", "Longitude", "Is Capital", "Population", "Weather", 
                "Temperature", "Humidity", "Pressure", "Wind Speed", "Sunrise", "Sunset");
    }

    // Formats a single row of city and weather data
    private List<Object> formatCityWeatherRow(CityData city, WeatherData weather) {
        String countryName = countryApiService.getCountryName(city.getCountry());  
    
        int timezoneOffset = weather.getTimezone(); 
        int sunriseTimestamp = weather.getSys().getSunrise();
        int sunsetTimestamp = weather.getSys().getSunset();
    
        String formattedSunrise = formatTimestampWithTimezone(sunriseTimestamp, timezoneOffset);
        String formattedSunset = formatTimestampWithTimezone(sunsetTimestamp, timezoneOffset);
    
        return Arrays.asList(
            city.getName(), city.getRegion(), countryName, city.getLatitude(),
            city.getLongitude(), city.isIs_capital(), city.getPopulation(),
            weather.getWeather().get(0).getMain(),
            weather.getMain().getTemp() + " " + unitService.determineUnitForCountry(city.getCountry()),
            weather.getMain().getHumidity() + "%", weather.getMain().getPressure() + " hPa",
            weather.getWind().getSpeed() + " m/s", formattedSunrise, formattedSunset
        );
    }

    // Formats a timestamp according to the city's timezone
    private String formatTimestampWithTimezone(int timestamp, int timezoneOffset) {
        ZonedDateTime localTime = Instant.ofEpochSecond(timestamp)
                                         .atZone(ZoneId.ofOffset("UTC", ZoneOffset.ofTotalSeconds(timezoneOffset)));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy h:mm:ss a z", Locale.ENGLISH);
        return localTime.format(formatter);
    }

    // Automatically resizes the columns of the spreadsheet for better readability for PDF
    private void autoResizeColumns(Sheets sheetsService, String spreadsheetId) throws IOException {
        Request autoResizeRequest = new Request()
                .setAutoResizeDimensions(new AutoResizeDimensionsRequest()
                        .setDimensions(new DimensionRange()
                                .setSheetId(FIRST_SHEET_ID) 
                                .setDimension(DIMENSION_COLUMNS) 
                                .setStartIndex(START_COLUMN_INDEX) 
                                .setEndIndex(END_COLUMN_INDEX)
                        ));

        BatchUpdateSpreadsheetRequest batchRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(Collections.singletonList(autoResizeRequest));

        BatchUpdateSpreadsheetResponse response = sheetsService.spreadsheets()
                .batchUpdate(spreadsheetId, batchRequest)
                .execute();

        log.info("Columns resized successfully: {}", response);
    }
    
    // Downloads the spreadsheet as a PDF and saves it to the configured path
    private void downloadSpreadsheetAsPdf(String spreadsheetId, String downloadLocation) throws IOException, GeneralSecurityException {
        Drive driveService = createDriveService();
        String baseFileName = "Detailed City Report";

        String outputPath = (downloadLocation != null && !downloadLocation.isEmpty()) ?
                downloadLocation + "/" + baseFileName + DEFAULT_FILE_EXTENSION :
                getDefaultDownloadLocation() + "/" + baseFileName + DEFAULT_FILE_EXTENSION;

        int fileIndex = 1;
        File file = new File(outputPath);
        while (file.exists()) {
            outputPath = (downloadLocation != null && !downloadLocation.isEmpty()) ?
                    downloadLocation + "/" + baseFileName + "(" + fileIndex + ")" + DEFAULT_FILE_EXTENSION :
                    getDefaultDownloadLocation() + "/" + baseFileName + "(" + fileIndex + ")" + DEFAULT_FILE_EXTENSION;
            file = new File(outputPath);
            fileIndex++;
        }

        try (OutputStream outputStream = new FileOutputStream(outputPath)) {
            driveService.files().export(spreadsheetId, PDF_MIME_TYPE)
                    .executeMediaAndDownloadTo(outputStream);
            log.info("Spreadsheet successfully downloaded as PDF to {}", outputPath);
        }
    }

    // Returns the default downloads folder
    private String getDefaultDownloadLocation() {
        String userHome = System.getProperty("user.home");
        String downloadsFolder = userHome + "/Downloads";
        return downloadsFolder;
    }

    // Creates and returns a Drive service object for file export
    private Drive createDriveService() throws GeneralSecurityException, IOException {
        return new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY,
                new HttpCredentialsAdapter(credentials))
                .setApplicationName(applicationName)
                .build();
    }
}