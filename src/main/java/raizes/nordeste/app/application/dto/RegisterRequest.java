package raizes.nordeste.app.application.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(@NotBlank String name,
                              @NotBlank @Email String email,
                              @NotBlank String password,
                              @NotNull Boolean lgpdConsent,
                              String phone,
                              Integer age) {
}
