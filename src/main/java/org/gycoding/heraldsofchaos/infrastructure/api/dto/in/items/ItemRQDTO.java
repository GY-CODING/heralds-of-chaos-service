package org.gycoding.heraldsofchaos.infrastructure.api.dto.in.items;

import lombok.Builder;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;

@Builder
public record ItemRQDTO(
        TranslatedString name,
        TranslatedString description,
        String image,
        TranslatedString type
) { }