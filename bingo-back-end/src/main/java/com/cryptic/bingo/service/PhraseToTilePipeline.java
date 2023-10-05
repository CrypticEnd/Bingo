package com.cryptic.bingo.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cryptic.bingo.dto.BoardTileDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.model.Phrase;

public final class PhraseToTilePipeline {
	private static Logger logger = LoggerFactory.getLogger(PhraseToTilePipeline.class);

	public static BoardTileDTO[][] convert(List<Phrase> phrases, int boardSize, boolean centerFree,
			boolean allSameWord) {
		logger.trace("Phrase to tile pipeline starting");

		BoardTileDTO[][] arrayOfArrays = new BoardTileDTO[boardSize][];

		int minNumberOfWords = boardSize * boardSize;

		if (phrases.size() < minNumberOfWords) {
			throw new BadRequestException("Not enough pharses for board size");
		}
		if (phrases.size() > minNumberOfWords && allSameWord) {
			logger.debug(String.format("Trimming pharse list to size from $d to $d", phrases.size(), minNumberOfWords));
			phrases = trimListToSize(phrases, minNumberOfWords);
		}	
		
		logger.debug("Shuffling pharse list");
		
		Collections.shuffle(phrases);

		int phraseIndex = 0;
		int centerPos = boardSize / 2;
		for (int col = 0; col < boardSize; col++) {
			arrayOfArrays[col] = new BoardTileDTO[boardSize];

			for (int row = 0; row < boardSize; row++) {
				Phrase phrase = phrases.get(phraseIndex);

				BoardTileDTO tile = new BoardTileDTO(phrase.getColor(), phrase.getFont(), phrase.getPhrase());

				arrayOfArrays[col][row] = tile;

				if (centerFree && centerPos == col && centerPos == row) {
					tile.setSelected(true);
				}

				phraseIndex++;
			}
		}

		return arrayOfArrays;
	}

	private static List<Phrase> trimListToSize(List<Phrase> phrases, int size) {
		List<Phrase> useablePhrases = new ArrayList<Phrase>();

		for (int i = 0; i <= size; i++) {
			useablePhrases.add(phrases.get(i));
		}

		return useablePhrases;
	}

}
