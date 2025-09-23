package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.domain.repository.CharacterRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.CharacterDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.CharacterMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.WorldMongoRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CharacterDatabaseImpl implements CharacterRepository {
    private final CharacterMongoRepository repository;

    private final CharacterDatabaseMapper mapper;

    private final WorldMongoRepository worldRepository;

    @Override
    public CharacterMO save(CharacterMO character) throws APIException {
        final var persistedWorld = worldRepository.findByIdentifier(character.world()).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.status
                )
        );

        Logger.debug(String.format("World found for character: %s", character.identifier()), character.world());

        return mapper.toMO(repository.save(mapper.toEntity(character, persistedWorld)));
    }

    @Override
    public CharacterMO update(CharacterMO character) throws APIException {
        final var persistedCharacter = repository.findByIdentifier(character.identifier()).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.status
                )
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
        return repository.findAll().stream()
                .map(mapper::toMO)
                .toList();
    }

    @Override
    public Page<CharacterMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}
