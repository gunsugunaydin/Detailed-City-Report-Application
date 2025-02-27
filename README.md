# Detailed City Report Platform <img src="https://media.tenor.com/TwkVSDkjf1MAAAAj/dudu-bubu-raining.gif" alt="Rainy Gif" width="50" height="50">

## Overview  
The **Detailed City Report Platform** is a Spring Boot-based solution (combining both an application and an API) designed to generate detailed reports for a list of cities. The platform retrieves data from external APIs, formats it into a Google Spreadsheet, and downloads it as a PDF. It includes both backend and frontend components to deliver a seamless experience.  

## Demo

Below is a quick demo of the **Detailed City Report Platform**. The demo is divided into two parts:

### Part 1: Downloading the Report to the Default Downloads Folder

https://github.com/user-attachments/assets/560e7d74-c0cb-4696-a5be-f4671bd5c0bb

### Part 2: Downloading the Report to a Specified File Path

https://github.com/user-attachments/assets/4d559f0f-6c48-4d3d-83dc-4840722672b2

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
5. Obtain and add your own Google Cloud service account credentials (credentials.json) to the project. Ensure that your credentials file is properly configured to enable access to Google Sheets API.

---

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/gunsugunaydin/Detailed-City-Report-Platform
   cd detailed-city-report-platform
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

![image](https://github.com/user-attachments/assets/839a8ba6-9d7d-45f2-af77-ea4f3e6e7679)

Swagger UI Screenshot

### Endpoints
- `GET /api/v1/city/report-cities`: Fetch city data for predefined cities.
- `POST /api/v1/city/report-dynamic-cities`: Fetch city data for user-provided cities.
- `POST /generate-report`: Generate and download the report via the home page.
- `POST /api/v1/googlesheets/generate-report`: Generate and download the report programmatically.
- `GET /api/v1/weather/report-weather`: Fetch weather data for predefined cities.

---

## Future Enhancements
- **Authentication & Authorization**: Implement OAuth2 or another secure authentication mechanism for restricted access to certain platform features or API endpoints.
- **Error Handling**: Implement centralized error handling with proper logging and response codes to improve debugging and user experience.
- **Deployment**: Dockerize the platform for seamless deployment.

---

## Limitations
- The platform requires a minimum of 10 cities for report generation.
- Google Sheets API must be enabled, and credentials configured properly in order for the platform to interact with Google Sheets.
- Relies on external APIs for data retrieval; any downtime or rate limits imposed by the external APIs may affect the platform's functionality and data accuracy.

---

## Contact
Please feel free to explore the code and share your feedback. I am always open to suggestions and collaboration.

<img src="https://kawaiihoshi.com/wp-content/uploads/2023/07/1-peach-goma-animations.gif" alt="Get in Touch Gif" width="50" height="50"> Get in Touch:

- **Email**: [gunsugunay98@gmail.com](mailto:gunsugunay98@gmail.com)
- **LinkedIn**: [linkedin.com/in/gunsugunaydin](https://www.linkedin.com/in/gunsugunaydin/)

