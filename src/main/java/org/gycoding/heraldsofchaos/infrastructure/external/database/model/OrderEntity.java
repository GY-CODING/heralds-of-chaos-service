package org.gycoding.heraldsofchaos.infrastructure.external.database.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "_Order")
public class OrderEntity {
    private String collection;
    private List<String> order;
}
