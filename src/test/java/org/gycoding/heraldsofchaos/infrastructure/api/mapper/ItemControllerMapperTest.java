package org.gycoding.heraldsofchaos.infrastructure.api.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.items.ItemRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.items.ItemRSDTO;
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
public class ItemControllerMapperTest {
    @InjectMocks
    private ItemControllerMapperImpl mapper;

    @Test
    @DisplayName("[ITEM_CONTROLLER_MAPPER] - Test successful mapping from ItemRQDTO to ItemIDTO.")
    void testItemToIDTO() {
        // When
        final var itemRQDTO = mock(ItemRQDTO.class);
        final var id = "mock-item-id";

        when(itemRQDTO.name()).thenReturn(
                TranslatedString.builder()
                        .en("mock-name")
                        .es("mock-build")
                        .build()
        );

        try (MockedStatic<IdentifierGenerator> mockedStatic = mockStatic(IdentifierGenerator.class)) {
            mockedStatic.when(() -> IdentifierGenerator.generate(any())).thenReturn(id);

            // Then
            final var result = mapper.toIDTO(itemRQDTO);

            // Verify
            assertEquals(id, result.identifier());
        }
    }

    @Test
    @DisplayName("[ITEM_CONTROLLER_MAPPER] - Test unsuccessful mapping from ItemRQDTO to ItemIDTO.")
    void testWrongItemToIDTO() {
        // Then
        final var result = mapper.toIDTO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[ITEM_CONTROLLER_MAPPER] - Test successful mapping from ItemRQDTO to ItemIDTO without generating a new identifier.")
    void testItemToIDTOWithoutNewIdentifier() {
        // When
        final var itemRQDTO = mock(ItemRQDTO.class);
        final var itemIDTO = mock(ItemIDTO.class);
        final var id = "mock-item-id";

        // Then
        final var result = mapper.toIDTO(itemRQDTO, id);

        // Verify
        assertEquals(id, result.identifier());
        assertEquals(itemIDTO.name(), result.name());
        assertEquals(itemIDTO.description(), result.description());
        assertEquals(itemIDTO.image(), result.image());
        assertEquals(itemIDTO.type(), result.type());
    }

    @Test
    @DisplayName("[ITEM_CONTROLLER_MAPPER] - Test unsuccessful mapping from ItemRQDTO to ItemIDTO without generating a new identifier.")
    void testWrongItemToIDTOWithoutNewIdentifier() {
        // Then
        final var result = mapper.toIDTO(null, null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[ITEM_CONTROLLER_MAPPER] - Test successful mapping from ItemODTO to ItemRSDTO.")
    void testItemToRSDTO() {
        // When
        final var itemODTO = mock(ItemODTO.class);
        final var itemRSDTO = mock(ItemRSDTO.class);

        // Then
        final var result = mapper.toRSDTO(itemODTO);

        // Verify
        assertEquals(itemRSDTO.identifier(), result.identifier());
        assertEquals(itemRSDTO.name(), result.name());
        assertEquals(itemRSDTO.description(), result.description());
        assertEquals(itemRSDTO.image(), result.image());
        assertEquals(itemRSDTO.type(), result.type());
    }

    @Test
    @DisplayName("[ITEM_CONTROLLER_MAPPER] - Test unsuccessful mapping from ItemODTO to ItemRSDTO.")
    void testWrongItemToRSDTO() {
        // Then
        final var result = mapper.toRSDTO(null);

        // Verify
        assertNull(result);
    }
}