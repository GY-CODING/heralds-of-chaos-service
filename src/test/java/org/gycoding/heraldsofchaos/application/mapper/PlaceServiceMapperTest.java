package org.gycoding.heraldsofchaos.application.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.PlaceIDTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class PlaceServiceMapperTest {
    private final PlaceServiceMapper mapper = Mappers.getMapper(PlaceServiceMapper.class);

    @Test
    @DisplayName("[PLACE_SERVICE_MAPPER] - Test successful mapping from PlaceIDTO to PlaceMO.")
    void testPlaceToMO() {
        // When
        final var placeIDTO = mock(PlaceIDTO.class);

        // Then
        final var result = mapper.toMO(placeIDTO);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[PLACE_SERVICE_MAPPER] - Test unsuccessful mapping from PlaceIDTO to PlaceMO.")
    void testWrongPlaceToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[PLACE_SERVICE_MAPPER] - Test successful mapping from PlaceMO to PlaceODTO.")
    void testPlaceToODTO() {
        // When
        final var placeMO = mock(PlaceMO.class);

        // Then
        final var result = mapper.toODTO(placeMO, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }
}