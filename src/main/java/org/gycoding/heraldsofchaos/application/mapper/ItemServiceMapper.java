package org.gycoding.heraldsofchaos.application.mapper;

import org.gycoding.heraldsofchaos.application.dto.in.items.ItemIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.items.ItemODTO;
import org.gycoding.heraldsofchaos.domain.model.items.ItemMO;
import org.gycoding.heraldsofchaos.shared.StringTranslator;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", imports = { StringTranslator.class })
public interface ItemServiceMapper {
    ItemMO toMO(ItemIDTO item);

    @Mapping(target = "name", expression = "java(StringTranslator.translate(item.name(), language))")
    @Mapping(target = "description", expression = "java(StringTranslator.translate(item.description(), language))")
    @Mapping(target = "type", expression = "java(StringTranslator.translate(item.type(), language))")
    ItemODTO toODTO(ItemMO item, String language);
}
