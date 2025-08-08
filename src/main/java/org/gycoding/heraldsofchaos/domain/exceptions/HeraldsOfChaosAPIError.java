package org.gycoding.heraldsofchaos.domain.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum HeraldsOfChaosAPIError {
    RESOURCE_NOT_FOUND("This resource was not found.", HttpStatus.NOT_FOUND),
    CONFLICT("A conflict has occurred while operating with this resource.", HttpStatus.CONFLICT),
    FORBIDDEN("The user has no permission to access this resource.", HttpStatus.FORBIDDEN);

    public final String code;
    public final String message;
    public final HttpStatus status;

    HeraldsOfChaosAPIError(String message, HttpStatus status) {
        this.code       = this.name();
        this.message    = message;
        this.status     = status;
    }
}
