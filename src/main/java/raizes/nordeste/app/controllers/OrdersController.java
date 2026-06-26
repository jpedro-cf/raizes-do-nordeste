package raizes.nordeste.app.controllers;

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
import raizes.nordeste.app.application.dto.UpdateOrderStatusRequest;
import raizes.nordeste.app.application.services.OrdersService;
import raizes.nordeste.app.domain.entities.Order;

@RestController
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService ordersService;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody @Valid CreateOrderRequest request) {
        return ResponseEntity.ok(ordersService.create(request));
    }

    @GetMapping("/unit/{unitId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Order>> findAllByUnit(
            @PathVariable Long unitId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(ordersService.findAllByUnit(unitId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> findById(@PathVariable Long id) {
        return ResponseEntity.ok(ordersService.findById(id));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateStatus(@PathVariable Long id,
                                                      @RequestBody @Valid UpdateOrderStatusRequest request) {
        return ResponseEntity.ok(ordersService.updateStatus(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        ordersService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}