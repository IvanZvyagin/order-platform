package service;

import entity.NsOrderEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.NsOrderRepository;

import java.time.LocalDateTime;
import java.util.UUID;

public interface NsOrderService {
    Flux<NsOrderEntity> getAllOrders();
    Mono<NsOrderEntity> getOrderByOrderId(Long orderId);
    Flux<NsOrderEntity> getOrdersByUserId(Long userId);
    Flux<NsOrderEntity> getOrdersInPeriod(LocalDateTime start, LocalDateTime end);
    Flux<NsOrderRepository.UserStats> getUserStatistics();
    Flux<NsOrderRepository.ProductStats> getProductStatistic();
}
