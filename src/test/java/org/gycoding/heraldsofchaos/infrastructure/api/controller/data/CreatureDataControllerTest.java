package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.heraldsofchaos.application.service.CreatureService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.creatures.CreatureRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CreatureControllerMapper;
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
public class CreatureDataControllerTest {
    @Mock
    private CreatureService service;

    @Mock
    private CreatureControllerMapper mapper;

    @InjectMocks
    private CreatureDataController controller;

    @Test
    @DisplayName("[CREATURE_DATA_CONTROLLER] - Test successful retrieval of a Creature.")
    void testGetCreature() throws APIException {
        // When
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);
        final var id = "mock-creature-id";
        final var lang = "en";

        when(service.get(id, lang)).thenReturn(creatureODTO);
        when(mapper.toRSDTO(creatureODTO)).thenReturn(creatureRSDTO);

        // Then
        final var result = controller.getCreature(id, lang);

        // Verify
        verify(service).get(id, lang);
        verify(mapper).toRSDTO(creatureODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(creatureRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CREATURE_DATA_CONTROLLER] - Test successful retrieval of a list of Creatures.")
    void testListCreatures() throws APIException {
        // When
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);
        final var lang = "en";

        when(service.list(lang)).thenReturn(List.of(creatureODTO));
        when(mapper.toRSDTO(creatureODTO)).thenReturn(creatureRSDTO);

        // Then
        final var result = controller.listCreatures(lang);

        // Verify
        verify(service).list(lang);
        verify(mapper).toRSDTO(creatureODTO);
        verifyNoMoreInteractions(mapper, service);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[CREATURE_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Creatures.")
    void testPageCreatures() throws APIException {
        // When
        final var lang = "en";
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedCreatures = mock(Page.class);
        final Map<String, Object> creatureMap = Map.of("id", "mock-id", "name", "mock-name");

        when(service.page(pageable, lang)).thenReturn(pagedCreatures);
        when(pagedCreatures.getContent()).thenReturn(List.of(creatureMap));

        // Then
        final var result = controller.pageCreatures(pageable, lang);

        // Verify
        verify(service).page(pageable, lang);
        verifyNoMoreInteractions(service);

        assertNotEquals(List.of(), result.getBody());
    }
}