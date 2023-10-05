package com.cryptic.bingo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_settings")
public class GameSettings {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "setting_id")
	private int id;

	@OneToOne
	@JoinColumn(name = "mix_id")
	private BingoMix mix;

	@Column(name = "board_size", nullable = false)
	private int boardSize = 5;

	@Column(name = "center_free")
	private Boolean centerFree = true;

	@Column(name = "all_same_words")
	private Boolean allSameWords = true;

	@Column(name = "board_color_primary", length = 6, nullable = false)
	private String boardColorPrimary = "000000";

	@Column(name = "board_color_secondary", length = 6, nullable = false)
	private String boardColorSecondary = "000000";

	@Column(name = "board_color_selected", length = 6, nullable = false)
	private String boardColorSelected = "000000";

	public GameSettings(BingoMix mix, int boardSize, String boardColorPrimary, String boardColorSecondary,
			String boardColorSelected) {
		super();
		this.mix = mix;
		this.boardSize = boardSize;
		this.boardColorPrimary = boardColorPrimary;
		this.boardColorSecondary = boardColorSecondary;
		this.boardColorSelected = boardColorSelected;
	}

	public GameSettings() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public BingoMix getMix() {
		return mix;
	}

	public void setMix(BingoMix mix) {
		this.mix = mix;
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public Boolean getCenterFree() {
		return centerFree;
	}

	public void setCenterFree(Boolean centerFree) {
		this.centerFree = centerFree;
	}

	public Boolean getAllSameWords() {
		return allSameWords;
	}

	public void setAllSameWords(Boolean allSameWords) {
		this.allSameWords = allSameWords;
	}

	public String getBoardColorPrimary() {
		return boardColorPrimary;
	}

	public void setBoardColorPrimary(String boardColorPrimary) {
		this.boardColorPrimary = boardColorPrimary;
	}

	public String getBoardColorSecondary() {
		return boardColorSecondary;
	}

	public void setBoardColorSecondary(String boardColorSecondary) {
		this.boardColorSecondary = boardColorSecondary;
	}

	public String getBoardColorSelected() {
		return boardColorSelected;
	}

	public void setBoardColorSelected(String boardColorSelected) {
		this.boardColorSelected = boardColorSelected;
	}

	@Override
	public String toString() {
		return String.format(
				"GameSettings [ id = %d, mixName = %s, boardSize = %dX&d, centerFree = %b, allSameWords = %b ]",
				this.id, mix.getName(), boardSize, boardSize, centerFree, allSameWords);
	}

}
