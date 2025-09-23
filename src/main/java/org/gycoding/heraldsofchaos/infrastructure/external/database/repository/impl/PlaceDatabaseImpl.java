package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.gycoding.heraldsofchaos.domain.repository.PlaceRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.PlaceDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.PlaceMongoRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PlaceDatabaseImpl implements PlaceRepository {
    private final PlaceMongoRepository repository;

    private final PlaceDatabaseMapper mapper;

    @Override
    public PlaceMO save(PlaceMO place) {
        return mapper.toMO(repository.save(mapper.toEntity(place)));
    }

    @Override
    public PlaceMO update(PlaceMO place) throws APIException {
        final var persistedPlace = repository.findByIdentifier(place.identifier()).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.PLACE_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.PLACE_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.PLACE_NOT_FOUND.status
                )
        );

        Logger.debug("Place to be updated found", place.identifier());

        return mapper.toMO(repository.save(mapper.toUpdatedEntity(persistedPlace, place)));
    }

    @Override
    public void delete(String identifier) {
        repository.removeByIdentifier(identifier);
    }

    @Override
    public Optional<PlaceMO> get(String identifier) {
        return repository.findByIdentifier(identifier)
                .map(mapper::toMO);
    }

    @Override
    public List<PlaceMO> list() {
        return repository.findAll().stream()
                .map(mapper::toMO)
                .toList();
    }

    @Override
    public Page<PlaceMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}