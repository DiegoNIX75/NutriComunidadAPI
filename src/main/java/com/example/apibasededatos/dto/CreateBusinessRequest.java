package com.example.apibasededatos.dto;

public record CreateBusinessRequest(
        String businessName,
        String legalName,
        String typeOfBusiness,
        String phoneNumber,
        String mail,
        String street,
        String exteriorNumber,
        String interiorNumber,
        String neighborhood,
        String postalCode,
        String municipality,
        String state
) {
}
