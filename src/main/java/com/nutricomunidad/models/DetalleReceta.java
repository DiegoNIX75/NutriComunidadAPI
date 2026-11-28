package com.nutricomunidad.models;

import java.math.BigDecimal;

/** Ingrediente de una receta. nombreAlimento se obtiene por JOIN. */
public record DetalleReceta(
        int idDetalleReceta,
        BigDecimal cantidad,
        String unidadMedida,
        int idAlimento,
        int idReceta,
        String nombreAlimento) {
}
