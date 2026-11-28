package com.nutricomunidad.models;

import java.time.LocalDate;

public record Solicitud(
        int idSolicitud,
        LocalDate fechaSolicitud,
        String motivo,
        Integer numeroIntegrantes,
        String prioridad,
        String estadoSolicitud,
        int idOrganizacion,
        int idBeneficiario) {
}
