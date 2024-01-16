package com.sgbank.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "customer_id")
	private int id;

	private String name;

	private String email;

	@Column(name = "mobile_number")
	private String mobileNumber;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // WRITE _ONLY --> mean, save password in DB and not send to hashPassword to UI
	private String pwd;

	private String role;

	@Column(name = "create_dt")
	private String createDt;

	@JsonIgnore // this annotation mean, we are not sending this filled to UI-application
	@OneToMany(mappedBy = "customer", fetch = FetchType.EAGER) // single record of customer can be mapped to many authorities, EAGER mean --> when trying to load customer details also load the Authorities details also EAGERLY
	private Set<Authority> authorities;

}
