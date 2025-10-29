package org.gycoding.heraldsofchaos.application.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class WorldServiceMapperTest {
    private final WorldServiceMapper mapper = Mappers.getMapper(WorldServiceMapper.class);

    @Test
    @DisplayName("[WORLD_SERVICE_MAPPER] - Test successful mapping from WorldIDTO to WorldMO.")
    void testWorldToMO() {
        // When
        final var worldIDTO = mock(WorldIDTO.class);

        // Then
        final var result = mapper.toMO(worldIDTO);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE_MAPPER] - Test unsuccessful mapping from WorldIDTO to WorldMO.")
    void testWrongWorldToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE_MAPPER] - Test successful mapping from WorldMO to WorldODTO.")
    void testWorldToODTO() {
        // When
        final var worldMO = mock(WorldMO.class);

        // Then
        final var result = mapper.toODTO(worldMO, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE_MAPPER] - Test successful mapping from a list of PlaceMOs to PlaceODTOs.")
    void testWorldPlacesToODTO() {
        // When
        final var placeMO = mock(PlaceMO.class);
        final var places = List.of(placeMO);

        // Then
        final var result = mapper.toPlaceODTOList(places, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[WORLD_SERVICE_MAPPER] - Test successful mapping from PlaceMO to PlaceODTO.")
    void testWorldPlaceToODTO() {
        // When
        final var placeMO = mock(PlaceMO.class);

        // Then
        final var result = mapper.toPlaceODTO(placeMO, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }
}