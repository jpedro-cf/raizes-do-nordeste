package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.OrderItem;

public record OrderItemResponse(
        Long id,
        ProductResponse product,
        Integer quantity
) {
    public static OrderItemResponse from(OrderItem item){
        return new OrderItemResponse(
                item.getId(),
                new ProductResponse(
                        item.getStockItem().getProduct().getId(),
                        item.getStockItem().getProduct().getName(),
                        item.getStockItem().getProduct().getCreatedAt()
                ),
                item.getQuantity()
        );
    }
}
