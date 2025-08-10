package org.gycoding.heraldsofchaos.infrastructure.api.dto.out.items;

import lombok.Builder;

@Builder
public record ItemRSDTO(
        String identifier,
        String name,
        String description,
        String image,
        String type
) { }