package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotNull;

/** Registra una alergia del beneficiario indicado en la ruta. */
public record AlergiaRequest(
        @NotNull(message = "El campo idPreferencias es obligatorio") Integer idPreferencias,
        @NotNull(message = "El campo idAlimento es obligatorio") Integer idAlimento) {
}
