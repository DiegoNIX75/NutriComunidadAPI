package com.nutricomunidad.dto;

import com.nutricomunidad.models.DetalleDonacion;
import com.nutricomunidad.models.Donacion;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Donación con sus detalles. */
public record DonacionResponse(
        int idDonacion,
        LocalDate fechaDonacion,
        BigDecimal cantidadTotal,
        String estadoDonacion,
        String observacionesDonacion,
        List<DetalleDonacion> detalles) {

    public static DonacionResponse de(Donacion d, List<DetalleDonacion> detalles) {
        return new DonacionResponse(d.idDonacion(), d.fechaDonacion(), d.cantidadTotal(), d.estadoDonacion(),
                d.observacionesDonacion(), detalles);
    }
}
