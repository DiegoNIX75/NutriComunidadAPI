package com.example.apibasededatos.models;

public record beneficiaries(
        int id,
        String name,
        String lastName,
        String phoneNumber,
        String mail,
        int familyMembers,
        String street,
        String exteriorNumber,
        String interiorNumber,
        String neighborhood,
        String postalCode,
        String municipality,
        String state



) {
}
