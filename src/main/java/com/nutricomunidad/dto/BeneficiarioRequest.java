package com.nutricomunidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record BeneficiarioRequest(
        @NotBlank(message = "El campo nombreBeneficiario es obligatorio")
        @Size(max = 100, message = "nombreBeneficiario no debe superar 100 caracteres") String nombreBeneficiario,
        @NotBlank(message = "El campo ap es obligatorio")
        @Size(max = 100, message = "ap no debe superar 100 caracteres") String ap,
        @Size(max = 100, message = "am no debe superar 100 caracteres") String am,
        @Pattern(regexp = Validaciones.TELEFONO, message = Validaciones.TELEFONO_MENSAJE) String telefono,
        @Email(message = "El correo no es válido")
        @Size(max = 150, message = "correo no debe superar 150 caracteres") String correo,
        @NotNull(message = "El campo numeroIntegrantes es obligatorio")
        @Positive(message = "numeroIntegrantes debe ser mayor que cero") Integer numeroIntegrantes,
        @Size(max = 150, message = "calle no debe superar 150 caracteres") String calle,
        @Size(max = 20, message = "numeroExterior no debe superar 20 caracteres") String numeroExterior,
        @Size(max = 20, message = "numeroInterior no debe superar 20 caracteres") String numeroInterior,
        @Size(max = 100, message = "colonia no debe superar 100 caracteres") String colonia,
        @Pattern(regexp = Validaciones.CODIGO_POSTAL, message = Validaciones.CODIGO_POSTAL_MENSAJE) String codigoPostal,
        @Size(max = 100, message = "municipio no debe superar 100 caracteres") String municipio,
        @Size(max = 100, message = "estadoBeneficiario no debe superar 100 caracteres") String estadoBeneficiario) {
}
