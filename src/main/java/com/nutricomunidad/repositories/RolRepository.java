package com.nutricomunidad.repositories;

import com.nutricomunidad.dto.RolRequest;
import com.nutricomunidad.models.Rol;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RolRepository extends BaseRepository<Rol> {

    public RolRepository(JdbcTemplate jdbc) {
        super(jdbc, "roles", "id_rol", (rs, fila) -> new Rol(
                rs.getInt("id_rol"),
                rs.getString("nombre_rol"),
                rs.getString("descripcion"),
                rs.getString("estado")));
    }

    public Optional<Rol> buscarPorNombre(String nombre) {
        return listar(new Filtro().igualSinMayusculas("nombre_rol", nombre)).stream().findFirst();
    }

    public int crear(RolRequest r, String estado) {
        return insertar("""
                INSERT INTO roles (nombre_rol, descripcion, estado)
                VALUES (?, ?, ?) RETURNING id_rol
                """, r.nombreRol().trim(), r.descripcion(), estado);
    }

    public void actualizar(int id, RolRequest r, String estado) {
        jdbc.update("UPDATE roles SET nombre_rol = ?, descripcion = ?, estado = ? WHERE id_rol = ?",
                r.nombreRol().trim(), r.descripcion(), estado, id);
    }
}
