package com.nutricomunidad.security;

import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.errors.Errores;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Valida el JWT en cada petición protegida, deja el usuario en el atributo "usuario"
 * del request y verifica los roles indicados con {@link RequiereRol}.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    public static final String ATRIBUTO_USUARIO = "usuario";

    private final JwtService jwt;

    public AuthInterceptor(JwtService jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod metodo)) {
            return true;
        }

        UsuarioToken usuario = jwt.leerToken(request.getHeader("Authorization"));
        request.setAttribute(ATRIBUTO_USUARIO, usuario);

        RequiereRol requiere = metodo.getMethodAnnotation(RequiereRol.class);
        if (requiere == null) {
            requiere = metodo.getBeanType().getAnnotation(RequiereRol.class);
        }
        if (requiere != null && Arrays.stream(requiere.value()).noneMatch(r -> r.equalsIgnoreCase(usuario.rol()))) {
            throw Errores.prohibido("No tienes permiso para realizar esta acción");
        }
        return true;
    }
}
