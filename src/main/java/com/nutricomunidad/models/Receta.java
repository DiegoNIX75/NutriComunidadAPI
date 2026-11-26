package com.nutricomunidad.models;

import java.math.BigDecimal;

public record Receta(
        int idReceta,
        String nombreReceta,
        String descripcion,
        String tiempoPreparacion,
        String proporciones,
        BigDecimal costoEstimado) {
}
