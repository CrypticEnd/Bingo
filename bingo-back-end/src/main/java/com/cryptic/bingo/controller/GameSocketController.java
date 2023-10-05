package com.cryptic.bingo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.cryptic.bingo.service.GameService;
import com.cryptic.bingo.socket.model.Winner;

@Controller
@MessageMapping("game")
public class GameSocketController {
	private GameService gameService;

	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	public GameSocketController(GameService gameService, SimpMessagingTemplate simpMessagingTemplate) {
		super();
		this.gameService = gameService;
		this.simpMessagingTemplate = simpMessagingTemplate;

		this.simpMessagingTemplate.setDefaultDestination("/game/");
	}

	private Logger logger = LoggerFactory.getLogger(GameController.class);

	@MessageMapping("bingo/g/{gameCode}/{userId}")
	public void setWinnerGuest(@DestinationVariable int gameCode, @DestinationVariable int userId) {
		logger.trace(String.format("ENDPOINT : Set Winner guest called. Prams : %s, %s", gameCode, userId));

		Winner winner = new Winner(this.gameService.setBingoGameWinner(gameCode, userId));

		simpMessagingTemplate.convertAndSendToUser(Integer.toString(gameCode), "/winner", winner);
	}

	@MessageMapping("bingo/r/{gameCode}/{username}")
	public void setWinnerRegistered(@DestinationVariable int gameCode, @DestinationVariable String username) {
		logger.trace(String.format("ENDPOINT : Set winner registered called. Prams : %s, %s", gameCode, username));

		Winner winner = new Winner(this.gameService.setBingoGameWinner(gameCode, username));

		simpMessagingTemplate.convertAndSendToUser(Integer.toString(gameCode), "/winner", winner);
	}
}
