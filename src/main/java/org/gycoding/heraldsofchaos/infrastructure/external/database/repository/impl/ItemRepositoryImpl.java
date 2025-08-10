package org.gycoding.heraldsofchaos.infrastructure.external.database.repository.impl;

import lombok.AllArgsConstructor;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.domain.repository.ItemRepository;
import org.gycoding.heraldsofchaos.infrastructure.external.database.mapper.ItemDatabaseMapper;
import org.gycoding.heraldsofchaos.infrastructure.external.database.repository.ItemMongoRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemMongoRepository repository;

    private final ItemDatabaseMapper mapper;

    @Override
    public ItemMO save(ItemMO item) {
        return mapper.toMO(repository.save(mapper.toEntity(item)));
    }

    @Override
    public ItemMO update(ItemMO item) throws APIException {
        final var persistedItem = repository.findByIdentifier(item.identifier()).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.RESOURCE_NOT_FOUND.status
                )
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
        return repository.findAll().stream()
                .map(mapper::toMO)
                .toList();
    }

    @Override
    public Page<ItemMO> page(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toMO);
    }
}