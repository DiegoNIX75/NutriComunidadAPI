package com.nutricomunidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OrganizacionRequest(
        @NotBlank(message = "El campo nombreOrganizacion es obligatorio")
        @Size(max = 150, message = "nombreOrganizacion no debe superar 150 caracteres") String nombreOrganizacion,
        @Size(max = 100, message = "tipoOrganizacion no debe superar 100 caracteres") String tipoOrganizacion,
        @Size(max = 150, message = "responsableOrganizacion no debe superar 150 caracteres") String responsableOrganizacion,
        @Pattern(regexp = Validaciones.TELEFONO, message = Validaciones.TELEFONO_MENSAJE) String telefonoOrganizacion,
        @Email(message = "El correo no es válido")
        @Size(max = 150, message = "correoOrganizacion no debe superar 150 caracteres") String correoOrganizacion,
        @Size(max = 150, message = "calleOrganizacion no debe superar 150 caracteres") String calleOrganizacion,
        @Size(max = 20, message = "numeroExteriorOrganizacion no debe superar 20 caracteres") String numeroExteriorOrganizacion,
        @Size(max = 20, message = "numeroInteriorOrganizacion no debe superar 20 caracteres") String numeroInteriorOrganizacion,
        @Size(max = 100, message = "coloniaOrganizacion no debe superar 100 caracteres") String coloniaOrganizacion,
        @Pattern(regexp = Validaciones.CODIGO_POSTAL, message = Validaciones.CODIGO_POSTAL_MENSAJE) String codigoPostalOrganizacion,
        @Size(max = 100, message = "municipioOrganizacion no debe superar 100 caracteres") String municipioOrganizacion,
        @Size(max = 100, message = "estadoOrganizacion no debe superar 100 caracteres") String estadoOrganizacion) {
}
