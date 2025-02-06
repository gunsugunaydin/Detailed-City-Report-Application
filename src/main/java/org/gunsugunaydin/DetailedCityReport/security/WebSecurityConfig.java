package org.gunsugunaydin.DetailedCityReport.security;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
    
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        
        http
            .authorizeHttpRequests(requests -> 
                requests.anyRequest().permitAll() 
            )
            .csrf(csrf -> csrf.disable()) 
            .headers(headers -> 
                headers.frameOptions(options -> options.sameOrigin()) 
            );
        
        return http.build(); 
    }
}    