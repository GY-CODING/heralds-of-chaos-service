package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.out.characters.CharacterODTO;
import org.gycoding.heraldsofchaos.application.service.CharacterService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.characters.CharacterRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CharacterControllerMapper;
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
public class CharacterDataControllerTest {
    @Mock
    private CharacterService service;

    @Mock
    private CharacterControllerMapper mapper;

    @InjectMocks
    private CharacterDataController controller;

    @Test
    @DisplayName("[CHARACTER_DATA_CONTROLLER] - Test successful retrieval of a Character.")
    void testGetCharacter() throws APIException {
        // When
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);
        final var id = "mock-character-id";
        final var lang = "en";

        when(service.get(id, lang)).thenReturn(characterODTO);
        when(mapper.toRSDTO(characterODTO)).thenReturn(characterRSDTO);

        // Then
        final var result = controller.getCharacter(id, lang);

        // Verify
        verify(service).get(id, lang);
        verify(mapper).toRSDTO(characterODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(characterRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CHARACTER_DATA_CONTROLLER] - Test successful retrieval of a list of Characters.")
    void testListCharacters() throws APIException {
        // When
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);
        final var lang = "en";

        when(service.list(lang)).thenReturn(List.of(characterODTO));
        when(mapper.toRSDTO(characterODTO)).thenReturn(characterRSDTO);

        // Then
        final var result = controller.listCharacters(lang);

        // Verify
        verify(service).list(lang);
        verify(mapper).toRSDTO(characterODTO);
        verifyNoMoreInteractions(mapper, service);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[CHARACTER_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Characters.")
    void testPageCharacters() throws APIException {
        // When
        final var lang = "en";
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedCharacters = mock(Page.class);
        final Map<String, Object> characterMap = Map.of("id", "mock-id", "name", "mock-name");

        when(service.page(pageable, lang)).thenReturn(pagedCharacters);
        when(pagedCharacters.getContent()).thenReturn(List.of(characterMap));

        // Then
        final var result = controller.pageCharacters(pageable, lang);

        // Verify
        verify(service).page(pageable, lang);
        verifyNoMoreInteractions(service);

        assertNotEquals(List.of(), result.getBody());
    }
}