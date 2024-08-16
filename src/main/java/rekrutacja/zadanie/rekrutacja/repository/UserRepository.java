package rekrutacja.zadanie.rekrutacja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rekrutacja.zadanie.rekrutacja.model.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
