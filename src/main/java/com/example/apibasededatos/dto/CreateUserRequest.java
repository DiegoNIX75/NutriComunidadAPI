package com.example.apibasededatos.dto;

public record CreateUserRequest(
        String name,
        String mail,
        String password
) {
}
