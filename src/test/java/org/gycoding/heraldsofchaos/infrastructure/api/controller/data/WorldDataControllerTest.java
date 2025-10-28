package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.application.service.PlaceService;
import org.gycoding.heraldsofchaos.application.service.WorldService;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.worlds.PlaceRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.worlds.WorldRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.PlaceControllerMapper;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.WorldControllerMapper;
import org.gycoding.quasar.exceptions.model.QuasarException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorldDataControllerTest {
    @Mock
    private WorldService worldService;

    @Mock
    private WorldControllerMapper worldMapper;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceControllerMapper placeMapper;

    @InjectMocks
    private WorldDataController controller;

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a World.")
    void testGetWorld() throws QuasarException {
        // When
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);
        final var id = "mock-world-id";

        when(worldService.get(id, TranslatedString.EN)).thenReturn(worldODTO);
        when(worldMapper.toRSDTO(worldODTO)).thenReturn(worldRSDTO);

        // Then
        final var result = controller.getWorld(id, TranslatedString.EN);

        // Verify
        verify(worldService).get(id, TranslatedString.EN);
        verify(worldMapper).toRSDTO(worldODTO);
        verifyNoMoreInteractions(worldMapper, worldService);

        assertEquals(worldRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a list of Worlds.")
    void testListWorld() throws QuasarException {
        // When
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);

        when(worldService.list(TranslatedString.EN)).thenReturn(List.of(worldODTO));
        when(worldMapper.toRSDTO(worldODTO)).thenReturn(worldRSDTO);

        // Then
        final var result = controller.listWorlds(TranslatedString.EN);

        // Verify
        verify(worldService).list(TranslatedString.EN);
        verify(worldMapper).toRSDTO(worldODTO);
        verifyNoMoreInteractions(worldMapper, worldService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Worlds.")
    void testPageWorld() throws QuasarException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedWorlds = mock(Page.class);
        final Map<String, Object> worldMap = Map.of("id", "mock-id", "name", "mock-name");

        when(worldService.page(pageable, TranslatedString.EN)).thenReturn(pagedWorlds);
        when(pagedWorlds.getContent()).thenReturn(List.of(worldMap));

        // Then
        final var result = controller.pageWorlds(pageable, TranslatedString.EN);

        // Verify
        verify(worldService).page(pageable, TranslatedString.EN);
        verifyNoMoreInteractions(worldService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a Place.")
    void testGetPlace() throws QuasarException {
        // When
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);
        final var id = "mock-place-id";

        when(placeService.get(id, TranslatedString.EN)).thenReturn(placeODTO);
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.getPlace(id, TranslatedString.EN);

        // Verify
        verify(placeService).get(id, TranslatedString.EN);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, placeService);

        assertEquals(placeRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a list of Places.")
    void testListPlaces() throws QuasarException {
        // When
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);

        when(placeService.list(TranslatedString.EN)).thenReturn(List.of(placeODTO));
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.listPlaces(TranslatedString.EN);

        // Verify
        verify(placeService).list(TranslatedString.EN);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, placeService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Places.")
    void testPagePlaces() throws QuasarException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedPlaces = mock(Page.class);
        final Map<String, Object> placeMap = Map.of("id", "mock-id", "name", "mock-name");

        when(placeService.page(pageable, TranslatedString.EN)).thenReturn(pagedPlaces);
        when(pagedPlaces.getContent()).thenReturn(List.of(placeMap));

        // Then
        final var result = controller.pagePlaces(pageable, TranslatedString.EN);

        // Verify
        verify(placeService).page(pageable, TranslatedString.EN);
        verifyNoMoreInteractions(placeService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a list of Places inside an specified World.")
    void testListWorldPlaces() throws QuasarException {
        // When
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);
        final var id = "mock-world-id";

        when(worldService.listPlaces(id, TranslatedString.EN)).thenReturn(List.of(placeODTO));
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.listWorldPlaces(id, TranslatedString.EN);

        // Verify
        verify(worldService).listPlaces(id, TranslatedString.EN);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, worldService);

        assertNotEquals(List.of(), result.getBody());
    }
}