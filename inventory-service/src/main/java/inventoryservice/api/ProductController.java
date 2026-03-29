package inventoryservice.api;

import http.inventory.ProductRequestDto;
import http.inventory.ProductResponseDto;
import inventoryservice.domain.InventoryProcessor;
import inventoryservice.domain.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final InventoryProcessor inventoryProcessor;


    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<ProductResponseDto> products = inventoryProcessor.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id){
       ProductResponseDto product = inventoryProcessor.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductRequestDto request){
        ProductResponseDto product = inventoryProcessor.createProduct(request);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id){
        inventoryProcessor.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
