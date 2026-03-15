package orderservice.domain.utils;

import http.order.OrderItemDto;
import http.order.OrderItemRequestDto;
import orderservice.domain.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderItemMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "discount", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    OrderItemEntity toEntity(OrderItemRequestDto request);

    OrderItemDto toItemDto(OrderItemEntity entity);
}
