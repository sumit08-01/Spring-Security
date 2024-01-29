package com.otp.email.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.otp.email.model.User;
import com.otp.email.register.UserRegister;
import com.otp.email.repository.UserRepository;
import com.otp.email.response.UserResponse;
import com.otp.email.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/email/v1")
@RequiredArgsConstructor
public class UserRegisterEmailOtpVerification {

	private final UserService service;

	@PostMapping("/register")
	public ResponseEntity<UserResponse> registerUser(@RequestBody UserRegister userRegister) {
		UserResponse userResponse = service.register(userRegister);
		return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
	}

	@PostMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestParam String email, @RequestParam String otp) {
		service.verify(email, otp);
		return new ResponseEntity<>("User Verified successfully", HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) {
		User user = service.login(email, password);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
}
