package com.cryptic.bingo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cryptic.bingo.dal.BingoMixRepository;
import com.cryptic.bingo.dal.RegisteredUserRepository;
import com.cryptic.bingo.dto.MixCardDTO;
import com.cryptic.bingo.exceptions.BadRequestException;
import com.cryptic.bingo.exceptions.NotFoundException;
import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.model.Phrase;
import com.cryptic.bingo.model.RegisteredUser;
import com.cryptic.bingo.service.BingoMixService;

@ExtendWith(MockitoExtension.class)
public class BingoMixServiceTests {
	private BingoMixService service;

	@Mock
	private BingoMixRepository bingoMixRepoMock;

	@Mock
	private RegisteredUserRepository registeredUserRepMock;

	@Mock
	private BingoMix bingoMixMock;

	@Mock
	private RegisteredUser registeredUserMock;

	@BeforeEach
	public void setup() {
		service = new BingoMixService(bingoMixRepoMock, registeredUserRepMock);
	}

	@Test
	public void getAllMixesOrderedByOwned_throwsNotFound_onBadUserReturn() {
		int id = 5;

		when(registeredUserRepMock.findById(id)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.getAllMixesOrderedByOwned(id);
		});

	}

	@Test
	public void getAllMixesOrderedByOwned_callsCorrectMethods_andReturnsProperSize() {
		int id = 5;
		List<BingoMix> mixes = new ArrayList<>();
		mixes.add(bingoMixMock);

		when(registeredUserRepMock.findById(id)).thenReturn(Optional.of(registeredUserMock));
		when(bingoMixRepoMock.getByOwnerOrderByName(registeredUserMock)).thenReturn(mixes);
		when(bingoMixRepoMock.getByOwnerIsNotOrderByName(registeredUserMock)).thenReturn(mixes);
		when(bingoMixRepoMock.getByOwnerIsNullOrderByName()).thenReturn(mixes);

		List<MixCardDTO> returned = service.getAllMixesOrderedByOwned(id);

		assertNotEquals(mixes, returned);
		assertEquals(3, returned.size());
		verify(registeredUserRepMock, times(1)).findById(id);
		verify(bingoMixRepoMock, times(1)).getByOwnerOrderByName(registeredUserMock);
		verify(bingoMixRepoMock, times(1)).getByOwnerIsNotOrderByName(registeredUserMock);
		verify(bingoMixRepoMock, times(1)).getByOwnerIsNullOrderByName();
	}

	@Test
	public void findById_callsCorrectMethods_andReturnsObject() {
		int userid = 5;

		when(bingoMixRepoMock.findById(userid)).thenReturn(Optional.of(bingoMixMock));

		BingoMix returned = service.findById(userid);

		assertEquals(bingoMixMock, returned);
		verify(bingoMixRepoMock, times(1)).findById(userid);
	}

	@Test
	public void updateBingoMix_throwsNotFound_whenUserNotFound() {
		String username = "username";

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.updateBingoMix(bingoMixMock, username);
		});

		verify(registeredUserRepMock, times(1)).findByUsername(username);
		verify(bingoMixRepoMock, times(0)).save(any());
	}

	@Test
	public void updateBingoMix_throwsBadRequest_whenNotOwner() {
		RegisteredUser notOwner = new RegisteredUser();
		String username = "username";

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.of(notOwner));
		when(bingoMixMock.getOwner()).thenReturn(registeredUserMock);

		assertThrows(BadRequestException.class, () -> {
			service.updateBingoMix(bingoMixMock, username);
		});

		verify(registeredUserRepMock, times(1)).findByUsername(username);
		verify(bingoMixMock, times(2)).getOwner();
		verify(bingoMixMock, times(0)).setOwner(registeredUserMock);
		verify(bingoMixRepoMock, times(0)).save(bingoMixMock);
	}

	@Test
	public void updateBingoMix_callsCorrectMethods_whenMixHasNoOwner() {
		String username = "username";

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.of(registeredUserMock));
		when(bingoMixMock.getOwner()).thenReturn(null);

		service.updateBingoMix(bingoMixMock, username);

		verify(registeredUserRepMock, times(1)).findByUsername(username);
		verify(bingoMixMock, times(1)).getOwner();
		verify(bingoMixMock, times(1)).setOwner(registeredUserMock);
		verify(bingoMixRepoMock, times(1)).save(bingoMixMock);
	}

	@Test
	public void deleteMixById_callsCorrectMethods() {
		int id = 5;

		service.deleteMixById(id);

		verify(bingoMixRepoMock, times(1)).delete(any());
	}

	@Test
	public void copyBingoMix_throwsNotFound_onNoUser() {
		int mixId = 5;
		String username = "username";

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.copyBingoMix(mixId, username);
		});

		verify(registeredUserRepMock, times(1)).findByUsername(username);

		verify(bingoMixRepoMock, times(0)).save(any());
	}

	@Test
	public void copyBingoMix_throwsNotFound_onNoMix() {
		int mixId = 5;
		String username = "username";

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.of(registeredUserMock));
		when(bingoMixRepoMock.findById(mixId)).thenReturn(Optional.empty());

		assertThrows(NotFoundException.class, () -> {
			service.copyBingoMix(mixId, username);
		});

		verify(registeredUserRepMock, times(1)).findByUsername(username);
		verify(bingoMixRepoMock, times(1)).findById(mixId);

		verify(bingoMixRepoMock, times(0)).save(any());
	}

	@Test
	public void copyBingoMix_callCorrectMethods_andReturnsNotEqual() {
		int mixId = 5;
		String username = "username";
		String bingoMixName = "mix1";
		List<Phrase> phrases = new ArrayList<>();

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.of(registeredUserMock));
		when(bingoMixRepoMock.findById(mixId)).thenReturn(Optional.of(bingoMixMock));
		when(bingoMixMock.getName()).thenReturn(bingoMixName);
		when(bingoMixMock.getPhrases()).thenReturn(phrases);

		BingoMix returned = service.copyBingoMix(mixId, username);

		assertNotEquals(bingoMixMock, returned);

		verify(registeredUserRepMock, times(1)).findByUsername(username);
		verify(bingoMixRepoMock, times(1)).findById(mixId);
		verify(bingoMixMock, times(1)).getName();
		verify(bingoMixMock, times(1)).getPhrases();

		verify(bingoMixRepoMock, times(1)).save(any());
	}

	@Test
	public void copyBingoMix_returnsDeepCopy_andNameHasCopyInIt() {
		int mixId = 5;
		String username = "username";
		String bingoMixName = "mix1";
		List<Phrase> phrases = new ArrayList<>();

		for (int i = 0; i < 5; i++) {
			phrases.add(new Phrase(i + "", "", ""));
		}

		when(registeredUserRepMock.findByUsername(username)).thenReturn(Optional.of(registeredUserMock));
		when(bingoMixRepoMock.findById(mixId)).thenReturn(Optional.of(bingoMixMock));
		when(bingoMixMock.getName()).thenReturn(bingoMixName);
		when(bingoMixMock.getPhrases()).thenReturn(phrases);

		BingoMix returned = service.copyBingoMix(mixId, username);

		assertNotEquals(bingoMixMock, returned);

		assertTrue(returned.getName().contains(bingoMixName));
		assertTrue(returned.getName().contains("(copy)"));
		
		List<Phrase> copy = returned.getPhrases();
		
		for (int i = 0; i < phrases.size(); i++) {
			// memory space different
			assertNotEquals(phrases.get(i), copy.get(i));
			
			// same values
			assertEquals(phrases.get(i).getPhrase(), copy.get(i).getPhrase());
			assertEquals(phrases.get(i).getColor(), copy.get(i).getColor());
			assertEquals(phrases.get(i).getFont(), copy.get(i).getFont());
		}
	}

}
