package com.nutricomunidad.models;

public record Beneficiario(
        int idBeneficiario,
        String nombreBeneficiario,
        String ap,
        String am,
        String telefono,
        String correo,
        Integer numeroIntegrantes,
        String calle,
        String numeroExterior,
        String numeroInterior,
        String colonia,
        String codigoPostal,
        String municipio,
        String estadoBeneficiario,
        Integer idUsuario) {
}
