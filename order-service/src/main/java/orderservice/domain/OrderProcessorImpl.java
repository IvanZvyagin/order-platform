package orderservice.api;

import lombok.RequiredArgsConstructor;
import orderservice.domain.OrderEntity;
import orderservice.domain.OrderJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class OrderProcessorImpl implements OrderProcessor {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public OrderEntity createOrder(OrderEntity orderEntity) {
        return orderJpaRepository.save(orderEntity);
    }

    @Override
    public OrderEntity getOrderOrThrow(Long id) {
        Optional<OrderEntity> orderEntityOptional = orderJpaRepository.findById(id);
        return orderEntityOptional
                .orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }
}
