package org.gycoding.heraldsofchaos.application.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.characters.CharacterIDTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CharacterServiceMapperTest {
    private final CharacterServiceMapper mapper = Mappers.getMapper(CharacterServiceMapper.class);

    @Test
    @DisplayName("[CHARACTER_SERVICE_MAPPER] - Test successful mapping from CharacterIDTO to CharacterMO.")
    void testCharacterToMO() {
        // When
        final var characterIDTO = mock(CharacterIDTO.class);

        // Then
        final var result = mapper.toMO(characterIDTO);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE_MAPPER] - Test unsuccessful mapping from CharacterIDTO to CharacterMO.")
    void testWrongCharacterToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CHARACTER_SERVICE_MAPPER] - Test successful mapping from CharacterMO to CharacterODTO.")
    void testCharacterToODTO() {
        // When
        final var characterMO = mock(CharacterMO.class);

        // Then
        final var result = mapper.toODTO(characterMO, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }
}