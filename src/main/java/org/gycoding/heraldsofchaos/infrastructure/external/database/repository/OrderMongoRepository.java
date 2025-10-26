package org.gycoding.heraldsofchaos.infrastructure.external.database.repository;

import org.gycoding.heraldsofchaos.infrastructure.external.database.model.OrderEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderMongoRepository extends MongoRepository<OrderEntity, String> {
    Optional<OrderEntity> findByCollection(String collection);
}
