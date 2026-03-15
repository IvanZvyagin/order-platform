package orderservice.domain.grpc;

import inventoryservice.grpc.*;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InventoryGrpcClient {

    @GrpcClient("inventory-service")
    private InventoryServiceGrpc.InventoryServiceBlockingStub inventoryStub;
    /**
     * Проверить наличие товара и получить его полную информацию.
     *
     * @param productId ID товара
     * @param quantity  запрашиваемое количество
     * @return ProductInfo с данными товара
     */
    public ProductInfo checkAndGet(Long productId, Integer quantity){
        CheckRequest request = CheckRequest.newBuilder()
                .setProductId(productId)
                .setQuantity(quantity)
                .build();
        try {
            return inventoryStub.checkAndGet(request);
        }catch (StatusRuntimeException e){
            log.error("gRPC error while checking product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to check product", e);
        }

    }

    /**
     * Зарезервировать товар (уменьшить остаток).
     * @param productId ID товара
     * @param quantity  количество для резервирования
     * @param sagaId уникальный идентификатор саги
     * @return информация о товаре(цена, название, скидка)
     */

    public ReserveResponse reserve(Long productId, Integer quantity, UUID sagaId){
            ReserveRequest request = ReserveRequest.newBuilder()
                    .setProductId(productId)
                    .setQuantity(quantity)
                    .setSagaId(sagaId.toString())
                    .build();
        try {
            ReserveResponse response = inventoryStub.reserve(request);
            if(!response.getSuccess()){
                throw new RuntimeException("Reserve failed: " + response.getMessage());
            }
            log.info("Reserved {} units of product {}", quantity, productId);
            return  response;
        }catch (StatusRuntimeException e){
            log.error("gRPC error while reserving product {}: {}", productId, e.getMessage());
            throw new RuntimeException("Failed to reserve product", e);
        }
    }

    public void release (Long productId, Integer quantity, UUID sagaId){

            ReleaseRequest request = ReleaseRequest.newBuilder()
                    .setProductId(productId)
                    .setQuantity(quantity)
                    .setSagaId(sagaId.toString())
                    .build();
        try {
            ReleaseResponse response = inventoryStub.release(request);
            if(!response.getSuccess()) {
                log.error("Release failed for product {}: {}", productId, response.getMessage());
            }else{
                log.info("Released {} units of product {}", quantity, productId);
            }
        }catch (StatusRuntimeException e){
            log.error("gRPC error while realising product {}: {}", productId, e.getMessage());
        }
    }
}
