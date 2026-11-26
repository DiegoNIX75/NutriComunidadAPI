package com.nutricomunidad.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** Actualiza los datos generales de una receta; los ingredientes tienen su propio endpoint. */
public record RecetaActualizacionRequest(
        @NotBlank(message = "El campo nombreReceta es obligatorio")
        @Size(max = 150, message = "nombreReceta no debe superar 150 caracteres") String nombreReceta,
        @Size(max = 5000, message = "descripcion no debe superar 5000 caracteres") String descripcion,
        @Size(max = 50, message = "tiempoPreparacion no debe superar 50 caracteres") String tiempoPreparacion,
        @Size(max = 50, message = "proporciones no debe superar 50 caracteres") String proporciones,
        @PositiveOrZero(message = "costoEstimado no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "costoEstimado admite hasta 8 enteros y 2 decimales") BigDecimal costoEstimado) {
}
