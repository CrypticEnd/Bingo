package com.cryptic.bingo.dto;

import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.model.GameSettings;

public class GameSettingsDTO {

	private int userId;
	private int mixId;
	private int boardSize;

	private String gameName;
	private String colorPrimary;
	private String colorSecondary;
	private String colorSelected;

	private boolean isCenterFree;
	private boolean isAllSameWord;

	public GameSettingsDTO(Game game) {
		GameSettings settings = game.getSettings();

		this.userId = game.getOwner().getId();

		if (settings.getMix() != null)
			this.mixId = settings.getMix().getId();
		else
			this.mixId = -1;

		this.boardSize = settings.getBoardSize();

		this.gameName = game.getName();
		this.colorPrimary = settings.getBoardColorPrimary();
		this.colorSecondary = settings.getBoardColorSecondary();
		this.colorSelected = settings.getBoardColorSelected();

		this.isCenterFree = settings.getCenterFree();
		this.isAllSameWord = settings.getAllSameWords();
	}

	public GameSettingsDTO() {
		super();
	}

	public GameSettingsDTO(int userId, int mixId, int boardSize, String gameName, String colorPrimary,
			String colorSecondary, String colorSelected, boolean isCenterFree, boolean isAllSameWord) {
		super();
		this.userId = userId;
		this.mixId = mixId;
		this.boardSize = boardSize;
		this.gameName = gameName;
		this.colorPrimary = colorPrimary;
		this.colorSecondary = colorSecondary;
		this.colorSelected = colorSelected;
		this.isCenterFree = isCenterFree;
		this.isAllSameWord = isAllSameWord;
	}

	public Game convertToGame(Game game) {
		GameSettings settings = game.getSettings();

		game.setName(gameName);

		settings.setBoardSize(boardSize);
		settings.setBoardColorPrimary(colorPrimary);
		settings.setBoardColorSecondary(colorSecondary);
		settings.setBoardColorSelected(colorSelected);

		settings.setCenterFree(isCenterFree);
		settings.setAllSameWords(isAllSameWord);

		return game;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getMixId() {
		return mixId;
	}

	public void setMixId(int mixId) {
		this.mixId = mixId;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getColorPrimary() {
		return colorPrimary;
	}

	public void setColorPrimary(String colorPrimary) {
		this.colorPrimary = colorPrimary;
	}

	public String getColorSecondary() {
		return colorSecondary;
	}

	public void setColorSecondary(String colorSecondary) {
		this.colorSecondary = colorSecondary;
	}

	public String getColorSelected() {
		return colorSelected;
	}

	public void setColorSelected(String colorSelected) {
		this.colorSelected = colorSelected;
	}

	public boolean isCenterFree() {
		return isCenterFree;
	}

	public void setCenterFree(boolean isCenterFree) {
		this.isCenterFree = isCenterFree;
	}

	public boolean isAllSameWord() {
		return isAllSameWord;
	}

	public void setAllSameWord(boolean isAllSameWord) {
		this.isAllSameWord = isAllSameWord;
	}

	@Override
	public String toString() {
		return "GameSettingsDTO [userId=" + userId + ", mixId=" + mixId + ", boardSize=" + boardSize + ", gameName="
				+ gameName + ", colorPrimary=" + colorPrimary + ", colorSecondary=" + colorSecondary
				+ ", colorSelected=" + colorSelected + ", isCenterFree=" + isCenterFree + ", isAllSameWord="
				+ isAllSameWord + "]";
	}

}
