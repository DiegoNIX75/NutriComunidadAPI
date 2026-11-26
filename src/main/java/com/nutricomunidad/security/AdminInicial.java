package com.nutricomunidad.security;

import com.nutricomunidad.repositories.RolRepository;
import com.nutricomunidad.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Crea el primer Administrador a partir de ADMIN_CORREO y ADMIN_PASSWORD (si ambos existen
 * y el correo aún no está registrado). Así no se guardan credenciales en el repositorio.
 */
@Component
public class AdminInicial implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInicial.class);

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final BCryptPasswordEncoder encoder;
    private final String correo;
    private final String contrasena;

    public AdminInicial(UsuarioRepository usuarios, RolRepository roles, BCryptPasswordEncoder encoder,
                        @Value("${app.admin.correo:}") String correo,
                        @Value("${app.admin.contrasena:}") String contrasena) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.encoder = encoder;
        this.correo = correo == null ? "" : correo.trim().toLowerCase();
        this.contrasena = contrasena == null ? "" : contrasena;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (correo.isEmpty() || contrasena.isEmpty()) {
            return;
        }
        if (contrasena.length() < 8 || contrasena.length() > 72) {
            log.warn("ADMIN_PASSWORD debe tener entre 8 y 72 caracteres; no se creó el administrador inicial");
            return;
        }
        try {
            if (usuarios.buscarPorCorreo(correo).isPresent()) {
                return;
            }
            roles.buscarPorNombre(Roles.ADMIN).ifPresentOrElse(
                    rol -> {
                        usuarios.crear("Administrador", "NutriComunidad", null, correo,
                                encoder.encode(contrasena), null, rol.idRol());
                        log.info("Administrador inicial creado ({})", correo);
                    },
                    () -> log.warn("No existe el rol {}; ejecuta sql/schema.sql antes de crear el administrador", Roles.ADMIN));
        } catch (DataAccessException e) {
            log.warn("No se pudo crear el administrador inicial: {}", e.getMostSpecificCause().getMessage());
        }
    }
}
