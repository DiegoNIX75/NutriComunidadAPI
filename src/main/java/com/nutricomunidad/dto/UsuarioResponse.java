package com.nutricomunidad.dto;

import com.nutricomunidad.models.Usuario;
import java.time.LocalDate;

/** Datos públicos del usuario (sin contraseña ni hash). */
public record UsuarioResponse(
        int idUsuario,
        String nombreUsuario,
        String apUsuario,
        String amUsuario,
        String correoUsuario,
        String telefonoUsuario,
        LocalDate fechaRegistro,
        String estado,
        int idRol,
        String nombreRol) {

    public static UsuarioResponse de(Usuario u) {
        return new UsuarioResponse(u.idUsuario(), u.nombreUsuario(), u.apUsuario(), u.amUsuario(),
                u.correoUsuario(), u.telefonoUsuario(), u.fechaRegistro(), u.estado(), u.idRol(), u.nombreRol());
    }
}
