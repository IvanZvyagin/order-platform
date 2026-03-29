package notificationservice.repository;

import notificationservice.entity.NsOrderEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface NsOrderRepository extends R2dbcRepository<NsOrderEntity, Long> {

    Mono<NsOrderEntity> findByOrderId(Long id);

    Flux<NsOrderEntity> findAllByUserId(Long userId);

    Flux<NsOrderEntity> findByStatus(String status);
}
