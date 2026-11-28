package com.nutricomunidad.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Registro público. rol es opcional (Beneficiario, Comercio u Organizacion; por defecto Beneficiario).
 * Opcionalmente puede incluir los datos de la entidad del rol (beneficiario, comercio u organizacion);
 * se guardan en la misma transacción y quedan vinculados a la nueva cuenta.
 */
public record RegistroRequest(
        @NotBlank(message = "El campo nombreUsuario es obligatorio")
        @Size(max = 100, message = "nombreUsuario no debe superar 100 caracteres") String nombreUsuario,
        @NotBlank(message = "El campo apUsuario es obligatorio")
        @Size(max = 100, message = "apUsuario no debe superar 100 caracteres") String apUsuario,
        @Size(max = 100, message = "amUsuario no debe superar 100 caracteres") String amUsuario,
        @NotBlank(message = "El campo correoUsuario es obligatorio")
        @Email(message = "El correo no es válido")
        @Size(max = 150, message = "correoUsuario no debe superar 150 caracteres") String correoUsuario,
        @NotBlank(message = "El campo contrasena es obligatorio")
        @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres") String contrasena,
        @Pattern(regexp = Validaciones.TELEFONO, message = Validaciones.TELEFONO_MENSAJE) String telefonoUsuario,
        String rol,
        @Valid BeneficiarioRequest beneficiario,
        @Valid ComercioRequest comercio,
        @Valid OrganizacionRequest organizacion) {

    /** Registro sólo de la cuenta, sin datos de la entidad. */
    public RegistroRequest(String nombreUsuario, String apUsuario, String amUsuario, String correoUsuario,
                           String contrasena, String telefonoUsuario, String rol) {
        this(nombreUsuario, apUsuario, amUsuario, correoUsuario, contrasena, telefonoUsuario, rol, null, null, null);
    }
}
