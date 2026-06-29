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
import raizes.nordeste.app.application.dto.CreateStockItemRequest;
import raizes.nordeste.app.application.dto.StockItemResponse;
import raizes.nordeste.app.application.dto.UpdateStockItemRequest;
import raizes.nordeste.app.application.services.StockService;
import raizes.nordeste.app.domain.entities.StockItem;

@RestController
@RequestMapping("stock")
@RequiredArgsConstructor
public class StockController {
    private final StockService stockService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StockItem> create(@RequestBody @Valid CreateStockItemRequest request) {
        return ResponseEntity.ok(stockService.create(request));
    }

    @GetMapping("{unitId}")
    public ResponseEntity<Page<StockItemResponse>> findAllByUnit(
            @PathVariable Long unitId,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(stockService.findAllByUnit(unitId, pageable));
    }

    @PatchMapping("{stockItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<StockItem> update(@PathVariable Long stockItemId,
                                                      @RequestBody @Valid UpdateStockItemRequest request) {
        return ResponseEntity.ok(stockService.update(stockItemId, request));
    }

    @DeleteMapping("{stockItemId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long stockItemId) {
        stockService.delete(stockItemId);
        return ResponseEntity.noContent().build();
    }
}