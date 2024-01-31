package com.otp.email.serviceImpl;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.otp.email.model.User;
import com.otp.email.register.UserRegister;
import com.otp.email.repository.UserRepository;
import com.otp.email.response.UserResponse;
import com.otp.email.service.EmailService;
import com.otp.email.service.UserService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final EmailService emailService;

	@Override
	public UserResponse register(UserRegister userRegister) {
		User exUser = userRepository.findByEmail(userRegister.getEmail());
		if (exUser != null && exUser.isVerified()) {
			throw new RuntimeException("User is already register with this email");
		}
		User user = User.builder().userName(userRegister.getUserName()).email(userRegister.getEmail())
				.password(userRegister.getPassword()).build();
		String otp = generateOtp();
		user.setOtp(otp);
		User savedUser = userRepository.save(user);
		try {
			sendVerificationEmail(savedUser.getEmail(), otp);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		UserResponse userResponse = UserResponse.builder().email(user.getEmail()).userName(user.getUserName()).build();
		return userResponse;
	}

	private String generateOtp() {
		Random random = new Random();
		int otpValue = 100000 + random.nextInt(900000);
		return String.valueOf(otpValue);
	}

	private void sendVerificationEmail(String email, String otp) throws MessagingException {
		String subject = "Email verifcation";
		String body = "Your verifcation Otp is " + otp;
		emailService.sendMail(email, subject, body);

	}

	@Override
	public void verify(String email, String otp) {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new RuntimeException("User Not found");
		} else if (user.isVerified()) {
			throw new RuntimeException("User is already verified");
		} else if (otp.equals(user.getOtp())) {
			user.setVerified(true);
			userRepository.save(user);
		} else {
			throw new RuntimeException("Internal Server Error");
		}
	}

	@Override
	public User login(String email, String password) {
		User byEmail = userRepository.findByEmail(email);
		if (byEmail != null && byEmail.isVerified() && byEmail.getPassword().equals(password)) {
			return byEmail;
		} else {
			throw new RuntimeException("Internal Server Error");
		}
	}
}
