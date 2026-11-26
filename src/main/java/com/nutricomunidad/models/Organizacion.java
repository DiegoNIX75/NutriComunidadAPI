package com.nutricomunidad.models;

public record Organizacion(
        int idOrganizacion,
        String nombreOrganizacion,
        String tipoOrganizacion,
        String responsableOrganizacion,
        String telefonoOrganizacion,
        String correoOrganizacion,
        String calleOrganizacion,
        String numeroExteriorOrganizacion,
        String numeroInteriorOrganizacion,
        String coloniaOrganizacion,
        String codigoPostalOrganizacion,
        String municipioOrganizacion,
        String estadoOrganizacion,
        Integer idUsuario) {
}
