package com.sgbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//		http.authorizeHttpRequests().anyRequest().authenticated();
//		http.formLogin();
//		return http.build();

		http.authorizeHttpRequests(
				(requests) -> requests
						.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
						.requestMatchers("/contact", "/bankNotices").permitAll());
						formLogin
		return http.build();


	}
}
