package org.gycoding.heraldsofchaos.application.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.creatures.CreatureIDTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CreatureServiceMapperTest {
    private final CreatureServiceMapper mapper = Mappers.getMapper(CreatureServiceMapper.class);

    @Test
    @DisplayName("[CREATURE_SERVICE_MAPPER] - Test successful mapping from CreatureIDTO to CreatureMO.")
    void testCreatureToMO() {
        // When
        final var creatureIDTO = mock(CreatureIDTO.class);

        // Then
        final var result = mapper.toMO(creatureIDTO);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE_MAPPER] - Test unsuccessful mapping from CreatureIDTO to CreatureMO.")
    void testWrongCreatureToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CREATURE_SERVICE_MAPPER] - Test successful mapping from CreatureMO to CreatureODTO.")
    void testCreatureToODTO() {
        // When
        final var creatureMO = mock(CreatureMO.class);

        // Then
        final var result = mapper.toODTO(creatureMO, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }
}