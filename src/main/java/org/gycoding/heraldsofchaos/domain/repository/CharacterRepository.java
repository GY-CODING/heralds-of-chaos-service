package org.gycoding.heraldsofchaos.domain.repository;

import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.quasar.exceptions.model.DatabaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharacterRepository {
    CharacterMO save(CharacterMO character) throws DatabaseException;

    CharacterMO update(CharacterMO character) throws DatabaseException;
    void delete(String identifier);

    Optional<CharacterMO> get(String identifier);
    List<CharacterMO> list();
    Page<CharacterMO> page(Pageable pageable);
}
