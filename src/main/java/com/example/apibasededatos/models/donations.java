package com.example.apibasededatos.models;

public record donations(
        int id,
        String donationDate,
        Double totalQuantity,
        String status,
        String observations
) {
}
