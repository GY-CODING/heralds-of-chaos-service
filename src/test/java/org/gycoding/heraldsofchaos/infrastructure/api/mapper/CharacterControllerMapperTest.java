package org.gycoding.heraldsofchaos.infrastructure.api.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.characters.CharacterIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.characters.CharacterODTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.characters.CharacterRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.characters.CharacterRSDTO;
import org.gycoding.heraldsofchaos.shared.IdentifierGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterControllerMapperTest {
    private final CharacterControllerMapper mapper = Mappers.getMapper(CharacterControllerMapper.class);

    @Test
    @DisplayName("[CHARACTER_CONTROLLER_MAPPER] - Test successful mapping from CharacterRQDTO to CharacterIDTO.")
    void testCharacterToIDTO() {
        // When
        final var characterRQDTO = mock(CharacterRQDTO.class);
        final var id = "mock-character-id";

        when(characterRQDTO.name()).thenReturn(
                TranslatedString.builder()
                        .en("mock-name")
                        .es("mock-build")
                        .build()
        );

        try (MockedStatic<IdentifierGenerator> mockedStatic = mockStatic(IdentifierGenerator.class)) {
            mockedStatic.when(() -> IdentifierGenerator.generate(any())).thenReturn(id);

            // Then
            final var result = mapper.toIDTO(characterRQDTO);

            // Verify
            assertEquals(id, result.identifier());
        }
    }

    @Test
    @DisplayName("[CHARACTER_CONTROLLER_MAPPER] - Test unsuccessful mapping from CharacterRQDTO to CharacterIDTO.")
    void testWrongCharacterToIDTO() {
        // Then
        final var result = mapper.toIDTO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CHARACTER_CONTROLLER_MAPPER] - Test successful mapping from CharacterRQDTO to CharacterIDTO without generating a new identifier.")
    void testCharacterToIDTOWithoutNewIdentifier() {
        // When
        final var characterRQDTO = mock(CharacterRQDTO.class);
        final var characterIDTO = mock(CharacterIDTO.class);
        final var id = "mock-character-id";

        // Then
        final var result = mapper.toIDTO(characterRQDTO, id);

        // Verify
        assertEquals(id, result.identifier());
        assertEquals(characterIDTO.name(), result.name());
        assertEquals(characterIDTO.title(), result.title());
        assertEquals(characterIDTO.description(), result.description());
        assertEquals(characterIDTO.world(), result.world());
        assertEquals(characterIDTO.image(), result.image());
        assertEquals(characterIDTO.race(), result.race());
    }

    @Test
    @DisplayName("[CHARACTER_CONTROLLER_MAPPER] - Test unsuccessful mapping from CharacterRQDTO to CharacterIDTO without generating a new identifier.")
    void testWrongCharacterToIDTOWithoutNewIdentifier() {
        // Then
        final var result = mapper.toIDTO(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CHARACTER_CONTROLLER_MAPPER] - Test successful mapping from CharacterODTO to CharacterRSDTO.")
    void testCharacterToRSDTO() {
        // When
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);

        // Then
        final var result = mapper.toRSDTO(characterODTO);

        // Verify
        assertEquals(characterRSDTO.identifier(), result.identifier());
        assertEquals(characterRSDTO.name(), result.name());
        assertEquals(characterRSDTO.title(), result.title());
        assertEquals(characterRSDTO.description(), result.description());
        assertEquals(characterRSDTO.world(), result.world());
        assertEquals(characterRSDTO.image(), result.image());
        assertEquals(characterRSDTO.race(), result.race());
    }

    @Test
    @DisplayName("[CHARACTER_CONTROLLER_MAPPER] - Test unsuccessful mapping from CharacterODTO to CharacterRSDTO.")
    void testWrongCharacterToRSDTO() {
        // Then
        final var result = mapper.toRSDTO(null);

        // Verify
        assertNull(result);
    }
}