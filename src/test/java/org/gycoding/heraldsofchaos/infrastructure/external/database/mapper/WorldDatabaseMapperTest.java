package org.gycoding.heraldsofchaos.infrastructure.external.database.mapper;

import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.PlaceEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.WorldEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class WorldDatabaseMapperTest {
    @InjectMocks
    private WorldDatabaseMapperImpl mapper;

    @Test
    @DisplayName("[WORLD_DATABASE_MAPPER] - Test successful mapping from WorldEntity to WorldMO.")
    void testWorldToMO() {
        // When
        final var worldEntity = mock(WorldEntity.class);
        final var worldMO = mock(WorldMO.class);

        // Then
        final var result = mapper.toMO(worldEntity);

        // Verify
        assertEquals(worldMO.identifier(), result.identifier());
        assertEquals(worldMO.name(), result.name());
        assertEquals(worldMO.description(), result.description());
        assertEquals(worldMO.image(), result.image());
        assertEquals(worldMO.detailedIcon(), result.detailedIcon());
        assertEquals(worldMO.mainColor(), result.mainColor());
    }

    @Test
    @DisplayName("[WORLD_DATABASE_MAPPER] - Test unsuccessful mapping from WorldEntity to WorldMO.")
    void testWrongWorldToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[WORLD_DATABASE_MAPPER] - Test successful mapping from WorldMO to WorldEntity.")
    void testWorldToEntity() {
        // When
        final var placeEntity = mock(PlaceEntity.class);
        final var worldMO = mock(WorldMO.class);
        final var worldEntity = mock(WorldEntity.class);

        // Then
        final var result = mapper.toEntity(worldMO, List.of(placeEntity));

        // Verify
        assertEquals(worldEntity.getIdentifier(), result.getIdentifier());
        assertEquals(worldEntity.getName(), result.getName());
        assertEquals(worldEntity.getDescription(), result.getDescription());
        assertEquals(worldEntity.getImage(), result.getImage());
        assertEquals(worldEntity.getDetailedIcon(), result.getDetailedIcon());
        assertEquals(worldEntity.getMainColor(), result.getMainColor());
        assertEquals(List.of(placeEntity), result.getPlaces());
    }

    @Test
    @DisplayName("[WORLD_DATABASE_MAPPER] - Test unsuccessful mapping from WorldMO to WorldEntity.")
    void testWrongWorldToEntity() {
        // Then
        final var result = mapper.toEntity(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[WORLD_DATABASE_MAPPER] - Test successful mapping from WorldMO to updated WorldEntity.")
    void testWorldToUpdatedEntity() {
        // When
        final var placeEntity = mock(PlaceEntity.class);
        final var worldMO = mock(WorldMO.class);
        final var worldEntity = mock(WorldEntity.class);
        final var worldUpdatedEntity = mock(WorldEntity.class);

        // Then
        final var result = mapper.toUpdatedEntity(worldEntity, worldMO, List.of(placeEntity));

        // Verify
        assertEquals(worldUpdatedEntity.getIdentifier(), result.getIdentifier());
        assertEquals(worldUpdatedEntity.getName(), result.getName());
        assertEquals(worldUpdatedEntity.getDescription(), result.getDescription());
        assertEquals(worldUpdatedEntity.getImage(), result.getImage());
        assertEquals(worldEntity.getDetailedIcon(), result.getDetailedIcon());
        assertEquals(worldEntity.getMainColor(), result.getMainColor());
        assertEquals(worldEntity.getPlaces(), result.getPlaces());
    }

    @Test
    @DisplayName("[WORLD_DATABASE_MAPPER] - Test unsuccessful mapping from WorldODTO to WorldRSDTO.")
    void testWrongWorldToRSDTO() {
        // Then
        final var result = mapper.toUpdatedEntity(null, null, null);

        // Verify
        assertNull(result);
    }
}