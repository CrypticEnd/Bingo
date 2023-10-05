package com.cryptic.bingo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.cryptic.bingo.dal.BingoMixRepository;
import com.cryptic.bingo.dal.GameRepository;
import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.dal.UserRepository;
import com.cryptic.bingo.dto.BoardTileDTO;
import com.cryptic.bingo.dto.GameCardDTO;
import com.cryptic.bingo.dto.GameDTO;
import com.cryptic.bingo.dto.GameSettingsDTO;
import com.cryptic.bingo.dto.GameSettingsToDisplayDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.model.GameSettings;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;

import jakarta.persistence.criteria.Join;

@Service
public class GameService {
	private GameRepository gameRepo;
	private RegisteredUserRepository registeredUserRep;
	private UserRepository userRepo;
	private BingoMixRepository bingoMixRepository;

	private static Logger logger = LoggerFactory.getLogger(GameService.class);

	@Autowired
	public GameService(GameRepository gameRepo, RegisteredUserRepository registeredUserRep, UserRepository userRepo,
			BingoMixRepository bingoMixRepository) {
		super();
		this.gameRepo = gameRepo;
		this.registeredUserRep = registeredUserRep;
		this.userRepo = userRepo;
		this.bingoMixRepository = bingoMixRepository;
	}

	public int createGame(String ownerName) {
		logger.trace(String.format("Create game called. Ownername = %s", ownerName));

		RegisteredUser user = this.registeredUserRep.findByUsername(ownerName)
				.orElseThrow(() -> new NotFoundException("User not found with username: " + ownerName));

		Game game = new Game();
		game.setOwner(user);
		game.setSettings(new GameSettings());
		game.setName("New Bingo Game");

		logger.debug(String.format("Game default settings set: %s", game));
		logger.trace("Saving game object");

		gameRepo.save(game);

		return game.getId();
	}

	public BoardTileDTO[][] getGameBoard(int gamecode) {
		logger.trace(String.format("Get game board called. gamecode = %d", gamecode));

		Game game = gameRepo.findByJoinCode(gamecode)
				.orElseThrow(() -> new NotFoundException(String.format("Game with code %s does not exist", gamecode)));

		return this.getGameBoard(game);
	}
	
	public BoardTileDTO[][] getGameBoard(Game game) {
		logger.trace(String.format("Get game board called. game = %s", game));

		GameSettings settings = game.getSettings();

		return PhraseToTilePipeline.convert(settings.getMix().getPhrases(), settings.getBoardSize(),
				settings.getCenterFree(), settings.getAllSameWords());
	}

	public boolean doesGameCodeExist(int gamecode) {
		logger.trace(String.format("Does game code exist called. Gamecode = %d", gamecode));

		return gameRepo.existsByJoinCode(gamecode);
	}

	public Game getGameById(int gameId) {
		logger.trace(String.format("Get game by id called. gameId = %d", gameId));

		Game game = this.gameRepo.findById(gameId)
				.orElseThrow(() -> new NotFoundException("Game not found with gameId: " + gameId));

		return game;
	}

	public GameSettingsDTO getGameForSetupById(int gameId, String username) {
		logger.trace(String.format("Get game for setup by id called. gameId = %d, username = %s", gameId, username));

		Game game = this.gameRepo.findById(gameId)
				.orElseThrow(() -> new NotFoundException("Game not found with gameId: " + gameId));

		// Ownership of game
		if (!game.getOwner().getUsername().equals(username)) {
			throw new BadRequestException("Game is not owned by user");
		}

		// Game is currently being hosted
		if (game.getJoinCode() > 0) {
			throw new BadRequestException("Game is already being hosted");
		}

		return new GameSettingsDTO(game);
	}

	public GameSettingsToDisplayDTO getSettingsByGamejoinCode(int gameJoinCode, Authentication auth) {
		logger.trace(String.format("Get game settings by game join code called. JoinCode = %d", gameJoinCode));

		Game game = gameRepo.findByJoinCode(gameJoinCode)
				.orElseThrow(() -> new NotFoundException("Game not found with joincode: " + gameJoinCode));

		return this.getSettingsByGamejoinCode(game, auth);
	}
	
	public GameSettingsToDisplayDTO getSettingsByGamejoinCode(Game game, Authentication auth) {
		logger.trace(String.format("Get game settings by game join code called. JoinCode = %s", game));

		GameSettingsToDisplayDTO gameSettingsDTO = new GameSettingsToDisplayDTO(game);

		if (auth != null && auth.isAuthenticated()) {
			gameSettingsDTO.setHoast(auth.getName().equals(game.getOwner().getUsername()));

			if (gameSettingsDTO.isHoast())
				logger.debug(String.format("User: %s is host of game", auth.getName()));
		}

		return gameSettingsDTO;
	}

	public void updateGameSettings(GameSettingsDTO gameSettingsDTO, int gameId) {
		logger.trace(String.format("update game settings called. gameSettingsDTO = %s, gameId = %d", gameSettingsDTO,
				gameId));

		Game game = this.gameRepo.findById(gameId)
				.orElseThrow(() -> new NotFoundException("Game not found with gameId: " + gameId));

		// Ownership of game
		if (game.getOwner().getId() != gameSettingsDTO.getUserId()) {
			throw new BadRequestException("Game is not owned by user");
		}

		BingoMix mix = bingoMixRepository.findById(gameSettingsDTO.getMixId()).orElse(null);

		logger.debug(String.format("Mix set to : %s", mix));

		game = gameSettingsDTO.convertToGame(game);
		game.getSettings().setMix(mix);

		// clear game winner
		game.setWinner(null);

		logger.trace("Saving game object");
		this.gameRepo.save(game);
	}

	public GameCardDTO[] getAllGamesOwnedBy(String ownerUsername) {
		logger.trace(String.format("Get all games owned by called. ownerUsername = %s", ownerUsername));

		RegisteredUser user = this.registeredUserRep.findByUsername(ownerUsername)
				.orElseThrow(() -> new NotFoundException("User not found with username: " + ownerUsername));

		List<Game> games = this.gameRepo.getAllByOwnerOrderByName(user);

		GameCardDTO[] gameCards = games.stream().map(g -> new GameCardDTO(g)).toArray(GameCardDTO[]::new);

		return gameCards;
	}

	public void deleteGameById(int gameId) {
		logger.trace(String.format("Delete game by id called. gameId = %d", gameId));

		Game game = new Game();
		game.setId(gameId);
		this.gameRepo.delete(game);
	}

	public void endHostGameByJoinCode(int joincode, String username) {
		logger.trace(
				String.format("End host game by join code called. joincode = %d, username = %s", joincode, username));

		if (joincode <= 0)
			throw new BadRequestException("Game code cannot be zero or less - thats an un-hosted game");

		Game game = this.gameRepo.findByJoinCode(joincode)
				.orElseThrow(() -> new NotFoundException("Game not found with gamecode: " + joincode));

		// Ownership of game
		if (!game.getOwner().getUsername().equals(username)) {
			throw new BadRequestException("Game is not owned by user");
		}

		game.setJoinCode(0);

		logger.trace("Saving game object");
		this.gameRepo.save(game);
	}

	public int hostGameById(GameSettingsDTO gameSettingsDTO, int gameId) {
		logger.trace(String.format("Host game by game id called. GameSettingsDTO = %s, gameId = %d", gameSettingsDTO,
				gameId));

		Game game = this.gameRepo.findById(gameId)
				.orElseThrow(() -> new NotFoundException("Game not found with gameId: " + gameId));

		// Ownership of game
		if (game.getOwner().getId() != gameSettingsDTO.getUserId()) {
			throw new BadRequestException("Game is not owned by user");
		}

		if (game.getJoinCode() > 0) {
			throw new BadRequestException("Game already hosted");
		}

		BingoMix mix = bingoMixRepository.findById(gameSettingsDTO.getMixId())
				.orElseThrow(() -> new NotFoundException("Mix nof found with mixId: " + gameSettingsDTO.getMixId()));

		game = gameSettingsDTO.convertToGame(game);
		game.getSettings().setMix(mix);

		// Test if game can start
		int boardSize = game.getSettings().getBoardSize();
		if (mix.getPhrases().size() < boardSize * boardSize) {
			throw new BadRequestException("Game board too small");
		}

		game.setJoinCode(this.generateJoinCode());

		logger.trace("Saving game object");
		this.gameRepo.save(game);

		return game.getJoinCode();

	}

	public String setBingoGameWinner(int gamecode, int userId) {
		logger.trace(String.format("Set bingo game winner called. gamecode = %d, userId = %d", gamecode, userId));

		Game game = this.gameRepo.findByJoinCode(gamecode)
				.orElseThrow(() -> new NotFoundException("Game not found with gamecode: " + gamecode));

		if (game.getWinner() != null) {
			return game.getWinner().getUsername();
		}

		User user = this.userRepo.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found with userID : " + userId));

		game.setWinner(user);

		logger.trace("Saving game object");
		this.gameRepo.save(game);
		return user.getUsername();
	}

	public String setBingoGameWinner(int gamecode, String username) {
		logger.trace(String.format("Set bingo game winner called. gamecode = %d, username = %s", gamecode, username));

		Game game = this.gameRepo.findByJoinCode(gamecode)
				.orElseThrow(() -> new NotFoundException("Game not found with gamecode: " + gamecode));

		if (game.getWinner() != null) {
			return game.getWinner().getUsername();
		}

		User user = this.registeredUserRep.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User not found with username : " + username));

		game.setWinner(user);

		logger.trace("Saving game object");
		this.gameRepo.save(game);
		return user.getUsername();
	}

	protected int generateJoinCode() {
		logger.trace("Making new join code");

		int min = 1000;
		int max = 9999;

		while (true) {
			int joinCode = (int) Math.floor(Math.random() * (max - min + 1) + min);

			logger.debug("Join code made :" + joinCode);

			if (!this.doesGameCodeExist(joinCode))
				return joinCode;
			else
				logger.debug("Join code already in use");
		}
	}

	public GameDTO getGameBoardFull(int gameJoinCode, Authentication auth) {
		logger.trace("getGameBoardFull called");
		
		GameDTO gameDTO = new GameDTO();
		
		Game game = gameRepo.findByJoinCode(gameJoinCode)
				.orElseThrow(() -> new NotFoundException("Game not found with joincode: " + gameJoinCode));
		
		gameDTO.setBoard(this.getGameBoard(game));
		gameDTO.setSettings(this.getSettingsByGamejoinCode(game, auth));
		
		if(game.getWinner() != null) {
			gameDTO.setWinner(game.getWinner().getUsername());
		}
		
		return gameDTO;
	}

}
