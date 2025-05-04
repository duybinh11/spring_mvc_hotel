package Repository;

import Entity.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RevokedRepository extends JpaRepository<RevokedToken,Long> {
    Optional<RevokedToken> findByToken(String token);
}
