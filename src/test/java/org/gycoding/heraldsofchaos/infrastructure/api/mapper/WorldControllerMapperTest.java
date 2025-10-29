package org.gycoding.heraldsofchaos.infrastructure.api.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.worlds.WorldRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.worlds.WorldRSDTO;
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
public class WorldControllerMapperTest {
    private final WorldControllerMapper mapper = Mappers.getMapper(WorldControllerMapper.class);

    @Test
    @DisplayName("[WORLD_CONTROLLER_MAPPER] - Test successful mapping from WorldRQDTO to WorldIDTO.")
    void testWorldToIDTO() {
        // When
        final var worldRQDTO = mock(WorldRQDTO.class);
        final var id = "mock-world-id";

        when(worldRQDTO.name()).thenReturn(
                TranslatedString.builder()
                        .en("mock-name")
                        .es("mock-build")
                        .build()
        );

        try (MockedStatic<IdentifierGenerator> mockedStatic = mockStatic(IdentifierGenerator.class)) {
            mockedStatic.when(() -> IdentifierGenerator.generate(any())).thenReturn(id);

            // Then
            final var result = mapper.toIDTO(worldRQDTO);

            // Verify
            assertEquals(id, result.identifier());
        }
    }

    @Test
    @DisplayName("[WORLD_CONTROLLER_MAPPER] - Test unsuccessful mapping from WorldRQDTO to WorldIDTO.")
    void testWrongWorldToIDTO() {
        // Then
        final var result = mapper.toIDTO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[WORLD_CONTROLLER_MAPPER] - Test successful mapping from WorldRQDTO to WorldIDTO without generating a new identifier.")
    void testWorldToIDTOWithoutNewIdentifier() {
        // When
        final var worldRQDTO = mock(WorldRQDTO.class);
        final var worldIDTO = mock(WorldIDTO.class);
        final var id = "mock-world-id";

        // Then
        final var result = mapper.toIDTO(worldRQDTO, id);

        // Verify
        assertEquals(id, result.identifier());
        assertEquals(worldIDTO.name(), result.name());
        assertEquals(worldIDTO.description(), result.description());
        assertEquals(worldIDTO.image(), result.image());
        assertEquals(worldIDTO.detailedIcon(), result.detailedIcon());
        assertEquals(worldIDTO.mainColor(), result.mainColor());
        assertEquals(worldIDTO.places(), result.places());
    }

    @Test
    @DisplayName("[WORLD_CONTROLLER_MAPPER] - Test unsuccessful mapping from WorldRQDTO to WorldIDTO without generating a new identifier.")
    void testWrongWorldToIDTOWithoutNewIdentifier() {
        // Then
        final var result = mapper.toIDTO(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[WORLD_CONTROLLER_MAPPER] - Test successful mapping from WorldODTO to WorldRSDTO.")
    void testWorldToRSDTO() {
        // When
        final var worldODTO = mock(WorldODTO.class);
        final var worldRSDTO = mock(WorldRSDTO.class);

        // Then
        final var result = mapper.toRSDTO(worldODTO);

        // Verify
        assertEquals(worldRSDTO.identifier(), result.identifier());
        assertEquals(worldRSDTO.name(), result.name());
        assertEquals(worldRSDTO.description(), result.description());
        assertEquals(worldRSDTO.image(), result.image());
        assertEquals(worldRSDTO.detailedIcon(), result.detailedIcon());
        assertEquals(worldRSDTO.mainColor(), result.mainColor());
        assertEquals(worldRSDTO.places(), result.places());
    }

    @Test
    @DisplayName("[WORLD_CONTROLLER_MAPPER] - Test unsuccessful mapping from WorldODTO to WorldRSDTO.")
    void testWrongWorldToRSDTO() {
        // Then
        final var result = mapper.toRSDTO(null);

        // Verify
        assertNull(result);
    }
}