package org.gycoding.heraldsofchaos.application.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.heraldsofchaos.application.mapper.WorldServiceMapper;
import org.gycoding.heraldsofchaos.application.service.WorldService;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.heraldsofchaos.domain.repository.WorldRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class WorldServiceImpl implements WorldService {
    private final WorldRepository repository;

    private final WorldServiceMapper mapper;

    @Override
    public WorldODTO save(WorldIDTO world) throws APIException {
        final WorldMO savedWorld;

        if (repository.get(world.identifier()).isPresent()) {
            Logger.error("World already exists.", world.identifier());

            throw new APIException(
                    HeraldsOfChaosAPIError.WORLD_ALREADY_EXISTS_CONFLICT.code,
                    HeraldsOfChaosAPIError.WORLD_ALREADY_EXISTS_CONFLICT.message,
                    HeraldsOfChaosAPIError.WORLD_ALREADY_EXISTS_CONFLICT.status
            );
        }

        try {
            savedWorld = repository.save(mapper.toMO(world), world.places());
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while saving a world: %s.", world.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.WORLD_SAVE_CONFLICT.code,
                    HeraldsOfChaosAPIError.WORLD_SAVE_CONFLICT.message,
                    HeraldsOfChaosAPIError.WORLD_SAVE_CONFLICT.status
            );
        }

        Logger.info("World saved successfully.", savedWorld.identifier());

        return mapper.toODTO(savedWorld, TranslatedString.EN);
    }

    @Override
    public WorldODTO update(WorldIDTO world) throws APIException {
        final WorldMO updatedWorld;

        try {
            updatedWorld = repository.update(mapper.toMO(world), world.places());
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while updating a world: %s.", world.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.WORLD_UPDATE_CONFLICT.code,
                    HeraldsOfChaosAPIError.WORLD_UPDATE_CONFLICT.message,
                    HeraldsOfChaosAPIError.WORLD_UPDATE_CONFLICT.status
            );
        }

        Logger.info("World updated successfully.", updatedWorld.identifier());

        return mapper.toODTO(updatedWorld, TranslatedString.EN);
    }

    @Override
    public void delete(String identifier) throws APIException {
        try {
            repository.delete(identifier);
        } catch (Exception e) {
            Logger.error(String.format("An error has occurred while removing a world: %s.", identifier), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.WORLD_DELETE_CONFLICT.code,
                    HeraldsOfChaosAPIError.WORLD_DELETE_CONFLICT.message,
                    HeraldsOfChaosAPIError.WORLD_DELETE_CONFLICT.status
            );
        }

        Logger.info("World removed successfully.", identifier);
    }

    @Override
    public WorldODTO get(String identifier, String language) throws APIException {
        final var world = repository.get(identifier).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.WORLD_NOT_FOUND.status
                )
        );

        return mapper.toODTO(world, language);
    }

    @Override
    public List<WorldODTO> list(String language) throws APIException {
        try {
            final var worlds = repository.list();

            return worlds.stream().map(world -> mapper.toODTO(world, language)).toList();
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.WORLD_LIST_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.WORLD_LIST_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.WORLD_LIST_NOT_FOUND.status
            );
        }
    }

    @Override
    public Page<Map<String, Object>> page(Pageable pageable, String language) throws APIException {
        try {
            final var worlds = repository.page(pageable);

            return worlds.map(world -> mapper.toODTO(world, language).toMap());
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.WORLD_LIST_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.WORLD_LIST_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.WORLD_LIST_NOT_FOUND.status
            );
        }
    }

    @Override
    public List<PlaceODTO> listPlaces(String idWorld, String language) throws APIException {
        final var world = this.get(idWorld, language);

        return world.places();
    }
}
