package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.PlaceIDTO;
import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.application.service.PlaceService;
import org.gycoding.heraldsofchaos.application.service.WorldService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.worlds.PlaceRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.worlds.WorldRQDTO;
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
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorldManagementControllerTest {
    @Mock
    private WorldService worldService;

    @Mock
    private WorldControllerMapper worldMapper;

    @Mock
    private PlaceService placeService;

    @Mock
    private PlaceControllerMapper placeMapper;

    @InjectMocks
    private WorldManagementController controller;

    @Test
    @DisplayName("[WORLD_MANAGEMENT_CONTROLLER] - Test successful save of a World.")
    void testSaveWorld() throws Exception {
        // When
        final var worldRQDTO = mock(WorldRQDTO.class);
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);

        when(worldMapper.toIDTO(worldRQDTO)).thenReturn(worldIDTO);
        when(worldService.save(worldIDTO)).thenReturn(worldODTO);
        when(worldMapper.toRSDTO(worldODTO)).thenReturn(worldRSDTO);

        // Then
        final var result = controller.save(worldRQDTO);

        // Verify
        verify(worldMapper).toIDTO(worldRQDTO);
        verify(worldService).save(worldIDTO);
        verify(worldMapper).toRSDTO(worldODTO);
        verifyNoMoreInteractions(worldMapper, worldService);

        assertEquals(worldRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_MANAGEMENT_CONTROLLER] - Test successful update of a World.")
    void testUpdateWorld() throws Exception {
        // When
        final var worldRQDTO = mock(WorldRQDTO.class);
        final var worldIDTO = mock(WorldIDTO.class);
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);
        final var id = "mock-world-id";

        when(worldMapper.toIDTO(worldRQDTO, id)).thenReturn(worldIDTO);
        when(worldService.update(worldIDTO)).thenReturn(worldODTO);
        when(worldMapper.toRSDTO(worldODTO)).thenReturn(worldRSDTO);

        // Then
        final var result = controller.update(worldRQDTO, id);

        // Verify
        verify(worldMapper).toIDTO(worldRQDTO, id);
        verify(worldService).update(worldIDTO);
        verify(worldMapper).toRSDTO(worldODTO);
        verifyNoMoreInteractions(worldMapper, worldService);

        assertEquals(worldRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_MANAGEMENT_CONTROLLER] - Test successful removal of a World.")
    void testRemoveWorld() throws Exception {
        // When
        final var id = "mock-world-id";

        // Then
        final var result = controller.removeWorld(id);

        // Verify
        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
    }

    @Test
    @DisplayName("[WORLD_MANAGEMENT_CONTROLLER] - Test successful save of a Place.")
    void testSavePlace() throws Exception {
        // When
        final var placeRQDTO = mock(PlaceRQDTO.class);
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);

        when(placeMapper.toIDTO(placeRQDTO)).thenReturn(placeIDTO);
        when(placeService.save(placeIDTO)).thenReturn(placeODTO);
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.save(placeRQDTO);

        // Verify
        verify(placeMapper).toIDTO(placeRQDTO);
        verify(placeService).save(placeIDTO);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, placeService);

        assertEquals(placeRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_MANAGEMENT_CONTROLLER] - Test successful update of a Place.")
    void testUpdatePlace() throws Exception {
        //Place
        final var placeRQDTO = mock(PlaceRQDTO.class);
        final var placeIDTO = mock(PlaceIDTO.class);
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);
        final var id = "mock-place-place";

        when(placeMapper.toIDTO(placeRQDTO, id)).thenReturn(placeIDTO);
        when(placeService.update(placeIDTO)).thenReturn(placeODTO);
        when(placeMapper.toRSDTO(placeODTO)).thenReturn(placeRSDTO);

        // Then
        final var result = controller.update(placeRQDTO, id);

        // Verify
        verify(placeMapper).toIDTO(placeRQDTO, id);
        verify(placeService).update(placeIDTO);
        verify(placeMapper).toRSDTO(placeODTO);
        verifyNoMoreInteractions(placeMapper, placeService);

        assertEquals(placeRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[WORLD_MANAGEMENT_CONTROLLER] - Test successful removal of a Place.")
    void testRemovePlace() throws Exception {
        // When
        final var id = "mock-place-id";

        // Then
        final var result = controller.removePlace(id);

        // Verify
        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
    }
}