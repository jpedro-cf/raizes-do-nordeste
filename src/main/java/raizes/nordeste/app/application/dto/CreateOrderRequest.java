package raizes.nordeste.app.application.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import raizes.nordeste.app.config.validation.ValidEnum;
import raizes.nordeste.app.domain.entities.CanalPedido;

import java.util.List;

public record CreateOrderRequest(
        @NotNull Long unitId,
        @NotNull
        @ValidEnum(enumClass = CanalPedido.class, message = "Invalid value. Accepted values: APP, TOTEM, BALCAO, PICKUP")
        String canalPedido,
        @NotEmpty List<@Valid OrderItemRequest> items
) {}