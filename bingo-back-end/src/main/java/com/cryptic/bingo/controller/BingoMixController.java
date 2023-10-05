package com.cryptic.bingo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cryptic.bingo.dto.MixCardDTO;
import com.cryptic.bingo.exceptions.ForbiddenException;
import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.service.BingoMixService;

@RestController
@RequestMapping("mix")
public class BingoMixController {
	private BingoMixService bingoMixService;
	
	private Logger logger = LoggerFactory.getLogger(BingoMixController.class);

	@Autowired
	public BingoMixController(BingoMixService bingoMixService) {
		super();
		this.bingoMixService = bingoMixService;
	}
	
	@GetMapping("all/{userId}")
	public List<MixCardDTO> getAllMixesOrderedByOwned(@PathVariable int userId) {
		logger.trace("ENDPOINT : Get all mixes order by owned called. Prams : " + userId);
		
		return this.bingoMixService.getAllMixesOrderedByOwned(userId);
	}
	
	@GetMapping("{mixId}")
	public BingoMix getMixById(@PathVariable int mixId) {
		return this.bingoMixService.findById(mixId);
	}
	
	@PutMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void updateBingoMix(@RequestBody BingoMix mix, Authentication auth) {
		logger.trace("ENDPOINT : Update bingo mix called. Prams : " + mix);
		
		if (auth == null || !auth.isAuthenticated()) {
			throw new ForbiddenException("");
		}
		
		this.bingoMixService.updateBingoMix(mix, auth.getName());
	}
	
	@PostMapping("{mixId}")
	@ResponseStatus(HttpStatus.CREATED)
	public BingoMix copyBingoMix(@PathVariable int mixId, Authentication auth) {
		logger.trace("ENDPOINT : Copy bingo mix called. Prams : " + mixId);
		
		if (auth == null || !auth.isAuthenticated()) {
			throw new ForbiddenException("");
		}
		
		return this.bingoMixService.copyBingoMix(mixId, auth.getName());
	}
	
	@DeleteMapping("{mixId}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteMixById(@PathVariable int mixId) {
		logger.trace("ENDPOINT : Delete bingo mix called. Prams : " + mixId);
		
		this.bingoMixService.deleteMixById(mixId);
	}
}
