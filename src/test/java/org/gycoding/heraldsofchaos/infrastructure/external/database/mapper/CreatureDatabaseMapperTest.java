package org.gycoding.heraldsofchaos.infrastructure.external.database.mapper;

import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.creatures.CreatureEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class CreatureDatabaseMapperTest {
    private final CreatureDatabaseMapper mapper = Mappers.getMapper(CreatureDatabaseMapper.class);

    @Test
    @DisplayName("[CREATURE_DATABASE_MAPPER] - Test successful mapping from CreatureEntity to CreatureMO.")
    void testCreatureToMO() {
        // When
        final var creatureEntity = mock(CreatureEntity.class);
        final var creatureMO = mock(CreatureMO.class);

        // Then
        final var result = mapper.toMO(creatureEntity);

        // Verify
        assertEquals(creatureMO.identifier(), result.identifier());
        assertEquals(creatureMO.name(), result.name());
        assertEquals(creatureMO.description(), result.description());
        assertEquals(creatureMO.image(), result.image());
        assertEquals(creatureMO.race(), result.race());
    }

    @Test
    @DisplayName("[CREATURE_DATABASE_MAPPER] - Test unsuccessful mapping from CreatureEntity to CreatureMO.")
    void testWrongCreatureToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE_MAPPER] - Test successful mapping from CreatureMO to CreatureEntity.")
    void testCreatureToEntity() {
        // When
        final var creatureMO = mock(CreatureMO.class);
        final var creatureEntity = mock(CreatureEntity.class);

        // Then
        final var result = mapper.toEntity(creatureMO);

        // Verify
        assertEquals(creatureEntity.getIdentifier(), result.getIdentifier());
        assertEquals(creatureEntity.getName(), result.getName());
        assertEquals(creatureEntity.getDescription(), result.getDescription());
        assertEquals(creatureEntity.getImage(), result.getImage());
        assertEquals(creatureEntity.getRace(), result.getRace());
    }

    @Test
    @DisplayName("[CREATURE_DATABASE_MAPPER] - Test unsuccessful mapping from CreatureMO to CreatureEntity.")
    void testWrongCreatureToEntity() {
        // Then
        final var result = mapper.toEntity(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE_MAPPER] - Test successful mapping from CreatureMO to updated CreatureEntity.")
    void testCreatureToUpdatedEntity() {
        // When
        final var creatureMO = mock(CreatureMO.class);
        final var creatureEntity = mock(CreatureEntity.class);
        final var creatureUpdatedEntity = mock(CreatureEntity.class);

        // Then
        final var result = mapper.toUpdatedEntity(creatureEntity, creatureMO);

        // Verify
        assertEquals(creatureUpdatedEntity.getIdentifier(), result.getIdentifier());
        assertEquals(creatureUpdatedEntity.getName(), result.getName());
        assertEquals(creatureUpdatedEntity.getDescription(), result.getDescription());
        assertEquals(creatureUpdatedEntity.getImage(), result.getImage());
        assertEquals(creatureUpdatedEntity.getRace(), result.getRace());
    }

    @Test
    @DisplayName("[CREATURE_DATABASE_MAPPER] - Test unsuccessful mapping from CreatureODTO to CreatureRSDTO.")
    void testWrongCreatureToRSDTO() {
        // Then
        final var result = mapper.toUpdatedEntity(null, null);

        // Verify
        assertNull(result);
    }
}