package com.example.apibasededatos.dto;

public record CreateDonationRequest(
        String donationDate,
        Double totalQuantity,
        String status,
        String observations
) {
}
