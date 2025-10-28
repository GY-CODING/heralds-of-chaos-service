package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.gycoding.heraldsofchaos.domain.repository.CreatureRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.CreatureDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.CreatureMongoRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.OrderMongoRepository;
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
public class CreatureDatabaseImpl implements CreatureRepository {
    private final OrderMongoRepository orderRepository;
    private final CreatureMongoRepository repository;
    private final CreatureDatabaseMapper mapper;

    @Override
    public CreatureMO save(CreatureMO creature) {
        return mapper.toMO(repository.save(mapper.toEntity(creature)));
    }

    @Override
    public CreatureMO update(CreatureMO creature) throws DatabaseException {
        final var persistedCreature = repository.findByIdentifier(creature.identifier()).orElseThrow(() ->
                new DatabaseException(HeraldsOfChaosError.CREATURE_NOT_FOUND)
        );

        Logger.debug("Creature to be updated found", creature.identifier());

        return mapper.toMO(repository.save(mapper.toUpdatedEntity(persistedCreature, creature)));
    }

    @Override
    public void delete(String identifier) {
        repository.removeByIdentifier(identifier);
    }

    @Override
    public Optional<CreatureMO> get(String identifier) {
        return repository.findByIdentifier(identifier)
                .map(mapper::toMO);
    }

    @Override
    public List<CreatureMO> list() {
        final var order = orderRepository.findByCollection("Creature")
                .orElse(null)
                .getOrder();

        return repository.findAll().stream()
                .map(mapper::toMO)
                .sorted(Comparator.comparingInt(creature -> order.indexOf(creature.identifier())))
                .toList();
    }

    @Override
    public Page<CreatureMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}