package com.nutricomunidad.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/** fecha_actualizacion la asigna el servidor en cada alta o modificación. */
public record InventarioRequest(
        @NotNull(message = "El campo cantidadDisponible es obligatorio")
        @PositiveOrZero(message = "cantidadDisponible no puede ser negativa")
        @Digits(integer = 10, fraction = 2, message = "cantidadDisponible admite hasta 10 enteros y 2 decimales")
        BigDecimal cantidadDisponible,
        @Size(max = 30, message = "estadoInventario no debe superar 30 caracteres") String estadoInventario,
        @NotNull(message = "El campo idDonacion es obligatorio") Integer idDonacion,
        @NotNull(message = "El campo idOrganizacion es obligatorio") Integer idOrganizacion) {
}
