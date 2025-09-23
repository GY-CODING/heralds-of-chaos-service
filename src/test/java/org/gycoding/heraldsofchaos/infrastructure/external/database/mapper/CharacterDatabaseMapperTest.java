package org.gycoding.heraldsofchaos.infrastructure.external.database.mapper;

import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.characters.CharacterEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.WorldEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CharacterDatabaseMapperTest {
    @InjectMocks
    private CharacterDatabaseMapperImpl mapper;

    @Test
    @DisplayName("[CHARACTER_DATABASE_MAPPER] - Test successful mapping from CharacterEntity to CharacterMO.")
    void testCharacterToMO() {
        // When
        final var characterEntity = mock(CharacterEntity.class);
        final var characterMO = mock(CharacterMO.class);

        // Then
        final var result = mapper.toMO(characterEntity);

        // Verify
        assertEquals(characterMO.identifier(), result.identifier());
        assertEquals(characterMO.name(), result.name());
        assertEquals(characterMO.title(), result.title());
        assertEquals(characterMO.description(), result.description());
        assertEquals(characterMO.world(), result.world());
        assertEquals(characterMO.image(), result.image());
        assertEquals(characterMO.race(), result.race());
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE_MAPPER] - Test unsuccessful mapping from CharacterEntity to CharacterMO.")
    void testWrongCharacterToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE_MAPPER] - Test successful mapping from CharacterMO to CharacterEntity.")
    void testCharacterToEntity() {
        // When
        final var worldEntity = mock(WorldEntity.class);
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);

        // Then
        final var result = mapper.toEntity(characterMO, worldEntity);

        // Verify
        assertEquals(characterEntity.getIdentifier(), result.getIdentifier());
        assertEquals(characterEntity.getName(), result.getName());
        assertEquals(characterEntity.getTitle(), result.getTitle());
        assertEquals(characterEntity.getDescription(), result.getDescription());
        assertEquals(worldEntity, result.getWorld());
        assertEquals(characterEntity.getImage(), result.getImage());
        assertEquals(characterEntity.getRace(), result.getRace());
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE_MAPPER] - Test unsuccessful mapping from CharacterMO to CharacterEntity.")
    void testWrongCharacterToEntity() {
        // Then
        final var result = mapper.toEntity(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE_MAPPER] - Test successful mapping from CharacterMO to updated CharacterEntity.")
    void testCharacterToUpdatedEntity() {
        // When
        final var worldEntity = mock(WorldEntity.class);
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);
        final var characterUpdatedEntity = mock(CharacterEntity.class);

        // Then
        final var result = mapper.toUpdatedEntity(characterEntity, characterMO, worldEntity);

        // Verify
        assertEquals(characterUpdatedEntity.getIdentifier(), result.getIdentifier());
        assertEquals(characterUpdatedEntity.getName(), result.getName());
        assertEquals(characterUpdatedEntity.getTitle(), result.getTitle());
        assertEquals(characterUpdatedEntity.getDescription(), result.getDescription());
        assertEquals(characterUpdatedEntity.getWorld(), result.getWorld());
        assertEquals(characterUpdatedEntity.getImage(), result.getImage());
        assertEquals(characterUpdatedEntity.getRace(), result.getRace());
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE_MAPPER] - Test unsuccessful mapping from CharacterODTO to CharacterRSDTO.")
    void testWrongCharacterToRSDTO() {
        // Then
        final var result = mapper.toUpdatedEntity(null, null, null);

        // Verify
        assertNull(result);
    }
}