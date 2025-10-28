package org.gycoding.heraldsofchaos.application.service.impl;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.PlaceIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.mapper.PlaceServiceMapper;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.gycoding.heraldsofchaos.domain.repository.PlaceRepository;
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
public class PlaceServiceImplTest {
    @Mock
    private PlaceRepository repository;

    @Mock
    private PlaceServiceMapper mapper;

    @InjectMocks
    private PlaceServiceImpl service;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test successful save of a Place.")
    void testSavePlace() throws ServiceException {
        // When
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeMO = mock(PlaceMO.class);
        final var placeODTO = mock(PlaceODTO.class);

        when(repository.get(placeMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(placeIDTO)).thenReturn(placeMO);
        when(repository.save(placeMO)).thenReturn(placeMO);
        when(mapper.toODTO(placeMO, TranslatedString.EN)).thenReturn(placeODTO);

        // Then
        final var result = service.save(placeIDTO);

        // Verify
        verify(repository).get(placeMO.identifier());
        verify(mapper).toMO(placeIDTO);
        verify(repository).save(placeMO);
        verify(mapper).toODTO(placeMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(placeODTO, result);
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test unsuccessful save of a Place due to it already existing.")
    void testWrongSavePlaceAlreadyExists() {
        // When
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeMO = mock(PlaceMO.class);
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.PLACE_ALREADY_EXISTS_CONFLICT.code,
                HeraldsOfChaosError.PLACE_ALREADY_EXISTS_CONFLICT.message,
                HeraldsOfChaosError.PLACE_ALREADY_EXISTS_CONFLICT.status
        );

        when(repository.get(placeMO.identifier())).thenReturn(Optional.of(placeMO));

        // Then
        final var error = assertThrows(
                ServiceException.class,
                () -> service.save(placeIDTO)
        );

        // Verify
        verify(repository).get(placeMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test unsuccessful save of a Place due to an unknown conflict while saving.")
    void testWrongSavePlaceUnknownConflict() throws ServiceException {
        // When
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeMO = mock(PlaceMO.class);
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.PLACE_SAVE_CONFLICT.code,
                HeraldsOfChaosError.PLACE_SAVE_CONFLICT.message,
                HeraldsOfChaosError.PLACE_SAVE_CONFLICT.status
        );

        when(repository.get(placeMO.identifier())).thenReturn(Optional.empty());
        when(mapper.toMO(placeIDTO)).thenReturn(placeMO);
        when(repository.save(placeMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                ServiceException.class,
                () -> service.save(placeIDTO)
        );

        // Verify
        verify(repository).get(placeMO.identifier());
        verify(mapper).toMO(placeIDTO);
        verify(repository).save(placeMO);
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test successful update of a Place.")
    void testUpdatePlace() throws ServiceException, DatabaseException {
        // When
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeMO = mock(PlaceMO.class);
        final var placeUpdatedMO = mock(PlaceMO.class);
        final var placeODTO = mock(PlaceODTO.class);

        when(mapper.toMO(placeIDTO)).thenReturn(placeMO);
        when(repository.update(placeMO)).thenReturn(placeUpdatedMO);
        when(mapper.toODTO(placeUpdatedMO, TranslatedString.EN)).thenReturn(placeODTO);

        // Then
        final var result = service.update(placeIDTO);

        // Verify
        verify(mapper).toMO(placeIDTO);
        verify(repository).update(placeMO);
        verify(mapper).toODTO(placeUpdatedMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(placeODTO, result);
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test unsuccessful update of a Place due to an unknown conflict while updating.")
    void testWrongUpdatePlaceUnknownConflict() throws ServiceException, DatabaseException {
        // When
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeMO = mock(PlaceMO.class);
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.PLACE_UPDATE_CONFLICT.code,
                HeraldsOfChaosError.PLACE_UPDATE_CONFLICT.message,
                HeraldsOfChaosError.PLACE_UPDATE_CONFLICT.status
        );

        when(mapper.toMO(placeIDTO)).thenReturn(placeMO);
        when(repository.update(placeMO)).thenThrow(new RuntimeException("Any exception."));

        // Then
        final var error = assertThrows(
                ServiceException.class,
                () -> service.update(placeIDTO)
        );

        // Verify
        verify(mapper).toMO(placeIDTO);
        verify(repository).update(placeMO);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test successful removal of a Place.")
    void testDeletePlace() throws ServiceException {
        // When
        final var id = "mock-place-identifier";

        // Then
        service.delete(id);

        // Verify
        verify(repository).delete(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test unsuccessful removal of a Place due to an unknown conflict while deleting.")
    void testWrongDeletePlaceUnknownConflict() {
        // When
        final var id = "mock-place-identifier";
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.PLACE_DELETE_CONFLICT.code,
                HeraldsOfChaosError.PLACE_DELETE_CONFLICT.message,
                HeraldsOfChaosError.PLACE_DELETE_CONFLICT.status
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
    @DisplayName("[PLACE_SERVICE] - Test successful retrieval of a Place.")
    void testGetPlace() throws ServiceException {
        // When
        final var id = "mock-place-identifier";
        final var placeMO = mock(PlaceMO.class);
        final var placeODTO = mock(PlaceODTO.class);

        when(repository.get(id)).thenReturn(Optional.of(placeMO));
        when(mapper.toODTO(placeMO, TranslatedString.EN)).thenReturn(placeODTO);

        // Then
        final var result = service.get(id, TranslatedString.EN);

        // Verify
        verify(repository).get(id);
        verify(mapper).toODTO(placeMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(placeODTO, result);
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test unsuccessful retrieval of a Place due to it not being found.")
    void testWrongGetPlaceNotFound() {
        // When
        final var id = "mock-place-identifier";
        final var expectedException = new ServiceException(
                HeraldsOfChaosError.PLACE_NOT_FOUND.code,
                HeraldsOfChaosError.PLACE_NOT_FOUND.message,
                HeraldsOfChaosError.PLACE_NOT_FOUND.status
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
    @DisplayName("[PLACE_SERVICE] - Test successful retrieval of a list of Places.")
    void testListPlaces() throws ServiceException {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var placeODTO = mock(PlaceODTO.class);

        when(repository.list()).thenReturn(List.of(placeMO));
        when(mapper.toODTO(placeMO, TranslatedString.EN)).thenReturn(placeODTO);

        // Then
        final var result = service.list(TranslatedString.EN);

        // Verify
        verify(repository).list();
        verify(mapper).toODTO(placeMO, TranslatedString.EN);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(List.of(placeODTO), result);
    }

    @Test
    @DisplayName("[PLACE_SERVICE] - Test successful retrieval of a paginated list of Places.")
    void testPagePlaces() throws ServiceException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedPlaces = mock(Page.class);

        when(repository.page(pageable)).thenReturn(pagedPlaces);

        // Then
        final var result = service.page(pageable, TranslatedString.EN);

        // Verify
        verify(repository).page(pageable);
        verifyNoMoreInteractions(mapper, repository);
    }
}