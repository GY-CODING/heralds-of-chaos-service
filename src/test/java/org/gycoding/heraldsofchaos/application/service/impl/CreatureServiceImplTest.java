package org.gycoding.heraldsofchaos.application.service.impl;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.creatures.CreatureIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.heraldsofchaos.application.mapper.CreatureServiceMapper;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.gycoding.heraldsofchaos.domain.repository.CreatureRepository;
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
public class CreatureServiceImplTest {
    @Mock
    private CreatureRepository repository;

    @Mock
    private CreatureServiceMapper mapper;

    @InjectMocks
    private CreatureServiceImpl service;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test successful save of a Creature.")
    void testSaveCreature() throws APIException {
        // When
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureMO = mock(CreatureMO.class);
        final var creatureODTO = mock(CreatureODTO.class);

        when(repository.get(creatureMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(creatureIDTO)).thenReturn(creatureMO);
        when(repository.save(creatureMO)).thenReturn(creatureMO);
        when(mapper.toODTO(creatureMO, TranslatedString.EN)).thenReturn(creatureODTO);

        // Then
        final var result = service.save(creatureIDTO);

        // Verify
        verify(repository).get(creatureMO.identifier());
        verify(mapper).toMO(creatureIDTO);
        verify(repository).save(creatureMO);
        verify(mapper).toODTO(creatureMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(creatureODTO, result);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test unsuccessful save of a Creature due to it already existing.")
    void testWrongSaveCreatureAlreadyExists() {
        // When
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureMO = mock(CreatureMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CREATURE_ALREADY_EXISTS_CONFLICT.code,
                HeraldsOfChaosAPIError.CREATURE_ALREADY_EXISTS_CONFLICT.message,
                HeraldsOfChaosAPIError.CREATURE_ALREADY_EXISTS_CONFLICT.status
        );

        when(repository.get(creatureMO.identifier())).thenReturn(Optional.of(creatureMO));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.save(creatureIDTO)
        );

        // Verify
        verify(repository).get(creatureMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test unsuccessful save of a Creature due to an unknown conflict while saving.")
    void testWrongSaveCreatureUnknownConflict() throws APIException {
        // When
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureMO = mock(CreatureMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CREATURE_SAVE_CONFLICT.code,
                HeraldsOfChaosAPIError.CREATURE_SAVE_CONFLICT.message,
                HeraldsOfChaosAPIError.CREATURE_SAVE_CONFLICT.status
        );

        when(repository.get(creatureMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(creatureIDTO)).thenReturn(creatureMO);
        when(repository.save(creatureMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.save(creatureIDTO)
        );

        // Verify
        verify(repository).get(creatureMO.identifier());
        verify(mapper).toMO(creatureIDTO);
        verify(repository).save(creatureMO);
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test successful update of a Creature.")
    void testUpdateCreature() throws APIException {
        // When
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureMO = mock(CreatureMO.class);
        final var creatureUpdatedMO = mock(CreatureMO.class);
        final var creatureODTO = mock(CreatureODTO.class);

        when(mapper.toMO(creatureIDTO)).thenReturn(creatureMO);
        when(repository.update(creatureMO)).thenReturn(creatureUpdatedMO);
        when(mapper.toODTO(creatureUpdatedMO, TranslatedString.EN)).thenReturn(creatureODTO);

        // Then
        final var result = service.update(creatureIDTO);

        // Verify
        verify(mapper).toMO(creatureIDTO);
        verify(repository).update(creatureMO);
        verify(mapper).toODTO(creatureUpdatedMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(creatureODTO, result);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test unsuccessful update of a Creature due to an unknown conflict while updating.")
    void testWrongUpdateCreatureUnknownConflict() throws APIException {
        // When
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureMO = mock(CreatureMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CREATURE_UPDATE_CONFLICT.code,
                HeraldsOfChaosAPIError.CREATURE_UPDATE_CONFLICT.message,
                HeraldsOfChaosAPIError.CREATURE_UPDATE_CONFLICT.status
        );

        when(mapper.toMO(creatureIDTO)).thenReturn(creatureMO);
        when(repository.update(creatureMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.update(creatureIDTO)
        );

        // Verify
        verify(mapper).toMO(creatureIDTO);
        verify(repository).update(creatureMO);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test successful removal of a Creature.")
    void testDeleteCreature() throws APIException {
        // When
        final var id = "mock-creature-identifier";

        // Then
        service.delete(id);

        // Verify
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test unsuccessful removal of a Creature due to an unknown conflict while deleting.")
    void testWrongDeleteCreatureUnknownConflict() {
        // When
        final var id = "mock-creature-identifier";
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CREATURE_DELETE_CONFLICT.code,
                HeraldsOfChaosAPIError.CREATURE_DELETE_CONFLICT.message,
                HeraldsOfChaosAPIError.CREATURE_DELETE_CONFLICT.status
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
    @DisplayName("[CREATURE_SERVICE] - Test successful retrieval of a Creature.")
    void testGetCreature() throws APIException {
        // When
        final var id = "mock-creature-identifier";
        final var creatureMO = mock(CreatureMO.class);
        final var creatureODTO = mock(CreatureODTO.class);

        when(repository.get(id)).thenReturn(Optional.of(creatureMO));
        when(mapper.toODTO(creatureMO, TranslatedString.EN)).thenReturn(creatureODTO);

        // Then
        final var result = service.get(id, TranslatedString.EN);

        // Verify
        verify(repository).get(id);
        verify(mapper).toODTO(creatureMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(creatureODTO, result);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test unsuccessful retrieval of a Creature due to it not being found.")
    void testWrongGetCreatureNotFound() {
        // When
        final var id = "mock-creature-identifier";
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CREATURE_NOT_FOUND.code,
                HeraldsOfChaosAPIError.CREATURE_NOT_FOUND.message,
                HeraldsOfChaosAPIError.CREATURE_NOT_FOUND.status
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
    @DisplayName("[CREATURE_SERVICE] - Test successful retrieval of a list of Creatures.")
    void testListCreatures() throws APIException {
        // When
        final var creatureMO = mock(CreatureMO.class);
        final var creatureODTO = mock(CreatureODTO.class);

        when(repository.list()).thenReturn(List.of(creatureMO));
        when(mapper.toODTO(creatureMO, TranslatedString.EN)).thenReturn(creatureODTO);

        // Then
        final var result = service.list(TranslatedString.EN);

        // Verify
        verify(repository).list();
        verify(mapper).toODTO(creatureMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(List.of(creatureODTO), result);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE] - Test successful retrieval of a paginated list of Creatures.")
    void testPageCreatures() throws APIException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedCreatures = mock(Page.class);

        when(repository.page(pageable)).thenReturn(pagedCreatures);

        // Then
        final var result = service.page(pageable, TranslatedString.EN);

        // Verify
        verify(repository).page(pageable);
        verifyNoMoreInteractions(mapper, repository);
    }
}