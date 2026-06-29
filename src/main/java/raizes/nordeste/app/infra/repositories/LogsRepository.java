package raizes.nordeste.app.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.Log;

public interface LogsRepository extends JpaRepository<Log, Long> {
}
