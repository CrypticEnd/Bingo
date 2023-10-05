package com.cryptic.bingo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.booleanThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.cryptic.bingo.dto.BoardTileDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.model.Phrase;
import com.cryptic.bingo.service.PhraseToTilePipeline;

public class PhraseToTilePipelineTests {
	private PhraseToTilePipeline pipeline;
	private static List<Phrase> phrases;
	private int boardSize =5;
	private boolean centerFree = false;
	private boolean allSameWord = false;
	
	
	@BeforeAll
	public static void phraseListSetup() {
		phrases = new ArrayList<>();
		
		for (int i = 1; i <= 100; i++) {
			phrases.add(new Phrase(i+"", "", ""));
		}
	}
	
	@Test
	public void throwsExpection_whenBoardSizeLargerThanList() {
		boardSize = 11;
		
		assertThrows(BadRequestException.class, ()->{
			PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		});
	}
	
	@Test
	public void returnSizeSrinksToBoardSize() {
		BoardTileDTO[][] board = PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		
		int returnedCount = 0;
		int expectedSize = boardSize * boardSize;
		
		for (BoardTileDTO[] boardTileDTOs : board) {
			returnedCount += boardTileDTOs.length;
		}
		
		assertEquals(expectedSize, returnedCount);
	}
	
	@Test
	public void returnCenterAsSelected_whenTrue() {
		centerFree = true;
		
		BoardTileDTO[][] board = PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		
		int center = boardSize /2;
		
		boolean selected = board[center][center].getSelected();
		
		assertTrue(selected);
	}
	
	@Test
	public void returnCenterAsNotSelected_whenFalse() {
		centerFree = false;
		
		BoardTileDTO[][] board = PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		
		int center = boardSize /2;
		
		boolean selected = board[center][center].getSelected();
		
		assertFalse(selected);
	}
	
	@Test
	public void returnsNoDuplicates() {
		allSameWord = true;
		HashSet<String> set = new HashSet<>();
		
		BoardTileDTO[][] board = PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		
		for (BoardTileDTO[] boardTileDTOs : board) {
			for (BoardTileDTO tile : boardTileDTOs) {
				if(set.contains(tile.getPhrase())) {
					assertTrue(false);
					break;
				}
				set.add(tile.getPhrase());
			}
		}
	}
	
	// There is a very low chance that the shuffle will have all the phrases "in order"
	// So having it repeat five times lowers that chance a lot 
	@RepeatedTest(5)
	public void returnsArrayInRandomOrder() {
		allSameWord = true;
		
		BoardTileDTO[][] board = PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		
		int lastPharse = 0;
		
		for (BoardTileDTO[] boardTileDTOs : board) {
			for (BoardTileDTO tile : boardTileDTOs) {
				int currentPharse = Integer.parseInt(tile.getPhrase());
				
				// If still ordered it would count up by one
				if(lastPharse +1 != currentPharse) {
					break;
				}
				
				lastPharse = currentPharse;
			}
		}
	}
	
	@Test
	public void returnsAllSameWords() {
		allSameWord = true;
		
		BoardTileDTO[][] board = PhraseToTilePipeline.convert(phrases, boardSize, centerFree, allSameWord);
		
		int outOfRange = boardSize*boardSize+1;
		
		String pharseShouldNotHave = phrases.get(outOfRange).getPhrase();
		
		for (BoardTileDTO[] boardTileDTOs : board) {
			for (BoardTileDTO tile : boardTileDTOs) {
				if(pharseShouldNotHave.equals(tile.getPhrase())) {
					assertTrue(false);
					break;
				}
			}
		}
	}
	
}
