package com.sgbank.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.sgbank.model.Authority;
import com.sgbank.model.Customer;
import com.sgbank.repository.CustomerRepository;

@Component
public class SGBankUsernamePwdAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName(); // getting username from the UI entered by the user
		String pwd = authentication.getCredentials().toString(); // getting password from the UI entered by the user
		System.out.println("username and pwd from UI: " + username + " " + pwd);
		List<Customer> customer = customerRepository.findByEmail(username); // getting password from the DB(encrypted pwd)
		if (customer.size() > 0) {
			if (passwordEncoder.matches(pwd, customer.get(0).getPwd())) { // PasswordEncoder matches method, match the password if okay return true(Returns true if the encoded password should be encoded
																		  // again for better security,else false. The default implementation always returns false.)
				System.out.println("Password fetch from DB : " + customer.get(0).getPwd());
//				List<GrantedAuthority> authorities = new ArrayList<>();
//				authorities.add(new SimpleGrantedAuthority(customer.get(0).getRole())); // this will create a object of SimpleGrantedAuthority and get role from the DB and send to this method
//				return new UsernamePasswordAuthenticationToken(username, pwd, authorities); // this will return the object of the Authentication
				return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(customer.get(0).getAuthorities()));	// this will return the object of the Authentication but own create getAuthority method																
																							
			} else {
				throw new BadCredentialsException("Invalid User");
			}
		} else {
			throw new BadCredentialsException("No user registed with this details");
		}
	}

	@SuppressWarnings("unused")
	private List<GrantedAuthority> getGrantedAuthorities(Set<Authority> authorities) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
		for (Authority authority : authorities) {
			grantedAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
		}
		return grantedAuthorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}

}
