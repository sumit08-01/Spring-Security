package com.sgbank.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {

	@Id // this id showing this id field will act as a primary key in my table
//	@GeneratedValue(strategy = GenerationType.AUTO) // and this will be auto generated
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native") // this will generate id auto by DB 
	@GenericGenerator(name = "native", strategy = "native")
	private int id;
	private String email;
	private String pwd;
	private String role;
	
	public Customer() {
	}

	public Customer(int id, String email, String pwd, String role) {
		super();
		this.id = id;
		this.email = email;
		this.pwd = pwd;
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
}
