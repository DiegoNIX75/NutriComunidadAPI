package com.nutricomunidad.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoteRequest(
        @NotBlank(message = "El campo numeroLote es obligatorio")
        @Size(max = 50, message = "numeroLote no debe superar 50 caracteres") String numeroLote,
        @NotNull(message = "El campo fechaRecepcion es obligatorio")
        @PastOrPresent(message = "fechaRecepcion no puede ser una fecha futura") LocalDate fechaRecepcion,
        LocalDate fechaCaducidad,
        LocalDate fechaConsumo,
        @NotNull(message = "El campo cantidadInicial es obligatorio")
        @Positive(message = "cantidadInicial debe ser mayor que cero")
        @Digits(integer = 10, fraction = 2, message = "cantidadInicial admite hasta 10 enteros y 2 decimales")
        BigDecimal cantidadInicial,
        @NotNull(message = "El campo cantidadActual es obligatorio")
        @PositiveOrZero(message = "cantidadActual no puede ser negativa")
        @Digits(integer = 10, fraction = 2, message = "cantidadActual admite hasta 10 enteros y 2 decimales")
        BigDecimal cantidadActual,
        @NotNull(message = "El campo idInventario es obligatorio") Integer idInventario) {
}
