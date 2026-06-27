package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.StockItem;

import java.math.BigInteger;
import java.time.Instant;

public record StockItemResponse(
        Long id,
        Long amountInStock,
        BigInteger price,
        ProductResponse product,
        Long unitId,
        Instant createdAt
) {
    public static StockItemResponse from(StockItem item){
        return new StockItemResponse(
                item.getId(),
                item.getAmountInStock(),
                item.getPrice(),
                ProductResponse.from(item.getProduct()),
                item.getUnit().getId(),
                item.getCreatedAt()
        );
    }
}
