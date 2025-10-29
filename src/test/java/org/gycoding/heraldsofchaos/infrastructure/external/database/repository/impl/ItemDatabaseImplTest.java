package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.ItemDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.OrderEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.items.ItemEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.ItemMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
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
public class ItemDatabaseImplTest {
    @Mock
    private OrderMongoRepository orderRepository;

    @Mock
    private ItemMongoRepository repository;

    @Mock
    private ItemDatabaseMapper mapper;

    @InjectMocks
    private ItemDatabaseImpl database;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test successful save of a Item.")
    void testSaveItem() {
        // When
        final var itemMO = mock(ItemMO.class);
        final var itemEntity = mock(ItemEntity.class);

        when(mapper.toEntity(itemMO)).thenReturn(itemEntity);
        when(repository.save(itemEntity)).thenReturn(itemEntity);
        when(mapper.toMO(itemEntity)).thenReturn(itemMO);

        // Then
        final var result = database.save(itemMO);

        // Verify
        verify(mapper).toEntity(itemMO);
        verify(repository).save(itemEntity);
        verify(mapper).toMO(itemEntity);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(itemMO, result);
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test successful update of a Item.")
    void testUpdateItem() throws DatabaseException {
        // When
        final var itemMO = mock(ItemMO.class);
        final var itemEntity = mock(ItemEntity.class);
        final var itemUpdatedEntity = mock(ItemEntity.class);

        when(repository.findByIdentifier(itemMO.identifier())).thenReturn(Optional.of(itemEntity));
        when(mapper.toUpdatedEntity(itemEntity, itemMO)).thenReturn(itemUpdatedEntity);
        when(repository.save(itemUpdatedEntity)).thenReturn(itemUpdatedEntity);
        when(mapper.toMO(itemUpdatedEntity)).thenReturn(itemMO);

        // Then
        final var result = database.update(itemMO);

        // Verify
        verify(repository).findByIdentifier(itemMO.identifier());
        verify(mapper).toUpdatedEntity(itemEntity, itemMO);
        verify(repository).save(itemUpdatedEntity);
        verify(mapper).toMO(itemUpdatedEntity);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(itemMO, result);
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test unsuccessful update of a Item due to its no existence.")
    void testWrongUpdateItem() {
        // When
        final var itemMO = mock(ItemMO.class);
        final var expectedException = new ServiceException(HeraldsOfChaosError.ITEM_NOT_FOUND);

        when(repository.findByIdentifier(itemMO.identifier())).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                DatabaseException.class,
                () -> database.update(itemMO)
        );

        // Verify
        verify(repository).findByIdentifier(itemMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test successful removal of a Item.")
    void testDeleteItem() {
        // When
        final var id = "mock-item-identifier";

        // Then
        database.delete(id);

        // Verify
        verify(repository).removeByIdentifier(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test successful retrieval of a Item.")
    void testGetItem() {
        // When
        final var id = "mock-item-identifier";
        final var itemMO = mock(ItemMO.class);
        final var itemEntity = mock(ItemEntity.class);

        when(repository.findByIdentifier(id)).thenReturn(Optional.of(itemEntity));
        when(mapper.toMO(itemEntity)).thenReturn(itemMO);

        // Then
        final var result = database.get(id);

        // Verify
        verify(repository).findByIdentifier(id);
        verify(mapper).toMO(itemEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(Optional.of(itemMO), result);
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test successful retrieval of a list of Items.")
    void testListItems() {
        // When
        final var orderEntity = mock(OrderEntity.class);
        final var itemMO = mock(ItemMO.class);
        final var itemEntity = mock(ItemEntity.class);

        when(orderRepository.findByCollection("Item")).thenReturn(Optional.of(orderEntity));
        when(repository.findAll()).thenReturn(List.of(itemEntity));
        when(mapper.toMO(itemEntity)).thenReturn(itemMO);

        // Then
        final var result = database.list();

        // Verify
        verify(orderRepository).findByCollection("Item");
        verify(repository).findAll();
        verify(mapper).toMO(itemEntity);
        verifyNoMoreInteractions(orderRepository, repository, mapper);

        assertEquals(List.of(itemMO), result);
    }

    @Test
    @DisplayName("[ITEM_DATABASE] - Test successful retrieval of a paginated list of Items.")
    void testPageItems() {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedItems = mock(Page.class);

        when(repository.findAll(pageable)).thenReturn(pagedItems);

        // Then
        final var result = database.page(pageable);

        // Verify
        verify(repository).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }
}