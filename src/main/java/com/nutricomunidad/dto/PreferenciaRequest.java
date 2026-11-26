package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PreferenciaRequest(
        @NotBlank(message = "El campo tipoPreferencia es obligatorio")
        @Size(max = 100, message = "tipoPreferencia no debe superar 100 caracteres") String tipoPreferencia,
        @Size(max = 255, message = "descripcionPreferencia no debe superar 255 caracteres") String descripcionPreferencia,
        @Size(max = 255, message = "observacionesPreferencia no debe superar 255 caracteres") String observacionesPreferencia,
        @NotNull(message = "El campo idBeneficiario es obligatorio") Integer idBeneficiario) {
}
