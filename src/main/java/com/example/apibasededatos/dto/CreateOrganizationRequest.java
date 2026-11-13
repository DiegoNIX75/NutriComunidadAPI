package com.example.apibasededatos.dto;

public record CreateOrganizationRequest(
        String nameORG,
        String typeOfORG,
        String manager,
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
