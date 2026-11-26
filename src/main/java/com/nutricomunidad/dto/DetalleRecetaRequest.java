package com.nutricomunidad.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record DetalleRecetaRequest(
        @NotNull(message = "El campo cantidad es obligatorio")
        @Positive(message = "cantidad debe ser mayor que cero")
        @Digits(integer = 8, fraction = 2, message = "cantidad admite hasta 8 enteros y 2 decimales") BigDecimal cantidad,
        @Size(max = 20, message = "unidadMedida no debe superar 20 caracteres") String unidadMedida,
        @NotNull(message = "El campo idAlimento es obligatorio") Integer idAlimento) {
}
