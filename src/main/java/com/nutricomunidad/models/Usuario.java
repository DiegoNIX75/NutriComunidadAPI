package com.nutricomunidad.models;

import java.time.LocalDate;

/** Usuario del sistema. contrasenaUsuario contiene el hash BCrypt y nunca se expone en la API. */
public record Usuario(
        int idUsuario,
        String nombreUsuario,
        String apUsuario,
        String amUsuario,
        String correoUsuario,
        String contrasenaUsuario,
        String telefonoUsuario,
        LocalDate fechaRegistro,
        String estado,
        int idRol,
        String nombreRol) {

    public static final String ESTADO_ACTIVO = "Activo";

    public boolean estaActivo() {
        return ESTADO_ACTIVO.equalsIgnoreCase(estado);
    }
}
