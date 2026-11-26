package com.nutricomunidad.dto;

/** Datos del usuario autenticado extraídos del JWT. */
public record UsuarioToken(int id, String nombre, String rol) {
}
