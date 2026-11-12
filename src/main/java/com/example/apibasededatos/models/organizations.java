package com.example.apibasededatos.models;

public record organizations(
        int id, String nameORG,String typeOfORG,String manager, int PhoneNumber,String mail,
        String street,
        String exteriorNumber,
        String interiorNumber,
        String neighborhood,
        String postalCode,
        String municipality,
        String state
        ) {
}
