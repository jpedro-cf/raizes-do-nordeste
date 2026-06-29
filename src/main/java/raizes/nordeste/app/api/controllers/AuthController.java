package raizes.nordeste.app.api.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import raizes.nordeste.app.application.dto.AuthResponse;
import raizes.nordeste.app.application.dto.LoginRequest;
import raizes.nordeste.app.application.dto.RegisterRequest;
import raizes.nordeste.app.application.services.AuthService;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest dto) {
        var response = authService.register(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest dto) {
        var response = authService.login(dto);
        return ResponseEntity.ok(response);
    }
}