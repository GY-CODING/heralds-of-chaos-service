package org.gycoding.heraldsofchaos.application.service;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.PlaceIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface PlaceService {
    PlaceODTO save(PlaceIDTO place) throws ServiceException;

    PlaceODTO update(PlaceIDTO place) throws ServiceException;

    void delete(String identifier) throws ServiceException;

    PlaceODTO get(String identifier, String language) throws ServiceException;

    List<PlaceODTO> list(String language) throws ServiceException;

    Page<Map<String, Object>> page(Pageable pageable, String language) throws ServiceException;
}
