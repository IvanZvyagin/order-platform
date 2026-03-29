package orderservice.domain.service.orders;

import orderservice.api.dto.OrderCreatedEvent;
import orderservice.domain.entity.OrderEntity;

public interface OrderStatusPersistenceService {
    OrderEntity saveOrderWithOutbox(OrderEntity order, OrderCreatedEvent event);
}
