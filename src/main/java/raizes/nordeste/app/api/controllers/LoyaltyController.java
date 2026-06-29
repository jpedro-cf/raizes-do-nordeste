package raizes.nordeste.app.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raizes.nordeste.app.application.dto.LoyaltyResponse;
import raizes.nordeste.app.application.services.LoyaltyService;

@RestController
@RequestMapping("loyalty")
@RequiredArgsConstructor
public class LoyaltyController {
    private final LoyaltyService loyaltyService;

    @GetMapping
    public ResponseEntity<LoyaltyResponse> history() {
        return ResponseEntity.ok(loyaltyService.findAllByUser());
    }
}