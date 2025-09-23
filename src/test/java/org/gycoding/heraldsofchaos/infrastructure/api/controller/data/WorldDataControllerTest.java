package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.application.service.PlaceService;
import org.gycoding.heraldsofchaos.application.service.WorldService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.worlds.PlaceRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.worlds.WorldRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.PlaceControllerMapper;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.WorldControllerMapper;
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
    void testGetWorld() throws APIException {
        // When
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);
        final var id = "mock-world-id";
        final var lang = "en";

        when(worldService.get(id, lang)).thenReturn(worldODTO);
        when(worldMapper.toRSDTO(worldODTO)).thenReturn(worldRSDTO);

        // Then
        final var result = controller.getWorld(id, lang);

        // Verify
        verify(worldService).get(id, lang);
        verify(worldMapper).toRSDTO(worldODTO);
        verifyNoMoreInteractions(worldMapper, worldService);

        assertEquals(worldRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a list of Worlds.")
    void testListWorld() throws APIException {
        // When
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);
        final var lang = "en";

        when(worldService.list(lang)).thenReturn(List.of(worldODTO));
        when(worldMapper.toRSDTO(worldODTO)).thenReturn(worldRSDTO);

        // Then
        final var result = controller.listWorlds(lang);

        // Verify
        verify(worldService).list(lang);
        verify(worldMapper).toRSDTO(worldODTO);
        verifyNoMoreInteractions(worldMapper, worldService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Worlds.")
    void testPageWorld() throws APIException {
        // When
        final var lang = "en";
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedWorlds = mock(Page.class);
        final Map<String, Object> worldMap = Map.of("id", "mock-id", "name", "mock-name");

        when(worldService.page(pageable, lang)).thenReturn(pagedWorlds);
        when(pagedWorlds.getContent()).thenReturn(List.of(worldMap));

        // Then
        final var result = controller.pageWorlds(pageable, lang);

        // Verify
        verify(worldService).page(pageable, lang);
        verifyNoMoreInteractions(worldService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a Place.")
    void testGetPlace() throws APIException {
        // When
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);
        final var id = "mock-place-id";
        final var lang = "en";

        when(placeService.get(id, lang)).thenReturn(placeODTO);
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.getPlace(id, lang);

        // Verify
        verify(placeService).get(id, lang);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, placeService);

        assertEquals(placeRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a list of Places.")
    void testListPlaces() throws APIException {
        // When
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);
        final var lang = "en";

        when(placeService.list(lang)).thenReturn(List.of(placeODTO));
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.listPlaces(lang);

        // Verify
        verify(placeService).list(lang);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, placeService);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[WORLD_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Places.")
    void testPagePlaces() throws APIException {
        // When
        final var lang = "en";
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedPlaces = mock(Page.class);
        final Map<String, Object> placeMap = Map.of("id", "mock-id", "name", "mock-name");

        when(placeService.page(pageable, lang)).thenReturn(pagedPlaces);
        when(pagedPlaces.getContent()).thenReturn(List.of(placeMap));

        // Then
        final var result = controller.pagePlaces(pageable, lang);

        // Verify
        verify(placeService).page(pageable, lang);
        verifyNoMoreInteractions(placeService);

        assertNotEquals(List.of(), result.getBody());
    }
}