package com.nutricomunidad.models;

public record Comercio(
        int idComercio,
        String nombreComercio,
        String razonSocial,
        String tipoComercio,
        String telefonoComercio,
        String correoComercio,
        String calleComercio,
        String numeroExteriorComercio,
        String numeroInteriorComercio,
        String coloniaComercio,
        String codigoPostalComercio,
        String municipioComercio,
        String estadoComercio,
        Integer idUsuario) {
}
