package com.cryptic.bingo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "bingo_mixes")
public class BingoMix {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "mix_id")
	private int id;

	@Column(nullable = false)
	private String name;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "mix_id")
	private List<Phrase> phrases;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private RegisteredUser owner;

	public BingoMix(String name, List<Phrase> phrases, RegisteredUser owner) {
		super();
		this.name = name;
		this.phrases = phrases;
		this.owner = owner;
	}

	public BingoMix() {
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

	public List<Phrase> getPhrases() {
		return phrases;
	}

	public void setPhrases(List<Phrase> phrases) {
		this.phrases = phrases;
	}

	public RegisteredUser getOwner() {
		return owner;
	}

	public void setOwner(RegisteredUser owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "BingoMix [id=" + id + ", name=" + name + ", phrases=" + phrases + "]";
	}

}
