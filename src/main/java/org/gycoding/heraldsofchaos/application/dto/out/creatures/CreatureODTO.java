package org.gycoding.heraldsofchaos.application.dto.out.creatures;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

@Builder
public record CreatureODTO(
        String identifier,
        String name,
        String description,
        String image,
        String race
) {
    public Map<String, Object> toMap() {
        return new HashMap<String, Object>(Map.of(
                "identifier", identifier,
                "name", name,
                "description", description,
                "image", image,
                "race", race
        ));
    }
}

