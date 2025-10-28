package org.gycoding.heraldsofchaos.application.service;

import org.gycoding.heraldsofchaos.application.dto.in.creatures.CreatureIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.creatures.CreatureODTO;
import org.gycoding.quasar.exceptions.model.ServiceException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CreatureService {
    CreatureODTO save(CreatureIDTO creature) throws ServiceException;

    CreatureODTO update(CreatureIDTO creature) throws ServiceException;

    void delete(String identifier) throws ServiceException;

    CreatureODTO get(String identifier, String language) throws ServiceException;

    List<CreatureODTO> list(String language) throws ServiceException;

    Page<Map<String, Object>> page(Pageable pageable, String language) throws ServiceException;
}
