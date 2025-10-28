package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.domain.repository.CharacterRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.CharacterDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.CharacterMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.WorldMongoRepository;
import org.gycoding.quasar.exceptions.model.DatabaseException;
import org.gycoding.quasar.logs.service.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterDatabaseImpl implements CharacterRepository {
    private final OrderMongoRepository orderRepository;
    private final CharacterMongoRepository repository;
    private final CharacterDatabaseMapper mapper;
    private final WorldMongoRepository worldRepository;

    @Override
    public CharacterMO save(CharacterMO character) throws DatabaseException {
        final var persistedWorld = worldRepository.findByIdentifier(character.world()).orElseThrow(() ->
                new DatabaseException(HeraldsOfChaosError.WORLD_NOT_FOUND)
        );

        Logger.debug(String.format("World found for character: %s", character.identifier()), character.world());

        return mapper.toMO(repository.save(mapper.toEntity(character, persistedWorld)));
    }

    @Override
    public CharacterMO update(CharacterMO character) throws DatabaseException {
        final var persistedCharacter = repository.findByIdentifier(character.identifier()).orElseThrow(() ->
                new DatabaseException(HeraldsOfChaosError.CHARACTER_NOT_FOUND)
        );

        Logger.debug("Character to be updated found", character.identifier());

        final var persistedWorld = worldRepository.findByIdentifier(character.world()).orElse(null);

        Logger.debug(String.format("World searched (not necessary found) for character: %s", character.identifier()), character.world());

        return mapper.toMO(repository.save(mapper.toUpdatedEntity(persistedCharacter, character, persistedWorld)));
    }

    @Override
    public void delete(String identifier) {
        repository.removeByIdentifier(identifier);
    }

    @Override
    public Optional<CharacterMO> get(String identifier) {
        return repository.findByIdentifier(identifier)
                .map(mapper::toMO);
    }

    @Override
    public List<CharacterMO> list() {
        final var order = orderRepository.findByCollection("Character")
                .orElse(null)
                .getOrder();

        return repository.findAll().stream()
                .map(mapper::toMO)
                .sorted(Comparator.comparingInt(character -> order.indexOf(character.identifier())))
                .toList();
    }

    @Override
    public Page<CharacterMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}
