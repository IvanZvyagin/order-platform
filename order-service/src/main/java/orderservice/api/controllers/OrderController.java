package orderservice.api.controllers;

import http.order.OrderCreateRequestDto;
import http.order.OrderDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import orderservice.domain.secuity.UserDetailsImpl;
import orderservice.domain.service.orders.OrderProcessor;
import orderservice.domain.utils.OrderEntityMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderEntityMapper orderMapper;
    private final OrderProcessor orderProcessor;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderDto> createOder(
            @Valid
            @RequestBody OrderCreateRequestDto request,
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        log.info("Created order request {}", request);
        return ResponseEntity.ok(orderProcessor.createOrder(request));
    }


    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<OrderDto> getOneById(
            @PathVariable Long id
    ) {
        log.info("Retrieving order with id {}", id);
        var found = orderProcessor.getOrderOrThrow(id);
        return ResponseEntity.ok(orderMapper.toOrderDto(found));
    }
}
