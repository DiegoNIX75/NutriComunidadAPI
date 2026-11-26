package com.nutricomunidad.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/** Alta de usuarios por un Administrador (puede asignar cualquier rol). */
public record UsuarioRequest(
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
        @NotNull(message = "El campo idRol es obligatorio") Integer idRol) {
}
