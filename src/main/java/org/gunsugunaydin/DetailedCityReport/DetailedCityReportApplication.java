package org.gunsugunaydin.DetailedCityReport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DetailedCityReportApplication {

	public static void main(String[] args) {
		SpringApplication.run(DetailedCityReportApplication.class, args);
	}
}
