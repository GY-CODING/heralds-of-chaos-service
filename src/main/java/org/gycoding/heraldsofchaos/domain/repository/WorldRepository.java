package org.gycoding.heraldsofchaos.domain.repository;

import org.gycoding.heraldsofchaos.domain.model.worlds.WorldMO;
import org.gycoding.quasar.exceptions.model.DatabaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorldRepository {
    WorldMO save(WorldMO world, List<String> places);

    WorldMO update(WorldMO world, List<String> places) throws DatabaseException;
    void delete(String identifier);

    Optional<WorldMO> get(String identifier);
    List<WorldMO> list();
    Page<WorldMO> page(Pageable pageable);
}
