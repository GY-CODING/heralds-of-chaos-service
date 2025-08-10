package org.gycoding.heraldsofchaos.domain.model.items;

import lombok.Builder;
import org.gycoding.heraldsofchaos.domain.model.TranslatedString;

@Builder
public record ItemMO(
    String identifier,
    TranslatedString name,
    TranslatedString description,
    String image,
    TranslatedString type
) {}
