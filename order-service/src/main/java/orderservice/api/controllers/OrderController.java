package orderservice.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.api.dto.OrderCreateRequestDto;
import orderservice.api.dto.OrderDto;
import orderservice.domain.utils.OrderEntityMapper;
import orderservice.domain.service.OrderProcessor;
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
