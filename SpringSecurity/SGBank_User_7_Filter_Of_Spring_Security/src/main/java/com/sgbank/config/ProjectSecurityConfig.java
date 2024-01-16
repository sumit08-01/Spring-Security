package com.sgbank.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import com.sgbank.filter.RequestValidationBeforeFilter;

import jakarta.servlet.http.HttpServletRequest;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

//Custom Security configuration --->>> 
	@SuppressWarnings("removal")
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		CsrfTokenRequestAttributeHandler requestsHandler = new CsrfTokenRequestAttributeHandler();
		requestsHandler.setCsrfRequestAttributeName("_csrf"); // if we are not mention this line spring by default
																// consider this line and attribute name "_csrf"

		http.securityContext().requireExplicitSave(true).and()
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.NEVER))
				.cors().configurationSource(new CorsConfigurationSource() {
					@Override
					public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
						CorsConfiguration config = new CorsConfiguration();
						config.setAllowedOrigins(Collections.singletonList("http://localhost:4200")); // this will allow the request from UI
						config.setAllowedMethods(Collections.singletonList("*")); // all methods
						config.setAllowCredentials(true);
						config.setAllowedHeaders(Collections.singletonList("*")); // all type header
						config.setMaxAge(3600L); // max connection 1hr
						return config;
					}
				}).and()
				.csrf((csrf) -> csrf.csrfTokenRequestHandler(requestsHandler)
						.ignoringRequestMatchers("/contact", "/register")
						.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())) // that persists the CSRF token in a cookie named * "XSRF-TOKEN" and reads from the header  "X-XSRF-TOKEN"
																							 // following the conventions of * AngularJS. When using with AngularJS be sure to use {@link #withHttpOnlyFalse()}.
				.addFilterAfter(new CsrfCookieFilter(), BasicAuthenticationFilter.class)
				.addFilterBefore(new RequestValidationBeforeFilter(), BasicAuthenticationFilter.class) // --AddFilterBefore
				.addFilterAt(new AuthoritiesLoggingAtFilter(), BasicAuthenticationFilter.class) // --AddFilterAt
				.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class) // --AddFilterAfter
				.authorizeHttpRequests((requests) -> requests
// By Authority Methods
//						.requestMatchers("/myAccount").hasAuthority("VIEWACCOUNT")
//						.requestMatchers("/myBalance").hasAnyAuthority("VIEWACCOUNT","VIEWBALANCE")
//						.requestMatchers("/myLoans").hasAuthority("VIEWLOANS")
//						.requestMatchers("/myCards").hasAuthority("VIEWCARDS")
						
// By Role Methods
						.requestMatchers("/myAccount").hasRole("VIEWACCOUNT")
						.requestMatchers("/myBalance").hasAnyRole("VIEWACCOUNT","VIEWBALANCE")
						.requestMatchers("/myLoans").hasRole("VIEWLOANS")
						.requestMatchers("/myCards").hasRole("VIEWCARDS")
						.requestMatchers("/user").authenticated()
						
//						.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans", "/user").authenticated()
						.requestMatchers("/contact", "/notices", "/register").permitAll())
//				.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
				.formLogin();
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