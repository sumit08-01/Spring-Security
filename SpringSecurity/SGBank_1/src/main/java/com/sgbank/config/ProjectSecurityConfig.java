package com.sgbank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
		
// Default security configuration --->>>
//		http.authorizeHttpRequests().anyRequest().authenticated();
//		http.formLogin();
//		return http.build();

		
//Custom Security configuration --->>> 
		http.authorizeHttpRequests(
				(requests) -> requests
						.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
						.requestMatchers("/contact", "/bankNotices").permitAll()).formLogin();						
		return http.build();


//Deny all requests --->>> Not for production
//		http.authorizeHttpRequests().anyRequest().denyAll()
//		.and().formLogin()
//		.and().httpBasic();
//		return http.build();
		

//permit all requests --->>> Not for production, expose without any security
//		http.authorizeHttpRequests().anyRequest().permitAll()
//		.and().formLogin()
//		.and().httpBasic();
//		return http.build();
	}
}
