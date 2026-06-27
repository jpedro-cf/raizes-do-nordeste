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
import raizes.nordeste.app.application.dto.PaymentResponse;
import raizes.nordeste.app.application.dto.ProcessPaymentRequest;
import raizes.nordeste.app.application.services.PaymentsService;

@RestController
@RequestMapping("payments")
@RequiredArgsConstructor
public class PaymentsController {
    private final PaymentsService paymentsService;

    @PostMapping
    public ResponseEntity<PaymentResponse> process(@RequestBody @Valid ProcessPaymentRequest request) {
        return ResponseEntity.ok(paymentsService.process(request));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<PaymentResponse>> getAll(
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(paymentsService.getAll(pageable));
    }
}