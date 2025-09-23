package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.application.service.ItemService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.items.ItemRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.ItemControllerMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemDataControllerTest {
    @Mock
    private ItemService service;

    @Mock
    private ItemControllerMapper mapper;

    @InjectMocks
    private ItemDataController controller;

    @Test
    @DisplayName("[ITEM_DATA_CONTROLLER] - Test successful retrieval of a Item.")
    void testGetItem() throws APIException {
        // When
        final var itemODTO = mock(ItemODTO.class);
        final var itemRSDTO = mock(ItemRSDTO.class);
        final var id = "mock-item-id";
        final var lang = "en";

        when(service.get(id, lang)).thenReturn(itemODTO);
        when(mapper.toRSDTO(itemODTO)).thenReturn(itemRSDTO);

        // Then
        final var result = controller.getItem(id, lang);

        // Verify
        verify(service).get(id, lang);
        verify(mapper).toRSDTO(itemODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(itemRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[ITEM_DATA_CONTROLLER] - Test successful retrieval of a list of Items.")
    void testListItems() throws APIException {
        // When
        final var itemODTO = mock(ItemODTO.class);
        final var itemRSDTO = mock(ItemRSDTO.class);
        final var lang = "en";

        when(service.list(lang)).thenReturn(List.of(itemODTO));
        when(mapper.toRSDTO(itemODTO)).thenReturn(itemRSDTO);

        // Then
        final var result = controller.listItems(lang);

        // Verify
        verify(service).list(lang);
        verify(mapper).toRSDTO(itemODTO);
        verifyNoMoreInteractions(mapper, service);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[ITEM_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Items.")
    void testPageItems() throws APIException {
        // When
        final var lang = "en";
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedItems = mock(Page.class);
        final Map<String, Object> itemMap = Map.of("id", "mock-id", "name", "mock-name");

        when(service.page(pageable, lang)).thenReturn(pagedItems);
        when(pagedItems.getContent()).thenReturn(List.of(itemMap));

        // Then
        final var result = controller.pageItems(pageable, lang);

        // Verify
        verify(service).page(pageable, lang);
        verifyNoMoreInteractions(service);

        assertNotEquals(List.of(), result.getBody());
    }
}