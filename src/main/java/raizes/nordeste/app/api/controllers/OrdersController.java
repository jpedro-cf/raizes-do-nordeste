package raizes.nordeste.app.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import raizes.nordeste.app.application.dto.CreateOrderRequest;
import raizes.nordeste.app.application.dto.OrderResponse;
import raizes.nordeste.app.application.dto.UpdateOrderStatusRequest;
import raizes.nordeste.app.application.services.OrdersService;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping
    public ResponseEntity<OrderResponse> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(ordersService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> findAll(
            @RequestParam(required = false)
            String canalPedido,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ordersService.findAll(canalPedido, pageable));
    }

    @GetMapping("/unit/{unitId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<OrderResponse>> findAllByUnit(
            @PathVariable Long unitId,
            @RequestParam(required = false)
            String canalPedido,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ordersService.findAllByUnit(unitId, canalPedido, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ordersService.findById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @RequestBody @Valid UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(ordersService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        ordersService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}