package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CambioContrasenaRequest(
        @NotBlank(message = "El campo contrasenaActual es obligatorio") String contrasenaActual,
        @NotBlank(message = "El campo contrasenaNueva es obligatorio")
        @Size(min = 8, max = 72, message = "La contraseña nueva debe tener entre 8 y 72 caracteres") String contrasenaNueva) {
}
