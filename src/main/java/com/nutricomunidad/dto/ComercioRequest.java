package com.nutricomunidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ComercioRequest(
        @NotBlank(message = "El campo nombreComercio es obligatorio")
        @Size(max = 150, message = "nombreComercio no debe superar 150 caracteres") String nombreComercio,
        @Size(max = 200, message = "razonSocial no debe superar 200 caracteres") String razonSocial,
        @Size(max = 100, message = "tipoComercio no debe superar 100 caracteres") String tipoComercio,
        @Pattern(regexp = Validaciones.TELEFONO, message = Validaciones.TELEFONO_MENSAJE) String telefonoComercio,
        @Email(message = "El correo no es válido")
        @Size(max = 150, message = "correoComercio no debe superar 150 caracteres") String correoComercio,
        @Size(max = 150, message = "calleComercio no debe superar 150 caracteres") String calleComercio,
        @Size(max = 20, message = "numeroExteriorComercio no debe superar 20 caracteres") String numeroExteriorComercio,
        @Size(max = 20, message = "numeroInteriorComercio no debe superar 20 caracteres") String numeroInteriorComercio,
        @Size(max = 100, message = "coloniaComercio no debe superar 100 caracteres") String coloniaComercio,
        @Pattern(regexp = Validaciones.CODIGO_POSTAL, message = Validaciones.CODIGO_POSTAL_MENSAJE) String codigoPostalComercio,
        @Size(max = 100, message = "municipioComercio no debe superar 100 caracteres") String municipioComercio,
        @Size(max = 100, message = "estadoComercio no debe superar 100 caracteres") String estadoComercio) {
}
