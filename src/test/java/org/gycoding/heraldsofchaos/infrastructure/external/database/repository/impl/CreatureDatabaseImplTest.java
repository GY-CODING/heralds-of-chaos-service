package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.CreatureDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.OrderEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.creatures.CreatureEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.CreatureMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
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
public class CreatureDatabaseImplTest {
    @Mock
    private OrderMongoRepository orderRepository;

    @Mock
    private CreatureMongoRepository repository;

    @Mock
    private CreatureDatabaseMapper mapper;

    @InjectMocks
    private CreatureDatabaseImpl database;

    @BeforeAll
    static void setup() {
        try {
            mockStatic(Logger.class);
        } catch (Exception ignored) {
        }
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test successful save of a Creature.")
    void testSaveCreature() {
        // When
        final var creatureMO = mock(CreatureMO.class);
        final var creatureEntity = mock(CreatureEntity.class);

        when(mapper.toEntity(creatureMO)).thenReturn(creatureEntity);
        when(repository.save(creatureEntity)).thenReturn(creatureEntity);
        when(mapper.toMO(creatureEntity)).thenReturn(creatureMO);

        // Then
        final var result = database.save(creatureMO);

        // Verify
        verify(mapper).toEntity(creatureMO);
        verify(repository).save(creatureEntity);
        verify(mapper).toMO(creatureEntity);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(creatureMO, result);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test successful update of a Creature.")
    void testUpdateCreature() throws APIException {
        // When
        final var creatureMO = mock(CreatureMO.class);
        final var creatureEntity = mock(CreatureEntity.class);
        final var creatureUpdatedEntity = mock(CreatureEntity.class);

        when(repository.findByIdentifier(creatureMO.identifier())).thenReturn(Optional.of(creatureEntity));
        when(mapper.toUpdatedEntity(creatureEntity, creatureMO)).thenReturn(creatureUpdatedEntity);
        when(repository.save(creatureUpdatedEntity)).thenReturn(creatureUpdatedEntity);
        when(mapper.toMO(creatureUpdatedEntity)).thenReturn(creatureMO);

        // Then
        final var result = database.update(creatureMO);

        // Verify
        verify(repository).findByIdentifier(creatureMO.identifier());
        verify(mapper).toUpdatedEntity(creatureEntity, creatureMO);
        verify(repository).save(creatureUpdatedEntity);
        verify(mapper).toMO(creatureUpdatedEntity);
        verifyNoMoreInteractions(mapper, repository);

        assertEquals(creatureMO, result);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test unsuccessful update of a Creature due to its no existence.")
    void testWrongUpdateCreature() {
        // When
        final var creatureMO = mock(CreatureMO.class);
        final var expectedException = new APIException(
                HeraldsOfChaosAPIError.CREATURE_NOT_FOUND.code,
                HeraldsOfChaosAPIError.CREATURE_NOT_FOUND.message,
                HeraldsOfChaosAPIError.CREATURE_NOT_FOUND.status
        );

        when(repository.findByIdentifier(creatureMO.identifier())).thenReturn(Optional.empty());

        // Then
        final var error = assertThrows(
                APIException.class,
                () -> database.update(creatureMO)
        );

        // Verify
        verify(repository).findByIdentifier(creatureMO.identifier());
        verifyNoMoreInteractions(repository);

        assertEquals(expectedException.getStatus(), error.getStatus());
        assertEquals(expectedException.getCode(), error.getCode());
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test successful removal of a Creature.")
    void testDeleteCreature() {
        // When
        final var id = "mock-creature-identifier";

        // Then
        database.delete(id);

        // Verify
        verify(repository).removeByIdentifier(id);
        verifyNoMoreInteractions(repository);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test successful retrieval of a Creature.")
    void testGetCreature() {
        // When
        final var id = "mock-creature-identifier";
        final var creatureMO = mock(CreatureMO.class);
        final var creatureEntity = mock(CreatureEntity.class);

        when(repository.findByIdentifier(id)).thenReturn(Optional.of(creatureEntity));
        when(mapper.toMO(creatureEntity)).thenReturn(creatureMO);

        // Then
        final var result = database.get(id);

        // Verify
        verify(repository).findByIdentifier(id);
        verify(mapper).toMO(creatureEntity);
        verifyNoMoreInteractions(repository, mapper);

        assertEquals(Optional.of(creatureMO), result);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test successful retrieval of a list of Creatures.")
    void testListCreatures() {
        // When
        final var orderEntity = mock(OrderEntity.class);
        final var creatureMO = mock(CreatureMO.class);
        final var creatureEntity = mock(CreatureEntity.class);

        when(orderRepository.findByCollection("Creature")).thenReturn(Optional.of(orderEntity));
        when(repository.findAll()).thenReturn(List.of(creatureEntity));
        when(mapper.toMO(creatureEntity)).thenReturn(creatureMO);

        // Then
        final var result = database.list();

        // Verify
        verify(orderRepository).findByCollection("Creature");
        verify(repository).findAll();
        verify(mapper).toMO(creatureEntity);
        verifyNoMoreInteractions(orderRepository, repository, mapper);

        assertEquals(List.of(creatureMO), result);
    }

    @Test
    @DisplayName("[CREATURE_DATABASE] - Test successful retrieval of a paginated list of Creatures.")
    void testPageCreatures() {
        // When
        final Pageable pageable = Pageable.ofSize(10).withPage(0);
        final Page pagedCreatures = mock(Page.class);

        when(repository.findAll(pageable)).thenReturn(pagedCreatures);

        // Then
        final var result = database.page(pageable);

        // Verify
        verify(repository).findAll(pageable);
        verifyNoMoreInteractions(repository);
    }
}