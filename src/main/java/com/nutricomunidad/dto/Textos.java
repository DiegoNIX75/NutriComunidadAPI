package com.nutricomunidad.dto;

/** Normalización de textos recibidos en las peticiones. */
public final class Textos {

    private Textos() {
    }

    /** Recorta espacios y convierte cadenas vacías en null. */
    public static String limpiar(String valor) {
        if (valor == null) {
            return null;
        }
        String recortado = valor.trim();
        return recortado.isEmpty() ? null : recortado;
    }

    /** Recorta espacios y usa el valor por defecto si el texto viene vacío. */
    public static String oPorDefecto(String valor, String porDefecto) {
        String limpio = limpiar(valor);
        return limpio == null ? porDefecto : limpio;
    }

    public static String correo(String valor) {
        String limpio = limpiar(valor);
        return limpio == null ? null : limpio.toLowerCase();
    }
}
