package org.gunsugunaydin.DetailedCityReport.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.*;

@Configuration
@OpenAPIDefinition(
  info = @Info(
    title = "DETAILED CITY REPORT API",
    version = "Version 1.0",
    description = "Detailed City Report API developed with Spring Boot. This API provides detailed city and weather data.",
    contact = @Contact(
      name = "Günsu Günaydin", 
      email = "gunsugunay98@gmail.com", 
      url = "https://www.linkedin.com/in/gunsugunaydin/"
    ),
    license = @License(
      name = "Apache 2.0", 
      url = "https://www.apache.org/licenses/LICENSE-2.0"
    )
  )
)
public class SwaggerConfig {
  
}