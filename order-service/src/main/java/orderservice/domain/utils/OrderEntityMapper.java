package orderservice.domain.utils;

import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import http.order.OrderStatus;
import orderservice.domain.entity.OrderEntity;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING,
uses = OrderItemMapper.class)
public interface OrderEntityMapper {

    @Mapping(target = "orderId", ignore = true)
    @Mapping(target = "customerId", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "items", ignore = true)
    OrderEntity toEntity(OrderCreateRequestDto request);

    @Mapping(target = "items", source = "items")
    OrderDto toOrderDto(OrderEntity orderEntity);
}
