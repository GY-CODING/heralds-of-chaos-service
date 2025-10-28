package org.gycoding.heraldsofchaos.application.service.impl;

import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.application.mapper.ItemServiceMapper;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.domain.repository.ItemRepository;
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
public class ItemServiceImplTest {
    @Mock
    private ItemRepository repository;

    @Mock
    private ItemServiceMapper mapper;

    @InjectMocks
    private ItemServiceImpl service;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test successful save of a Item.")
    void testSaveItem() throws ServiceException {
        // When
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemMO = mock(ItemMO.class);
        final var itemODTO = mock(ItemODTO.class);

        when(repository.get(itemMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(itemIDTO)).thenReturn(itemMO);
        when(repository.save(itemMO)).thenReturn(itemMO);
        when(mapper.toODTO(itemMO, TranslatedString.EN)).thenReturn(itemODTO);

        // Then
        final var result = service.save(itemIDTO);

        // Verify
        verify(repository).get(itemMO.identifier());
        verify(mapper).toMO(itemIDTO);
        verify(repository).save(itemMO);
        verify(mapper).toODTO(itemMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(itemODTO, result);
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test unsuccessful save of a Item due to it already existing.")
    void testWrongSaveItemAlreadyExists() {
        // When
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemMO = mock(ItemMO.class);
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.ITEM_ALREADY_EXISTS_CONFLICT.code,
                HeraldsOfChaosError.ITEM_ALREADY_EXISTS_CONFLICT.message,
                HeraldsOfChaosError.ITEM_ALREADY_EXISTS_CONFLICT.status
        );

        when(repository.get(itemMO.identifier())).thenReturn(Optional.of(itemMO));

        // Then
        final var error = assertThrows(
                ServiceException.class,
                () -> service.save(itemIDTO)
        );

        // Verify
        verify(repository).get(itemMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test unsuccessful save of a Item due to an unknown conflict while saving.")
    void testWrongSaveItemUnknownConflict() throws ServiceException {
        // When
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemMO = mock(ItemMO.class);
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.ITEM_SAVE_CONFLICT.code,
                HeraldsOfChaosError.ITEM_SAVE_CONFLICT.message,
                HeraldsOfChaosError.ITEM_SAVE_CONFLICT.status
        );

        when(repository.get(itemMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(itemIDTO)).thenReturn(itemMO);
        when(repository.save(itemMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                ServiceException.class,
                () -> service.save(itemIDTO)
        );

        // Verify
        verify(repository).get(itemMO.identifier());
        verify(mapper).toMO(itemIDTO);
        verify(repository).save(itemMO);
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test successful update of a Item.")
    void testUpdateItem() throws ServiceException, DatabaseException {
        // When
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemMO = mock(ItemMO.class);
        final var itemUpdatedMO = mock(ItemMO.class);
        final var itemODTO = mock(ItemODTO.class);

        when(mapper.toMO(itemIDTO)).thenReturn(itemMO);
        when(repository.update(itemMO)).thenReturn(itemUpdatedMO);
        when(mapper.toODTO(itemUpdatedMO, TranslatedString.EN)).thenReturn(itemODTO);

        // Then
        final var result = service.update(itemIDTO);

        // Verify
        verify(mapper).toMO(itemIDTO);
        verify(repository).update(itemMO);
        verify(mapper).toODTO(itemUpdatedMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(itemODTO, result);
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test unsuccessful update of a Item due to an unknown conflict while updating.")
    void testWrongUpdateItemUnknownConflict() throws ServiceException, DatabaseException {
        // When
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemMO = mock(ItemMO.class);
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.ITEM_UPDATE_CONFLICT.code,
                HeraldsOfChaosError.ITEM_UPDATE_CONFLICT.message,
                HeraldsOfChaosError.ITEM_UPDATE_CONFLICT.status
        );

        when(mapper.toMO(itemIDTO)).thenReturn(itemMO);
        when(repository.update(itemMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                ServiceException.class,
                () -> service.update(itemIDTO)
        );

        // Verify
        verify(mapper).toMO(itemIDTO);
        verify(repository).update(itemMO);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test successful removal of a Item.")
    void testDeleteItem() throws ServiceException {
        // When
        final var id = "mock-item-identifier";

        // Then
        service.delete(id);

        // Verify
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test unsuccessful removal of a Item due to an unknown conflict while deleting.")
    void testWrongDeleteItemUnknownConflict() {
        // When
        final var id = "mock-item-identifier";
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.ITEM_DELETE_CONFLICT.code,
                HeraldsOfChaosError.ITEM_DELETE_CONFLICT.message,
                HeraldsOfChaosError.ITEM_DELETE_CONFLICT.status
        );

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
    @DisplayName("[ITEM_SERVICE] - Test successful retrieval of a Item.")
    void testGetItem() throws ServiceException {
        // When
        final var id = "mock-item-identifier";
        final var itemMO = mock(ItemMO.class);
        final var itemODTO = mock(ItemODTO.class);

        when(repository.get(id)).thenReturn(Optional.of(itemMO));
        when(mapper.toODTO(itemMO, TranslatedString.EN)).thenReturn(itemODTO);

        // Then
        final var result = service.get(id, TranslatedString.EN);

        // Verify
        verify(repository).get(id);
        verify(mapper).toODTO(itemMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(itemODTO, result);
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test unsuccessful retrieval of a Item due to it not being found.")
    void testWrongGetItemNotFound() {
        // When
        final var id = "mock-item-identifier";
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.ITEM_NOT_FOUND.code,
                HeraldsOfChaosError.ITEM_NOT_FOUND.message,
                HeraldsOfChaosError.ITEM_NOT_FOUND.status
        );

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
    @DisplayName("[ITEM_SERVICE] - Test successful retrieval of a list of Items.")
    void testListItems() throws ServiceException {
        // When
        final var itemMO = mock(ItemMO.class);
        final var itemODTO = mock(ItemODTO.class);

        when(repository.list()).thenReturn(List.of(itemMO));
        when(mapper.toODTO(itemMO, TranslatedString.EN)).thenReturn(itemODTO);

        // Then
        final var result = service.list(TranslatedString.EN);

        // Verify
        verify(repository).list();
        verify(mapper).toODTO(itemMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(List.of(itemODTO), result);
    }

    @Test
    @DisplayName("[ITEM_SERVICE] - Test successful retrieval of a paginated list of Items.")
    void testPageItems() throws ServiceException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedItems = mock(Page.class);

        when(repository.page(pageable)).thenReturn(pagedItems);

        // Then
        final var result = service.page(pageable, TranslatedString.EN);

        // Verify
        verify(repository).page(pageable);
        verifyNoMoreInteractions(mapper, repository);
    }
}