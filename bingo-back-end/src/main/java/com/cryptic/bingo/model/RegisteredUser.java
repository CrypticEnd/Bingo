package com.cryptic.bingo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Registered")
public class RegisteredUser extends User {
	private String password;

	public RegisteredUser(String userName, String password) {
		super(userName);
		this.password = password;
	}

	public RegisteredUser() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return String.format(
				"Registered-User [id = %d, userName = %s ]", getId(),
				getUsername());
	}

}
