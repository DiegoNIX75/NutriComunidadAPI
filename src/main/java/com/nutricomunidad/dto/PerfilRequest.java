package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Actualización del perfil propio (el correo y el rol sólo los cambia un Administrador). */
public record PerfilRequest(
        @NotBlank(message = "El campo nombreUsuario es obligatorio")
        @Size(max = 100, message = "nombreUsuario no debe superar 100 caracteres") String nombreUsuario,
        @NotBlank(message = "El campo apUsuario es obligatorio")
        @Size(max = 100, message = "apUsuario no debe superar 100 caracteres") String apUsuario,
        @Size(max = 100, message = "amUsuario no debe superar 100 caracteres") String amUsuario,
        @Pattern(regexp = Validaciones.TELEFONO, message = Validaciones.TELEFONO_MENSAJE) String telefonoUsuario) {
}
