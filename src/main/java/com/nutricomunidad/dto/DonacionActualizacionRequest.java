package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Actualiza los datos generales de una donación; los detalles se gestionan en su propio endpoint. */
public record DonacionActualizacionRequest(
        @NotNull(message = "El campo fechaDonacion es obligatorio")
        @PastOrPresent(message = "fechaDonacion no puede ser una fecha futura") LocalDate fechaDonacion,
        @Size(max = 30, message = "estadoDonacion no debe superar 30 caracteres") String estadoDonacion,
        @Size(max = 255, message = "observacionesDonacion no debe superar 255 caracteres") String observacionesDonacion) {
}
