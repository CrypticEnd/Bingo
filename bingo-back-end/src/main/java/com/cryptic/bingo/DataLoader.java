package com.cryptic.bingo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import com.cryptic.bingo.dal.BingoMixRepository;
import com.cryptic.bingo.dal.GameRepository;
import com.cryptic.bingo.dal.UserRepository;
import com.cryptic.bingo.model.*;
import com.cryptic.bingo.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class DataLoader implements ApplicationRunner {
	private UserService userService;
	private GameRepository gameRepo;
	private BingoMixRepository bingoMixRepo;

	private static List<User> users;
	private static List<Game> games;
	private static List<BingoMix> mixes;
	
	private static Logger logger = LoggerFactory.getLogger(DataLoader.class);
			
	@Autowired
	public DataLoader(UserService userService, GameRepository gameRepo, BingoMixRepository bingoMixRepo) {
		super();
		this.userService = userService;
		this.gameRepo = gameRepo;
		this.bingoMixRepo = bingoMixRepo;
	}

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {
//		logger.info("Data loader starting ");
//		
//		try {
//			genUsers();
//			genMixes();
//			genGames();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		logger.info("Data loader saving to DB");
//		
//		userService.saveAll(users);
//		gameRepo.saveAll(games);
//		bingoMixRepo.saveAll(mixes);
//		
//		logger.info("Data loader completed ");
	}

	public static void genUsers() {
		logger.info("Data loader: Creating users");
		
		users = new ArrayList<>();

		// User with many games
		users.add(new RegisteredUser("Ryan", "1"));

		// User with no games
		users.add(new RegisteredUser("Devon", "1"));

		// Two guest users
		users.add(new GuestUser("Alyssa"));
		users.add(new GuestUser("James"));
		users.add(new GuestUser("Ryan"));
		
		logger.info(String.format("Data created %d users ", users.size()));
	}

	public static void genMixes() throws IOException {
		logger.info("Data loader: Creating mixes");
		
		// Needed for file changes in docker
		String filepath;
		if(Files.exists(Paths.get("app"))) {
			filepath = "app/bingoMixes.txt";
		}
		else {
			filepath = "src/main/resources/bingoMixes.txt";
		}
		
		
		File file = new File(filepath);
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// Defualt values
		String defaultCol = "000000";
		String defaultFont = "Arial";

		// Sets up owner for some mixes
		RegisteredUser owner = (RegisteredUser) users.get(0);
		List<BingoMix> ownedMixes = new ArrayList<>();

		// Sets up needed values for loop
		String line;
		BingoMix currentMix = null;
		List<Phrase> phrases = null;
		mixes = new ArrayList<>();

		// Reads from the file and makes all the owned mixes
		while ((line = reader.readLine()) != null) {
			// Checking to see if string has -- at the the start
			if (line.matches("^--.+$")) {
				phrases = new ArrayList<>();

				currentMix = new BingoMix(line.substring(2).trim(), phrases, owner);

				mixes.add(currentMix);
				ownedMixes.add(currentMix);
			} else if (currentMix != null) {
				phrases.add(new Phrase(line.trim(), defaultCol, defaultFont));
			}
		}

		// Makes a default number mix owned by noone
		phrases = new ArrayList<>();
		currentMix = new BingoMix("Numbers", phrases, null);

		for (int i = 1; i <= 100; i++) {
			phrases.add(new Phrase(i + "", defaultCol, defaultFont));
		}

		mixes.add(currentMix);
		
		logger.info(String.format("Data created %d mixes ", mixes.size()));
	}

	public static void genGames() {
		logger.info("Data loader: Creating games");
		
		// Defaults
		String defaultBoardColP = "3dfb99";
		String defaultBoardColS = "000000";
		String defaultBoardColSel = "ff224a";

		// Game one (won game)
		GameSettings settings1 = new GameSettings(mixes.get(0), 5, defaultBoardColP, defaultBoardColS,
				defaultBoardColSel);
		Game game1 = new Game();
		game1.setOwner(users.get(0));
		game1.setSettings(settings1);
		game1.setWinner(users.get(users.size() - 1));
		game1.setName("Not epic game");

		// Game two (no winner, with different settings)
		GameSettings settings2 = new GameSettings(mixes.get(mixes.size() - 2), 5, defaultBoardColP, defaultBoardColS,
				defaultBoardColSel);
		settings1.setAllSameWords(false);
		settings1.setCenterFree(false);
		Game game2 = new Game();
		game2.setOwner(users.get(0));
		game2.setSettings(settings2);
		game2.setName("Epic game");
		game2.setJoinCode(1111);

		// Add to list
		games = new ArrayList<>();

		games.add(game1);
		games.add(game2);
		
		logger.info(String.format("Data created %d games ", games.size()));
	}

}
