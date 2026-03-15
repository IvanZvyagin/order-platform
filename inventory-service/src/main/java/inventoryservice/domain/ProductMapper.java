package inventoryservice.domain;

import http.inventory.ProductRequestDto;
import http.inventory.ProductResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    ProductEntity toEntity(ProductRequestDto productRequestDto);

    ProductResponseDto toProductResponseDto(ProductEntity entity);

    List<ProductResponseDto> toResponseList(List<ProductEntity> products);
}