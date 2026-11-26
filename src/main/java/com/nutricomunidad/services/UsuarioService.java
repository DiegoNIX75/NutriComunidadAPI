package com.nutricomunidad.services;

import com.nutricomunidad.dto.Textos;
import com.nutricomunidad.dto.UsuarioActualizacionRequest;
import com.nutricomunidad.dto.UsuarioRequest;
import com.nutricomunidad.dto.UsuarioResponse;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.RolRepository;
import com.nutricomunidad.repositories.UsuarioRepository;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/** Administración de usuarios (sólo Administrador). */
@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;
    private final RolRepository roles;
    private final BCryptPasswordEncoder encoder;

    public UsuarioService(UsuarioRepository usuarios, RolRepository roles, BCryptPasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.roles = roles;
        this.encoder = encoder;
    }

    public List<UsuarioResponse> listar(String estado, Integer idRol, String buscar) {
        Filtro filtro = new Filtro()
                .igualSinMayusculas("u.estado", estado)
                .igual("u.id_rol", idRol)
                .contiene(buscar, "u.nombre_usuario", "u.ap_usuario", "u.am_usuario", "u.correo_usuario");
        return usuarios.listar(filtro).stream().map(UsuarioResponse::de).toList();
    }

    public UsuarioResponse obtener(int id) {
        return usuarios.buscarPorId(id).map(UsuarioResponse::de)
                .orElseThrow(() -> Errores.noEncontrado("Usuario", id));
    }

    public UsuarioResponse crear(UsuarioRequest r) {
        Errores.validarReferencia(roles.existe(r.idRol()), "El rol", r.idRol());
        String correo = Textos.correo(r.correoUsuario());
        if (usuarios.existeCorreo(correo, null)) {
            throw Errores.conflicto("Ya existe una cuenta con ese correo");
        }
        int id = usuarios.crear(r.nombreUsuario().trim(), r.apUsuario().trim(), Textos.limpiar(r.amUsuario()),
                correo, encoder.encode(r.contrasena()), Textos.limpiar(r.telefonoUsuario()), r.idRol());
        return obtener(id);
    }

    public UsuarioResponse actualizar(int id, UsuarioActualizacionRequest r) {
        asegurarExiste(id);
        String correo = Textos.correo(r.correoUsuario());
        if (usuarios.existeCorreo(correo, id)) {
            throw Errores.conflicto("Ya existe una cuenta con ese correo");
        }
        usuarios.actualizarDatos(id, r.nombreUsuario().trim(), r.apUsuario().trim(), Textos.limpiar(r.amUsuario()),
                correo, Textos.limpiar(r.telefonoUsuario()));
        return obtener(id);
    }

    public UsuarioResponse cambiarEstado(int id, String estado, int idActor) {
        asegurarExiste(id);
        if (id == idActor) {
            throw Errores.conflicto("No puedes cambiar el estado de tu propia cuenta");
        }
        usuarios.actualizarEstado(id, estado.trim());
        return obtener(id);
    }

    public UsuarioResponse cambiarRol(int id, int idRol, int idActor) {
        asegurarExiste(id);
        if (id == idActor) {
            throw Errores.conflicto("No puedes cambiar el rol de tu propia cuenta");
        }
        Errores.validarReferencia(roles.existe(idRol), "El rol", idRol);
        usuarios.actualizarRol(id, idRol);
        return obtener(id);
    }

    public void eliminar(int id, int idActor) {
        if (id == idActor) {
            throw Errores.conflicto("No puedes eliminar tu propia cuenta");
        }
        if (!usuarios.eliminar(id)) {
            throw Errores.noEncontrado("Usuario", id);
        }
    }

    private void asegurarExiste(int id) {
        if (!usuarios.existe(id)) {
            throw Errores.noEncontrado("Usuario", id);
        }
    }
}
