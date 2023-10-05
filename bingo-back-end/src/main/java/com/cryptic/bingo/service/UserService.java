package com.cryptic.bingo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.dal.UserRepository;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.GuestUser;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;

@Service
public class UserService {
	private UserRepository userepo;
	private RegisteredUserRepository registeredUserRepo;
	private PasswordEncoder encoder;

	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	public UserService(UserRepository userepo, RegisteredUserRepository registeredUserRepo, PasswordEncoder encoder) {
		super();
		this.userepo = userepo;
		this.registeredUserRepo = registeredUserRepo;
		this.encoder = encoder;
	}

	public RegisteredUser getRegisteredUserByName(String username) {
		logger.trace(String.format("Get registered user by name called. Username = %s ", username));

		return registeredUserRepo.findByUsername(username)
				.orElseThrow(() -> new NotFoundException(String.format("User: '%s' not found", username)));
	}

	public int save(User user) {
		logger.trace(String.format("save new user called. %s", user));

		// This is before checking if user already exists due to low performance and
		// updateGuestUserToRegisteredUser needing it
		this.checkUserName(user);

		// Check if user already exists
		User userFromDb = this.userepo.findById(user.getId()).orElse(null);
		if (userFromDb != null) {
			// If user from database is a RegisteredUser
			// If user is a guest updating it to a registeredUser is expected
			if (userFromDb.getClass() == RegisteredUser.class)
				throw new BadRequestException("Trying to update exsiting registered user");
			else if (user.getClass() == RegisteredUser.class) {
				this.updateGuestUserToRegisteredUser((RegisteredUser) user, (GuestUser) userFromDb);
				return user.getId();
			}

			else
				throw new BadRequestException("Trying to update exsiting user");
		}

		if (user.getClass() == RegisteredUser.class) {
			return this.save((RegisteredUser) user);

		} else {
			logger.trace("Saving guestUser object");
			this.userepo.save(user);
			return user.getId();
		}
	}

	private void updateGuestUserToRegisteredUser(RegisteredUser userToSave, GuestUser userFromDb) {
		// Check registered user and perform needed changes
		this.checkPasswordAndExists(userToSave);

		logger.trace("Encoding password");
		userToSave.setPassword(encoder.encode(userToSave.getPassword()));

		logger.trace("Updating guest user to registered user");
		logger.debug(String.format("Attempting to update guest userID: %d into RegisteredUser: %s", userToSave.getId(),
				userToSave));

		this.userepo.updateGuestUserToRegistered(userToSave.getId(), userToSave.getUsername(),
				userToSave.getPassword());
	}

	private int save(RegisteredUser user) {
		this.checkPasswordAndExists(user);

		logger.trace("Encoding password");
		user.setPassword(encoder.encode(user.getPassword()));

		logger.trace("Saving RegisteredUser object");
		this.registeredUserRepo.save(user);

		return user.getId();
	}

	public void saveAll(List<User> users) {
		logger.trace(String.format("save all called. Saving %d users", users.size()));

		for (User user : users) {
			this.save(user);
		}
	}

	private boolean RegisteredUserExistsByUsername(String username) {
		logger.trace(String.format("Registered user exists by username or id called. Username = %s", username));

		return this.registeredUserRepo.existsByUsername(username);
	}

	private void checkPasswordAndExists(RegisteredUser user) {
		logger.trace(String.format("save new registered user called. %s", user));

		if (user.getPassword().trim().length() == 0) {
			throw new BadRequestException("Username or password is null");
		}

		if (this.RegisteredUserExistsByUsername(user.getUsername())) {
			throw new BadRequestException(
					String.format("%s already exists - username matches a registered user", user));
		}
	}

	private void checkUserName(User user) {
		logger.trace(String.format("save new guest user called. %s", user));

		if (user.getUsername().trim().length() == 0) {
			throw new BadRequestException("Username is null");
		}
	}

	public void update(User user, User userFromDb) {
		// Check user are of the same type
		if (userFromDb.getClass() != user.getClass())
			throw new BadRequestException("Users are not of matching classes");

		// If guest user check username and save
		if (user.getClass() != RegisteredUser.class) {
			this.checkUserName(user);

			logger.trace("Updating guestUser object");
			this.userepo.save(user);
			return;
		}

		// If registered user
		RegisteredUser registeredUser = (RegisteredUser) user;
		RegisteredUser registeredUserFromDb = (RegisteredUser) userFromDb;

		// Check if name has updated
		if (!registeredUser.getUsername().equals(userFromDb.getUsername())) {
			this.checkUserName(user);
			this.checkPasswordAndExists(registeredUser);
		}

		// Check if password is not null and not the current
		String updatedPassword = registeredUser.getPassword();

		if (!updatedPassword.trim().equals("") && !updatedPassword.equals(registeredUserFromDb.getPassword())) {
			registeredUser.setPassword(encoder.encode(updatedPassword));
		}

		logger.trace("Updating RegisteredUser object");
		this.registeredUserRepo.save(registeredUser);
	}

	public void update(GuestUser user) {
		logger.trace(String.format("update guest user called. %s", user));

		// Checks if user exists
		// Using bad request not not found Exception because its on the user to provide
		// a valid user to update
		User userFromDb = this.userepo.findById(user.getId()).orElseThrow(
				() -> new BadRequestException(String.format("UserId: %d not found, cannot update", user.getId())));

		this.update(user, userFromDb);
	}

	public void update(RegisteredUser user, Authentication auth) {
		logger.trace(String.format("update registered user called. %s", user));

		// Checks if user exists
		// Using bad request not not found Exception because its on the user to provide
		// a valid user to update
		User userFromDb = this.userepo.findById(user.getId()).orElseThrow(
				() -> new BadRequestException(String.format("UserId: %d not found, cannot update", user.getId())));

		if (!auth.getName().equals(userFromDb.getUsername())) {
			throw new BadRequestException(
					String.format("User to update does not match logged in user: %s", auth.getName()));
		}

		this.update(user, userFromDb);
	}

}
