package org.gycoding.heraldsofchaos.infrastructure.api.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.creatures.CreatureIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.creatures.CreatureRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.creatures.CreatureRSDTO;
import org.gycoding.heraldsofchaos.shared.IdentifierGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreatureControllerMapperTest {
    @InjectMocks
    private CreatureControllerMapperImpl mapper;

    @Test
    @DisplayName("[CREATURE_CONTROLLER_MAPPER] - Test successful mapping from CreatureRQDTO to CreatureIDTO.")
    void testCreatureToIDTO() {
        // When
        final var creatureRQDTO = mock(CreatureRQDTO.class);
        final var id = "mock-creature-id";

        when(creatureRQDTO.name()).thenReturn(
                TranslatedString.builder()
                        .en("mock-name")
                        .es("mock-build")
                        .build()
        );

        try (MockedStatic<IdentifierGenerator> mockedStatic = mockStatic(IdentifierGenerator.class)) {
            mockedStatic.when(() -> IdentifierGenerator.generate(any())).thenReturn(id);

            // Then
            final var result = mapper.toIDTO(creatureRQDTO);

            // Verify
            assertEquals(id, result.identifier());
        }
    }

    @Test
    @DisplayName("[CREATURE_CONTROLLER_MAPPER] - Test unsuccessful mapping from CreatureRQDTO to CreatureIDTO.")
    void testWrongCreatureToIDTO() {
        // Then
        final var result = mapper.toIDTO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CREATURE_CONTROLLER_MAPPER] - Test successful mapping from CreatureRQDTO to CreatureIDTO without generating a new identifier.")
    void testCreatureToIDTOWithoutNewIdentifier() {
        // When
        final var creatureRQDTO = mock(CreatureRQDTO.class);
        final var creatureIDTO = mock(CreatureIDTO.class);
        final var id = "mock-creature-id";

        // Then
        final var result = mapper.toIDTO(creatureRQDTO, id);

        // Verify
        assertEquals(id, result.identifier());
        assertEquals(creatureIDTO.name(), result.name());
        assertEquals(creatureIDTO.description(), result.description());
        assertEquals(creatureIDTO.image(), result.image());
        assertEquals(creatureIDTO.race(), result.race());
    }

    @Test
    @DisplayName("[CREATURE_CONTROLLER_MAPPER] - Test unsuccessful mapping from CreatureRQDTO to CreatureIDTO without generating a new identifier.")
    void testWrongCreatureToIDTOWithoutNewIdentifier() {
        // Then
        final var result = mapper.toIDTO(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CREATURE_CONTROLLER_MAPPER] - Test successful mapping from CreatureODTO to CreatureRSDTO.")
    void testCreatureToRSDTO() {
        // When
        final var creatureODTO = mock(CreatureODTO.class);
        final var creatureRSDTO = mock(CreatureRSDTO.class);

        // Then
        final var result = mapper.toRSDTO(creatureODTO);

        // Verify
        assertEquals(creatureRSDTO.identifier(), result.identifier());
        assertEquals(creatureRSDTO.name(), result.name());
        assertEquals(creatureRSDTO.description(), result.description());
        assertEquals(creatureRSDTO.image(), result.image());
        assertEquals(creatureRSDTO.race(), result.race());
    }

    @Test
    @DisplayName("[CREATURE_CONTROLLER_MAPPER] - Test unsuccessful mapping from CreatureODTO to CreatureRSDTO.")
    void testWrongCreatureToRSDTO() {
        // Then
        final var result = mapper.toRSDTO(null);

        // Verify
        assertNull(result);
    }
}