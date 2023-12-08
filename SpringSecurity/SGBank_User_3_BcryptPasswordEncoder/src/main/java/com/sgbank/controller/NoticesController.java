package com.sgbank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticesController {

	@GetMapping("/bankNotices")
	public String getNotices() {
		return "Here Notices Details from the DB";
	}
}
