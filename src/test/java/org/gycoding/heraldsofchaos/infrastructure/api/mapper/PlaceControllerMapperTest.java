package org.gycoding.heraldsofchaos.infrastructure.api.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.PlaceIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.worlds.PlaceRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.worlds.PlaceRSDTO;
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
public class PlaceControllerMapperTest {
    @InjectMocks
    private PlaceControllerMapperImpl mapper;

    @Test
    @DisplayName("[PLACE_CONTROLLER_MAPPER] - Test successful mapping from PlaceRQDTO to PlaceIDTO.")
    void testPlaceToIDTO() {
        // When
        final var placeRQDTO = mock(PlaceRQDTO.class);
        final var id = "mock-place-id";

        when(placeRQDTO.name()).thenReturn(
                TranslatedString.builder()
                        .en("mock-name")
                        .es("mock-build")
                        .build()
        );

        try (MockedStatic<IdentifierGenerator> mockedStatic = mockStatic(IdentifierGenerator.class)) {
            mockedStatic.when(() -> IdentifierGenerator.generate(any())).thenReturn(id);

            // Then
            final var result = mapper.toIDTO(placeRQDTO);

            // Verify
            assertEquals(id, result.identifier());
        }
    }

    @Test
    @DisplayName("[PLACE_CONTROLLER_MAPPER] - Test unsuccessful mapping from PlaceRQDTO to PlaceIDTO.")
    void testWrongPlaceToIDTO() {
        // Then
        final var result = mapper.toIDTO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[PLACE_CONTROLLER_MAPPER] - Test successful mapping from PlaceRQDTO to PlaceIDTO without generating a new identifier.")
    void testPlaceToIDTOWithoutNewIdentifier() {
        // When
        final var placeRQDTO = mock(PlaceRQDTO.class);
        final var placeIDTO = mock(PlaceIDTO.class);
        final var id = "mock-place-id";

        // Then
        final var result = mapper.toIDTO(placeRQDTO, id);

        // Verify
        assertEquals(id, result.identifier());
        assertEquals(placeIDTO.name(), result.name());
        assertEquals(placeIDTO.description(), result.description());
        assertEquals(placeIDTO.image(), result.image());
    }

    @Test
    @DisplayName("[PLACE_CONTROLLER_MAPPER] - Test unsuccessful mapping from PlaceRQDTO to PlaceIDTO without generating a new identifier.")
    void testWrongPlaceToIDTOWithoutNewIdentifier() {
        // Then
        final var result = mapper.toIDTO(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[PLACE_CONTROLLER_MAPPER] - Test successful mapping from PlaceODTO to PlaceRSDTO.")
    void testPlaceToRSDTO() {
        // When
        final var placeODTO = mock(PlaceODTO.class);
        final var placeRSDTO = mock(PlaceRSDTO.class);

        // Then
        final var result = mapper.toRSDTO(placeODTO);

        // Verify
        assertEquals(placeRSDTO.identifier(), result.identifier());
        assertEquals(placeRSDTO.name(), result.name());
        assertEquals(placeRSDTO.description(), result.description());
        assertEquals(placeRSDTO.image(), result.image());
    }

    @Test
    @DisplayName("[PLACE_CONTROLLER_MAPPER] - Test unsuccessful mapping from PlaceODTO to PlaceRSDTO.")
    void testWrongPlaceToRSDTO() {
        // Then
        final var result = mapper.toRSDTO(null);

        // Verify
        assertNull(result);
    }
}