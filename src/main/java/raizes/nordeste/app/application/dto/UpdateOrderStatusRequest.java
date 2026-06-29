package raizes.nordeste.app.application.dto;

import jakarta.validation.constraints.NotNull;
import raizes.nordeste.app.api.config.validation.ValidEnum;
import raizes.nordeste.app.domain.entities.OrderStatus;

public record UpdateOrderStatusRequest(
        @NotNull
        @ValidEnum(enumClass = OrderStatus.class,
                message = "Invalid value. Accepted values: PENDING, CONFIRMED, PREPARING, DELIVERED, CANCELLED")
        String status
) {}
