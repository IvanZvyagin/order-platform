package orderservice.domain.utils;

import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import orderservice.domain.entity.OrderEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

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
