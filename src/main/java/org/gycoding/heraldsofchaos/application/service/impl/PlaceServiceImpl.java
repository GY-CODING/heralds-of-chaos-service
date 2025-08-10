package org.gycoding.heraldsofchaos.application.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.worlds.PlaceIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.mapper.PlaceServiceMapper;
import org.gycoding.heraldsofchaos.application.service.PlaceService;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.worlds.PlaceMO;
import org.gycoding.heraldsofchaos.domain.repository.PlaceRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository repository;

    private final PlaceServiceMapper mapper;

    @Override
    public PlaceODTO save(PlaceIDTO place) throws APIException {
        final PlaceMO savedPlace;

        if (repository.get(place.identifier()).isPresent()) {
            Logger.error("Place already exists.", place.identifier());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        try {
            savedPlace = repository.save(mapper.toMO(place));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while saving a place: %s.", place.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        Logger.info("Place saved successfully.", savedPlace.identifier());

        return mapper.toODTO(savedPlace, TranslatedString.EN);
    }

    @Override
    public PlaceODTO update(PlaceIDTO place) throws APIException {
        final PlaceMO updatedPlace;

        try {
            updatedPlace = repository.update(mapper.toMO(place));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while updating a place: %s.", place.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        Logger.info("Place updated successfully.", updatedPlace.identifier());

        return mapper.toODTO(updatedPlace, TranslatedString.EN);
    }

    @Override
    public void delete(String identifier) throws APIException {
        try {
            repository.delete(identifier);
        } catch (Exception e) {
            Logger.error(String.format("An error has occurred while removing a place: %s.", identifier), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        Logger.info("Place removed successfully.", identifier);
    }

    @Override
    public PlaceODTO get(String identifier, String language) throws APIException {
        final var place = repository.get(identifier).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.status
                )
        );

        return mapper.toODTO(place, language);
    }

    @Override
    public List<PlaceODTO> list(String language) throws APIException {
        try {
            final var places = repository.list();

            return places.stream().map(place -> mapper.toODTO(place, language)).toList();
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.status
            );
        }
    }

    @Override
    public Page<Map<String, Object>> page(Pageable pageable, String language) throws APIException {
        try {
            final var places = repository.page(pageable);

            return places.map(place -> mapper.toODTO(place, language).toMap());
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.status
            );
        }
    }
}
