package org.gycoding.heraldsofchaos.domain.exceptions;

import lombok.Getter;
import org.gycoding.quasar.exceptions.model.ExceptionError;
import org.springframework.http.HttpStatus;

@Getter
public enum HeraldsOfChaosError implements ExceptionError {
    CHARACTER_NOT_FOUND("Character was not found.", HttpStatus.NOT_FOUND),
    CHARACTER_LIST_NOT_FOUND("List of Characters not found.", HttpStatus.NOT_FOUND),
    CREATURE_NOT_FOUND("Creature was not found.", HttpStatus.NOT_FOUND),
    CREATURE_LIST_NOT_FOUND("List of Creatures not found.", HttpStatus.NOT_FOUND),
    ITEM_NOT_FOUND("Item was not found.", HttpStatus.NOT_FOUND),
    ITEM_LIST_NOT_FOUND("List of Items not found.", HttpStatus.NOT_FOUND),
    PLACE_NOT_FOUND("Place was not found.", HttpStatus.NOT_FOUND),
    PLACE_LIST_NOT_FOUND("List of Places not found.", HttpStatus.NOT_FOUND),
    WORLD_NOT_FOUND("World was not found.", HttpStatus.NOT_FOUND),
    WORLD_LIST_NOT_FOUND("List of Worlds not found.", HttpStatus.NOT_FOUND),

    CHARACTER_ALREADY_EXISTS_CONFLICT("Character trying to be saved already exists.", HttpStatus.CONFLICT),
    CHARACTER_SAVE_CONFLICT("An error has occurred while trying to save a new Character.", HttpStatus.CONFLICT),
    CHARACTER_UPDATE_CONFLICT("An error has occurred while trying to update a new Character.", HttpStatus.CONFLICT),
    CHARACTER_DELETE_CONFLICT("An error has occurred while trying to remove a new Character.", HttpStatus.CONFLICT),
    CREATURE_ALREADY_EXISTS_CONFLICT("Creature trying to be saved already exists.", HttpStatus.CONFLICT),
    CREATURE_SAVE_CONFLICT("An error has occurred while trying to save a new Creature.", HttpStatus.CONFLICT),
    CREATURE_UPDATE_CONFLICT("An error has occurred while trying to update a new Creature.", HttpStatus.CONFLICT),
    CREATURE_DELETE_CONFLICT("An error has occurred while trying to remove a new Creature.", HttpStatus.CONFLICT),
    ITEM_ALREADY_EXISTS_CONFLICT("Item trying to be saved already exists.", HttpStatus.CONFLICT),
    ITEM_SAVE_CONFLICT("An error has occurred while trying to save a new Item.", HttpStatus.CONFLICT),
    ITEM_UPDATE_CONFLICT("An error has occurred while trying to update a new Item.", HttpStatus.CONFLICT),
    ITEM_DELETE_CONFLICT("An error has occurred while trying to remove a new Item.", HttpStatus.CONFLICT),
    PLACE_ALREADY_EXISTS_CONFLICT("Place trying to be saved already exists.", HttpStatus.CONFLICT),
    PLACE_SAVE_CONFLICT("An error has occurred while trying to save a new Place.", HttpStatus.CONFLICT),
    PLACE_UPDATE_CONFLICT("An error has occurred while trying to update a new Place.", HttpStatus.CONFLICT),
    PLACE_DELETE_CONFLICT("An error has occurred while trying to remove a new Place.", HttpStatus.CONFLICT),
    WORLD_ALREADY_EXISTS_CONFLICT("World trying to be saved already exists.", HttpStatus.CONFLICT),
    WORLD_SAVE_CONFLICT("An error has occurred while trying to save a new World.", HttpStatus.CONFLICT),
    WORLD_UPDATE_CONFLICT("An error has occurred while trying to update a new World.", HttpStatus.CONFLICT),
    WORLD_DELETE_CONFLICT("An error has occurred while trying to remove a new World.", HttpStatus.CONFLICT);

    public final String code;
    public final String message;
    public final HttpStatus status;

    HeraldsOfChaosError(String message, HttpStatus status) {
        this.code       = this.name();
        this.message    = message;
        this.status     = status;
    }
}
