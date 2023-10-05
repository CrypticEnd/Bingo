package com.cryptic.bingo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.exceptions.BadRequestException;

@Service
public class AuthUserService implements UserDetailsService{
	private RegisteredUserRepository userRepo;
	
	
	public AuthUserService(RegisteredUserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return new AuthUser(this.userRepo.findByUsername(username).orElseThrow(()->new BadRequestException("Username not found")));
	}

}
