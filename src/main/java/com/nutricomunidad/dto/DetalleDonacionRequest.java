package com.nutricomunidad.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record DetalleDonacionRequest(
        @NotNull(message = "El campo cantidadDetalleDonacion es obligatorio")
        @Positive(message = "cantidadDetalleDonacion debe ser mayor que cero")
        @Digits(integer = 10, fraction = 2, message = "cantidadDetalleDonacion admite hasta 10 enteros y 2 decimales")
        BigDecimal cantidadDetalleDonacion,
        @Size(max = 20, message = "unidadMedidaDetalleDonacion no debe superar 20 caracteres") String unidadMedidaDetalleDonacion,
        @NotNull(message = "El campo idAlimento es obligatorio") Integer idAlimento,
        @NotNull(message = "El campo idComercio es obligatorio") Integer idComercio) {
}
