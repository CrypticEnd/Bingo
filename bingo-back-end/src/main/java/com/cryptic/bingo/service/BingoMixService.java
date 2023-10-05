package com.cryptic.bingo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cryptic.bingo.dal.BingoMixRepository;
import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.dto.MixCardDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.model.Phrase;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.model.User;

@Service
public class BingoMixService {
	private BingoMixRepository bingoMixRepo;
	private RegisteredUserRepository registeredUserRep;

	private Logger logger = LoggerFactory.getLogger(BingoMixService.class);

	@Autowired
	public BingoMixService(BingoMixRepository bingoMixRepo, RegisteredUserRepository registeredUserRep) {
		super();
		this.bingoMixRepo = bingoMixRepo;
		this.registeredUserRep = registeredUserRep;
	}

	public List<MixCardDTO> getAllMixesOrderedByOwned(int userId) {
		logger.trace("Get all mixes ordered by owner. User id : " + userId);

		RegisteredUser user = this.registeredUserRep.findById(userId)
				.orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

		//TODO change to a single request and sort in java 
		List<BingoMix> ownedMixes = this.bingoMixRepo.getByOwnerOrderByName(user);
		List<BingoMix> notOwnedMixes = this.bingoMixRepo.getByOwnerIsNotOrderByName(user);
		List<BingoMix> mixesWithNoOwner = this.bingoMixRepo.getByOwnerIsNullOrderByName();

		List<MixCardDTO> allMixes = new ArrayList<>();

		// Not really sure why this doesnt work
//		allMixes.addAll((ownedMixes.stream().map(m -> new MixCardDTO(m, true)).collect(Collectors.toList()));

		for (BingoMix mix : ownedMixes) {
			allMixes.add(new MixCardDTO(mix, true));
		}
		for (BingoMix mix : notOwnedMixes) {
			allMixes.add(new MixCardDTO(mix, false));
		}
		for (BingoMix mix : mixesWithNoOwner) {
			allMixes.add(new MixCardDTO(mix, false));
		}

		logger.info(String.format(
				"Get all mixes. User owns %d mixes, User does not own %d mixes, noone owns %d mixes. Total mixes returned : %d",
				ownedMixes.size(), notOwnedMixes.size(), mixesWithNoOwner.size(), allMixes.size()));

		return allMixes;
	}

	public BingoMix findById(int id) {
		logger.trace("Find by id : " + id);

		return this.bingoMixRepo.findById(id).orElse(null);
	}

	public void updateBingoMix(BingoMix mix, String username) {	
		logger.trace(String.format("Update game called. BingoMix = %s, username = %s", mix, username));
		
		RegisteredUser user = registeredUserRep.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User not found with username : " + username));
		
		// Check if mix has owner 
		if(mix.getOwner() == null) {
			mix.setOwner(user);
			logger.debug(String.format("Mix to update has no owner. So setting owner to %s and creating mix", user));
		}
		// check if owner
		else if(!mix.getOwner().equals(user)) {
			logger.debug("Trying to update a mix with a owner");
			throw new BadRequestException("Not owner of mix");
		}
		
		logger.trace("Saving mix");
		this.bingoMixRepo.save(mix);
	}

	public void deleteMixById(int mixId) {
		logger.trace(String.format("Delete mix by id called. Id = %d", mixId));
		
		BingoMix mix = new BingoMix();
		mix.setId(mixId);

		logger.trace("Deleting mix");
		this.bingoMixRepo.delete(mix);

	}

	public BingoMix copyBingoMix(int mixId, String username) {
		logger.trace(String.format("Copy bingo mix by id called. mixId = %d, username = %s", mixId, username));
		
		RegisteredUser user = registeredUserRep.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("User not found with username : " + username));
		
		BingoMix mixToCopy = this.bingoMixRepo.findById(mixId)
				.orElseThrow(() -> new NotFoundException("mix not found with id : " + mixId));
		
		BingoMix newMix = new BingoMix();
		newMix.setName(mixToCopy.getName() + " (copy)");
		newMix.setOwner(user);
		
		List<Phrase> phrasesClone = new ArrayList<>();
		
		mixToCopy.getPhrases().forEach((p) -> phrasesClone.add(
				new Phrase(p.getPhrase(), p.getColor(), p.getFont())));
		
		newMix.setPhrases(phrasesClone);
		
		logger.trace("Saving mix");
		this.bingoMixRepo.save(newMix);
		
		return newMix;
	}

}
