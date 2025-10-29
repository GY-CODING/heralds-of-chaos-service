package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.PlaceDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.OrderEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.PlaceEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.PlaceMongoRepository;
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
public class PlaceDatabaseImplTest {
    @Mock
    private OrderMongoRepository orderRepository;

    @Mock
    private PlaceMongoRepository repository;

    @Mock
    private PlaceDatabaseMapper mapper;

    @InjectMocks
    private PlaceDatabaseImpl database;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test successful save of a Place.")
    void testSavePlace() {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var placeEntity = mock(PlaceEntity.class);

        when(mapper.toEntity(placeMO)).thenReturn(placeEntity);
        when(repository.save(placeEntity)).thenReturn(placeEntity);
        when(mapper.toMO(placeEntity)).thenReturn(placeMO);

        // Then
        final var result = database.save(placeMO);

        // Verify
        verify(mapper).toEntity(placeMO);
        verify(repository).save(placeEntity);
        verify(mapper).toMO(placeEntity);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(placeMO, result);
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test successful update of a Place.")
    void testUpdatePlace() throws DatabaseException {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var placeEntity = mock(PlaceEntity.class);
        final var placeUpdatedEntity = mock(PlaceEntity.class);

        when(repository.findByIdentifier(placeMO.identifier())).thenReturn(Optional.of(placeEntity));
        when(mapper.toUpdatedEntity(placeEntity, placeMO)).thenReturn(placeUpdatedEntity);
        when(repository.save(placeUpdatedEntity)).thenReturn(placeUpdatedEntity);
        when(mapper.toMO(placeUpdatedEntity)).thenReturn(placeMO);

        // Then
        final var result = database.update(placeMO);

        // Verify
        verify(repository).findByIdentifier(placeMO.identifier());
        verify(mapper).toUpdatedEntity(placeEntity, placeMO);
        verify(repository).save(placeUpdatedEntity);
        verify(mapper).toMO(placeUpdatedEntity);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(placeMO, result);
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test unsuccessful update of a Place due to its no existence.")
    void testWrongUpdatePlace() {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var expectedException = new ServiceException(HeraldsOfChaosError.PLACE_NOT_FOUND);

        when(repository.findByIdentifier(placeMO.identifier())).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                DatabaseException.class,
                () -> database.update(placeMO)
        );

        // Verify
        verify(repository).findByIdentifier(placeMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test successful removal of a Place.")
    void testDeletePlace() {
        // When
        final var id = "mock-place-identifier";

        // Then
        database.delete(id);

        // Verify
        verify(repository).removeByIdentifier(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test successful retrieval of a Place.")
    void testGetPlace() {
        // When
        final var id = "mock-place-identifier";
        final var placeMO = mock(PlaceMO.class);
        final var placeEntity = mock(PlaceEntity.class);

        when(repository.findByIdentifier(id)).thenReturn(Optional.of(placeEntity));
        when(mapper.toMO(placeEntity)).thenReturn(placeMO);

        // Then
        final var result = database.get(id);

        // Verify
        verify(repository).findByIdentifier(id);
        verify(mapper).toMO(placeEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(Optional.of(placeMO), result);
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test successful retrieval of a list of Places.")
    void testListPlaces() {
        // When
        final var orderEntity = mock(OrderEntity.class);
        final var placeMO = mock(PlaceMO.class);
        final var placeEntity = mock(PlaceEntity.class);

        when(orderRepository.findByCollection("Place")).thenReturn(Optional.of(orderEntity));
        when(repository.findAll()).thenReturn(List.of(placeEntity));
        when(mapper.toMO(placeEntity)).thenReturn(placeMO);

        // Then
        final var result = database.list();

        // Verify
        verify(orderRepository).findByCollection("Place");
        verify(repository).findAll();
        verify(mapper).toMO(placeEntity);
        verifyNoMoreInteractions(orderRepository, repository, mapper);

        assertEquals(List.of(placeMO), result);
    }

    @Test
    @DisplayName("[PLACE_DATABASE] - Test successful retrieval of a paginated list of Places.")
    void testPagePlaces() {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedPlaces = mock(Page.class);

        when(repository.findAll(pageable)).thenReturn(pagedPlaces);

        // Then
        final var result = database.page(pageable);

        // Verify
        verify(repository).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }
}