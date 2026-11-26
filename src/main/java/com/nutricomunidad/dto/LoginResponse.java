package com.nutricomunidad.dto;

public record LoginResponse(String token, String tipo, long expiraEnHoras, UsuarioResponse usuario) {
}
