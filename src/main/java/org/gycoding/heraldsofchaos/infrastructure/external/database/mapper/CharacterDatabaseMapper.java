package org.gycoding.heraldsofchaos.infrastructure.external.database.mapper;

import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.characters.CharacterEntity;
import org.gycoding.heraldsofchaos.infrastructure.external.database.model.worlds.WorldEntity;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CharacterDatabaseMapper {
    @Mapping(target = "world", source = "world.identifier")
    CharacterMO toMO(CharacterEntity character);

    @Mapping(target = "mongoId", ignore = true)
    @Mapping(target = "world", source = "persistedWorld")
    @Mapping(target = "identifier", source = "character.identifier")
    @Mapping(target = "name", source = "character.name")
    @Mapping(target = "description", source = "character.description")
    @Mapping(target = "image", source = "character.image")
    CharacterEntity toEntity(CharacterMO character, WorldEntity persistedWorld);

    @Mapping(target = "mongoId", ignore = true)
    @Mapping(target = "world", source = "persistedWorld")
    @Mapping(target = "identifier", source = "character.identifier")
    @Mapping(target = "name", source = "character.name")
    @Mapping(target = "description", source = "character.description")
    @Mapping(target = "image", source = "character.image")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CharacterEntity toUpdatedEntity(@MappingTarget CharacterEntity persistedCharacter, CharacterMO character, WorldEntity persistedWorld);
}
