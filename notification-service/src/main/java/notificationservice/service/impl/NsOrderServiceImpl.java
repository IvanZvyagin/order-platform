package notificationservice.service.impl;

import exception.ErrorCode;
import exception.OrderServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import notificationservice.entity.NsOrderEntity;
import notificationservice.repository.NsOrderRepository;
import notificationservice.service.NsOrderService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NsOrderServiceImpl implements NsOrderService {
    private final NsOrderRepository nsOrderRepository;

    public Flux<NsOrderEntity> getAllOrders() {
        return nsOrderRepository.findAll()
                .doOnSubscribe(s -> log.debug("Fetching all orders"))
                .doOnError(e -> log.error("Error fetching orders", e));
    }

    public Mono<NsOrderEntity> getOrderByOrderId(Long orderId) {
        return nsOrderRepository.findByOrderId(orderId)
                .switchIfEmpty(Mono.error(new OrderServiceException(ErrorCode.ORDER_NOT_FOUND)))
                .doOnSubscribe(s -> log.debug("Fetching order by orderId: {}", orderId));
    }

    public Flux<NsOrderEntity> getOrdersByUserId(Long userId) {
        return nsOrderRepository.findAllByUserId(userId)
                .doOnSubscribe(s -> log.debug("Fetching orders for user: {}", userId));
    }
}
