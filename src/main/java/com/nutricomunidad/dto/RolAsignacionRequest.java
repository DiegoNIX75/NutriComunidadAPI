package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotNull;

public record RolAsignacionRequest(@NotNull(message = "El campo idRol es obligatorio") Integer idRol) {
}
