package com.nutricomunidad.models;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Inventario(
        int idInventario,
        BigDecimal cantidadDisponible,
        LocalDate fechaActualizacion,
        String estadoInventario,
        int idDonacion,
        int idOrganizacion) {
}
