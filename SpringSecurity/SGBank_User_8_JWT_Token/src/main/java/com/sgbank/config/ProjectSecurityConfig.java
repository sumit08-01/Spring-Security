package com.sgbank.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.sgbank.filter.AuthoritiesLoggingAfterFilter;
import com.sgbank.filter.AuthoritiesLoggingAtFilter;
import com.sgbank.filter.CsrfCookieFilter;
import com.sgbank.filter.JWTTokenGeneratorFilter;
import com.sgbank.filter.JWTTokenValidationFilter;
import com.sgbank.filter.RequestValidationBeforeFilter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

//Custom Security configuration --->>> 
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		CsrfTokenRequestAttributeHandler requestsHandler = new CsrfTokenRequestAttributeHandler();
		requestsHandler.setCsrfRequestAttributeName("_csrf");

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))// here we are not want to create the JsessionID for token for the UI, we are create the own Token
		.cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
				config.setAllowedMethods(Collections.singletonList("*"));
				config.setAllowCredentials(true);
				config.setAllowedHeaders(Collections.singletonList("*"));
				config.setExposedHeaders(Arrays.asList("Authorization")); // for UI we sending the Token for Header as Authorization as a header
				config.setMaxAge(3600L); // max connection 1hr
				return config;
			}
		})).csrf((csrf) -> csrf.csrfTokenRequestHandler(requestsHandler)
						.ignoringRequestMatchers("/contact", "/register")
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))

				.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class)
				.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
				.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new JWTTokenValidationFilter(), BasicAuthenticationFilter.class)
				.authorizeHttpRequests((requests) -> requests

// By Role Methods
						.requestMatchers("/myAccount").hasRole("USER")
						.requestMatchers("/myBalance").hasAnyRole("USER", "ADMIN")
						.requestMatchers("/myLoans").hasRole("USER")
						.requestMatchers("/myCards").hasRole("USER")
						.requestMatchers("/user").authenticated()
						.requestMatchers("/contact", "/notices", "/register").permitAll())
				.formLogin(Customizer.withDefaults())
				.httpBasic(Customizer.withDefaults());
		return http.build();
	}

// without this method above method doesn't work
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}

//.and().csrf().ignoringRequestMatchers("/contact", "/register") // we are ignoring csrf for these two apis,
// because it's public, and not including
// notices because it's get request and get request is open for every one.