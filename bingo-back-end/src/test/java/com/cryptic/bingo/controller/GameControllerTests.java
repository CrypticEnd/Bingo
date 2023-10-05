package com.cryptic.bingo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.cryptic.bingo.controller.GameController;
import com.cryptic.bingo.dto.BoardTileDTO;
import com.cryptic.bingo.dto.GameCardDTO;
import com.cryptic.bingo.dto.GameSettingsDTO;
import com.cryptic.bingo.dto.GameSettingsToDisplayDTO;
import com.cryptic.bingo.exceptions.ForbiddenException;
import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.service.GameService;

@ExtendWith(MockitoExtension.class)
public class GameControllerTests {
	private GameController controller;

	@Mock
	GameService serviceMock;

	@Mock
	Game gameMock;

	@Mock
	Authentication authMock;

	@Mock
	GameSettingsDTO gameSettingsDTOMock;

	@Mock
	GameSettingsToDisplayDTO gameSettingsToDisplayDTOMock;

	@BeforeEach
	public void setup() {
		controller = new GameController(serviceMock);
	}

	@Test
	public void createGame_callsCorrectMethods() {
		String username = "usernametest";
		int gameid = 5;

		when(authMock.isAuthenticated()).thenReturn(true);
		when(authMock.getName()).thenReturn(username);
		when(serviceMock.createGame(username)).thenReturn(gameid);

		int returned = controller.createGame(authMock);

		assertEquals(gameid, returned);
		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(1)).getName();
		verify(serviceMock, times(1)).createGame(username);
	}

	@Test
	public void createGame_throwsExcpectionOnNullAuth() {
		assertThrows(ForbiddenException.class, () -> {
			controller.createGame(null);
		});
	}

	@Test
	public void createGame_throwsExcpectionOnNotAuthenticated() {	
		when(authMock.isAuthenticated()).thenReturn(false);

		
		assertThrows(ForbiddenException.class, ()->{
			controller.createGame(authMock);
		});
	}

	@Test
	public void getGameBoard_callCorrectMethods() {
		int gamecode = 5;
		BoardTileDTO[][] expected = new BoardTileDTO[5][];

		when(serviceMock.getGameBoard(gamecode)).thenReturn(expected);

		BoardTileDTO[][] returned = controller.getGameBoard(gamecode);

		assertEquals(expected, returned);
		verify(serviceMock, times(1)).getGameBoard(gamecode);
	}

	@Test
	public void doesGameCodeExist_callCorrectMethods_true() {
		int gamecode = 5;
		Boolean expected = true;

		when(serviceMock.doesGameCodeExist(gamecode)).thenReturn(expected);

		Boolean returned = controller.doesGameCodeExist(gamecode);

		assertEquals(expected, returned);
		verify(serviceMock, times(1)).doesGameCodeExist(gamecode);
	}

	@Test
	public void doesGameCodeExist_callCorrectMethods_false() {
		int gamecode = 5;
		Boolean expected = false;

		when(serviceMock.doesGameCodeExist(gamecode)).thenReturn(expected);

		Boolean returned = controller.doesGameCodeExist(gamecode);

		assertEquals(expected, returned);
		verify(serviceMock, times(1)).doesGameCodeExist(gamecode);
	}

	@Test
	public void getGameById_callCorrectMethods() {
		int gameid = 5;

		when(serviceMock.getGameById(gameid)).thenReturn(gameMock);

		Game returned = controller.getGameById(gameid);

		assertEquals(gameMock, returned);
		verify(serviceMock, times(1)).getGameById(gameid);
	}

	@Test
	public void deleteGameById_callCorrectMethods() {
		int gameid = 5;

		controller.deleteGameById(gameid);

		verify(serviceMock, times(1)).deleteGameById(gameid);
	}

	@Test
	public void hostGame_callCorrectMethods() {
		int gameid = 5;

		when(serviceMock.hostGameById(gameSettingsDTOMock, gameid)).thenReturn(gameid);

		int returned = controller.hostGame(gameSettingsDTOMock, gameid);

		assertEquals(gameid, returned);
		verify(serviceMock, times(1)).hostGameById(gameSettingsDTOMock, gameid);
	}

	@Test
	public void endHostGameByJoinCode_callsCorrectMethods() {
		String username = "usernametest";
		int joincode = 5;

		when(authMock.isAuthenticated()).thenReturn(true);
		when(authMock.getName()).thenReturn(username);

		controller.endHostGameByJoinCode(joincode, authMock);

		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(1)).getName();
		verify(serviceMock, times(1)).endHostGameByJoinCode(joincode, username);
	}

	@Test
	public void endHostGameByJoinCode_throwsExcpectionOnNullAuth() {
		int joincode = 5;
		assertThrows(ForbiddenException.class, () -> {
			controller.endHostGameByJoinCode(joincode, null);
		});
	}

	@Test
	public void endHostGameByJoinCode_throwsExcpectionOnNotAuthenticated() {
		int joincode = 5;
		when(authMock.isAuthenticated()).thenReturn(false);

		assertThrows(ForbiddenException.class, () -> {
			controller.endHostGameByJoinCode(joincode, authMock);
		});
	}

	@Test
	public void getAllGamesOwnedByUser_callsCorrectMethods() {
		String username = "testusername";
		GameCardDTO[] expected = new GameCardDTO[2];

		when(authMock.isAuthenticated()).thenReturn(true);
		when(authMock.getName()).thenReturn(username);

		controller.getAllGamesOwnedByUser(authMock);

		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(1)).getName();
		verify(serviceMock, times(1)).getAllGamesOwnedBy(username);
	}

	@Test
	public void getAllGamesOwnedByUser_throwsExcpectionOnNullAuth() {
		assertThrows(ForbiddenException.class, () -> {
			controller.getAllGamesOwnedByUser(null);
		});
	}

	@Test
	public void getAllGamesOwnedByUser_throwsExcpectionOnNotAuthenticated() {	
		when(authMock.isAuthenticated()).thenReturn(false);

		
		assertThrows(ForbiddenException.class, ()->{
			controller.getAllGamesOwnedByUser(authMock);
		});
	}

	@Test
	public void getGameForSetupById_callsCorrectMethods() {
		String username = "testusername";
		int gameId = 5;

		when(authMock.isAuthenticated()).thenReturn(true);
		when(authMock.getName()).thenReturn(username);
		when(serviceMock.getGameForSetupById(gameId, username)).thenReturn(gameSettingsDTOMock);

		GameSettingsDTO returned = controller.getGameForSetupById(gameId, authMock);

		assertEquals(gameSettingsDTOMock, returned);
		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(1)).getName();
		verify(serviceMock, times(1)).getGameForSetupById(gameId, username);
	}

	@Test
	public void getGameForSetupById_throwsExcpectionOnNullAuth() {
		assertThrows(ForbiddenException.class, () -> {
			controller.getGameForSetupById(5, null);
		});
	}

	@Test
	public void getGameForSetupById_throwsExcpectionOnNotAuthenticated() {	
		when(authMock.isAuthenticated()).thenReturn(false);

		
		assertThrows(ForbiddenException.class, ()->{
			controller.getGameForSetupById(5,authMock);
		});
	}

	@Test
	public void UpdateGameSettings_callCorrectMethods() {
		int gameid = 5;

		controller.UpdateGameSettings(gameSettingsDTOMock, gameid);

		verify(serviceMock, times(1)).updateGameSettings(gameSettingsDTOMock, gameid);
	}

	@Test
	public void getGameSettingsByGameCode_callCorrectMethods() {
		int gamecode = 5;

		when(serviceMock.getSettingsByGamejoinCode(gamecode, authMock)).thenReturn(gameSettingsToDisplayDTOMock);

		GameSettingsToDisplayDTO returned = controller.getGameSettingsByGameCode(gamecode, authMock);

		assertEquals(gameSettingsToDisplayDTOMock, returned);
		verify(serviceMock, times(1)).getSettingsByGamejoinCode(gamecode, authMock);
	}
}
