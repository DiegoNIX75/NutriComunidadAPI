package com.nutricomunidad.models;

import java.math.BigDecimal;
import java.time.LocalDate;

/** cantidadTotal se calcula como la suma de las cantidades de sus detalles. */
public record Donacion(
        int idDonacion,
        LocalDate fechaDonacion,
        BigDecimal cantidadTotal,
        String estadoDonacion,
        String observacionesDonacion) {
}
