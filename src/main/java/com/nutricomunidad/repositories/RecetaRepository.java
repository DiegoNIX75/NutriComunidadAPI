package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.models.Receta;
import java.math.BigDecimal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RecetaRepository extends BaseRepository<Receta> {

    public RecetaRepository(JdbcTemplate jdbc) {
        super(jdbc, "recetas", "id_receta", (rs, fila) -> new Receta(
                rs.getInt("id_receta"),
                rs.getString("nombre_receta"),
                rs.getString("descripcion"),
                rs.getString("tiempo_preparacion"),
                rs.getString("proporciones"),
                decimal(rs, "costo_estimado")));
    }

    public int crear(String nombre, String descripcion, String tiempo, String proporciones, BigDecimal costo) {
        return insertar("""
                INSERT INTO recetas (nombre_receta, descripcion, tiempo_preparacion, proporciones, costo_estimado)
                VALUES (?, ?, ?, ?, ?) RETURNING id_receta
                """, limpiar(nombre), limpiar(descripcion), limpiar(tiempo), limpiar(proporciones), costo);
    }

    public void actualizar(int id, String nombre, String descripcion, String tiempo, String proporciones, BigDecimal costo) {
        jdbc.update("""
                UPDATE recetas SET nombre_receta = ?, descripcion = ?, tiempo_preparacion = ?, proporciones = ?,
                    costo_estimado = ?
                WHERE id_receta = ?
                """, limpiar(nombre), limpiar(descripcion), limpiar(tiempo), limpiar(proporciones), costo, id);
    }
}
