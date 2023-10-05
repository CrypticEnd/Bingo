package com.cryptic.bingo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.GuestUser;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;
import com.cryptic.bingo.service.UserService;

@RestController
@RequestMapping("user")
public class UserController {
	private UserService userService;
	
	private Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping("registered/{username}")
	public User getRegisteredUserByUsername(@PathVariable String username) {
		logger.trace("ENDPOINT : Get registered user called. Prams :" + username);
		
		User user = userService.getRegisteredUserByName(username);

		return user;
	}

	@PostMapping("registered")
	@ResponseStatus(HttpStatus.CREATED)
	public int saveNewRegisteredUser(@RequestBody RegisteredUser user) {
		logger.trace("ENDPOINT : Save registered user called. Body" + user);
		
		return this.userService.save(user);
	}
	
	@PutMapping("registered")
	public void updatedRegisteredUser(@RequestBody RegisteredUser user, Authentication auth) {
		logger.trace("ENDPOINT : Update registered user called. Body" + user);
		
		this.userService.update(user, auth);
	}
	
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public int saveNewGuestUser(@RequestBody GuestUser user) {
		logger.trace("ENDPOINT : Save guest user called. Body" + user);
		
		return this.userService.save(user);
	}
	
	@PutMapping()
	public void updateGuestUser(@RequestBody GuestUser user) {
		logger.trace("ENDPOINT : update guest user called. Body" + user);
		
		this.userService.update(user);
	}
	
}
