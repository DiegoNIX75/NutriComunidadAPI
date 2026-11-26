package com.nutricomunidad.models;

public record Preferencia(
        int idPreferencias,
        String tipoPreferencia,
        String descripcionPreferencia,
        String observacionesPreferencia,
        int idBeneficiario) {
}
