package com.cryptic.bingo.dal;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cryptic.bingo.model.RegisteredUser;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer> {
	public Optional<RegisteredUser> findByUsername(String username);

	public boolean existsByUsername(String username);
}
