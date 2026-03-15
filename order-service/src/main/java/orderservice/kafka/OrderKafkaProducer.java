//package orderservice.kafka;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import kafka.KafkaConstants;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import orderservice.domain.entity.OrderEntity;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.KafkaHeaders;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.support.MessageBuilder;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class OrderKafkaProducer {
//    private final KafkaTemplate<String, String> kafkaTemplate;
//    private final ObjectMapper objectMapper;
//
//    public void sendOrder(OrderEntity order){
//        try {
//            String orderJson = objectMapper.writeValueAsString(order);
//
//            Message<String> message = MessageBuilder
//                    .withPayload(orderJson)
//                    .setHeader(KafkaHeaders.TOPIC, KafkaConstants.ORDERS_TOPIC)
//                    .build();
//
//            kafkaTemplate.send(message);
//
//            log.info("Order sent to kafka: orderId={}, userId={}", order.getId(),order.getCustomerId());
//        }catch (JsonProcessingException e){
//            log.error("Failed to serialize order to JSON", e);
//            throw new RuntimeException("Failed to serialize order", e);
//        }
//    }
//}
