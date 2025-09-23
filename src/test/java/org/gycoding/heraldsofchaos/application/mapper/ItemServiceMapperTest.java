package org.gycoding.heraldsofchaos.application.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ItemServiceMapperTest {
    @InjectMocks
    private ItemServiceMapperImpl mapper;

    @Test
    @DisplayName("[ITEM_SERVICE_MAPPER] - Test successful mapping from ItemIDTO to ItemMO.")
    void testItemToMO() {
        // When
        final var itemIDTO = mock(ItemIDTO.class);

        // Then
        final var result = mapper.toMO(itemIDTO);

        // Verify
        assertNotNull(result);
    }

    @Test
    @DisplayName("[ITEM_SERVICE_MAPPER] - Test unsuccessful mapping from ItemIDTO to ItemMO.")
    void testWrongItemToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[ITEM_SERVICE_MAPPER] - Test successful mapping from ItemMO to ItemODTO.")
    void testItemToODTO() {
        // When
        final var itemMO = mock(ItemMO.class);

        // Then
        final var result = mapper.toODTO(itemMO, TranslatedString.EN);

        // Verify
        assertNotNull(result);
    }
}