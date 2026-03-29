package orderservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.api.dto.OrderCreatedEvent;
import orderservice.domain.entity.OutboxEventEntity;
import orderservice.domain.utils.OutboxJpaRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxRelay {
    private final OutboxJpaRepository outboxRepository;
    private final OrderKafkaProducer kafkaProducer;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishPendingEvents(){
        Pageable page = PageRequest.of(0, 100);
        List<OutboxEventEntity> pending = outboxRepository.findByStatusAndPublishedAtIsNull(
                "PENDING", page);
        if(pending.isEmpty()){
            return;
        }
        log.info("Found {} pending outbox events", pending.size());
        for (OutboxEventEntity outbox : pending){
            try {
                OrderCreatedEvent event = objectMapper.readValue(outbox.getPayload(),
                        OrderCreatedEvent.class);
                kafkaProducer.publish(event);
                outbox.setPublishedAt(LocalDateTime.now());
                outbox.setStatus("PUBLISHED");
                outboxRepository.save(outbox);
                log.info("Outbox event {} published successfully", outbox.getId());
            }catch (Exception e){
                log.error("Failed to publish outbox event{}: {}", outbox.getId(), e.getMessage());
            }
        }
    }
}
