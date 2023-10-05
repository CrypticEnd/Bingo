package com.cryptic.bingo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cryptic.bingo.dto.BoardTileDTO;
import com.cryptic.bingo.dto.GameCardDTO;
import com.cryptic.bingo.dto.GameDTO;
import com.cryptic.bingo.dto.GameSettingsDTO;
import com.cryptic.bingo.dto.GameSettingsToDisplayDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.ForbiddenException;
import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.service.GameService;
import com.cryptic.bingo.socket.model.Winner;

@RestController
@RequestMapping("game")
public class GameController {
	private GameService gameService;

	private Logger logger = LoggerFactory.getLogger(GameController.class);

	@Autowired
	public GameController(GameService gameService) {
		super();
		this.gameService = gameService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public int createGame(Authentication auth) {
		logger.trace("ENDPOINT : Create game called.");

		if (auth == null || !auth.isAuthenticated()) {
			throw new ForbiddenException("");
		}

		return this.gameService.createGame(auth.getName());
	}

	@GetMapping("board/{gamecode}")
	public BoardTileDTO[][] getGameBoard(@PathVariable int gamecode) {
		logger.trace("ENDPOINT : Get game board called. Prams : " + gamecode);

		return gameService.getGameBoard(gamecode);
	}

	@GetMapping("exist/{gamecode}")
	public boolean doesGameCodeExist(@PathVariable int gamecode) {
		logger.trace("ENDPOINT : Does game code exist called. Prams : " + gamecode);
		return gameService.doesGameCodeExist(gamecode);
	}

	@GetMapping("{gameId}")
	public Game getGameById(@PathVariable int gameId) {
		logger.trace("ENDPOINT : Get game by id called. Prams : " + gameId);

		return gameService.getGameById(gameId);
	}

	@DeleteMapping("{gameId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteGameById(@PathVariable int gameId) {
		logger.trace("ENDPOINT : Delete game by id called. Prams : " + gameId);

		gameService.deleteGameById(gameId);
	}

	@PutMapping("host/{gameId}")
	public int hostGame(@RequestBody GameSettingsDTO gameSettingsDTO, @PathVariable int gameId) {
		logger.trace(String.format("ENDPOINT : Hoast game called. Prams : %s, Body : %s", gameId, gameSettingsDTO));

		return this.gameService.hostGameById(gameSettingsDTO, gameId);
	}

	@PutMapping("endhost/{joinCode}")
	public void endHostGameByJoinCode(@PathVariable int joinCode, Authentication auth) {
		logger.trace(String.format("ENDPOINT : Hoast game called. Prams : %s", joinCode));

		if (auth == null || !auth.isAuthenticated()) {
			throw new ForbiddenException("");
		}

		this.gameService.endHostGameByJoinCode(joinCode, auth.getName());
	}

	@GetMapping
	public GameCardDTO[] getAllGamesOwnedByUser(Authentication auth) {
		logger.trace("ENDPOINT : Get all games owned by user called.");

		if (auth == null || !auth.isAuthenticated()) {
			throw new ForbiddenException("");
		}

		return this.gameService.getAllGamesOwnedBy(auth.getName());
	}

	@GetMapping("setup/{gameId}")
	public GameSettingsDTO getGameForSetupById(@PathVariable int gameId, Authentication auth) {
		logger.trace(String.format("ENDPOINT : Hoast game called. Prams : %s", gameId));

		if (auth == null || !auth.isAuthenticated()) {
			throw new ForbiddenException("");
		}

		return gameService.getGameForSetupById(gameId, auth.getName());
	}

	@PutMapping("setup/{gameId}")
	public void UpdateGameSettings(@RequestBody GameSettingsDTO gameSettingsDTO, @PathVariable int gameId) {
		logger.trace(String.format("ENDPOINT : Update game settings called. Prams : %s, Body : %s", gameId,
				gameSettingsDTO));

		this.gameService.updateGameSettings(gameSettingsDTO, gameId);
	}

	@GetMapping("settings/{gamecode}")
	public GameSettingsToDisplayDTO getGameSettingsByGameCode(@PathVariable int gamecode, Authentication auth) {
		logger.trace(String.format("ENDPOINT : Get game settings by game code called. Prams : %s", gamecode));

		return this.gameService.getSettingsByGamejoinCode(gamecode, auth);
	}
	
	@GetMapping("loadBoard/{gamecode}")
	public GameDTO getGameBoardFull(@PathVariable int gamecode, Authentication auth) {
		logger.trace(String.format("ENDPOINT : Get game board full called. Prams : %s", gamecode));
		
		return this.gameService.getGameBoardFull(gamecode, auth);
	}

}
