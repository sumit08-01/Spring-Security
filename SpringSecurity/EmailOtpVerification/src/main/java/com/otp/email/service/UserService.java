package com.otp.email.service;

import com.otp.email.model.User;
import com.otp.email.register.UserRegister;
import com.otp.email.response.UserResponse;

public interface UserService {

	UserResponse register(UserRegister userRegister);

	void verify(String email, String otp);

	User login(String email, String password);
}
