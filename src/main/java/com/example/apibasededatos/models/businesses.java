package com.example.apibasededatos.models;

public record businesses(
        String BusinessName, String LegalName,String typeOfBusiness,int phoneNumber,String mail,
        String street,
        String exteriorNumber,
        String interiorNumber,
        String neighborhood,
        String postalCode,
        String municipality,
        String state
) {
}
