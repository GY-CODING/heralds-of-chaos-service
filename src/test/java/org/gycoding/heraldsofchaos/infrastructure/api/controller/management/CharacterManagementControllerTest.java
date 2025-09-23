package org.gycoding.heraldsofchaos.infrastructure.api.controller.management;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.characters.CharacterIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.characters.CharacterODTO;
import org.gycoding.heraldsofchaos.application.service.CharacterService;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.in.characters.CharacterRQDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.dto.out.characters.CharacterRSDTO;
import org.gycoding.heraldsofchaos.infrastructure.api.mapper.CharacterControllerMapper;
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
public class CharacterManagementControllerTest {
    @Mock
    private CharacterService service;

    @Mock
    private CharacterControllerMapper mapper;

    @InjectMocks
    private CharacterManagementController controller;

    @Test
    @DisplayName("[CHARACTER_MANAGEMENT_CONTROLLER] - Test successful save of a Character.")
    void testSaveCharacter() throws APIException {
        // When
        final var characterRQDTO = mock(CharacterRQDTO.class);
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);

        when(mapper.toIDTO(characterRQDTO)).thenReturn(characterIDTO);
        when(service.save(characterIDTO)).thenReturn(characterODTO);
        when(mapper.toRSDTO(characterODTO)).thenReturn(characterRSDTO);

        // Then
        final var result = controller.save(characterRQDTO);

        // Verify
        verify(mapper).toIDTO(characterRQDTO);
        verify(service).save(characterIDTO);
        verify(mapper).toRSDTO(characterODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(characterRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CHARACTER_MANAGEMENT_CONTROLLER] - Test successful update of a Character.")
    void testUpdateCharacter() throws APIException {
        // When
        final var characterRQDTO = mock(CharacterRQDTO.class);
        final var characterIDTO = mock(CharacterIDTO.class);
        final var characterODTO = mock(CharacterODTO.class);
        final var characterRSDTO = mock(CharacterRSDTO.class);
        final var id = "mock-character-id";

        when(mapper.toIDTO(characterRQDTO, id)).thenReturn(characterIDTO);
        when(service.update(characterIDTO)).thenReturn(characterODTO);
        when(mapper.toRSDTO(characterODTO)).thenReturn(characterRSDTO);

        // Then
        final var result = controller.update(characterRQDTO, id);

        // Verify
        verify(mapper).toIDTO(characterRQDTO, id);
        verify(service).update(characterIDTO);
        verify(mapper).toRSDTO(characterODTO);
        verifyNoMoreInteractions(mapper, service);

        assertEquals(characterRSDTO, result.getBody());
    }

    @Test
    @DisplayName("[CHARACTER_MANAGEMENT_CONTROLLER] - Test successful removal of a Character.")
    void testRemoveCharacter() throws APIException {
        // When
        final var id = "mock-character-id";

        // Then
        final var result = controller.removeCharacter(id);

        // Verify
        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
    }
}