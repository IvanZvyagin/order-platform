package orderservice.domain;

import orderservice.api.OrderCreateRequestDto;
import orderservice.api.OrderDto;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderEntityMapper {
    OrderEntity toEntity(OrderCreateRequestDto request);

    @AfterMapping
    default void linkOrderItemEntities(@MappingTarget OrderEntity orderEntity) {
        orderEntity
                .getItems()
                .forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));
    }

    OrderDto toOrderDto(OrderEntity orderEntity);
}