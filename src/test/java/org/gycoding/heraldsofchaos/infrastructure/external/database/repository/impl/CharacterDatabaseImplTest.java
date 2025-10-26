package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.CharacterDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.OrderEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.characters.CharacterEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.WorldEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.CharacterMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.WorldMongoRepository;
import org.gycoding.logs.logger.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterDatabaseImplTest {
    @Mock
    private OrderMongoRepository orderRepository;

    @Mock
    private CharacterMongoRepository repository;

    @Mock
    private CharacterDatabaseMapper mapper;

    @Mock
    private WorldMongoRepository worldRepository;

    @InjectMocks
    private CharacterDatabaseImpl database;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful save of a Character.")
    void testSaveCharacter() throws APIException {
        // When
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);
        final var worldEntity = mock(WorldEntity.class);

        when(worldRepository.findByIdentifier(characterMO.world())).thenReturn(Optional.of(worldEntity));
        when(mapper.toEntity(characterMO, worldEntity)).thenReturn(characterEntity);
        when(repository.save(characterEntity)).thenReturn(characterEntity);
        when(mapper.toMO(characterEntity)).thenReturn(characterMO);

        // Then
        final var result = database.save(characterMO);

        // Verify
        verify(worldRepository).findByIdentifier(characterMO.world());
        verify(mapper).toEntity(characterMO, worldEntity);
        verify(repository).save(characterEntity);
        verify(mapper).toMO(characterEntity);
        verifyNoMoreInteractions(worldRepository, mapper, repository);

        assertEquals(characterMO, result);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test unsuccessful save of a Character due to the world not existing.")
    void testWrongSaveCharacter() {
        // When
        final var characterMO = mock(CharacterMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.WORLD_NOT_FOUND.code,
                HeraldsOfChaosAPIError.WORLD_NOT_FOUND.message,
                HeraldsOfChaosAPIError.WORLD_NOT_FOUND.status
        );

        when(worldRepository.findByIdentifier(characterMO.world())).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> database.save(characterMO)
        );

        // Verify
        verify(worldRepository).findByIdentifier(characterMO.world());
        verifyNoMoreInteractions(worldRepository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful update of a Character.")
    void testUpdateCharacter() throws APIException {
        // When
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);
        final var characterUpdatedEntity = mock(CharacterEntity.class);
        final var worldEntity = mock(WorldEntity.class);

        when(repository.findByIdentifier(characterMO.identifier())).thenReturn(Optional.of(characterEntity));
        when(worldRepository.findByIdentifier(characterMO.world())).thenReturn(Optional.of(worldEntity));
        when(mapper.toUpdatedEntity(characterEntity, characterMO, worldEntity)).thenReturn(characterUpdatedEntity);
        when(repository.save(characterUpdatedEntity)).thenReturn(characterUpdatedEntity);
        when(mapper.toMO(characterUpdatedEntity)).thenReturn(characterMO);

        // Then
        final var result = database.update(characterMO);

        // Verify
        verify(repository).findByIdentifier(characterMO.identifier());
        verify(worldRepository).findByIdentifier(characterMO.world());
        verify(mapper).toUpdatedEntity(characterEntity, characterMO, worldEntity);
        verify(repository).save(characterUpdatedEntity);
        verify(mapper).toMO(characterUpdatedEntity);
        verifyNoMoreInteractions(worldRepository, mapper, repository);

        assertEquals(characterMO, result);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test unsuccessful update of a Character due to its no existence.")
    void testWrongUpdateCharacter() {
        // When
        final var characterMO = mock(CharacterMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.code,
                HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.message,
                HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.status
        );

        when(repository.findByIdentifier(characterMO.identifier())).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> database.update(characterMO)
        );

        // Verify
        verify(repository).findByIdentifier(characterMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful removal of a Character.")
    void testDeleteCharacter() {
        // When
        final var id = "mock-character-identifier";

        // Then
        database.delete(id);

        // Verify
        verify(repository).removeByIdentifier(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful retrieval of a Character.")
    void testGetCharacter() {
        // When
        final var id = "mock-character-identifier";
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);

        when(repository.findByIdentifier(id)).thenReturn(Optional.of(characterEntity));
        when(mapper.toMO(characterEntity)).thenReturn(characterMO);

        // Then
        final var result = database.get(id);

        // Verify
        verify(repository).findByIdentifier(id);
        verify(mapper).toMO(characterEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(Optional.of(characterMO), result);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful retrieval of a list of Characters.")
    void testListCharacters() {
        // When
        final var orderEntity = mock(OrderEntity.class);
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);

        when(orderRepository.findByCollection("Character")).thenReturn(Optional.of(orderEntity));
        when(repository.findAll()).thenReturn(List.of(characterEntity));
        when(mapper.toMO(characterEntity)).thenReturn(characterMO);

        // Then
        final var result = database.list();

        // Verify
        verify(orderRepository).findByCollection("Character");
        verify(repository).findAll();
        verify(mapper).toMO(characterEntity);
        verifyNoMoreInteractions(orderRepository, repository, mapper);

        assertEquals(List.of(characterMO), result);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful retrieval of a paginated list of Characters.")
    void testPageCharacters() {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedCharacters = mock(Page.class);

        when(repository.findAll(pageable)).thenReturn(pagedCharacters);

        // Then
        final var result = database.page(pageable);

        // Verify
        verify(repository).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }
}