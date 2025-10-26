package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.heraldsofchaos.domain.repository.WorldRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.WorldDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.PlaceMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.WorldMongoRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WorldDatabaseImpl implements WorldRepository {
    private final OrderMongoRepository orderRepository;
    private final WorldMongoRepository repository;
    private final WorldDatabaseMapper mapper;
    private final PlaceMongoRepository placeRepository;

    @Override
    public WorldMO save(WorldMO world, List<String> places) {
        final var persistedPlaces = places.stream()
                        .map(placeRepository::findByIdentifier)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .toList();

        Logger.debug("Places searched (not specifically found) for world.", world.identifier());

        return mapper.toMO(repository.save(mapper.toEntity(world, persistedPlaces)));
    }

    @Override
    public WorldMO update(WorldMO world, List<String> places) throws APIException {
        final var persistedWorld = repository.findByIdentifier(world.identifier()).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.status
                )
        );

        Logger.debug("World to be updated found", world.identifier());

        final var persistedPlaces = places.stream()
                .map(placeRepository::findByIdentifier)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        Logger.debug("Places searched (not specifically found) for world.", world.identifier());

        return mapper.toMO(repository.save(mapper.toUpdatedEntity(persistedWorld, world, persistedPlaces)));
    }

    @Override
    public void delete(String identifier) {
        repository.removeByIdentifier(identifier);
    }

    @Override
    public Optional<WorldMO> get(String identifier) {
        return repository.findByIdentifier(identifier)
                .map(mapper::toMO);
    }

    @Override
    public List<WorldMO> list() {
        final var order = orderRepository.findByCollection("World")
                .orElse(null)
                .getOrder();

        return repository.findAll().stream()
                .map(mapper::toMO)
                .sorted(Comparator.comparingInt(character -> order.indexOf(character.identifier())))
                .toList();
    }

    @Override
    public Page<WorldMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}