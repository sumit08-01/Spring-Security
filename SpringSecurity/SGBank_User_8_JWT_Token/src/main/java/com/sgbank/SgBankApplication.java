package com.sgbank;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class SgBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgBankApplication.class, args);
	}
}
