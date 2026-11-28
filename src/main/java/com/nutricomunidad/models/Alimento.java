package com.nutricomunidad.models;

import java.math.BigDecimal;

/** Alimento del catálogo. nombreCategoria se obtiene por JOIN para facilitar su uso en el frontend. */
public record Alimento(
        int idAlimento,
        String nombreAlimento,
        String descripcion,
        String unidadMedida,
        BigDecimal caloriasPorPorcion,
        BigDecimal proteinas,
        BigDecimal carbohidratos,
        BigDecimal grasas,
        String estadoAlimento,
        int idCategoria,
        String nombreCategoria) {
}
