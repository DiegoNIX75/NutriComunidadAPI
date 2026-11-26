package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RolRequest(
        @NotBlank(message = "El campo nombreRol es obligatorio")
        @Size(max = 50, message = "nombreRol no debe superar 50 caracteres") String nombreRol,
        @Size(max = 255, message = "descripcion no debe superar 255 caracteres") String descripcion,
        @Size(max = 20, message = "estado no debe superar 20 caracteres") String estado) {
}
