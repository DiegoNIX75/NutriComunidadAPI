package com.nutricomunidad.security;

import java.util.Arrays;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Registra el interceptor de autenticación y la configuración de CORS. */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String ORIGEN_POR_DEFECTO = "http://localhost:*";

    private final AuthInterceptor authInterceptor;
    private final String[] origenes;

    public WebConfig(AuthInterceptor authInterceptor, @Value("${app.cors.origenes:}") String origenes) {
        this.authInterceptor = authInterceptor;
        String[] lista = Arrays.stream(origenes.split(","))
                .map(String::trim)
                .filter(o -> !o.isEmpty())
                .toArray(String[]::new);
        this.origenes = lista.length == 0 ? new String[] {ORIGEN_POR_DEFECTO} : lista;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns("/api/auth/login", "/api/auth/registro", "/api/health", "/api/health/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(origenes)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
