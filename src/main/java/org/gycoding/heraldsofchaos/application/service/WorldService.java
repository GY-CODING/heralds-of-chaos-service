package org.gycoding.heraldsofchaos.application.service;

import org.gycoding.heraldsofchaos.application.dto.in.worlds.WorldIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.PlaceODTO;
import org.gycoding.heraldsofchaos.application.dto.out.worlds.WorldODTO;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface WorldService {
    WorldODTO save(WorldIDTO world) throws ServiceException;

    WorldODTO update(WorldIDTO world) throws ServiceException;

    void delete(String identifier) throws ServiceException;

    WorldODTO get(String identifier, String language) throws ServiceException;

    List<WorldODTO> list(String language) throws ServiceException;

    Page<Map<String, Object>> page(Pageable pageable, String language) throws ServiceException;

    List<PlaceODTO> listPlaces(String idWorld, String language) throws ServiceException;
}
