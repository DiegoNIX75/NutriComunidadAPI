package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Cambio de estado de un registro (usuarios, solicitudes, entregas). */
public record EstadoRequest(
        @NotBlank(message = "El campo estado es obligatorio")
        @Size(max = 30, message = "El estado no debe superar 30 caracteres") String estado) {
}
