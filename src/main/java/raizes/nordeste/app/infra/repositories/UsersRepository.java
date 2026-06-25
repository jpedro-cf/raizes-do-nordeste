package raizes.nordeste.app.infra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raizes.nordeste.app.domain.entities.User;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
