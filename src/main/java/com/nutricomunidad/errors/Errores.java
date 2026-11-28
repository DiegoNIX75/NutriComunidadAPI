package com.nutricomunidad.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/** Fábrica de excepciones HTTP con mensajes consistentes. */
public final class Errores {

    private Errores() {
    }

    public static ResponseStatusException noEncontrado(String recurso, Object id) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, recurso + " con id " + id + " no encontrado");
    }

    public static ResponseStatusException noEncontrado(String mensaje) {
        return new ResponseStatusException(HttpStatus.NOT_FOUND, mensaje);
    }

    public static ResponseStatusException peticionInvalida(String mensaje) {
        return new ResponseStatusException(HttpStatus.BAD_REQUEST, mensaje);
    }

    public static ResponseStatusException conflicto(String mensaje) {
        return new ResponseStatusException(HttpStatus.CONFLICT, mensaje);
    }

    public static ResponseStatusException noAutorizado(String mensaje) {
        return new ResponseStatusException(HttpStatus.UNAUTHORIZED, mensaje);
    }

    public static ResponseStatusException prohibido(String mensaje) {
        return new ResponseStatusException(HttpStatus.FORBIDDEN, mensaje);
    }

    /** Lanza 400 si la entidad referenciada en el cuerpo de la petición no existe. */
    public static void validarReferencia(boolean existe, String recurso, Object id) {
        if (!existe) {
            throw peticionInvalida(recurso + " con id " + id + " no existe");
        }
    }
}
