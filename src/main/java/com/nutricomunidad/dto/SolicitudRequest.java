package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** fechaSolicitud es opcional: si no se envía se usa la fecha actual. */
public record SolicitudRequest(
        @PastOrPresent(message = "fechaSolicitud no puede ser una fecha futura") LocalDate fechaSolicitud,
        @NotBlank(message = "El campo motivo es obligatorio")
        @Size(max = 255, message = "motivo no debe superar 255 caracteres") String motivo,
        @Positive(message = "numeroIntegrantes debe ser mayor que cero") Integer numeroIntegrantes,
        @Size(max = 20, message = "prioridad no debe superar 20 caracteres") String prioridad,
        @Size(max = 30, message = "estadoSolicitud no debe superar 30 caracteres") String estadoSolicitud,
        @NotNull(message = "El campo idOrganizacion es obligatorio") Integer idOrganizacion,
        @NotNull(message = "El campo idBeneficiario es obligatorio") Integer idBeneficiario) {
}
