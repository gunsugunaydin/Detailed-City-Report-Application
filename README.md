# Detailed City Report Application <img src="https://media.tenor.com/TwkVSDkjf1MAAAAj/dudu-bubu-raining.gif" alt="Rainy Gif" width="50" height="50">

## Overview
The **Detailed City Report Application** is a Spring Boot based application designed to generate detailed reports for a list of cities. The application retrieves data from external APIs, formats it into a Google Spreadsheet, and downloads it as a PDF. The project includes both backend and frontend components to deliver a seamless experience.

## Demo

Below is a quick demo of the **Detailed City Report Application**. The demo is divided into two parts:

### Part 1: Downloading the Report to the Default Downloads Folder

https://github.com/user-attachments/assets/77892a3d-4eb5-420b-9e44-150a860d4cbf

### Part 2: Downloading the Report to a Specified File Path

https://github.com/user-attachments/assets/b9ff9400-c5fd-4122-bd17-48d85a962143

## Features

- Retrieve city details using [City API](https://www.api-ninjas.com/api/city).
- Retrieve weather details using [OpenWeather API](https://openweathermap.org/current).
- Fetch country names based on country codes using [RestCountries API](https://restcountries.com/)
- Includes caching for frequent RestCountry API lookups to reduce overhead.
- Save data to a Google Spreadsheet with timestamps.
- Download the spreadsheet as a PDF.
- User-friendly web interface for input and configuration.
- Handles timezone and temperature unit conversions based on country.
- Provides default values if `config.json` is missing or incomplete.
- Implements robust error handling for API calls and input validation.
- Uses asynchronous API calls with `CompletableFuture` to enhance performance for long city lists.
- Automatically avoids overwriting reports by appending unique identifiers to filenames.

---

## Prerequisites
### Tools
- **Java 21**
- **Spring Boot 3.x**
- Maven 3.6+
- Google Cloud account with Sheets and Drive API enabled
- API keys for:
  - [City API](https://www.api-ninjas.com/api/city)
  - [OpenWeather API](https://openweathermap.org/current)

### Configuration
1. Create a `config.json` file in the `resources` folder or use mine 🫠:


   ```json
   {
     "cityList": ["Ankara", "Istanbul", "Florence", "Rome", "Marseille", "Seoul", "Madrid", "Tokyo", "Los Angeles", "New York"],
     "apis": {
       "cityApi": "https://api.api-ninjas.com/v1/city",
       "weatherApi": "https://api.openweathermap.org/data/2.5/weather",
       "countryApi": "https://restcountries.com/v3.1/alpha/"
     },
     "apiKeys": {
       "cityApiKey": "city_api_key",
       "weatherApiKey": "weather_api_key"
     }
   }
   ```
3. Set up `application.properties`:


   ```properties
   google.application.name=Detailed City Report
   google.credentials.path=/path/to/credentials.json
   server.port=8080
   ```
5. Obtain and place Google Cloud service account credentials (`credentials.json`). Please don't try to use mine. Yes, I uploaded credentials to GitHub for reference purposes, but I intentionally included random alphanumeric values for security reasons — these are not my real credentials. If you want to run the application, you need to use your own credentials.

---

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/gunsugunaydin/Detailed-City-Report-Application
   cd detailed-city-report
   ```
2. Build the project:
   ```bash
   mvn clean install
   ```
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
Alternatively:
```bash
# Download the project as a ZIP file and extract it
# Navigate to the extracted folder and open the terminal
code .

# After opening the project, follow the steps below:
mvn clean install
mvn spring-boot:run
```
---
## Usage and API Documentation

Explore the API documentation through Swagger at `http://localhost:8080/swagger-ui/index.html`.

![image](https://github.com/user-attachments/assets/52f2799e-2e74-42da-a82d-84741efa401d)

Swagger UI Screenshot

### Endpoints
- `GET /api/v1/city/report-cities`: Fetch city data for predefined cities.
- `POST /api/v1/city/report-dynamic-cities`: Fetch city data for user-provided cities.
- `POST /generate-report`: Generate and download the report via the home page.
- `POST /api/v1/googlesheets/generate-report`: Generate and download the report programmatically.
- `GET /api/v1/weather/report-weather`: Fetch weather data for predefined cities.

---

## Future Enhancements
- **Authentication & Authorization**: Implement OAuth2 for secure API access.
- **Error Handling**: Centralized error handling for API calls.
- **Deployment**: Dockerize the application for seamless deployment.

---

## Limitations
- The application requires a minimum of 10 cities for report generation.
- Google Sheets API must be enabled and credentials configured properly.
- Relies on external APIs for data retrieval; any downtime affects functionality.

---

## Contact
Please feel free to explore the code and share your feedback. I am always open to suggestions.

<img src="https://media.tenor.com/v63_brUy45wAAAAi/peach-goma-love-peach-cat.gif" alt="Get in Touch Gif" width="50" height="50"> Get in Touch:

- **Email**: [gunsugunay98@gmail.com](mailto:gunsugunay98@gmail.com)
- **LinkedIn**: [linkedin.com/in/gunsugunaydin](https://www.linkedin.com/in/gunsugunaydin/)

