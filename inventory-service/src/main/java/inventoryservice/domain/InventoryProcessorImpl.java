package inventoryservice.domain;

import http.inventory.ProductRequestDto;
import http.inventory.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryProcessorImpl implements InventoryProcessor {
    private final ProductJpaRepository productJpaRepository;
    private final ProductMapper productMapper;
    public List<ProductResponseDto> getAllProducts(){
        log.info("Fetching all products");
        List<ProductEntity> products = productJpaRepository.findAll();
        return productMapper.toResponseList(products);
    }

    public ProductResponseDto getProductById(Long id){
        log.info("Fetching product with id: {}", id);
        ProductEntity product = productJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toProductResponseDto(product);
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto request){
        log.info("Creating product: {}", request.productName());

        if(productJpaRepository.existsByProductName(request.productName())){
            throw new RuntimeException("Product with name " + request.productName() + " already exist");
        }

        ProductEntity product = productMapper.toEntity(request);
        ProductEntity saveProduct = productJpaRepository.save(product);
        log.info("Product crate with id: {}", saveProduct.getId());
        return productMapper.toProductResponseDto(saveProduct);
    }

    @Transactional
    public void deleteProduct(Long id){
        log.info("Deleting product with id: {}", id);
        if(!productJpaRepository.existsById(id)){
            throw new RuntimeException("Product not found");
        }
        productJpaRepository.deleteById(id);
        log.info("Product deleted");
    }
//todo добавить проверку на наличие товара в БД
    public ProductEntity getProductByIdForGrpc(Long productId){
        return productJpaRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));
    }

//    public BigDecimal getPriceWithDiscount(){
//        return entity.getPrice().subtract(entity.getDiscount());
//    }
//
//    public boolean isAvailable(int requestQuantity){
//        return entity.getQuantity() >= requestQuantity;
//    }
}
