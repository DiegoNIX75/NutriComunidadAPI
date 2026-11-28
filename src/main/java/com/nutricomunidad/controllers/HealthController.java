package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.Mensaje;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoints públicos para verificar que la API y la base de datos responden. */
@RestController
@RequestMapping("/api/health")
public class HealthController {

    private final JdbcTemplate jdbc;

    public HealthController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping
    public Mensaje estado() {
        return new Mensaje("API NutriComunidad en funcionamiento");
    }

    /** Si la base no responde, el manejador global devuelve 503. */
    @GetMapping("/db")
    public Mensaje baseDeDatos() {
        jdbc.queryForObject("SELECT 1", Integer.class);
        return new Mensaje("Conexión a la base de datos correcta");
    }
}
