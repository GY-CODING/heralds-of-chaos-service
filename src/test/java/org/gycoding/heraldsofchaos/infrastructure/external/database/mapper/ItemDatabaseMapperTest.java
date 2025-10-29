package org.gycoding.heraldsofchaos.infrastructure.external.database.mapper;

import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.items.ItemEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ItemDatabaseMapperTest {
    private final ItemDatabaseMapper mapper = Mappers.getMapper(ItemDatabaseMapper.class);

    @Test
    @DisplayName("[ITEM_DATABASE_MAPPER] - Test successful mapping from ItemEntity to ItemMO.")
    void testItemToMO() {
        // When
        final var itemEntity = mock(ItemEntity.class);
        final var itemMO = mock(ItemMO.class);

        // Then
        final var result = mapper.toMO(itemEntity);

        // Verify
        assertEquals(itemMO.identifier(), result.identifier());
        assertEquals(itemMO.name(), result.name());
        assertEquals(itemMO.description(), result.description());
        assertEquals(itemMO.image(), result.image());
        assertEquals(itemMO.type(), result.type());
    }

    @Test
    @DisplayName("[ITEM_DATABASE_MAPPER] - Test unsuccessful mapping from ItemEntity to ItemMO.")
    void testWrongItemToMO() {
        // Then
        final var result = mapper.toMO(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[ITEM_DATABASE_MAPPER] - Test successful mapping from ItemMO to ItemEntity.")
    void testItemToEntity() {
        // When
        final var itemMO = mock(ItemMO.class);
        final var itemEntity = mock(ItemEntity.class);

        // Then
        final var result = mapper.toEntity(itemMO);

        // Verify
        assertEquals(itemEntity.getIdentifier(), result.getIdentifier());
        assertEquals(itemEntity.getName(), result.getName());
        assertEquals(itemEntity.getDescription(), result.getDescription());
        assertEquals(itemEntity.getImage(), result.getImage());
        assertEquals(itemEntity.getType(), result.getType());
    }

    @Test
    @DisplayName("[ITEM_DATABASE_MAPPER] - Test unsuccessful mapping from ItemMO to ItemEntity.")
    void testWrongItemToEntity() {
        // Then
        final var result = mapper.toEntity(null);

        // Verify
        assertNull(result);
    }

    @Test
    @DisplayName("[ITEM_DATABASE_MAPPER] - Test successful mapping from ItemMO to updated ItemEntity.")
    void testItemToUpdatedEntity() {
        // When
        final var itemMO = mock(ItemMO.class);
        final var itemEntity = mock(ItemEntity.class);
        final var itemUpdatedEntity = mock(ItemEntity.class);

        // Then
        final var result = mapper.toUpdatedEntity(itemEntity, itemMO);

        // Verify
        assertEquals(itemUpdatedEntity.getIdentifier(), result.getIdentifier());
        assertEquals(itemUpdatedEntity.getName(), result.getName());
        assertEquals(itemUpdatedEntity.getDescription(), result.getDescription());
        assertEquals(itemUpdatedEntity.getImage(), result.getImage());
        assertEquals(itemUpdatedEntity.getType(), result.getType());
    }

    @Test
    @DisplayName("[ITEM_DATABASE_MAPPER] - Test unsuccessful mapping from ItemODTO to ItemRSDTO.")
    void testWrongItemToRSDTO() {
        // Then
        final var result = mapper.toUpdatedEntity(null, null);

        // Verify
        assertNull(result);
    }
}