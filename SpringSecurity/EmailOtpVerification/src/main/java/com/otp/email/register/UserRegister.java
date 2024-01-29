package com.otp.email.register;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRegister {

	private String userName;
	private String email;
	private String password;
}
