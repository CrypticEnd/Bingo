package com.cryptic.bingo.dto;

public class GameDTO {
	private BoardTileDTO[][] board;
	private GameSettingsToDisplayDTO settings;
	private String winner;

	public GameDTO(BoardTileDTO[][] board, GameSettingsToDisplayDTO settings, String winner) {
		super();
		this.board = board;
		this.settings = settings;
		this.winner = winner;
	}

	public GameDTO() {
		super();
	}

	public BoardTileDTO[][] getBoard() {
		return board;
	}

	public void setBoard(BoardTileDTO[][] board) {
		this.board = board;
	}

	public GameSettingsToDisplayDTO getSettings() {
		return settings;
	}

	public void setSettings(GameSettingsToDisplayDTO settings) {
		this.settings = settings;
	}

	public String getWinner() {
		return winner;
	}

	public void setWinner(String winner) {
		this.winner = winner;
	}

}
