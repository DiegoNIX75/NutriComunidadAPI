package com.nutricomunidad.services;

import com.nutricomunidad.dto.Textos;
import com.nutricomunidad.dto.RolRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Rol;
import com.nutricomunidad.models.Usuario;
import com.nutricomunidad.repositories.RolRepository;
import com.nutricomunidad.security.Roles;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RolService {

    private final RolRepository roles;

    public RolService(RolRepository roles) {
        this.roles = roles;
    }

    public List<Rol> listar() {
        return roles.listar();
    }

    public Rol obtener(int id) {
        return roles.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Rol", id));
    }

    public Rol crear(RolRequest r) {
        validarNombreLibre(r.nombreRol(), null);
        int id = roles.crear(r, Textos.oPorDefecto(r.estado(), Usuario.ESTADO_ACTIVO));
        return obtener(id);
    }

    public Rol actualizar(int id, RolRequest r) {
        Rol actual = obtener(id);
        if (Roles.esDelSistema(actual.nombreRol()) && !actual.nombreRol().equalsIgnoreCase(r.nombreRol().trim())) {
            throw Errores.conflicto("No se puede renombrar un rol del sistema");
        }
        validarNombreLibre(r.nombreRol(), id);
        roles.actualizar(id, r, Textos.oPorDefecto(r.estado(), actual.estado()));
        return obtener(id);
    }

    public void eliminar(int id) {
        Rol actual = obtener(id);
        if (Roles.esDelSistema(actual.nombreRol())) {
            throw Errores.conflicto("No se puede eliminar un rol del sistema");
        }
        roles.eliminar(id);
    }

    private void validarNombreLibre(String nombre, Integer idActual) {
        roles.buscarPorNombre(nombre.trim())
                .filter(existente -> idActual == null || existente.idRol() != idActual)
                .ifPresent(existente -> {
                    throw Errores.conflicto("Ya existe un rol con el nombre " + existente.nombreRol());
                });
    }
}
