package com.basic_Ex_1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller_Ex1 {

	@GetMapping("/s")
	public String name() {
		return "Welcome Spring Application with spring Security";
	}
}
