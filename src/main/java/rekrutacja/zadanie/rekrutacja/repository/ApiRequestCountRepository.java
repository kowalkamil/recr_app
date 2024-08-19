package rekrutacja.zadanie.rekrutacja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rekrutacja.zadanie.rekrutacja.model.entity.ApiRequestCount;

public interface ApiRequestCountRepository extends JpaRepository<ApiRequestCount, String> {
}
