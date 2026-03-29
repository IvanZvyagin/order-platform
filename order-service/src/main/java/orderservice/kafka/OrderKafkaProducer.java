package orderservice.kafka;


import kafka.KafkaConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.api.dto.OrderCreatedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publish(OrderCreatedEvent event){
        try {
            kafkaTemplate.send(KafkaConstants.ORDERS_TOPIC, event).get(10, TimeUnit.SECONDS);
            log.info("Published OrderCreatedEven to kafka: orderId={}", event.orderId());
        }catch (Exception e){
            log.error("Failed to publish OrderCreatedEvent: orderId={}", event.orderId(),e);
            throw new RuntimeException("Kafka publish failed", e);
        }
    }
}
