package com.cryptic.bingo.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import com.cryptic.bingo.controller.BingoMixController;
import com.cryptic.bingo.dto.MixCardDTO;
import com.cryptic.bingo.exceptions.ForbiddenException;
import com.cryptic.bingo.model.BingoMix;
import com.cryptic.bingo.service.BingoMixService;

@ExtendWith(MockitoExtension.class)
public class BingoMixControllerTests {
	private BingoMixController controller;

	@Mock
	private BingoMixService serviceMock;

	@Mock
	private List<MixCardDTO> mixCardDTOsMock;

	@Mock
	private BingoMix bingoMixMock;

	@Mock
	private Authentication authMock;

	@BeforeEach
	public void setup() {
		controller = new BingoMixController(serviceMock);
	}

	@Test
	public void getAllMixesOrderedByOwned_callsCorrectMethods() {
		int userId = 5;

		when(serviceMock.getAllMixesOrderedByOwned(userId)).thenReturn(mixCardDTOsMock);

		List<MixCardDTO> returned = controller.getAllMixesOrderedByOwned(userId);

		assertEquals(mixCardDTOsMock, returned);
		verify(serviceMock, times(1)).getAllMixesOrderedByOwned(userId);
	}

	@Test
	public void getMixById_callsCorrectMethods() {
		int mixId = 5;

		when(serviceMock.findById(mixId)).thenReturn(bingoMixMock);

		BingoMix returned = controller.getMixById(mixId);

		assertEquals(bingoMixMock, returned);
		verify(serviceMock, times(1)).findById(mixId);
	}

	@Test
	public void updateBingoMix_callsCorrectMethods() {
		String username = "name";

		when(authMock.isAuthenticated()).thenReturn(true);
		when(authMock.getName()).thenReturn(username);

		controller.updateBingoMix(bingoMixMock, authMock);

		verify(serviceMock, times(1)).updateBingoMix(bingoMixMock, username);
		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(1)).getName();
	}

	@Test
	public void updateBingoMix_throwsForbiddenOnNotAuth() {
		when(authMock.isAuthenticated()).thenReturn(false);

		assertThrows(ForbiddenException.class, () ->{
			controller.updateBingoMix(bingoMixMock, authMock);
		});
		
		verify(serviceMock, times(0)).updateBingoMix(any(), anyString());
		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(0)).getName();
	}

	@Test
	public void updateBingoMix_throwsForbiddenOnNullAuth() {
		assertThrows(ForbiddenException.class, () -> {
			controller.updateBingoMix(bingoMixMock, null);
		});

		verify(serviceMock, times(0)).updateBingoMix(any(), anyString());
		verify(authMock, times(0)).isAuthenticated();
		verify(authMock, times(0)).getName();
	}

	@Test
	public void copyBingoMix_throwsForbiddenOnNotAuth() {
		int mixId = 5;

		when(authMock.isAuthenticated()).thenReturn(false);

		assertThrows(ForbiddenException.class, () -> {
			controller.copyBingoMix(mixId, authMock);
		});

		verify(serviceMock, times(0)).copyBingoMix(anyInt(), anyString());
		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(0)).getName();
	}

	@Test
	public void copyBingoMix_throwsForbiddenOnNullAuth() {
		int mixId = 5;

		assertThrows(ForbiddenException.class, () -> {
			controller.copyBingoMix(mixId, null);
		});

		verify(serviceMock, times(0)).copyBingoMix(anyInt(), anyString());
		verify(authMock, times(0)).isAuthenticated();
		verify(authMock, times(0)).getName();
	}

	@Test
	public void copyBingoMix_callsCorrectMethods() {
		String username = "name";
		int mixId = 5;

		when(authMock.isAuthenticated()).thenReturn(true);
		when(authMock.getName()).thenReturn(username);

		controller.copyBingoMix(mixId, authMock);

		verify(serviceMock, times(1)).copyBingoMix(mixId, username);
		verify(authMock, times(1)).isAuthenticated();
		verify(authMock, times(1)).getName();
	}
	
	@Test
	public void deleteMixById_callsCorrectMethods() {
		int mixId = 5;

		controller.deleteMixById(mixId);

		verify(serviceMock, times(1)).deleteMixById(mixId);
	}

}
