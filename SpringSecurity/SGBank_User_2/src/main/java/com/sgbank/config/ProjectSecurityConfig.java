package com.sgbank.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // saying it's a configuration class
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//Custom Security configuration --->>> 
		http.authorizeHttpRequests(
				(requests) -> requests.requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans")
						.authenticated().requestMatchers("/contact", "/bankNotices").permitAll())
				.formLogin();
		return http.build();
	}

	/*
	 * Approach --> 1 : I where we use withDefaultPasswordEncoder() method while
	 * creating the users detaisl
	 */
//	@Bean
//	public InMemoryUserDetailsManager userDetailsManager() {
//
//		UserDetails admin = User.withDefaultPasswordEncoder().username("admin").password("12345").authorities("admin")
//				.build();
//
//		UserDetails user = User.withDefaultPasswordEncoder().username("user").password("12345").authorities("read")
//				.build();
//		return new InMemoryUserDetailsManager(admin, user);
//	}
	/*
	 * Approach --> 2 : In Which we create a bean of PasswordEncoder separately
	 * 
	 */
//	@Bean
//	public InMemoryUserDetailsManager userDetailsManager() {
//
//		InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
//
//		UserDetails admin = User.withUsername("admin").password("12345").authorities("admin").build();
//
//		UserDetails user = User.withUsername("user").password("12345").authorities("read").build();
//		detailsManager.createUser(admin);
//		detailsManager.createUser(user);
//		return detailsManager;
//	}
//	
// without this method above method doesn't work
	@Bean 
	public PasswordEncoder encoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
// this method is saying we use jdbc style of saving user and password in physical database
	@Bean
	public UserDetailsService userDetails(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}
}

