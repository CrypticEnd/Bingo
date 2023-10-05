package com.cryptic.bingo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.h2.H2ConsoleProperties.Settings;
import org.springframework.security.core.Authentication;

import com.cryptic.bingo.dal.BingoMixRepository;
import com.cryptic.bingo.dal.GameRepository;
import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.dal.UserRepository;
import com.cryptic.bingo.dto.GameCardDTO;
import com.cryptic.bingo.dto.GameSettingsDTO;
import com.cryptic.bingo.dto.GameSettingsToDisplayDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.model.GameSettings;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;
import com.cryptic.bingo.service.GameService;

@ExtendWith(MockitoExtension.class)
public class GameServiceTests {
	private GameService service;

	@Mock
	private GameRepository gameRepoMock;

	@Mock
	private RegisteredUserRepository registeredUserRepMock;

	@Mock
	private UserRepository userRepoMock;

	@Mock
	private BingoMixRepository bingoMixRepositoryMock;

	@Mock
	private RegisteredUser registeredUserMock;

	@Mock
	private User userMock;

	@Mock
	private Game gameMock;

	@Mock
	private GameSettings settingsMock;

	@Mock
	private GameSettingsToDisplayDTO gameSettingsToDisplayDTOMock;

	@Mock
	private GameSettingsDTO gameSettingsDTOMock;

	@Mock
	private BingoMix bingoMixMock;

	@Mock
	Authentication authenticationMock;

	@BeforeEach
	public void setup() {
		service = new GameService(gameRepoMock, registeredUserRepMock, userRepoMock, bingoMixRepositoryMock);
	}

	@Test
	public void createGame_throwsNotFound_whenUserNotFound() {
		String ownerName = "test";

		when(registeredUserRepMock.findByUsername(ownerName)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.createGame(ownerName);
		});

		verify(registeredUserRepMock, times(1)).findByUsername(ownerName);
	}

	@Test
	public void createGame_callCorrectMethods() {
		String ownerName = "test";

		when(registeredUserRepMock.findByUsername(ownerName)).thenReturn(Optional.of(registeredUserMock));

		service.createGame(ownerName);

		verify(registeredUserRepMock, times(1)).findByUsername(ownerName);
		verify(gameRepoMock, times(1)).save(any());
	}

	@Test
	public void getGameBoard_throwsNotFound_whenUserNotFound() {
		int gameCode = 555;

		when(gameRepoMock.findByJoinCode(gameCode)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.getGameBoard(gameCode);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gameCode);
	}

	@Test
	public void doesGameCodeExist_callCorrectMethods_andReturnsTrue() {
		boolean expected = true;
		int gameCode = 555;

		when(gameRepoMock.existsByJoinCode(gameCode)).thenReturn(expected);

		boolean returned = service.doesGameCodeExist(gameCode);

		assertEquals(expected, returned);
		verify(gameRepoMock, times(1)).existsByJoinCode(gameCode);
	}

	@Test
	public void doesGameCodeExist_callCorrectMethods_andReturnsFalse() {
		boolean expected = false;
		int gameCode = 555;

		when(gameRepoMock.existsByJoinCode(gameCode)).thenReturn(expected);

		boolean returned = service.doesGameCodeExist(gameCode);

		assertEquals(expected, returned);
		verify(gameRepoMock, times(1)).existsByJoinCode(gameCode);
	}

	@Test
	public void getGameById_throwsNotFound_whenGameNotFound() {
		int gameId = 5;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.getGameById(gameId);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
	}

	@Test
	public void getGameById_callsCorrectMethods() {
		int gameId = 5;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));

		Game returned = service.getGameById(gameId);

		assertEquals(gameMock, returned);
		verify(gameRepoMock, times(1)).findById(gameId);
	}

	@Test
	public void getGameForSetupById_throwsNotFound_whenEmptyGame() {
		int gameId = 5;
		String username = "testname";

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.getGameForSetupById(gameId, username);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
	}

	@Test
	public void getGameForSetupById_throwsBadRequest_whenNotOwner() {
		int gameId = 5;
		String username = "testname";
		String ownerName = "not username";

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getUsername()).thenReturn(ownerName);

		assertThrows(BadRequestException.class, () -> {
			service.getGameForSetupById(gameId, username);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getUsername();
	}

	@Test
	public void getGameForSetupById_throwsBadRequest_whenNotGameNotHoasted() {
		int gameId = 5;
		String username = "testname";
		String ownerName = username;
		int gameJoinCode = 5555;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getUsername()).thenReturn(ownerName);
		when(gameMock.getJoinCode()).thenReturn(gameJoinCode);

		assertThrows(BadRequestException.class, () -> {
			service.getGameForSetupById(gameId, username);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getUsername();
		verify(gameMock, times(1)).getJoinCode();
	}

	@Test
	public void getSettingsByGamejoinCode_throwsNotFound_whenGameNotFound() {
		int gameJoinCode = 5;

		when(gameRepoMock.findByJoinCode(gameJoinCode)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.getSettingsByGamejoinCode(gameJoinCode, authenticationMock);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gameJoinCode);
	}

	@Test
	public void updateGameSettings_throwsNotFound_whenGameNotFound() {
		int gameId = 5;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.updateGameSettings(gameSettingsDTOMock, gameId);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
	}

	@Test
	public void updateGameSettings_callsCorrectMethods_setsMixAndWinnerToNull() {
		int gameId = 5;
		int userId = 10;
		int ownerId = userId;
		int bingoMixId = 7;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getId()).thenReturn(ownerId);
		when(gameSettingsDTOMock.getUserId()).thenReturn(userId);
		when(gameSettingsDTOMock.getMixId()).thenReturn(bingoMixId);
		when(bingoMixRepositoryMock.findById(bingoMixId)).thenReturn(Optional.of(bingoMixMock));
		when(gameSettingsDTOMock.convertToGame(gameMock)).thenReturn(gameMock);
		when(gameMock.getSettings()).thenReturn(settingsMock);

		service.updateGameSettings(gameSettingsDTOMock, gameId);

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getId();
		verify(gameSettingsDTOMock, times(1)).getUserId();
		verify(gameSettingsDTOMock, times(1)).getMixId();
		verify(bingoMixRepositoryMock, times(1)).findById(bingoMixId);
		verify(gameSettingsDTOMock, times(1)).convertToGame(gameMock);
		verify(gameMock, times(1)).getSettings();
		verify(settingsMock, times(1)).setMix(bingoMixMock);
		verify(gameMock, times(1)).setWinner(null);
		verify(gameRepoMock, times(1)).save(gameMock);
	}

	@Test
	public void getAllGamesOwnedBy_throwsNotFound_whenUserNull() {
		String ownerUsername = "username";

		when(registeredUserRepMock.findByUsername(ownerUsername)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.getAllGamesOwnedBy(ownerUsername);
		});

		verify(registeredUserRepMock, times(1)).findByUsername(ownerUsername);
	}

	@Test
	public void getAllGamesOwnedBy_callsCorrectMethods_andReturnsCorrectSize() {
		String ownerUsername = "username";
		int size = 8;
		List<Game> games = new ArrayList<>();

		for (int i = 0; i < size; i++) {
			games.add(new Game(registeredUserMock, null, new GameSettings(), "", 0));
		}

		when(registeredUserRepMock.findByUsername(ownerUsername)).thenReturn(Optional.of(registeredUserMock));
		when(gameRepoMock.getAllByOwnerOrderByName(registeredUserMock)).thenReturn(games);

		GameCardDTO[] returned = service.getAllGamesOwnedBy(ownerUsername);

		assertEquals(size, returned.length);
		verify(registeredUserRepMock, times(1)).findByUsername(ownerUsername);
		verify(gameRepoMock, times(1)).getAllByOwnerOrderByName(registeredUserMock);
	}

	@Test
	public void deleteGameById_callsCorrectMethods() {
		int gameId = 8;

		service.deleteGameById(gameId);

		verify(gameRepoMock, times(1)).delete(any());
	}

	@Test
	public void endHostGameByJoinCode_throwsBadRequest_whenGameUnhoasted() {
		int joincode = 0;
		String username = "username";

		assertThrows(BadRequestException.class, () -> {
			service.endHostGameByJoinCode(joincode, username);
		});
	}

	@Test
	public void endHostGameByJoinCode_throwsNotFound_whenNoGame() {
		int joincode = 555;
		String username = "username";

		when(gameRepoMock.findByJoinCode(joincode)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.endHostGameByJoinCode(joincode, username);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(joincode);
	}

	@Test
	public void endHostGameByJoinCode_throwsBadRequest_whenNotOwner() {
		int joincode = 555;
		String username = "username";
		String ownerUsername = "not username";

		when(gameRepoMock.findByJoinCode(joincode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getUsername()).thenReturn(ownerUsername);

		assertThrows(BadRequestException.class, () -> {
			service.endHostGameByJoinCode(joincode, username);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(joincode);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getUsername();
	}

	@Test
	public void endHostGameByJoinCode_CallsCorrectMethods_andSetsGameCodeToZero() {
		int joincode = 555;
		String username = "username";
		String ownerUsername = username;

		when(gameRepoMock.findByJoinCode(joincode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getUsername()).thenReturn(ownerUsername);

		service.endHostGameByJoinCode(joincode, username);

		verify(gameRepoMock, times(1)).findByJoinCode(joincode);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getUsername();
		verify(gameMock, times(1)).setJoinCode(0);
		verify(gameRepoMock, times(1)).save(gameMock);
	}

	@Test
	public void hostGameById_throwsNotFound_whenGameNotFound() {
		int gameId = 11;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.hostGameById(gameSettingsDTOMock, gameId);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
	}

	@Test
	public void hostGameById_throwsBadRequest_whenNotOwner() {
		int gameId = 11;
		int userId = 5;
		int ownerId = 15;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getId()).thenReturn(ownerId);
		when(gameSettingsDTOMock.getUserId()).thenReturn(userId);

		assertThrows(BadRequestException.class, () -> {
			service.hostGameById(gameSettingsDTOMock, gameId);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getId();
		verify(gameSettingsDTOMock, times(1)).getUserId();
	}

	@Test
	public void hostGameById_throwsBadRequest_whenGameAlreadyHoasted() {
		int gameId = 11;
		int userId = 5;
		int ownerId = userId;
		int joinCode = 5555;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getId()).thenReturn(ownerId);
		when(gameSettingsDTOMock.getUserId()).thenReturn(userId);
		when(gameMock.getJoinCode()).thenReturn(joinCode);

		assertThrows(BadRequestException.class, () -> {
			service.hostGameById(gameSettingsDTOMock, gameId);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getId();
		verify(gameSettingsDTOMock, times(1)).getUserId();
		verify(gameMock, times(1)).getJoinCode();
	}

	@Test
	public void hostGameById_throwsNotFound_whenNoBingoMix() {
		int gameId = 11;
		int userId = 5;
		int ownerId = userId;
		int joinCode = 0;
		int bingoMixId = 2;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getId()).thenReturn(ownerId);
		when(gameSettingsDTOMock.getUserId()).thenReturn(userId);
		when(gameMock.getJoinCode()).thenReturn(joinCode);
		when(gameSettingsDTOMock.getMixId()).thenReturn(bingoMixId);
		when(bingoMixRepositoryMock.findById(bingoMixId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.hostGameById(gameSettingsDTOMock, gameId);
		});

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getId();
		verify(gameSettingsDTOMock, times(1)).getUserId();
		verify(gameMock, times(1)).getJoinCode();
		verify(gameSettingsDTOMock, times(2)).getMixId();
		verify(bingoMixRepositoryMock, times(1)).findById(bingoMixId);
	}

	@Test
	public void hostGameById_callsCorrectMethods() {
		int gameId = 11;
		int userId = 5;
		int ownerId = userId;
		int joinCode = 0;
		int bingoMixId = 2;

		when(gameRepoMock.findById(gameId)).thenReturn(Optional.of(gameMock));
		when(gameMock.getOwner()).thenReturn(registeredUserMock);
		when(registeredUserMock.getId()).thenReturn(ownerId);
		when(gameSettingsDTOMock.getUserId()).thenReturn(userId);
		when(gameMock.getJoinCode()).thenReturn(joinCode);
		when(gameSettingsDTOMock.getMixId()).thenReturn(bingoMixId);
		when(bingoMixRepositoryMock.findById(bingoMixId)).thenReturn(Optional.of(bingoMixMock));
		when(gameRepoMock.existsByJoinCode(anyInt())).thenReturn(false);

		when(gameSettingsDTOMock.convertToGame(gameMock)).thenReturn(gameMock);
		when(gameMock.getSettings()).thenReturn(settingsMock);

		int newJoinCode = service.hostGameById(gameSettingsDTOMock, gameId);

		verify(gameRepoMock, times(1)).findById(gameId);
		verify(gameMock, times(1)).getOwner();
		verify(registeredUserMock, times(1)).getId();
		verify(gameSettingsDTOMock, times(1)).getUserId();
		verify(gameMock, times(2)).getJoinCode();
		verify(gameSettingsDTOMock, times(1)).getMixId();
		verify(bingoMixRepositoryMock, times(1)).findById(bingoMixId);
		verify(gameSettingsDTOMock, times(1)).convertToGame(gameMock);
		verify(gameMock, times(2)).getSettings();
		verify(settingsMock, times(1)).setMix(bingoMixMock);
		verify(gameMock, times(1)).setJoinCode(anyInt());
		verify(gameRepoMock, times(1)).save(gameMock);
	}

	@Test
	public void setBingoGameWinner_id_throwsNotFound_onGameNotFound() {
		int gamecode = 5555;
		int userId = 4;

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.setBingoGameWinner(gamecode, userId);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
	}

	@Test
	public void setBingoGameWinner_id_throwsBadRequest_onGameAlreayWon() {
		int gamecode = 5555;
		int userId = 4;

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getWinner()).thenReturn(userMock);

		assertThrows(BadRequestException.class, () -> {
			service.setBingoGameWinner(gamecode, userId);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
		verify(gameMock, times(1)).getWinner();
	}

	@Test
	public void setBingoGameWinner_id_throwsNotFound_onNoUserFound() {
		int gamecode = 5555;
		int userId = 4;

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getWinner()).thenReturn(null);
		when(userRepoMock.findById(userId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.setBingoGameWinner(gamecode, userId);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
		verify(gameMock, times(1)).getWinner();
		verify(userRepoMock, times(1)).findById(userId);
	}

	@Test
	public void setBingoGameWinner_id_callsCorrectMethods() {
		int gamecode = 5555;
		int userId = 4;

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getWinner()).thenReturn(null);
		when(userRepoMock.findById(userId)).thenReturn(Optional.of(registeredUserMock));

		service.setBingoGameWinner(gamecode, userId);

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
		verify(gameMock, times(1)).getWinner();
		verify(userRepoMock, times(1)).findById(userId);
		verify(gameMock, times(1)).setWinner(registeredUserMock);
		verify(gameRepoMock, times(1)).save(gameMock);
	}
	
	@Test
	public void setBingoGameWinner_username_throwsNotFound_onGameNotFound() {
		int gamecode = 5555;
		String username = "username";

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.setBingoGameWinner(gamecode, username);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
	}

	@Test
	public void setBingoGameWinner_username_throwsBadRequest_onGameAlreayWon() {
		int gamecode = 5555;
		String username = "username";

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getWinner()).thenReturn(userMock);

		assertThrows(BadRequestException.class, () -> {
			service.setBingoGameWinner(gamecode, username);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
		verify(gameMock, times(1)).getWinner();
	}

	@Test
	public void setBingoGameWinner_username_throwsNotFound_onNoUserFound() {
		int gamecode = 5555;
		String username = "username";

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getWinner()).thenReturn(null);
		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.setBingoGameWinner(gamecode, username);
		});

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
		verify(gameMock, times(1)).getWinner();
		verify(registeredUserRepMock, times(1)).findByUsername(username);
	}

	@Test
	public void setBingoGameWinner_username_callsCorrectMethods() {
		int gamecode = 5555;
		String username = "username";

		when(gameRepoMock.findByJoinCode(gamecode)).thenReturn(Optional.of(gameMock));
		when(gameMock.getWinner()).thenReturn(null);
		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.of(registeredUserMock));

		service.setBingoGameWinner(gamecode, username);

		verify(gameRepoMock, times(1)).findByJoinCode(gamecode);
		verify(gameMock, times(1)).getWinner();
		verify(registeredUserRepMock, times(1)).findByUsername(username);
		verify(gameMock, times(1)).setWinner(registeredUserMock);
		verify(gameRepoMock, times(1)).save(gameMock);
	}
	
	@Test
	public void generateJoinCode_callsCorrectMethods_andRepeatsThree() {	
		when(gameRepoMock.existsByJoinCode(anyInt())).thenReturn(true, true, false);
		
		service.generateJoinCode();
		
		verify(gameRepoMock, times(3)).existsByJoinCode(anyInt());
	}
	
}
