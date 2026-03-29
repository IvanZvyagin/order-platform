package inventoryservice.domain;

import http.inventory.ProductRequestDto;
import http.inventory.ProductResponseDto;

import java.util.List;

public interface InventoryProcessor {

    List<ProductResponseDto> getAllProducts();

    ProductResponseDto getProductById(Long id);

    ProductResponseDto createProduct(ProductRequestDto request);

    void deleteProduct(Long id);

    ProductEntity getProductByIdForGrpc(Long productId);

//    BigDecimal getPriceWithDiscount();

//    boolean isAvailable(int requestQuantity);


}
