package orderservice.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.domain.OrderEntityMapper;
import orderservice.domain.OrderProcessor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderEntityMapper orderEntityMapper;
    private final OrderProcessor orderProcessor;

    @PostMapping
    public OrderDto createOder(
            @RequestBody OrderCreateRequestDto request
    ) {
        log.info("Created order request {}", request);
        var saved = orderProcessor.createOrder(request);
        return orderEntityMapper.toOrderDto(saved);
    }

    @GetMapping("/{id}")
    public OrderDto getOneById(
            @PathVariable Long id
    ) {
        log.info("Retrieving order with id {}", id);
        var found = orderProcessor.getOrderOrThrow(id);
        return orderEntityMapper.toOrderDto(found);
    }
}
