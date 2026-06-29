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
import raizes.nordeste.app.application.dto.CreateUnitRequest;
import raizes.nordeste.app.application.services.UnitService;
import raizes.nordeste.app.domain.entities.Unit;

@RestController
@RequestMapping("units")
@RequiredArgsConstructor
public class UnitController {
    private final UnitService unitService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Unit> create(@RequestBody @Valid CreateUnitRequest request) {
        return ResponseEntity.ok(unitService.create(request));
    }

    @GetMapping
    public ResponseEntity<Page<Unit>> findAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(unitService.findAll(pageable));
    }

    @GetMapping("{id}")
    public ResponseEntity<Unit> findById(@PathVariable Long id) {
        return ResponseEntity.ok(unitService.findById(id));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        unitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}