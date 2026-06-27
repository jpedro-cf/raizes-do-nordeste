package raizes.nordeste.app.infra.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.PointsTransaction;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    Page<PointsTransaction> findAllByUserId(Long userId, Pageable pageable);
}