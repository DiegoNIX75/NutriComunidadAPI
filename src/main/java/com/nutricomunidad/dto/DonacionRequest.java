package com.nutricomunidad.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;

/** Alta de una donación junto con sus detalles (se guarda en una sola transacción). */
public record DonacionRequest(
        @PastOrPresent(message = "fechaDonacion no puede ser una fecha futura") LocalDate fechaDonacion,
        @Size(max = 30, message = "estadoDonacion no debe superar 30 caracteres") String estadoDonacion,
        @Size(max = 255, message = "observacionesDonacion no debe superar 255 caracteres") String observacionesDonacion,
        @NotEmpty(message = "La donación debe incluir al menos un detalle")
        List<@Valid DetalleDonacionRequest> detalles) {
}
