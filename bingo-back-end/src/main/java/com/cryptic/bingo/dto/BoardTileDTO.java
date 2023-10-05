package com.cryptic.bingo.dto;

import jakarta.persistence.Column;

public class BoardTileDTO {
	private String color;
	private String font;
	private String phrase;
	private Boolean selected = false;

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getFont() {
		return font;
	}

	public void setFont(String font) {
		this.font = font;
	}

	public String getPhrase() {
		return phrase;
	}

	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected(Boolean selected) {
		this.selected = selected;
	}

	public BoardTileDTO(String color, String font, String phrase) {
		super();
		this.color = color;
		this.font = font;
		this.phrase = phrase;
	}

	public BoardTileDTO() {
		super();
	}

}
