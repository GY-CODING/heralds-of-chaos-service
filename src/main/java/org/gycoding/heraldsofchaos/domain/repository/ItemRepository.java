package org.gycoding.heraldsofchaos.domain.repository;

import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.quasar.exceptions.model.DatabaseException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository {
    ItemMO save(ItemMO item);

    ItemMO update(ItemMO item) throws DatabaseException;
    void delete(String identifier);

    Optional<ItemMO> get(String identifier);
    List<ItemMO> list();
    Page<ItemMO> page(Pageable pageable);
}