package raizes.nordeste.app.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.Product;

public interface ProductsRepository extends JpaRepository<Product, Long> {
}
