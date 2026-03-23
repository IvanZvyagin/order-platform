//package orderservice.kafka;
//
//
//import com.google.common.util.concurrent.ListenableFuture;
//import kafka.KafkaConstants;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.clients.producer.RecordMetadata;
//import org.apache.kafka.common.KafkaException;
//import org.apache.kafka.common.protocol.Message;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Mono;
//
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OrderKafkaProducer {
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//
//    public CompletableFuture<Void> sendOrder(Object orderPayload){
//        Message<Object> message = MessageBuilder
//                .withPayload(orderPayload)
//                .setHeader(KafkaHeaders.TOPIC, KafkaConstants.ORDERS_TOPIC)
//                .setHeader(KafkaHeaders.KEY, generateKey(orderPayload))
//                .build();
//        return kafkaTemplate.send(message)
//                .completable()
//                .whenComplete((result, ex) -> {
//                    if (ex != null) {
//                        log.error("❌ Ошибка отправки в Kafka: {}", ex.getMessage(), ex);
//                    } else {
//                        RecordMetadata metadata = result.getRecordMetadata();
//                        log.info("✅ Сообщение доставлено | топик: {} | партиция: {} | смещение: {} | ключ: {}",
//                                metadata.topic(),
//                                metadata.partition(),
//                                metadata.offset(),
//                                metadata.serializedKeySize() > 0 ? metadata.key() : "N/A");
//                    }
//                });
//    }
//
//
//    public Mono<SendResult<String, Object>> sendOrderReactive(Object orderPayload) {
//        return Mono.fromFuture(() -> sendOrderAsync(orderPayload));
//    }
//
//    public SendResult<String, Object> sendOrderTransactional(Object orderPayload) {
//        return kafkaTemplate.executeInTransaction(operations -> {
//            ListenableFuture<SendResult<String, Object>> future =
//                    operations.send(KafkaConstants.ORDERS_TOPIC, orderPayload);
//            try {
//                return future.get(); // Ждём подтверждения внутри транзакции
//            } catch (Exception e) {
//                throw new KafkaException("Transactional send failed", e);
//            }
//        });
//    }
//
//    /**
//     * Генерация ключа для партиционирования (например, по userId)
//     * В реальном проекте извлекай userId из orderPayload
//     */
//    private String generateKey(Object orderPayload) {
//        // Пример для демонстрации - в реальности парсишь объект
//        if (orderPayload instanceof orderservice.domain.entity.OrderEntity order) {
//            return String.valueOf(order.getCustomerId());
//        }
//        return null; // null = Kafka сама выберет партицию
//    }
//
//    /**
//     * Отправка с кастомными заголовками (например, для трейсинга)
//     */
//    public CompletableFuture<SendResult<String, Object>> sendOrderWithHeaders(
//            Object orderPayload,
//            String traceId) {
//
//        ProducerRecord<String, Object> record = new ProducerRecord<>(
//                KafkaConstants.ORDERS_TOPIC,
//                null, // ключ
//                orderPayload
//        );
//
//        // Добавляем кастомные заголовки
//        record.headers().add("trace-id", traceId.getBytes());
//        record.headers().add("source", "order-service".getBytes());
//
//        return kafkaTemplate.send(record).completable();
//    }
//}
