package com.nutricomunidad.dto;

/** Expresiones regulares compartidas por los DTO. */
public final class Validaciones {

    public static final String TELEFONO = "^[0-9+()\\-\\s]{7,20}$";
    public static final String TELEFONO_MENSAJE = "El teléfono debe tener entre 7 y 20 dígitos o símbolos (+, -, paréntesis)";

    public static final String CODIGO_POSTAL = "^\\d{5}$";
    public static final String CODIGO_POSTAL_MENSAJE = "El código postal debe tener 5 dígitos";

    private Validaciones() {
    }
}
