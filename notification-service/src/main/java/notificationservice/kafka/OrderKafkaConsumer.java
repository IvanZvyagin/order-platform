package notificationservice.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kafka.KafkaConstants;
import notificationservice.entity.NsOrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.ReceiverRecord;
import notificationservice.repository.NsOrderRepository;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class OrderKafkaConsumer {
    private final NsOrderRepository nsOrderRepository;
    private final ObjectMapper objectMapper;
    private final Environment environment;

    @Bean
    public Flux<NsOrderEntity> kafkaOrderStream(){
        ReceiverOptions<String, String> options = ReceiverOptions.<String, String> create(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("kafka.bootstrap-servers",
                                "localhost:9092"),
                            ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("kafka.consumer.group-id",
                                "notification-reactive-group"),
                            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
                )
        )
            .subscription(Collections.singletonList(KafkaConstants.ORDERS_TOPIC))
            .addAssignListener(partitions -> log.info("Assigned partitions: {}", partitions))
            .addRevokeListener(partitions -> log.info("Revoked partitions: {}", partitions));
        KafkaReceiver<String, String> receiver = KafkaReceiver.create(options);

        return receiver.receive()
                .concatMap(record -> processRecord(record).onErrorResume(e -> {
                    log.error("Error processing record offset: {}: {}", record.offset(), e.getMessage());
                    record.receiverOffset().acknowledge();
                    return Mono.empty();
                }))
                .doOnNext(order -> log.info("Processed order: orderId = {}, userId = {}",
                        order.getOrderId(), order.getUserId()))
                .doOnError(e -> log.error("Stream error", e))
//                .retryWhen(retrySpec -> retrySpec
//                        .exponentialBackoff(Duration.ofSeconds(1), Duration.ofSeconds(10))
//                        .onRetryExhaustedThrow((spec, rs) -> rs.failure()))
                .onErrorContinue((e, obj) -> log.error("Non-fatal error", e));
    }

    private Mono<NsOrderEntity> processRecord(ReceiverRecord<String, String> record){
        return Mono.fromCallable(() -> {
            return objectMapper.readValue(record.value(), NsOrderEntity.class);
        })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(nsOrderRepository::save)
                .doFinally(sig -> record.receiverOffset().acknowledge());
    }
}
