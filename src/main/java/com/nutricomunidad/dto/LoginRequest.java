package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El campo correo es obligatorio") String correo,
        @NotBlank(message = "El campo contrasena es obligatorio") String contrasena) {
}
