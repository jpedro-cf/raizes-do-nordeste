package raizes.nordeste.app.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.Payment;

import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(Long orderId);
}
