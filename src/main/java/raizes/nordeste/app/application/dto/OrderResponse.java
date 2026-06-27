package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.CanalPedido;
import raizes.nordeste.app.domain.entities.Order;
import raizes.nordeste.app.domain.entities.OrderStatus;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        Long id,
        Long userId,
        Long unitId,
        OrderStatus status,
        CanalPedido canalPedido,
        BigInteger total,
        BigInteger subTotal,
        BigInteger discount,
        Instant createdAt,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order){
        return new OrderResponse(
                order.getId(),
                order.getUser().getId(),
                order.getUnit().getId(),
                order.getStatus(),
                order.getCanalPedido(),
                order.getTotal(),
                order.getSubTotal(),
                order.getDiscount(),
                order.getCreatedAt(),
                order.getItems()
                        .stream()
                        .map(OrderItemResponse::from)
                        .toList()
        );
    }
}
