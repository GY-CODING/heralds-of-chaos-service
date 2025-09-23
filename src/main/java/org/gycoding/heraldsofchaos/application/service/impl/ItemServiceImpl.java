package org.gycoding.heraldsofchaos.application.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.application.mapper.ItemServiceMapper;
import org.gycoding.heraldsofchaos.application.service.ItemService;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.domain.repository.ItemRepository;
import org.gycoding.logs.logger.Logger;
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
    public ItemODTO save(ItemIDTO item) throws APIException {
        final ItemMO savedItem;

        if (repository.get(item.identifier()).isPresent()) {
            Logger.error("Item already exists.", item.identifier());

            throw new APIException(
                    HeraldsOfChaosAPIError.ITEM_ALREADY_EXISTS_CONFLICT.code,
                    HeraldsOfChaosAPIError.ITEM_ALREADY_EXISTS_CONFLICT.message,
                    HeraldsOfChaosAPIError.ITEM_ALREADY_EXISTS_CONFLICT.status
            );
        }

        try {
            savedItem = repository.save(mapper.toMO(item));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while saving an item: %s.", item.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.ITEM_SAVE_CONFLICT.code,
                    HeraldsOfChaosAPIError.ITEM_SAVE_CONFLICT.message,
                    HeraldsOfChaosAPIError.ITEM_SAVE_CONFLICT.status
            );
        }

        Logger.info("Item saved successfully.", savedItem.identifier());

        return mapper.toODTO(savedItem, TranslatedString.EN);
    }

    @Override
    public ItemODTO update(ItemIDTO item) throws APIException {
        final ItemMO updatedItem;

        try {
            updatedItem = repository.update(mapper.toMO(item));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while updating an item: %s.", item.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.ITEM_UPDATE_CONFLICT.code,
                    HeraldsOfChaosAPIError.ITEM_UPDATE_CONFLICT.message,
                    HeraldsOfChaosAPIError.ITEM_UPDATE_CONFLICT.status
            );
        }

        Logger.info("Item updated successfully.", updatedItem.identifier());

        return mapper.toODTO(updatedItem, TranslatedString.EN);
    }

    @Override
    public void delete(String identifier) throws APIException {
        try {
            repository.delete(identifier);
        } catch (Exception e) {
            Logger.error(String.format("An error has occurred while removing an item: %s.", identifier), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.ITEM_DELETE_CONFLICT.code,
                    HeraldsOfChaosAPIError.ITEM_DELETE_CONFLICT.message,
                    HeraldsOfChaosAPIError.ITEM_DELETE_CONFLICT.status
            );
        }

        Logger.info("Item removed successfully.", identifier);
    }

    @Override
    public ItemODTO get(String identifier, String language) throws APIException {
        final var item = repository.get(identifier).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.ITEM_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.ITEM_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.ITEM_NOT_FOUND.status
                )
        );

        return mapper.toODTO(item, language);
    }

    @Override
    public List<ItemODTO> list(String language) throws APIException {
        final var items = repository.list();

        return items.stream().map(item -> mapper.toODTO(item, language)).toList();
    }

    @Override
    public Page<Map<String, Object>> page(Pageable pageable, String language) throws APIException {
        final var items = repository.page(pageable);

        return items.map(item -> mapper.toODTO(item, language).toMap());
    }
}
