package com.nutricomunidad.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaAlimentoRequest(
        @NotBlank(message = "El campo nombreCategoria es obligatorio")
        @Size(max = 100, message = "nombreCategoria no debe superar 100 caracteres") String nombreCategoria,
        @Size(max = 255, message = "descripcionCategoria no debe superar 255 caracteres") String descripcionCategoria,
        @Size(max = 20, message = "estadoCategoria no debe superar 20 caracteres") String estadoCategoria) {
}
