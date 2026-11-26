package com.nutricomunidad.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record AlimentoRequest(
        @NotBlank(message = "El campo nombreAlimento es obligatorio")
        @Size(max = 100, message = "nombreAlimento no debe superar 100 caracteres") String nombreAlimento,
        @Size(max = 255, message = "descripcion no debe superar 255 caracteres") String descripcion,
        @Size(max = 20, message = "unidadMedida no debe superar 20 caracteres") String unidadMedida,
        @PositiveOrZero(message = "caloriasPorPorcion no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "caloriasPorPorcion admite hasta 8 enteros y 2 decimales") BigDecimal caloriasPorPorcion,
        @PositiveOrZero(message = "proteinas no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "proteinas admite hasta 8 enteros y 2 decimales") BigDecimal proteinas,
        @PositiveOrZero(message = "carbohidratos no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "carbohidratos admite hasta 8 enteros y 2 decimales") BigDecimal carbohidratos,
        @PositiveOrZero(message = "grasas no puede ser negativo")
        @Digits(integer = 8, fraction = 2, message = "grasas admite hasta 8 enteros y 2 decimales") BigDecimal grasas,
        @Size(max = 20, message = "estadoAlimento no debe superar 20 caracteres") String estadoAlimento,
        @NotNull(message = "El campo idCategoria es obligatorio") Integer idCategoria) {
}
