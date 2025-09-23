package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.application.service.ItemService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.items.ItemRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.items.ItemRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.ItemControllerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemManagementControllerTest {
    @Mock
    private ItemService service;

    @Mock
    private ItemControllerMapper mapper;

    @InjectMocks
    private ItemManagementController controller;

    @Test
    @DisplayName("[ITEM_MANAGEMENT_CONTROLLER] - Test successful save of a Item.")
    void testSaveItem() throws APIException {
        // When
        final var itemRQDTO = mock(ItemRQDTO.class);
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemODTO = mock(ItemODTO.class);
        final var itemRSDTO = mock(ItemRSDTO.class);

        when(mapper.toIDTO(itemRQDTO)).thenReturn(itemIDTO);
        when(service.save(itemIDTO)).thenReturn(itemODTO);
        when(mapper.toRSDTO(itemODTO)).thenReturn(itemRSDTO);

        // Then
        final var result = controller.save(itemRQDTO);

        // Verify
        verify(mapper).toIDTO(itemRQDTO);
        verify(service).save(itemIDTO);
        verify(mapper).toRSDTO(itemODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(itemRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[ITEM_MANAGEMENT_CONTROLLER] - Test successful update of a Item.")
    void testUpdateItem() throws APIException {
        // When
        final var itemRQDTO = mock(ItemRQDTO.class);
        final var itemIDTO = mock(ItemIDTO.class);
        final var itemODTO = mock(ItemODTO.class);
        final var itemRSDTO = mock(ItemRSDTO.class);
        final var id = "mock-item-id";

        when(mapper.toIDTO(itemRQDTO, id)).thenReturn(itemIDTO);
        when(service.update(itemIDTO)).thenReturn(itemODTO);
        when(mapper.toRSDTO(itemODTO)).thenReturn(itemRSDTO);

        // Then
        final var result = controller.update(itemRQDTO, id);

        // Verify
        verify(mapper).toIDTO(itemRQDTO, id);
        verify(service).update(itemIDTO);
        verify(mapper).toRSDTO(itemODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(itemRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[ITEM_MANAGEMENT_CONTROLLER] - Test successful removal of a Item.")
    void testRemoveItem() throws APIException {
        // When
        final var id = "mock-item-id";

        // Then
        final var result = controller.removeItem(id);

        // Verify
        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
    }
}