package inventoryservice.grpc;

import inventoryservice.domain.ProductEntity;
import inventoryservice.domain.ProductJpaRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@GrpcService
@RequiredArgsConstructor
@ComponentScan
public class InventoryGrpcService extends InventoryServiceGrpc.InventoryServiceImplBase {
    private final ProductJpaRepository productRepository;

    @Override
    public void checkAndGet(CheckRequest request, StreamObserver<ProductInfo> responseObserver) {
        try {
            Long productId = request.getProductId();
            Integer requestedQuantity = request.getQuantity();

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> Status.NOT_FOUND
                            .withDescription("Product not found with id: " + productId)
                            .asRuntimeException());

            if (product.getQuantity() < requestedQuantity) {
                throw Status.FAILED_PRECONDITION
                        .withDescription("Insufficient stock for product " + productId +
                                ". Available: " + product.getQuantity() + ", requested: " + requestedQuantity)
                        .asRuntimeException();
            }

            double priceWithDiscount = product.getPrice().doubleValue()
                    * (1 - product.getDiscount().doubleValue() / 100);

            ProductInfo response = ProductInfo.newBuilder()
                    .setId(product.getId())
                    .setProductName(product.getProductName())
                    .setAvailableQuantity(product.getQuantity())
                    .setPrice(product.getPrice().doubleValue())
                    .setDiscount(product.getDiscount().doubleValue())
                    .setPriceWithDiscount(priceWithDiscount)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    @Transactional
    public void reserve(ReserveRequest request, StreamObserver<ReserveResponse> responseObserver) {
        try {
            Long productId = request.getProductId();
            Integer quantity = request.getQuantity();

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> Status.NOT_FOUND
                            .withDescription("Product not found with id: " + productId)
                            .asRuntimeException());
            if (product.getQuantity() < quantity) {
                throw Status.FAILED_PRECONDITION
                        .withDescription("Insufficient stock for product: " + productId)
                        .asRuntimeException();
            }
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            ReserveResponse response = ReserveResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Reserved " + quantity + " units of product " + productId)
                    .setPrice(product.getPrice().doubleValue())
                    .setDiscount(product.getDiscount().doubleValue())
                    .setProductName(product.getProductName())
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }

    @Override
    @Transactional
    public void release(ReleaseRequest request, StreamObserver<ReleaseResponse> responseObserver) {
        try {
            Long productId = request.getProductId();
            Integer quantity = request.getQuantity();

            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> Status.NOT_FOUND
                            .withDescription("Product not found with ID " + productId)
                            .asRuntimeException());
            product.setQuantity(product.getQuantity() + quantity);
            productRepository.save(product);

            ReleaseResponse response = ReleaseResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Released " + quantity + " units of product " + productId)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e);
        }
    }
}
