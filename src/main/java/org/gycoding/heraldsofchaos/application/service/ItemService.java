package org.gycoding.heraldsofchaos.application.service;

import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ItemService {
    ItemODTO save(ItemIDTO item) throws ServiceException;

    ItemODTO update(ItemIDTO item) throws ServiceException;

    void delete(String identifier) throws ServiceException;

    ItemODTO get(String identifier, String language) throws ServiceException;

    List<ItemODTO> list(String language) throws ServiceException;

    Page<Map<String, Object>> page(Pageable pageable, String language) throws ServiceException;
}
