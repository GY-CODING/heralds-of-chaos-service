package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.domain.repository.ItemRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.ItemDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.ItemMongoRepository;
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
public class ItemDatabaseImpl implements ItemRepository {
    private final OrderMongoRepository orderRepository;
    private final ItemMongoRepository repository;
    private final ItemDatabaseMapper mapper;

    @Override
    public ItemMO save(ItemMO item) {
        return mapper.toMO(repository.save(mapper.toEntity(item)));
    }

    @Override
    public ItemMO update(ItemMO item) throws DatabaseException {
        final var persistedItem = repository.findByIdentifier(item.identifier()).orElseThrow(() ->
                new DatabaseException(HeraldsOfChaosError.ITEM_NOT_FOUND)
        );

        Logger.debug("Item to be updated found", item.identifier());

        return mapper.toMO(repository.save(mapper.toUpdatedEntity(persistedItem, item)));
    }

    @Override
    public void delete(String identifier) {
        repository.removeByIdentifier(identifier);
    }

    @Override
    public Optional<ItemMO> get(String identifier) {
        return repository.findByIdentifier(identifier)
                .map(mapper::toMO);
    }

    @Override
    public List<ItemMO> list() {
        final var order = orderRepository.findByCollection("Item")
                .orElse(null)
                .getOrder();

        return repository.findAll().stream()
                .map(mapper::toMO)
                .sorted(Comparator.comparingInt(item -> order.indexOf(item.identifier())))
                .toList();
    }

    @Override
    public Page<ItemMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}