package org.gycoding.heraldsofchaos.application.service.impl;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.application.mapper.WorldServiceMapper;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.heraldsofchaos.domain.repository.WorldRepository;
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
public class WorldServiceImplTest {
    @Mock
    private WorldRepository repository;

    @Mock
    private WorldServiceMapper mapper;

    @InjectMocks
    private WorldServiceImpl service;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test successful save of a World.")
    void testSaveWorld() throws APIException {
        // When
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldMO = mock(WorldMO.class);
        final var worldODTO = mock(WorldODTO.class);

        when(repository.get(worldMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(worldIDTO)).thenReturn(worldMO);
        when(repository.save(worldMO, worldIDTO.places())).thenReturn(worldMO);
        when(mapper.toODTO(worldMO, TranslatedString.EN)).thenReturn(worldODTO);

        // Then
        final var result = service.save(worldIDTO);

        // Verify
        verify(repository).get(worldMO.identifier());
        verify(mapper).toMO(worldIDTO);
        verify(repository).save(worldMO, worldIDTO.places());
        verify(mapper).toODTO(worldMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(worldODTO, result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test unsuccessful save of a World due to it already existing.")
    void testWrongSaveWorldAlreadyExists() {
        // When
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldMO = mock(WorldMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.WORLD_ALREADY_EXISTS_CONFLICT.code,
                HeraldsOfChaosAPIError.WORLD_ALREADY_EXISTS_CONFLICT.message,
                HeraldsOfChaosAPIError.WORLD_ALREADY_EXISTS_CONFLICT.status
        );

        when(repository.get(worldMO.identifier())).thenReturn(Optional.of(worldMO));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.save(worldIDTO)
        );

        // Verify
        verify(repository).get(worldMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test unsuccessful save of a World due to an unknown conflict while saving.")
    void testWrongSaveWorldUnknownConflict() {
        // When
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldMO = mock(WorldMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.WORLD_SAVE_CONFLICT.code,
                HeraldsOfChaosAPIError.WORLD_SAVE_CONFLICT.message,
                HeraldsOfChaosAPIError.WORLD_SAVE_CONFLICT.status
        );

        when(repository.get(worldMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(worldIDTO)).thenReturn(worldMO);
        when(repository.save(worldMO, worldIDTO.places())).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.save(worldIDTO)
        );

        // Verify
        verify(repository).get(worldMO.identifier());
        verify(mapper).toMO(worldIDTO);
        verify(repository).save(worldMO, worldIDTO.places());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test successful update of a World.")
    void testUpdateWorld() throws APIException {
        // When
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldMO = mock(WorldMO.class);
        final var worldUpdatedMO = mock(WorldMO.class);
        final var worldODTO = mock(WorldODTO.class);

        when(mapper.toMO(worldIDTO)).thenReturn(worldMO);
        when(repository.update(worldMO, worldIDTO.places())).thenReturn(worldUpdatedMO);
        when(mapper.toODTO(worldUpdatedMO, TranslatedString.EN)).thenReturn(worldODTO);

        // Then
        final var result = service.update(worldIDTO);

        // Verify
        verify(mapper).toMO(worldIDTO);
        verify(repository).update(worldMO, worldIDTO.places());
        verify(mapper).toODTO(worldUpdatedMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(worldODTO, result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test unsuccessful update of a World due to an unknown conflict while updating.")
    void testWrongUpdateWorldUnknownConflict() throws APIException {
        // When
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldMO = mock(WorldMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.WORLD_UPDATE_CONFLICT.code,
                HeraldsOfChaosAPIError.WORLD_UPDATE_CONFLICT.message,
                HeraldsOfChaosAPIError.WORLD_UPDATE_CONFLICT.status
        );

        when(mapper.toMO(worldIDTO)).thenReturn(worldMO);
        when(repository.update(worldMO, worldIDTO.places())).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> service.update(worldIDTO)
        );

        // Verify
        verify(mapper).toMO(worldIDTO);
        verify(repository).update(worldMO, worldIDTO.places());
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test successful removal of a World.")
    void testDeleteWorld() throws APIException {
        // When
        final var id = "mock-world-identifier";

        // Then
        service.delete(id);

        // Verify
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test unsuccessful removal of a World due to an unknown conflict while deleting.")
    void testWrongDeleteWorldUnknownConflict() {
        // When
        final var id = "mock-world-identifier";
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.WORLD_DELETE_CONFLICT.code,
                HeraldsOfChaosAPIError.WORLD_DELETE_CONFLICT.message,
                HeraldsOfChaosAPIError.WORLD_DELETE_CONFLICT.status
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
    @DisplayName("[WORLD_SERVICE] - Test successful retrieval of a World.")
    void testGetWorld() throws APIException {
        // When
        final var id = "mock-world-identifier";
        final var worldMO = mock(WorldMO.class);
        final var worldODTO = mock(WorldODTO.class);

        when(repository.get(id)).thenReturn(Optional.of(worldMO));
        when(mapper.toODTO(worldMO, TranslatedString.EN)).thenReturn(worldODTO);

        // Then
        final var result = service.get(id, TranslatedString.EN);

        // Verify
        verify(repository).get(id);
        verify(mapper).toODTO(worldMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(worldODTO, result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test unsuccessful retrieval of a World due to it not being found.")
    void testWrongGetWorldNotFound() {
        // When
        final var id = "mock-world-identifier";
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.WORLD_NOT_FOUND.code,
                HeraldsOfChaosAPIError.WORLD_NOT_FOUND.message,
                HeraldsOfChaosAPIError.WORLD_NOT_FOUND.status
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
    @DisplayName("[WORLD_SERVICE] - Test successful retrieval of a list of Worlds.")
    void testListWorlds() throws APIException {
        // When
        final var worldMO = mock(WorldMO.class);
        final var worldODTO = mock(WorldODTO.class);

        when(repository.list()).thenReturn(List.of(worldMO));
        when(mapper.toODTO(worldMO, TranslatedString.EN)).thenReturn(worldODTO);

        // Then
        final var result = service.list(TranslatedString.EN);

        // Verify
        verify(repository).list();
        verify(mapper).toODTO(worldMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(List.of(worldODTO), result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test successful retrieval of a paginated list of Worlds.")
    void testPageWorlds() throws APIException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedWorlds = mock(Page.class);

        when(repository.page(pageable)).thenReturn(pagedWorlds);

        // Then
        final var result = service.page(pageable, TranslatedString.EN);

        // Verify
        verify(repository).page(pageable);
        verifyNoMoreInteractions(mapper, repository);
    }

    @Test
    @DisplayName("[WORLD_SERVICE] - Test successful retrieval of a list of Places inside a specified World.")
    void testListPlaces() throws APIException {
        // When
        final var id = "mock-world-identifier";
        final var worldMO = mock(WorldMO.class);
        final var worldODTO = mock(WorldODTO.class);

        when(repository.get(id)).thenReturn(Optional.of(worldMO));
        when(mapper.toODTO(worldMO, TranslatedString.EN)).thenReturn(worldODTO);

        // Then
        final var result = service.listPlaces(id, TranslatedString.EN);

        // Verify
        verify(repository).get(id);
        verify(mapper).toODTO(worldMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(worldODTO.places(), result);
    }
}