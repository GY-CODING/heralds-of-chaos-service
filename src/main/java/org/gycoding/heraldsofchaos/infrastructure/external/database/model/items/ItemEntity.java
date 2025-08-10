package org.gycoding.heraldsofchaos.infrastructure.external.database.model.items;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "Item")
public class ItemEntity {
    @Id
    public String mongoId;
    public String identifier;
    public TranslatedString name;
    public TranslatedString description;
    public String image;
    public TranslatedString type;

    @Builder
    public ItemEntity(String mongoId, String identifier, TranslatedString name, TranslatedString description, String image, TranslatedString type) {
        this.mongoId = mongoId;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.image = image;
        this.type = type;
    }
}