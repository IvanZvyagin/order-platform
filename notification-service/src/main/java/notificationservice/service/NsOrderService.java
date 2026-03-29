package notificationservice.service;

import notificationservice.entity.NsOrderEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import notificationservice.repository.NsOrderRepository;

import java.time.LocalDateTime;

public interface NsOrderService {
    Flux<NsOrderEntity> getAllOrders();
    Mono<NsOrderEntity> getOrderByOrderId(Long orderId);
    Flux<NsOrderEntity> getOrdersByUserId(Long userId);
}
