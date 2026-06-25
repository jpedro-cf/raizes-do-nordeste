package raizes.nordeste.app.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.Unit;

public interface UnitRepository extends JpaRepository<Unit, Long> {
}
