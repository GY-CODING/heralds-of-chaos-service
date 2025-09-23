package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.creatures.CreatureIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.heraldsofchaos.application.service.CreatureService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.creatures.CreatureRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.creatures.CreatureRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CreatureControllerMapper;
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
public class CreatureManagementControllerTest {
    @Mock
    private CreatureService service;

    @Mock
    private CreatureControllerMapper mapper;

    @InjectMocks
    private CreatureManagementController controller;

    @Test
    @DisplayName("[CREATURE_MANAGEMENT_CONTROLLER] - Test successful save of a Creature.")
    void testSaveCreature() throws APIException {
        // When
        final var creatureRQDTO = mock(CreatureRQDTO.class);
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);

        when(mapper.toIDTO(creatureRQDTO)).thenReturn(creatureIDTO);
        when(service.save(creatureIDTO)).thenReturn(creatureODTO);
        when(mapper.toRSDTO(creatureODTO)).thenReturn(creatureRSDTO);

        // Then
        final var result = controller.save(creatureRQDTO);

        // Verify
        verify(mapper).toIDTO(creatureRQDTO);
        verify(service).save(creatureIDTO);
        verify(mapper).toRSDTO(creatureODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(creatureRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CREATURE_MANAGEMENT_CONTROLLER] - Test successful update of a Creature.")
    void testUpdateCreature() throws APIException {
        // When
        final var creatureRQDTO = mock(CreatureRQDTO.class);
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);
        final var id = "mock-creature-id";

        when(mapper.toIDTO(creatureRQDTO, id)).thenReturn(creatureIDTO);
        when(service.update(creatureIDTO)).thenReturn(creatureODTO);
        when(mapper.toRSDTO(creatureODTO)).thenReturn(creatureRSDTO);

        // Then
        final var result = controller.update(creatureRQDTO, id);

        // Verify
        verify(mapper).toIDTO(creatureRQDTO, id);
        verify(service).update(creatureIDTO);
        verify(mapper).toRSDTO(creatureODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(creatureRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CREATURE_MANAGEMENT_CONTROLLER] - Test successful removal of a Creature.")
    void testRemoveCreature() throws APIException {
        // When
        final var id = "mock-creature-id";

        // Then
        final var result = controller.removeCreature(id);

        // Verify
        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
    }
}