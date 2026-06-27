package raizes.nordeste.app.infra.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.PointsTransaction;

import java.util.List;

public interface PointsTransactionRepository extends JpaRepository<PointsTransaction, Long> {
    Page<PointsTransaction> findAllByUserId(Long userId, Pageable pageable);
    List<PointsTransaction> findAllByUserId(Long userId);
}