package com.cryptic.bingo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "phrases")
public class Phrase {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pharse_id")
	private int id;

	@Column(length = 6, nullable = false)
	private String color = "000000";

	@Column(nullable = false)
	private String font = "Arial";

	@Column(nullable = false)
	private String phrase;

	public Phrase(String pharse, String color, String font) {
		super();
		this.phrase = pharse;
		this.color = color;
		this.font = font;
	}

	public Phrase() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

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

	public void setPharse(String pharse) {
		this.phrase = pharse;
	}

	@Override
	public String toString() {
		return "Phrase [id=" + id + ", color=" + color + ", font=" + font + ", pharse=" + phrase + "]";
	}

}
