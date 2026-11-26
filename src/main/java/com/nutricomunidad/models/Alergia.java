package com.nutricomunidad.models;

/** Alergia de un beneficiario a un alimento, asociada a una de sus preferencias. */
public record Alergia(int idPreferencias, int idAlimento, int idBeneficiario, String nombreAlimento) {
}
