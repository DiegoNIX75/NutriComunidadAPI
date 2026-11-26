package com.nutricomunidad.repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * Construye cláusulas WHERE dinámicas de forma segura: los nombres de columna los define
 * siempre el repositorio y los valores del usuario viajan como parámetros (?).
 * Los valores nulos o vacíos se ignoran.
 */
public final class Filtro {

    private final List<String> condiciones = new ArrayList<>();
    private final List<Object> parametros = new ArrayList<>();

    public Filtro igual(String columna, Object valor) {
        if (tieneValor(valor)) {
            condiciones.add(columna + " = ?");
            parametros.add(valor);
        }
        return this;
    }

    public Filtro igualSinMayusculas(String columna, String valor) {
        if (tieneValor(valor)) {
            condiciones.add("LOWER(" + columna + ") = LOWER(?)");
            parametros.add(valor.trim());
        }
        return this;
    }

    /** Búsqueda parcial (ILIKE) en una o varias columnas, unidas con OR. */
    public Filtro contiene(String valor, String... columnas) {
        if (tieneValor(valor)) {
            String patron = "%" + escaparLike(valor.trim()) + "%";
            List<String> partes = new ArrayList<>();
            for (String columna : columnas) {
                partes.add(columna + " ILIKE ?");
                parametros.add(patron);
            }
            condiciones.add("(" + String.join(" OR ", partes) + ")");
        }
        return this;
    }

    public Filtro menorOIgual(String columna, Object valor) {
        if (tieneValor(valor)) {
            condiciones.add(columna + " <= ?");
            parametros.add(valor);
        }
        return this;
    }

    /**
     * Condición SQL fija definida por el repositorio (por ejemplo un EXISTS) con un único
     * parámetro "?". Se omite si el valor es nulo o vacío.
     */
    public Filtro condicion(String sqlConUnParametro, Object valor) {
        if (tieneValor(valor)) {
            condiciones.add(sqlConUnParametro);
            parametros.add(valor);
        }
        return this;
    }

    public String where() {
        return condiciones.isEmpty() ? "" : " WHERE " + String.join(" AND ", condiciones);
    }

    public Object[] parametros() {
        return parametros.toArray();
    }

    private static boolean tieneValor(Object valor) {
        return valor != null && !(valor instanceof String texto && texto.isBlank());
    }

    private static String escaparLike(String texto) {
        return texto.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }
}
