package com.nutricomunidad.models;

import java.time.LocalDate;

public record Entrega(
        int idEntrega,
        LocalDate fechaEntrega,
        String responsable,
        String estado,
        String observaciones,
        int idSolicitud) {
}
