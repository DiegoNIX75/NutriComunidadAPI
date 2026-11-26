package com.nutricomunidad.security;

import java.util.Set;

/**
 * Nombres de los roles del sistema. Deben coincidir con los registros de la tabla roles
 * (ver sql/schema.sql).
 */
public final class Roles {

    public static final String ADMIN = "Administrador";
    public static final String BENEFICIARIO = "Beneficiario";
    public static final String COMERCIO = "Comercio";
    public static final String ORGANIZACION = "Organizacion";

    /** Rol asignado cuando el registro no indica uno. */
    public static final String POR_DEFECTO = BENEFICIARIO;

    /** Roles que un usuario puede elegir al registrarse por sí mismo. */
    public static final Set<String> REGISTRABLES = Set.of(BENEFICIARIO, COMERCIO, ORGANIZACION);

    /** Roles de los que depende la autorización: no se pueden renombrar ni eliminar. */
    public static final Set<String> SISTEMA = Set.of(ADMIN, BENEFICIARIO, COMERCIO, ORGANIZACION);

    private Roles() {
    }

    public static boolean esDelSistema(String rol) {
        return SISTEMA.stream().anyMatch(r -> r.equalsIgnoreCase(rol));
    }

    /** Devuelve el nombre canónico de un rol registrable, o null si no es registrable. */
    public static String registrable(String rol) {
        return REGISTRABLES.stream().filter(r -> r.equalsIgnoreCase(rol)).findFirst().orElse(null);
    }
}
