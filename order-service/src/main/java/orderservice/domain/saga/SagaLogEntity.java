package orderservice.domain.saga;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "saga_log")
public class SagaLogEntity {
    @Id
    @Column(name = "saga_id", nullable = false, unique = true)
    private UUID sagaId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "saga_type")
    private String sagaType;

    @Column(name = "current_state")
    private String currentState;

    @Column(name = "payload")
    private String payload;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
