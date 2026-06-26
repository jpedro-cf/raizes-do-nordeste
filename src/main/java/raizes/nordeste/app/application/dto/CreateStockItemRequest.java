package raizes.nordeste.app.application.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigInteger;

public record CreateStockItemRequest(
        @NotNull Long productId,
        @NotNull Long unitId,
        @NotNull @Positive Long amount,
        @NotNull @Positive BigInteger price
) {}