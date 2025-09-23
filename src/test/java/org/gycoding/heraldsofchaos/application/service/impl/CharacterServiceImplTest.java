package org.gycoding.heraldsofchaos.application.service.impl;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.characters.CharacterIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.characters.CharacterODTO;
import org.gycoding.heraldsofchaos.application.mapper.CharacterServiceMapper;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.domain.repository.CharacterRepository;
import org.gycoding.logs.logger.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterServiceImplTest {
    @Mock
    private CharacterRepository repository;

    @Mock
    private CharacterServiceMapper mapper;

    @InjectMocks
    private CharacterServiceImpl service;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test successful save of a Character.")
    void testSaveCharacter() throws APIException {
        // When
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterMO = mock(CharacterMO.class);
        final var characterODTO = mock(CharacterODTO.class);

        when(repository.get(characterMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(characterIDTO)).thenReturn(characterMO);
        when(repository.save(characterMO)).thenReturn(characterMO);
        when(mapper.toODTO(characterMO, TranslatedString.EN)).thenReturn(characterODTO);

        // Then
        final var result = service.save(characterIDTO);

        // Verify
        verify(repository).get(characterMO.identifier());
        verify(mapper).toMO(characterIDTO);
        verify(repository).save(characterMO);
        verify(mapper).toODTO(characterMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(characterODTO, result);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test unsuccessful save of a Character due to it already existing.")
    void testWrongSaveCharacterAsItAlreadyExists() {
        // When
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterMO = mock(CharacterMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CHARACTER_ALREADY_EXISTS_CONFLICT.code,
                HeraldsOfChaosAPIError.CHARACTER_ALREADY_EXISTS_CONFLICT.message,
                HeraldsOfChaosAPIError.CHARACTER_ALREADY_EXISTS_CONFLICT.status
        );

        when(repository.get(characterMO.identifier())).thenReturn(Optional.of(characterMO));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.save(characterIDTO)
        );

        // Verify
        verify(repository).get(characterMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test unsuccessful save of a Character due to an unknown conflict while saving.")
    void testWrongSaveCharacterAsAConflictHappened() throws APIException {
        // When
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterMO = mock(CharacterMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CHARACTER_SAVE_CONFLICT.code,
                HeraldsOfChaosAPIError.CHARACTER_SAVE_CONFLICT.message,
                HeraldsOfChaosAPIError.CHARACTER_SAVE_CONFLICT.status
        );

        when(repository.get(characterMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(characterIDTO)).thenReturn(characterMO);
        when(repository.save(characterMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.save(characterIDTO)
        );

        // Verify
        verify(repository).get(characterMO.identifier());
        verify(mapper).toMO(characterIDTO);
        verify(repository).save(characterMO);
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }
}