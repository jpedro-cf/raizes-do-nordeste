package raizes.nordeste.app.infra.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.CanalPedido;
import raizes.nordeste.app.domain.entities.Order;

public interface OrdersRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByUnitId(Long unitId, Pageable pageable);
    Page<Order> findAllByCanalPedido(CanalPedido canalPedido, Pageable pageable);
}
