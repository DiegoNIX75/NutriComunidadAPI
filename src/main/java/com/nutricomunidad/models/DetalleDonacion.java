package com.nutricomunidad.models;

import java.math.BigDecimal;

/** Detalle de una donación. nombreAlimento y nombreComercio se obtienen por JOIN. */
public record DetalleDonacion(
        int idDetalleDonacion,
        BigDecimal cantidadDetalleDonacion,
        String unidadMedidaDetalleDonacion,
        int idAlimento,
        int idComercio,
        int idDonacion,
        String nombreAlimento,
        String nombreComercio) {
}
