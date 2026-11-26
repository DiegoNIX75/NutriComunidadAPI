package com.nutricomunidad.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Lote(
        int idLote,
        String numeroLote,
        LocalDate fechaRecepcion,
        LocalDate fechaCaducidad,
        LocalDate fechaConsumo,
        BigDecimal cantidadInicial,
        BigDecimal cantidadActual,
        int idInventario) {
}
