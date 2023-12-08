package com.sgbank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sgbank.model.Customer;
import com.sgbank.repository.CustomerRepository;

@RestController
public class LoginController {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder; // autowired for password encoder

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody Customer customer) {
		Customer savedCustomer = null;
		ResponseEntity<String> response = null;
		try {
			String encodedPwd = passwordEncoder.encode(customer.getPwd()); // Bcrypt the password 
			customer.setPwd(encodedPwd); // set the bcrypt password in customer
			savedCustomer = customerRepository.save(customer); // same bcrypte password in DB(hashed password)
			if (savedCustomer.getId() > 0) {
				response = ResponseEntity.status(HttpStatus.CREATED)
						.body("Given user Details are successfully created : " + savedCustomer.getId());
			}
		} catch (Exception e) {
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An Exception occur due to : " + e.getMessage());
		}
		return response;
	}

}
