package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.CharacterDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.characters.CharacterEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.WorldEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.CharacterMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.WorldMongoRepository;
import org.gycoding.logs.logger.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CharacterDatabaseImplTest {
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
        mockStatic(Logger.class);
    }

    @Test
    @DisplayName("[CHARACTER_DATABASE] - Test successful save of a Character.")
    void testSaveCharacter() throws APIException {
        // When
        final var characterMO = mock(CharacterMO.class);
        final var characterEntity = mock(CharacterEntity.class);
        final var worldEntity = mock(WorldEntity.class);

        when(worldRepository.findByIdentifier(characterMO.identifier())).thenReturn(Optional.of(worldEntity));
        when(mapper.toEntity(characterMO, worldEntity)).thenReturn(characterEntity);
        when(repository.save(characterEntity)).thenReturn(characterEntity);
        when(mapper.toMO(characterEntity)).thenReturn(characterMO);

        // Then
        final var result = database.save(characterMO);

        // Verify
        verify(worldRepository).findByIdentifier(characterMO.identifier());
        verify(mapper).toEntity(characterMO, worldEntity);
        verify(repository).save(characterEntity);
        verify(mapper).toMO(characterEntity);
        verifyNoMoreInteractions(worldRepository, mapper, repository);

        assertEquals(characterMO, result);
    }
}