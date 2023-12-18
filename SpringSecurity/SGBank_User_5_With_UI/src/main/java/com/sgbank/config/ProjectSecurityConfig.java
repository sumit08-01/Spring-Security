package com.sgbank.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

//Custom Security configuration --->>> 
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.cors().configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://localhost:4200")); // this will allow the request
				config.setAllowedMethods(Collections.singletonList("*")); // all methods
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*")); // all type header
				config.setMaxAge(3600L); // max connection 1hr
				return config;
			}
		}).and().csrf().disable()
				.authorizeHttpRequests((requests) -> requests
						.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans", "/user").authenticated()
						.requestMatchers("/contact", "/notices", "/register").permitAll())
				.formLogin();
		return http.build();
	}

// without this method above method doesn't work
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}
