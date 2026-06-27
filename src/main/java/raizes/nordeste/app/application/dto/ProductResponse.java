package raizes.nordeste.app.application.dto;

import raizes.nordeste.app.domain.entities.Product;

import java.time.Instant;

public record ProductResponse(
        Long id,
        String name,
        Instant createdAt
) {
    public static ProductResponse from(Product product){
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getCreatedAt());
    }
}
