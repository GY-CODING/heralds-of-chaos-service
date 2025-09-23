package org.gycoding.heraldsofchaos.application.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gycoding.exceptions.model.APIException;
import org.gycoding.heraldsofchaos.application.dto.in.characters.CharacterIDTO;
import org.gycoding.heraldsofchaos.application.dto.out.characters.CharacterODTO;
import org.gycoding.heraldsofchaos.application.mapper.CharacterServiceMapper;
import org.gycoding.heraldsofchaos.application.service.CharacterService;
import org.gycoding.heraldsofchaos.domain.exceptions.HeraldsOfChaosAPIError;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;
import org.gycoding.heraldsofchaos.domain.model.characters.CharacterMO;
import org.gycoding.heraldsofchaos.domain.repository.CharacterRepository;
import org.gycoding.logs.logger.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class CharacterServiceImpl implements CharacterService {
    private final CharacterRepository repository;

    private final CharacterServiceMapper mapper;

    @Override
    public CharacterODTO save(CharacterIDTO character) throws APIException {
        final CharacterMO savedCharacter;

        if (repository.get(character.identifier()).isPresent()) {
            Logger.error("Character already exists.", character.identifier());

            throw new APIException(
                    HeraldsOfChaosAPIError.CHARACTER_ALREADY_EXISTS_CONFLICT.code,
                    HeraldsOfChaosAPIError.CHARACTER_ALREADY_EXISTS_CONFLICT.message,
                    HeraldsOfChaosAPIError.CHARACTER_ALREADY_EXISTS_CONFLICT.status
            );
        }

        try {
            savedCharacter = repository.save(mapper.toMO(character));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while saving a character: %s.", character.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CHARACTER_SAVE_CONFLICT.code,
                    HeraldsOfChaosAPIError.CHARACTER_SAVE_CONFLICT.message,
                    HeraldsOfChaosAPIError.CHARACTER_SAVE_CONFLICT.status
            );
        }

        Logger.info("Character saved successfully.", savedCharacter.identifier());

        return mapper.toODTO(savedCharacter, TranslatedString.EN);
    }

    @Override
    public CharacterODTO update(CharacterIDTO character) throws APIException {
        final CharacterMO updatedCharacter;

        try {
            updatedCharacter = repository.update(mapper.toMO(character));
        } catch(Exception e) {
            Logger.error(String.format("An error has occurred while updating a character: %s.", character.identifier()), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CHARACTER_UPDATE_CONFLICT.code,
                    HeraldsOfChaosAPIError.CHARACTER_UPDATE_CONFLICT.message,
                    HeraldsOfChaosAPIError.CHARACTER_UPDATE_CONFLICT.status
            );
        }

        Logger.info("Character updated successfully.", updatedCharacter.identifier());

        return mapper.toODTO(updatedCharacter, TranslatedString.EN);
    }

    @Override
    public void delete(String identifier) throws APIException {
        try {
            repository.delete(identifier);
        } catch (Exception e) {
            Logger.error(String.format("An error has occurred while removing a character: %s.", identifier), e.getMessage());

            throw new APIException(
                    HeraldsOfChaosAPIError.CHARACTER_DELETE_CONFLICT.code,
                    HeraldsOfChaosAPIError.CHARACTER_DELETE_CONFLICT.message,
                    HeraldsOfChaosAPIError.CHARACTER_DELETE_CONFLICT.status
            );
        }

        Logger.info("Character removed successfully.", identifier);
    }

    @Override
    public CharacterODTO get(String identifier, String language) throws APIException {
        final var character = repository.get(identifier).orElseThrow(() ->
                new APIException(
                        HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.code,
                        HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.message,
                        HeraldsOfChaosAPIError.CHARACTER_NOT_FOUND.status
                )
        );

        return mapper.toODTO(character, language);
    }

    @Override
    public List<CharacterODTO> list(String language) throws APIException {
        try {
            final var characters = repository.list();

            return characters.stream()
                    .map(character -> mapper.toODTO(character, language))
                    .toList();
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.CHARACTER_LIST_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.CHARACTER_LIST_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.CHARACTER_LIST_NOT_FOUND.status
            );
        }
    }

    @Override
    public Page<Map<String, Object>> page(Pageable pageable, String language) throws APIException {
        try {
            final var characters = repository.page(pageable);

            return characters.map(character -> mapper.toODTO(character, language).toMap());
        } catch (NullPointerException e) {
            throw new APIException(
                    HeraldsOfChaosAPIError.CHARACTER_LIST_NOT_FOUND.code,
                    HeraldsOfChaosAPIError.CHARACTER_LIST_NOT_FOUND.message,
                    HeraldsOfChaosAPIError.CHARACTER_LIST_NOT_FOUND.status
            );
        }
    }
}