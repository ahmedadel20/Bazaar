package org.bazaar.giza.clients;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CategoryDto(
    @NotNull Long id,
    String name
)
{

}
