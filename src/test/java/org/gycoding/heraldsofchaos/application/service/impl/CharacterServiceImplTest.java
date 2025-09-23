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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
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
    void testWrongSaveCharacterAlreadyExists() {
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
    void testWrongSaveCharacterUnknownConflict() throws APIException {
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

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test successful update of a Character.")
    void testUpdateCharacter() throws APIException {
        // When
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterMO = mock(CharacterMO.class);
        final var characterUpdatedMO = mock(CharacterMO.class);
        final var characterODTO = mock(CharacterODTO.class);

        when(mapper.toMO(characterIDTO)).thenReturn(characterMO);
        when(repository.update(characterMO)).thenReturn(characterUpdatedMO);
        when(mapper.toODTO(characterUpdatedMO, TranslatedString.EN)).thenReturn(characterODTO);

        // Then
        final var result = service.update(characterIDTO);

        // Verify
        verify(mapper).toMO(characterIDTO);
        verify(repository).update(characterMO);
        verify(mapper).toODTO(characterUpdatedMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(characterODTO, result);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test unsuccessful update of a Character due to an unknown conflict while updating.")
    void testWrongUpdateCharacterUnknownConflict() throws APIException {
        // When
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterMO = mock(CharacterMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CHARACTER_UPDATE_CONFLICT.code,
                HeraldsOfChaosAPIError.CHARACTER_UPDATE_CONFLICT.message,
                HeraldsOfChaosAPIError.CHARACTER_UPDATE_CONFLICT.status
        );

        when(mapper.toMO(characterIDTO)).thenReturn(characterMO);
        when(repository.update(characterMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.update(characterIDTO)
        );

        // Verify
        verify(mapper).toMO(characterIDTO);
        verify(repository).update(characterMO);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test successful removal of a Character.")
    void testDeleteCharacter() throws APIException {
        // When
        final var id = "mock-character-identifier";

        // Then
        service.delete(id);

        // Verify
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test unsuccessful removal of a Character due to an unknown conflict while deleting.")
    void testWrongDeleteCharacterUnknownConflict() {
        // When
        final var id = "mock-character-identifier";
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CHARACTER_DELETE_CONFLICT.code,
                HeraldsOfChaosAPIError.CHARACTER_DELETE_CONFLICT.message,
                HeraldsOfChaosAPIError.CHARACTER_DELETE_CONFLICT.status
        );

        doThrow(new RuntimeException("Any exception.")).when(repository).delete(id);

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.delete(id)
        );

        // Verify
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test successful retrieval of a Character.")
    void testGetCharacter() throws APIException {
        // When
        final var id = "mock-character-identifier";
        final var characterMO = mock(CharacterMO.class);
        final var characterODTO = mock(CharacterODTO.class);

        when(repository.get(id)).thenReturn(Optional.of(characterMO));
        when(mapper.toODTO(characterMO, TranslatedString.EN)).thenReturn(characterODTO);

        // Then
        final var result = service.get(id, TranslatedString.EN);

        // Verify
        verify(repository).get(id);
        verify(mapper).toODTO(characterMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(characterODTO, result);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test unsuccessful retrieval of a Character due to it not being found.")
    void testWrongGetCharacterNotFound() {
        // When
        final var id = "mock-character-identifier";
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.code,
                HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.message,
                HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.status
        );

        when(repository.get(id)).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.get(id, TranslatedString.EN)
        );

        // Verify
        verify(repository).get(id);
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test successful retrieval of a list of Characters.")
    void testListCharacters() throws APIException {
        // When
        final var characterMO = mock(CharacterMO.class);
        final var characterODTO = mock(CharacterODTO.class);

        when(repository.list()).thenReturn(List.of(characterMO));
        when(mapper.toODTO(characterMO, TranslatedString.EN)).thenReturn(characterODTO);

        // Then
        final var result = service.list(TranslatedString.EN);

        // Verify
        verify(repository).list();
        verify(mapper).toODTO(characterMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(List.of(characterODTO), result);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE] - Test successful retrieval of a paginated list of Characters.")
    void testPageCharacters() throws APIException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedCharacters = mock(Page.class);

        when(repository.page(pageable)).thenReturn(pagedCharacters);

        // Then
        final var result = service.page(pageable, TranslatedString.EN);

        // Verify
        verify(repository).page(pageable);
        verifyNoMoreInteractions(mapper, repository);
    }
}