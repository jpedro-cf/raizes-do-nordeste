package raizes.nordeste.app.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

public record CreateProductRequest(
        @NotBlank String name,
        @NotNull @Positive BigInteger basePrice
) {}