package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.heraldsofchaos.application.service.CreatureService;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.creatures.CreatureRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CreatureControllerMapper;
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
public class CreatureDataControllerTest {
    @Mock
    private CreatureService service;

    @Mock
    private CreatureControllerMapper mapper;

    @InjectMocks
    private CreatureDataController controller;

    @Test
    @DisplayName("[CREATURE_DATA_CONTROLLER] - Test successful retrieval of a Creature.")
    void testGetCreature() throws QuasarException {
        // When
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);
        final var id = "mock-creature-id";

        when(service.get(id, TranslatedString.EN)).thenReturn(creatureODTO);
        when(mapper.toRSDTO(creatureODTO)).thenReturn(creatureRSDTO);

        // Then
        final var result = controller.getCreature(id, TranslatedString.EN);

        // Verify
        verify(service).get(id, TranslatedString.EN);
        verify(mapper).toRSDTO(creatureODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(creatureRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CREATURE_DATA_CONTROLLER] - Test successful retrieval of a list of Creatures.")
    void testListCreatures() throws QuasarException {
        // When
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);

        when(service.list(TranslatedString.EN)).thenReturn(List.of(creatureODTO));
        when(mapper.toRSDTO(creatureODTO)).thenReturn(creatureRSDTO);

        // Then
        final var result = controller.listCreatures(TranslatedString.EN);

        // Verify
        verify(service).list(TranslatedString.EN);
        verify(mapper).toRSDTO(creatureODTO);
        verifyNoMoreInteractions(mapper, service);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[CREATURE_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Creatures.")
    void testPageCreatures() throws QuasarException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedCreatures = mock(Page.class);
        final Map<String, Object> creatureMap = Map.of("id", "mock-id", "name", "mock-name");

        when(service.page(pageable, TranslatedString.EN)).thenReturn(pagedCreatures);
        when(pagedCreatures.getContent()).thenReturn(List.of(creatureMap));

        // Then
        final var result = controller.pageCreatures(pageable, TranslatedString.EN);

        // Verify
        verify(service).page(pageable, TranslatedString.EN);
        verifyNoMoreInteractions(service);

        assertNotEquals(List.of(), result.getBody());
    }
}