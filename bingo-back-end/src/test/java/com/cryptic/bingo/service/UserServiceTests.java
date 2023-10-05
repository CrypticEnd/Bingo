package com.cryptic.bingo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.dal.UserRepository;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.GuestUser;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;
import com.cryptic.bingo.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {
	private UserService service;
	
	@Mock
	private UserRepository userepoMock;
	
	@Mock
	private RegisteredUserRepository registeredUserRepoMock;
	
	@Mock
	private PasswordEncoder encoderMock;
	
	@Mock
	private RegisteredUser rUserMock;
	
	@BeforeEach
	public void setup() {
		service = new UserService(userepoMock, registeredUserRepoMock, encoderMock);
	}
	
	@Test
	public void getRegisteredUserByName_throwsNotFound_onEmptyOptional() {
		String username = "testusername";
		
		when(registeredUserRepoMock.findByUsername(username)).thenReturn(Optional.empty());
		
		assertThrows(NotFoundException.class, ()->{
			service.getRegisteredUserByName(username);
		});
	}
	
	@Test
	public void getRegisteredUserByName_callCorrectMethods() {
		String username = "testusername";
		
		when(registeredUserRepoMock.findByUsername(username)).thenReturn(Optional.of(rUserMock));
		
		RegisteredUser returned = service.getRegisteredUserByName(username);
		
		assertEquals(rUserMock, returned);
		verify(registeredUserRepoMock, times(1)).findByUsername(username);
	}
	
	@Test
	public void save_RegisteredUser_throwsBadRequest_OnBaduserName() {
		int id = 5;
		String username = " ";
		String password = "password";
		
		RegisteredUser user = new RegisteredUser(username, password);
		user.setId(id);
		
		assertThrows(BadRequestException.class, ()->{
			service.save(user);
		});
	}
	
	@Test
	public void save_RegisteredUser_throwsBadRequest_OnBadPassword() {
		int id = 5;
		String username = "username";
		String password = "";
		
		RegisteredUser user = new RegisteredUser(username, password);
		user.setId(id);
		
		assertThrows(BadRequestException.class, ()->{
			service.save(user);
		});
	}
	
	@Test
	public void save_RegisteredUser_throwsBadRequest_WhenUserNameExsists() {
		int id = 5;
		String username = "username";
		String password = "password";
		
		RegisteredUser user = new RegisteredUser(username, password);
		user.setId(id);
		
		when(registeredUserRepoMock.existsByUsername(username)).thenReturn(true);
		
		assertThrows(BadRequestException.class, ()->{
			service.save(user);
		});
		
		verify(registeredUserRepoMock, times(1)).existsByUsername(username);
		verify(userepoMock, times(0)).existsById(id);
	}
	
	@Test
	public void save_RegisteredUser_throwsBadRequest_WhenUserIdExsists() {
		int id = 5;
		String username = "username";
		String password = "password";
		
		RegisteredUser user = new RegisteredUser(username, password);
		user.setId(id);
		
		when(registeredUserRepoMock.existsByUsername(username)).thenReturn(false);
		when(userepoMock.existsById(id)).thenReturn(true);
		
		assertThrows(BadRequestException.class, ()->{
			service.save(user);
		});
		
		verify(registeredUserRepoMock, times(1)).existsByUsername(username);
		verify(userepoMock, times(1)).existsById(id);
	}
	
	@Test
	public void save_RegisteredUser_callsCorrectMethods_andReturnsId() {
		int id = 5;
		String username = "username";
		String password = "password";
		String encodedPassword = "password123";
		
		RegisteredUser user = new RegisteredUser(username, password);
		user.setId(id);
		
		when(registeredUserRepoMock.existsByUsername(username)).thenReturn(false);
		when(userepoMock.existsById(id)).thenReturn(false);
		when(encoderMock.encode(password)).thenReturn(encodedPassword);
		
		int returnedId = service.save(user);

		assertEquals(id, returnedId);
		verify(registeredUserRepoMock, times(1)).existsByUsername(username);
		verify(userepoMock, times(1)).existsById(id);
		verify(encoderMock, times(1)).encode(password);
		verify(registeredUserRepoMock, times(1)).save(user);
	}
	
	@Test
	public void saveAll_RegisteredUser_callsCorrectMethods_correctNumberOfTimes() {
		int numberOfTimes = 10;
		String username = "username";
		String password = "password";
		String encodedPassword = "password123";
		
		List<User> users = new ArrayList<>();
		for (int i = 0; i < numberOfTimes; i++) {
			users.add(new RegisteredUser(username, password));
		}
			
		when(registeredUserRepoMock.existsByUsername(username)).thenReturn(false);
		when(userepoMock.existsById(anyInt())).thenReturn(false);
		when(encoderMock.encode(password)).thenReturn(encodedPassword);
		
		service.saveAll(users);

		verify(registeredUserRepoMock, times(numberOfTimes)).existsByUsername(username);
		verify(userepoMock, times(numberOfTimes)).existsById(anyInt());
		verify(encoderMock, times(numberOfTimes)).encode(password);
		verify(registeredUserRepoMock, times(numberOfTimes)).save(any());
	}
	
	@Test
	public void save_guestUser_throwsBadRequest_OnBaduserName() {
		int id = 5;
		String username = " ";
		
		GuestUser user = new GuestUser(username);
		user.setId(id);
		
		assertThrows(BadRequestException.class, ()->{
			service.save(user);
		});
	}
	
	@Test
	public void save_guestUser_callsCorrectMethods() {
		int id = 5;
		String username = "user";
		
		GuestUser user = new GuestUser(username);
		user.setId(id);
		
		int returnedId = service.save(user);
		
		assertEquals(id, returnedId);
		verify(userepoMock, times(1)).save(user);

	}
}
