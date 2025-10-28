package org.gycoding.heraldsofchaos.application.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.application.mapper.ItemServiceMapper;
import org.gycoding.heraldsofchaos.application.service.ItemService;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.domain.repository.ItemRepository;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.gycoding.quasar.logs.service.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository repository;

    private final ItemServiceMapper mapper;

    @Override
    public ItemODTO save(ItemIDTO item) throws ServiceException {
        final ItemMO savedItem;

        if (repository.get(item.identifier()).isPresent()) {
            Logger.error("Item already exists.", item.identifier());

            throw new ServiceException(HeraldsOfChaosError.ITEM_ALREADY_EXISTS_CONFLICT);
        }

        try {
            savedItem = repository.save(mapper.toMO(item));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while saving an item: %s.", item.identifier()), e.getMessage());

            throw new ServiceException(HeraldsOfChaosError.ITEM_SAVE_CONFLICT);
        }

        Logger.info("Item saved successfully.", savedItem.identifier());

        return mapper.toODTO(savedItem, TranslatedString.EN);
    }

    @Override
    public ItemODTO update(ItemIDTO item) throws ServiceException {
        final ItemMO updatedItem;

        try {
            updatedItem = repository.update(mapper.toMO(item));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while updating an item: %s.", item.identifier()), e.getMessage());

            throw new ServiceException(HeraldsOfChaosError.ITEM_UPDATE_CONFLICT);
        }

        Logger.info("Item updated successfully.", updatedItem.identifier());

        return mapper.toODTO(updatedItem, TranslatedString.EN);
    }

    @Override
    public void delete(String identifier) throws ServiceException {
        try {
            repository.delete(identifier);
        } catch (Exception e) {
            Logger.error(String.format("An error has occurred while removing an item: %s.", identifier), e.getMessage());

            throw new ServiceException(HeraldsOfChaosError.ITEM_DELETE_CONFLICT);
        }

        Logger.info("Item removed successfully.", identifier);
    }

    @Override
    public ItemODTO get(String identifier, String language) throws ServiceException {
        final var item = repository.get(identifier).orElseThrow(() ->
                new ServiceException(HeraldsOfChaosError.ITEM_NOT_FOUND)
        );

        return mapper.toODTO(item, language);
    }

    @Override
    public List<ItemODTO> list(String language) throws ServiceException {
        final var items = repository.list();

        return items.stream().map(item -> mapper.toODTO(item, language)).toList();
    }

    @Override
    public Page<Map<String, Object>> page(Pageable pageable, String language) throws ServiceException {
        final var items = repository.page(pageable);

        return items.map(item -> mapper.toODTO(item, language).toMap());
    }
}
