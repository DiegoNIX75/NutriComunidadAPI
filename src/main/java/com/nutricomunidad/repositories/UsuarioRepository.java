package com.nutricomunidad.repositories;

import com.nutricomunidad.models.Usuario;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepository extends BaseRepository<Usuario> {

    public UsuarioRepository(JdbcTemplate jdbc) {
        super(jdbc, "usuarios", "id_usuario", (rs, fila) -> new Usuario(
                rs.getInt("id_usuario"),
                rs.getString("nombre_usuario"),
                rs.getString("ap_usuario"),
                rs.getString("am_usuario"),
                rs.getString("correo_usuario"),
                rs.getString("contrasena_usuario"),
                rs.getString("telefono_usuario"),
                fecha(rs, "fecha_registro"),
                rs.getString("estado"),
                rs.getInt("id_rol"),
                rs.getString("nombre_rol")));
    }

    @Override
    protected String select() {
        return """
                SELECT u.*, r.nombre_rol
                FROM usuarios u
                JOIN roles r ON r.id_rol = u.id_rol
                """;
    }

    public Optional<Usuario> buscarPorCorreo(String correo) {
        return listar(new Filtro().igualSinMayusculas("u.correo_usuario", correo)).stream().findFirst();
    }

    /** Indica si el correo ya pertenece a otro usuario (excluirId puede ser null). */
    public boolean existeCorreo(String correo, Integer excluirId) {
        Integer total = jdbc.queryForObject(
                "SELECT COUNT(*) FROM usuarios WHERE LOWER(correo_usuario) = LOWER(?) AND id_usuario <> ?",
                Integer.class, correo, excluirId == null ? -1 : excluirId);
        return total != null && total > 0;
    }

    public int crear(String nombre, String ap, String am, String correo, String hash, String telefono, int idRol) {
        return insertar("""
                INSERT INTO usuarios (nombre_usuario, ap_usuario, am_usuario, correo_usuario,
                                      contrasena_usuario, telefono_usuario, id_rol)
                VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_usuario
                """, nombre, ap, am, correo, hash, telefono, idRol);
    }

    public void actualizarDatos(int id, String nombre, String ap, String am, String correo, String telefono) {
        jdbc.update("""
                UPDATE usuarios
                SET nombre_usuario = ?, ap_usuario = ?, am_usuario = ?, correo_usuario = ?, telefono_usuario = ?
                WHERE id_usuario = ?
                """, nombre, ap, am, correo, telefono, id);
    }

    public void actualizarContrasena(int id, String hash) {
        jdbc.update("UPDATE usuarios SET contrasena_usuario = ? WHERE id_usuario = ?", hash, id);
    }

    public void actualizarEstado(int id, String estado) {
        jdbc.update("UPDATE usuarios SET estado = ? WHERE id_usuario = ?", estado, id);
    }

    public void actualizarRol(int id, int idRol) {
        jdbc.update("UPDATE usuarios SET id_rol = ? WHERE id_usuario = ?", idRol, id);
    }
}
