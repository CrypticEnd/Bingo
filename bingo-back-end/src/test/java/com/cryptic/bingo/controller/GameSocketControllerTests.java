package com.cryptic.bingo.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import com.cryptic.bingo.controller.GameSocketController;
import com.cryptic.bingo.service.GameService;

//TODO update tests to check returns on setWinnerGuest
public class GameSocketControllerTests {
	private GameSocketController controller;

	@Mock
	GameService serviceMock;
	
	@Test
	public void setWinnerGuest_callCorrectMethods() {
		int gamecode = 5;
		int userId = 5;

		controller.setWinnerGuest(gamecode, userId);

		verify(serviceMock, times(1)).setBingoGameWinner(gamecode, userId);
	}

	@Test
	public void setWinnerRegistered_callCorrectMethods() {
		int gamecode = 5;
		String username = "username";

		controller.setWinnerRegistered(gamecode, username);

		verify(serviceMock, times(1)).setBingoGameWinner(gamecode, username);
	}
}
