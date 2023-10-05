package com.cryptic.bingo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cryptic.bingo.controller.UserController;
import com.cryptic.bingo.model.GuestUser;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;
import com.cryptic.bingo.service.UserService;

@ExtendWith(MockitoExtension.class)
public class UserControllerTests {
	private UserController controller;
	
	@Mock
	private UserService serviceMock;
	
	@Mock
	private RegisteredUser rUserMock;
	
	@Mock
	private GuestUser gUserMock;

	@BeforeEach
	public void setup() {
		controller = new UserController(serviceMock);
	}
	
	@Test
	public void getRegisteredUserByUsername_callsCorrectMethods() {
		String username = "testuser";
		
		when(serviceMock.getRegisteredUserByName(username)).thenReturn(rUserMock);
		
		User returned = controller.getRegisteredUserByUsername(username);
		
		assertEquals(rUserMock, returned);
		verify(serviceMock, times(1)).getRegisteredUserByName(username);
	}
	
	@Test
	public void saveNewRegisteredUser_callsCorrectMethods() {
		int userID = 2;
		
		when(serviceMock.save(rUserMock)).thenReturn(userID);
		
		int returned = controller.saveNewRegisteredUser(rUserMock);
		
		assertEquals(userID, returned);
		verify(serviceMock, times(1)).save(rUserMock);
	}
	
	@Test
	public void saveNewGuestUser_callsCorrectMethods() {
		int userID = 2;
		
		when(serviceMock.save(gUserMock)).thenReturn(userID);
		
		int returned = controller.saveNewGuestUser(gUserMock);
		
		assertEquals(userID, returned);
		verify(serviceMock, times(1)).save(gUserMock);
	}
	
}
