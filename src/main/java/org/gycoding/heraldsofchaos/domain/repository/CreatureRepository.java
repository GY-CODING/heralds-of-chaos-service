package org.gycoding.heraldsofchaos.domain.repository;

import org.gycoding.heraldsofchaos.domain.model.creatures.CreatureMO;
import org.gycoding.quasar.exceptions.model.DatabaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreatureRepository {
    CreatureMO save(CreatureMO creature);

    CreatureMO update(CreatureMO creature) throws DatabaseException;
    void delete(String identifier);

    Optional<CreatureMO> get(String identifier);
    List<CreatureMO> list();
    Page<CreatureMO> page(Pageable pageable);
}
