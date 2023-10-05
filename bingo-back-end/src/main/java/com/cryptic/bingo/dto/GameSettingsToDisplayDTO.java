package com.cryptic.bingo.dto;

import com.cryptic.bingo.model.Game;
import com.cryptic.bingo.model.GameSettings;

public class GameSettingsToDisplayDTO {
	private int boardSize;
	private int gamePin;
	private String colorPrim;
	private String colorSec;
	private String colorSelected;
	private String mixName;
	private String gameName;
	private String hoastName;
	private boolean isCenterFree;
	private boolean isAllSameWords;
	private boolean isHoast;

	public GameSettingsToDisplayDTO(Game game) {
		GameSettings settings = game.getSettings();

		this.boardSize = settings.getBoardSize();
		this.colorPrim = settings.getBoardColorPrimary();
		this.colorSec = settings.getBoardColorSecondary();
		this.colorSelected = settings.getBoardColorSelected();
		this.mixName = settings.getMix().getName();
		this.isCenterFree = settings.getCenterFree();
		this.isAllSameWords = settings.getAllSameWords();

		this.gameName = game.getName();
		this.gamePin = game.getJoinCode();
		this.hoastName = game.getOwner().getUsername();

		this.isHoast = false;

	}

	public GameSettingsToDisplayDTO() {
		super();
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public int getGamePin() {
		return gamePin;
	}

	public void setGamePin(int gamePin) {
		this.gamePin = gamePin;
	}

	public String getColorPrim() {
		return colorPrim;
	}

	public void setColorPrim(String colorPrim) {
		this.colorPrim = colorPrim;
	}

	public String getColorSec() {
		return colorSec;
	}

	public void setColorSec(String colorSec) {
		this.colorSec = colorSec;
	}

	public String getColorSelected() {
		return colorSelected;
	}

	public void setColorSelected(String colorSelected) {
		this.colorSelected = colorSelected;
	}

	public String getMixName() {
		return mixName;
	}

	public void setMixName(String mixName) {
		this.mixName = mixName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getHoastName() {
		return hoastName;
	}

	public void setHoastName(String hoastName) {
		this.hoastName = hoastName;
	}

	public boolean isCenterFree() {
		return isCenterFree;
	}

	public void setCenterFree(boolean isCenterFree) {
		this.isCenterFree = isCenterFree;
	}

	public boolean isAllSameWords() {
		return isAllSameWords;
	}

	public void setAllSameWords(boolean isAllSameWords) {
		this.isAllSameWords = isAllSameWords;
	}

	public boolean isHoast() {
		return isHoast;
	}

	public void setHoast(boolean isHoast) {
		this.isHoast = isHoast;
	}

}
