package com.cryptic.bingo.model;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("Guest")
public class GuestUser extends User {

	public GuestUser(String userName) {
		super(userName);
	}

	public GuestUser() {
		super();
	}

	@Override
	public String toString() {
		return String.format("Guest-User [id = %d, userName = %s ]", getId(), getUsername());
	}
}
