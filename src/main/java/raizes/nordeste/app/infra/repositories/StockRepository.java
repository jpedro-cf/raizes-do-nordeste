package raizes.nordeste.app.infra.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.StockItem;

import java.util.Optional;

public interface StockRepository extends JpaRepository<StockItem, Long> {
    Page<StockItem> findAllByUnitId(Long unitId, Pageable pageable);
    Optional<StockItem> findByProductIdAndUnitId(Long productId, Long unitId);
}
