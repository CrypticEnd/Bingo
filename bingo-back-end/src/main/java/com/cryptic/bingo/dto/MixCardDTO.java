package com.cryptic.bingo.dto;

import com.cryptic.bingo.model.BingoMix;

public class MixCardDTO {
	private int id;
	private String name;
	private boolean owned;
	private int phrases;
	
	public MixCardDTO(BingoMix mix, boolean owned) {
		this.id = mix.getId();
		this.name=mix.getName();
		this.phrases=mix.getPhrases().size();
		this.owned = owned;
	}
	
	public MixCardDTO(int id, String name, boolean owned, int phrases) {
		super();
		this.id = id;
		this.name = name;
		this.owned = owned;
		this.phrases = phrases;
	}
	public MixCardDTO() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isOwned() {
		return owned;
	}
	public void setOwned(boolean owned) {
		this.owned = owned;
	}
	public int getPhrases() {
		return phrases;
	}
	public void setPhrases(int phrases) {
		this.phrases = phrases;
	}
	
	
}
