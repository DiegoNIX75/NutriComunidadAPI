package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record EntregaRequest(
        @NotNull(message = "El campo fechaEntrega es obligatorio") LocalDate fechaEntrega,
        @NotBlank(message = "El campo responsable es obligatorio")
        @Size(max = 150, message = "responsable no debe superar 150 caracteres") String responsable,
        @Size(max = 30, message = "estado no debe superar 30 caracteres") String estado,
        @Size(max = 255, message = "observaciones no debe superar 255 caracteres") String observaciones,
        @NotNull(message = "El campo idSolicitud es obligatorio") Integer idSolicitud) {
}
