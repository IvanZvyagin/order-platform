package notificationservice.service.impl;

import notificationservice.entity.NsOrderEntity;
import exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import notificationservice.repository.NsOrderRepository;
import notificationservice.service.NsOrderService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NsOrderServiceImpl implements NsOrderService {
    private final NsOrderRepository nsOrderRepository;

    public Flux<NsOrderEntity> getAllOrders(){
        return nsOrderRepository.findAll()
                .doOnSubscribe(s -> log.debug("Fetching all orders"))
                .doOnError(e -> log.error("Error fetching orders", e));
    }

    public Mono<NsOrderEntity> getOrderByOrderId(Long orderId){
        return nsOrderRepository.findByOrderId(orderId)
                .switchIfEmpty(Mono.error(new OrderServiceException(OrderServiceException.ErrorCode.ORDER_NOT_FOUND)))
                .doOnSubscribe(s -> log.debug("Fetching order by orderId: {}", orderId));
    }

    public Flux<NsOrderEntity> getOrdersByUserId(Long userId){
        return nsOrderRepository.findAllByUserId(userId)
                .doOnSubscribe(s -> log.debug("Fetching orders for user: {}", userId));
    }

    public Flux<NsOrderEntity> getOrdersInPeriod(LocalDateTime start, LocalDateTime end){
        return nsOrderRepository.findOrdersInPeriod(start, end)
                .doOnSubscribe(s -> log.debug("Fetching orders from {} to {}", start,end));
    }

    public Flux<NsOrderRepository.UserStats> getUserStatistics(){
        return nsOrderRepository.getUserStatistic()
                .doOnSubscribe(s -> log.debug("Fetching user statistic"));
    }

    public Flux<NsOrderRepository.ProductStats> getProductStatistic(){
        return nsOrderRepository.getProductsStatistic()
                .doOnSubscribe(s -> log.debug("Fetching product statistic"));
    }
}
