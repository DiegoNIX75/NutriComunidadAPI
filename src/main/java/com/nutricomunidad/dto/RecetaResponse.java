package com.nutricomunidad.dto;

import com.nutricomunidad.models.DetalleReceta;
import com.nutricomunidad.models.Receta;
import java.math.BigDecimal;
import java.util.List;

/** Receta con sus ingredientes. */
public record RecetaResponse(
        int idReceta,
        String nombreReceta,
        String descripcion,
        String tiempoPreparacion,
        String proporciones,
        BigDecimal costoEstimado,
        List<DetalleReceta> ingredientes) {

    public static RecetaResponse de(Receta r, List<DetalleReceta> ingredientes) {
        return new RecetaResponse(r.idReceta(), r.nombreReceta(), r.descripcion(), r.tiempoPreparacion(),
                r.proporciones(), r.costoEstimado(), ingredientes);
    }
}
