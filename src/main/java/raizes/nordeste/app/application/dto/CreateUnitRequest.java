package raizes.nordeste.app.application.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateUnitRequest(
        @NotBlank String name,
        @NotBlank String address
) {}