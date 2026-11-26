package com.nutricomunidad.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Usuario;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** Genera y valida los JWT (HMAC256). El secreto proviene de la variable JWT_SECRET. */
@Service
public class JwtService {

    private static final String EMISOR = "nutricomunidad-api";
    private static final String PREFIJO = "Bearer ";
    private static final int LONGITUD_MINIMA_SECRETO = 32;

    private final Algorithm algoritmo;
    private final JWTVerifier verificador;
    private final long horasDeVida;

    public JwtService(@Value("${jwt.secreto}") String secreto,
                      @Value("${jwt.expiracion-horas}") long horasDeVida) {
        if (secreto == null || secreto.length() < LONGITUD_MINIMA_SECRETO) {
            throw new IllegalStateException(
                    "La variable JWT_SECRET debe definirse con al menos " + LONGITUD_MINIMA_SECRETO + " caracteres");
        }
        if (horasDeVida <= 0) {
            throw new IllegalStateException("JWT_EXPIRACION_HORAS debe ser mayor que cero");
        }
        this.algoritmo = Algorithm.HMAC256(secreto);
        this.verificador = JWT.require(algoritmo).withIssuer(EMISOR).build();
        this.horasDeVida = horasDeVida;
    }

    public String crearToken(Usuario usuario) {
        Instant ahora = Instant.now();
        return JWT.create()
                .withIssuer(EMISOR)
                .withSubject(String.valueOf(usuario.idUsuario()))
                .withClaim("nombre", usuario.nombreUsuario())
                .withClaim("rol", usuario.nombreRol())
                .withIssuedAt(ahora)
                .withExpiresAt(ahora.plus(horasDeVida, ChronoUnit.HOURS))
                .sign(algoritmo);
    }

    public long getHorasDeVida() {
        return horasDeVida;
    }

    /** Lee el header Authorization ("Bearer token") y devuelve el usuario contenido en el token. */
    public UsuarioToken leerToken(String authorization) {
        if (authorization == null || !authorization.startsWith(PREFIJO)) {
            throw Errores.noAutorizado("Falta el token en el header Authorization (Bearer)");
        }
        try {
            DecodedJWT jwt = verificador.verify(authorization.substring(PREFIJO.length()).trim());
            return new UsuarioToken(
                    Integer.parseInt(jwt.getSubject()),
                    jwt.getClaim("nombre").asString(),
                    jwt.getClaim("rol").asString());
        } catch (TokenExpiredException e) {
            throw Errores.noAutorizado("El token ha expirado, inicia sesión nuevamente");
        } catch (JWTVerificationException | NumberFormatException e) {
            throw Errores.noAutorizado("Token inválido");
        }
    }
}
