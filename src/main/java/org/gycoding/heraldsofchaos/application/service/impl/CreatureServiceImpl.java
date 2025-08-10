package org.gycoding.heraldsofchaos.application.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.creatures.CreatureIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.heraldsofchaos.application.mapper.CreatureServiceMapper;
import org.gycoding.heraldsofchaos.application.service.CreatureService;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.gycoding.heraldsofchaos.domain.repository.CreatureRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CreatureServiceImpl implements CreatureService {
    private final CreatureRepository repository;

    private final CreatureServiceMapper mapper;

    @Override
    public CreatureODTO save(CreatureIDTO creature) throws APIException {
        final CreatureMO savedCreature;

        if (repository.get(creature.identifier()).isPresent()) {
            Logger.error("Creature already exists.", creature.identifier());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        try {
            savedCreature = repository.save(mapper.toMO(creature));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while saving a creature: %s.", creature.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        Logger.info("Creature saved successfully.", savedCreature.identifier());

        return mapper.toODTO(savedCreature, TranslatedString.EN);
    }

    @Override
    public CreatureODTO update(CreatureIDTO creature) throws APIException {
        final CreatureMO updatedCreature;

        try {
            updatedCreature = repository.update(mapper.toMO(creature));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while updating a creature: %s.", creature.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        Logger.info("Creature updated successfully.", updatedCreature.identifier());

        return mapper.toODTO(updatedCreature, TranslatedString.EN);
    }

    @Override
    public void delete(String identifier) throws APIException {
        try {
            repository.delete(identifier);
        } catch (Exception e) {
            Logger.error(String.format("An error has occurred while removing a creature: %s.", identifier), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CONFLICT.code,
                    HeraldsOfChaosAPIError.CONFLICT.message,
                    HeraldsOfChaosAPIError.CONFLICT.status
            );
        }

        Logger.info("Creature removed successfully.", identifier);
    }

    @Override
    public CreatureODTO get(String identifier, String language) throws APIException {
        final var creature = repository.get(identifier).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.status
                )
        );

        return mapper.toODTO(creature, language);
    }

    @Override
    public List<CreatureODTO> list(String language) throws APIException {
        try {
            final var creatures = repository.list();

            return creatures.stream().map(creature -> mapper.toODTO(creature, language)).toList();
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
            final var creatures = repository.page(pageable);

            return creatures.map(creature -> mapper.toODTO(creature, language).toMap());
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.status
            );
        }
    }
}
