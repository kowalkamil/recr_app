package rekrutacja.zadanie.rekrutacja.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "request_count")
public class ApiRequestCount {

    @Id
    private String login;
    private int requestCount;

    public void incrementRequestCount() {
        this.requestCount++;
    }

}
