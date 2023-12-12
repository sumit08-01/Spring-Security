package com.sgbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

//Custom Security configuration --->>> 
	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans")
						.authenticated().requestMatchers("/contact", "/bankNotices", "/register").permitAll())
				.formLogin();
		return http.build();
	}

// without this method above method doesn't work
	@Bean 
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
}

