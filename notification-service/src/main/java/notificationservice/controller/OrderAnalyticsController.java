package notificationservice.controller;

import notificationservice.entity.NsOrderEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import notificationservice.repository.NsOrderRepository;
import notificationservice.service.NsOrderService;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderAnalyticsController {
    private final NsOrderService nsOrderService;
    private final NsOrderRepository nsOrderRepository;

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<NsOrderEntity> getAllOrdersStream(){
        log.info("Starting SEE streams for all orders");
        return nsOrderService.getAllOrders()
                .take(Duration.ofSeconds(30))
                .doOnCancel(() -> log.info("SEE stream canceled by client"));
    }

    @GetMapping("/all/json")
    public Flux<NsOrderEntity> getAllOrdersJson(){
        return nsOrderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Mono<NsOrderEntity> getOrderByOrderId(@PathVariable Long orderId){
        return nsOrderService.getOrderByOrderId(orderId);
    }

    @GetMapping("/user/{userId}")
    public Flux<NsOrderEntity> getOrdersByUserId(@PathVariable Long userId){
        return nsOrderService.getOrdersByUserId(userId);
    }

    @GetMapping("/period")
    public Flux<NsOrderEntity> getOrdersInPeriod(
            @RequestParam String start,
            @RequestParam String end){
        LocalDateTime startDate = LocalDateTime.parse(start);
        LocalDateTime endDate = LocalDateTime.parse(end);
        return nsOrderService.getOrdersInPeriod(startDate, endDate);
    }

    @GetMapping("/stats/users")
    public Flux<NsOrderRepository.UserStats> getUserStatistics(){
        return nsOrderService.getUserStatistics();
    }

    @GetMapping("/stats/products")
    public Flux<NsOrderRepository.ProductStats> getProductStatistic(){
        return nsOrderService.getProductStatistic();
    }

    @PostMapping("/debug")
    public Mono<NsOrderEntity> saveDebugOrder(@RequestBody NsOrderEntity entity){
        return nsOrderRepository.save(entity);
    }
}
