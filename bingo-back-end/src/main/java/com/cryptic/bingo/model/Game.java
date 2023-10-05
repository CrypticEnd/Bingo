package com.cryptic.bingo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "games")
public class Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "game_id")
	private int id;

	@ManyToOne(cascade = {})
	@JoinColumn(name = "owner_id", nullable = false)
	private User owner;

	@OneToOne
	@JoinColumn(name = "winner_id")
	private User winner;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "settings_id", nullable = false)
	private GameSettings settings;

	@Column(nullable = false)
	private String name;

	private int joinCode;

	public Game(User owner, User winner, GameSettings settings, String name, int code) {
		super();
		this.owner = owner;
		this.winner = winner;
		this.settings = settings;
		this.name = name;
		this.joinCode = code;
	}

	public Game() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public User getWinner() {
		return winner;
	}

	public void setWinner(User winner) {
		this.winner = winner;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public void setSettings(GameSettings settings) {
		this.settings = settings;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getJoinCode() {
		return joinCode;
	}

	public void setJoinCode(int joinCode) {
		this.joinCode = joinCode;
	}

	@Override
	public String toString() {
		return String.format("Game: %s [ id = %d, owner = %s settingId = %d, code = %d ]", name, id,
				owner.getUsername(), settings.getId(), joinCode);
	}

}
