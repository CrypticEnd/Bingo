package com.cryptic.bingo.dto;

import com.cryptic.bingo.model.Game;

public class GameCardDTO {
	private int gameId;
	private String gameName;
	private String winnerName;
	private String bingoSet;
	private int hostLink;

	public GameCardDTO() {
		super();
	}

	public GameCardDTO(Game game) {
		this.gameId = game.getId();
		this.gameName = game.getName();

		if (game.getWinner() != null)
			this.winnerName = game.getWinner().getUsername();

		if (game.getSettings().getMix() != null)
			this.bingoSet = game.getSettings().getMix().getName();

		this.hostLink = game.getJoinCode();
	}

	public GameCardDTO(int gameId, String gameName, String winnerName, String bingoSet, int hostLink) {
		super();
		this.gameId = gameId;
		this.gameName = gameName;
		this.winnerName = winnerName;
		this.bingoSet = bingoSet;
		this.hostLink = hostLink;
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getWinnerName() {
		return winnerName;
	}

	public void setWinnerName(String winnerName) {
		this.winnerName = winnerName;
	}

	public String getBingoSet() {
		return bingoSet;
	}

	public void setBingoSet(String bingoSet) {
		this.bingoSet = bingoSet;
	}

	public int getHostLink() {
		return hostLink;
	}

	public void setHostLink(int hostLink) {
		this.hostLink = hostLink;
	}

}
