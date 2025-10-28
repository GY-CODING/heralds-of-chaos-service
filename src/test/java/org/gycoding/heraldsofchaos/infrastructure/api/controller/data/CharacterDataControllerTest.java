package org.gycoding.heraldsofchaos.infrastructure.api.controller.data;

import org.gycoding.heraldsofchaos.application.dto.out.characters.CharacterODTO;
import org.gycoding.heraldsofchaos.application.service.CharacterService;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.characters.CharacterRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CharacterControllerMapper;
import org.gycoding.quasar.exceptions.model.QuasarException;
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
    void testGetCharacter() throws QuasarException {
        // When
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);
        final var id = "mock-character-id";

        when(service.get(id, TranslatedString.EN)).thenReturn(characterODTO);
        when(mapper.toRSDTO(characterODTO)).thenReturn(characterRSDTO);

        // Then
        final var result = controller.getCharacter(id, TranslatedString.EN);

        // Verify
        verify(service).get(id, TranslatedString.EN);
        verify(mapper).toRSDTO(characterODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(characterRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CHARACTER_DATA_CONTROLLER] - Test successful retrieval of a list of Characters.")
    void testListCharacters() throws QuasarException {
        // When
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);

        when(service.list(TranslatedString.EN)).thenReturn(List.of(characterODTO));
        when(mapper.toRSDTO(characterODTO)).thenReturn(characterRSDTO);

        // Then
        final var result = controller.listCharacters(TranslatedString.EN);

        // Verify
        verify(service).list(TranslatedString.EN);
        verify(mapper).toRSDTO(characterODTO);
        verifyNoMoreInteractions(mapper, service);

        assertNotEquals(List.of(), result.getBody());
    }

    @Test
    @DisplayName("[CHARACTER_DATA_CONTROLLER] - Test successful retrieval of a paginated list of Characters.")
    void testPageCharacters() throws QuasarException {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page<Map<String, Object>> pagedCharacters = mock(Page.class);
        final Map<String, Object> characterMap = Map.of("id", "mock-id", "name", "mock-name");

        when(service.page(pageable, TranslatedString.EN)).thenReturn(pagedCharacters);
        when(pagedCharacters.getContent()).thenReturn(List.of(characterMap));

        // Then
        final var result = controller.pageCharacters(pageable, TranslatedString.EN);

        // Verify
        verify(service).page(pageable, TranslatedString.EN);
        verifyNoMoreInteractions(service);

        assertNotEquals(List.of(), result.getBody());
    }
}