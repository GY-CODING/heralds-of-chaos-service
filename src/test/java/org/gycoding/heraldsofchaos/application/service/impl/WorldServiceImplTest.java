package org.gycoding.heraldsofchaos.application.service.impl;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.application.mapper.WorldServiceMapper;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.heraldsofchaos.domain.repository.WorldRepository;
import org.gycoding.quasar.exceptions.model.DatabaseException;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.gycoding.quasar.logs.service.Logger;
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
    void testSaveWorld() throws ServiceException {
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
        final var expectedException = new ServiceException(HeraldsOfChaosError.WORLD_ALREADY_EXISTS_CONFLICT);

        when(repository.get(worldMO.identifier())).thenReturn(Optional.of(worldMO));

        // Then
        final var error = assertThrows(
                ServiceException.class,
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
        final var expectedException = new ServiceException(HeraldsOfChaosError.WORLD_SAVE_CONFLICT);

        when(repository.get(worldMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(worldIDTO)).thenReturn(worldMO);
        when(repository.save(worldMO, worldIDTO.places())).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                ServiceException.class,
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
    void testUpdateWorld() throws ServiceException, DatabaseException {
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
    void testWrongUpdateWorldUnknownConflict() throws DatabaseException {
        // When
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldMO = mock(WorldMO.class);
        final var expectedException = new ServiceException(HeraldsOfChaosError.WORLD_UPDATE_CONFLICT);

        when(mapper.toMO(worldIDTO)).thenReturn(worldMO);
        when(repository.update(worldMO, worldIDTO.places())).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                ServiceException.class,
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
    void testDeleteWorld() throws ServiceException {
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
        final var expectedException = new ServiceException(HeraldsOfChaosError.WORLD_DELETE_CONFLICT);

        doThrow(new RuntimeException("Any exception.")).when(repository).delete(id);

        // Then
        final var error = assertThrows(
                ServiceException.class,
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
    void testGetWorld() throws ServiceException {
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
        final var expectedException = new ServiceException(HeraldsOfChaosError.WORLD_NOT_FOUND);

        when(repository.get(id)).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                ServiceException.class,
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
    void testListWorlds() throws ServiceException {
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
    void testPageWorlds() throws ServiceException {
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
    void testListPlaces() throws ServiceException {
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