package Repository;

import Entity.TailwindEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TailwindRepository extends JpaRepository<TailwindEntity,Long> {
}
