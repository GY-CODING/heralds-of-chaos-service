package org.gycoding.heraldsofchaos.application.dto.out.items;

import lombok.Builder;

import java.util.Map;

@Builder
public record ItemODTO(
        String identifier,
        String name,
        String description,
        String image,
        String type
) {
    public Map<String, Object> toMap() {
        return Map.of(
                "identifier", identifier,
                "name", name,
                "description", description,
                "image", image,
                "type", type
        );
    }
}