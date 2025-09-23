package org.gycoding.heraldsofchaos.infrastructure.external.database.mapper;

import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.PlaceEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class PlaceDatabaseMapperTest {
    @InjectMocks
    private PlaceDatabaseMapperImpl mapper;

    @Test
    @DisplayName("[PLACE_DATABASE_MAPPER] - Test successful mapping from PlaceEntity to PlaceMO.")
    void testPlaceToMO() {
        // When
        final var placeEntity = mock(PlaceEntity.class);
        final var placeMO = mock(PlaceMO.class);

        // Then
        final var result = mapper.toMO(placeEntity);

        // Verify
        assertEquals(placeMO.identifier(), result.identifier());
        assertEquals(placeMO.name(), result.name());
        assertEquals(placeMO.description(), result.description());
        assertEquals(placeMO.image(), result.image());
    }

    @Test
    @DisplayName("[PLACE_DATABASE_MAPPER] - Test unsuccessful mapping from PlaceEntity to PlaceMO.")
    void testWrongPlaceToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[PLACE_DATABASE_MAPPER] - Test successful mapping from PlaceMO to PlaceEntity.")
    void testPlaceToEntity() {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var placeEntity = mock(PlaceEntity.class);

        // Then
        final var result = mapper.toEntity(placeMO);

        // Verify
        assertEquals(placeEntity.getIdentifier(), result.getIdentifier());
        assertEquals(placeEntity.getName(), result.getName());
        assertEquals(placeEntity.getDescription(), result.getDescription());
        assertEquals(placeEntity.getImage(), result.getImage());
    }

    @Test
    @DisplayName("[PLACE_DATABASE_MAPPER] - Test unsuccessful mapping from PlaceMO to PlaceEntity.")
    void testWrongPlaceToEntity() {
        // Then
        final var result = mapper.toEntity(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[PLACE_DATABASE_MAPPER] - Test successful mapping from PlaceMO to updated PlaceEntity.")
    void testPlaceToUpdatedEntity() {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var placeEntity = mock(PlaceEntity.class);
        final var placeUpdatedEntity = mock(PlaceEntity.class);

        // Then
        final var result = mapper.toUpdatedEntity(placeEntity, placeMO);

        // Verify
        assertEquals(placeUpdatedEntity.getIdentifier(), result.getIdentifier());
        assertEquals(placeUpdatedEntity.getName(), result.getName());
        assertEquals(placeUpdatedEntity.getDescription(), result.getDescription());
        assertEquals(placeUpdatedEntity.getImage(), result.getImage());
    }

    @Test
    @DisplayName("[PLACE_DATABASE_MAPPER] - Test unsuccessful mapping from PlaceODTO to PlaceRSDTO.")
    void testWrongPlaceToRSDTO() {
        // Then
        final var result = mapper.toUpdatedEntity(null, null);

        // Verify
        assertNull(result);
    }
}