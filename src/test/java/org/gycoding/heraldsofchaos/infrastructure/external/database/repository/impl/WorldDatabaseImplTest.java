package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.WorldDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.OrderEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.PlaceEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.WorldEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.PlaceMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.WorldMongoRepository;
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
public class WorldDatabaseImplTest {
    @Mock
    private OrderMongoRepository orderRepository;

    @Mock
    private WorldMongoRepository repository;

    @Mock
    private WorldDatabaseMapper mapper;

    @Mock
    private PlaceMongoRepository placeRepository;

    @InjectMocks
    private WorldDatabaseImpl database;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test successful save of a World.")
    void testSaveWorld() {
        // When
        final var worldMO = mock(WorldMO.class);
        final var places = List.of("mock-place-identifier");
        final var placeEntity = mock(PlaceEntity.class);
        final var worldEntity = mock(WorldEntity.class);

        when(placeRepository.findByIdentifier("mock-place-identifier")).thenReturn(Optional.of(placeEntity));
        when(mapper.toEntity(worldMO, List.of(placeEntity))).thenReturn(worldEntity);
        when(repository.save(worldEntity)).thenReturn(worldEntity);
        when(mapper.toMO(worldEntity)).thenReturn(worldMO);

        // Then
        final var result = database.save(worldMO, places);

        // Verify
        verify(placeRepository).findByIdentifier("mock-place-identifier");
        verify(mapper).toEntity(worldMO, List.of(placeEntity));
        verify(repository).save(worldEntity);
        verify(mapper).toMO(worldEntity);
        verifyNoMoreInteractions(placeRepository, mapper, repository);

        assertEquals(worldMO, result);
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test successful update of a World.")
    void testUpdateWorld() throws DatabaseException {
        // When
        final var worldMO = mock(WorldMO.class);
        final var worldEntity = mock(WorldEntity.class);
        final var places = List.of("mock-place-identifier");
        final var placeEntity = mock(PlaceEntity.class);
        final var worldUpdatedEntity = mock(WorldEntity.class);

        when(repository.findByIdentifier(worldMO.identifier())).thenReturn(Optional.of(worldEntity));
        when(placeRepository.findByIdentifier("mock-place-identifier")).thenReturn(Optional.of(placeEntity));
        when(mapper.toUpdatedEntity(worldEntity, worldMO, List.of(placeEntity))).thenReturn(worldUpdatedEntity);
        when(repository.save(worldUpdatedEntity)).thenReturn(worldUpdatedEntity);
        when(mapper.toMO(worldUpdatedEntity)).thenReturn(worldMO);

        // Then
        final var result = database.update(worldMO, places);

        // Verify
        verify(repository).findByIdentifier(worldMO.identifier());
        verify(placeRepository).findByIdentifier("mock-place-identifier");
        verify(mapper).toUpdatedEntity(worldEntity, worldMO, List.of(placeEntity));
        verify(repository).save(worldUpdatedEntity);
        verify(mapper).toMO(worldUpdatedEntity);
        verifyNoMoreInteractions(mapper, placeRepository, repository);

        assertEquals(worldMO, result);
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test unsuccessful update of a World due to its no existence.")
    void testWrongUpdateWorld() {
        // When
        final var worldMO = mock(WorldMO.class);
        final var places = List.of("mock-place-identifier");
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.WORLD_NOT_FOUND.code,
                HeraldsOfChaosError.WORLD_NOT_FOUND.message,
                HeraldsOfChaosError.WORLD_NOT_FOUND.status
        );

        when(repository.findByIdentifier(worldMO.identifier())).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                DatabaseException.class,
                () -> database.update(worldMO, places)
        );

        // Verify
        verify(repository).findByIdentifier(worldMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test successful removal of a World.")
    void testDeleteWorld() {
        // When
        final var id = "mock-world-identifier";

        // Then
        database.delete(id);

        // Verify
        verify(repository).removeByIdentifier(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test successful retrieval of a World.")
    void testGetWorld() {
        // When
        final var id = "mock-world-identifier";
        final var worldMO = mock(WorldMO.class);
        final var worldEntity = mock(WorldEntity.class);

        when(repository.findByIdentifier(id)).thenReturn(Optional.of(worldEntity));
        when(mapper.toMO(worldEntity)).thenReturn(worldMO);

        // Then
        final var result = database.get(id);

        // Verify
        verify(repository).findByIdentifier(id);
        verify(mapper).toMO(worldEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(Optional.of(worldMO), result);
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test successful retrieval of a list of Worlds.")
    void testListWorlds() {
        // When
        final var orderEntity = mock(OrderEntity.class);
        final var worldMO = mock(WorldMO.class);
        final var worldEntity = mock(WorldEntity.class);

        when(orderRepository.findByCollection("World")).thenReturn(Optional.of(orderEntity));
        when(repository.findAll()).thenReturn(List.of(worldEntity));
        when(mapper.toMO(worldEntity)).thenReturn(worldMO);

        // Then
        final var result = database.list();

        // Verify
        verify(orderRepository).findByCollection("World");
        verify(repository).findAll();
        verify(mapper).toMO(worldEntity);
        verifyNoMoreInteractions(orderRepository, repository, mapper);

        assertEquals(List.of(worldMO), result);
    }

    @Test
    @DisplayName("[WORLD_DATABASE] - Test successful retrieval of a paginated list of Worlds.")
    void testPageWorlds() {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedWorlds = mock(Page.class);

        when(repository.findAll(pageable)).thenReturn(pagedWorlds);

        // Then
        final var result = database.page(pageable);

        // Verify
        verify(repository).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }
}