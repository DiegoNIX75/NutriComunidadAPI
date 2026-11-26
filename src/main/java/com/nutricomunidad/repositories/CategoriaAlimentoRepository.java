package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.CategoriaAlimentoRequest;
import com.nutricomunidad.models.CategoriaAlimento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CategoriaAlimentoRepository extends BaseRepository<CategoriaAlimento> {

    public CategoriaAlimentoRepository(JdbcTemplate jdbc) {
        super(jdbc, "categorias_alimentos", "id_categoria", (rs, fila) -> new CategoriaAlimento(
                rs.getInt("id_categoria"),
                rs.getString("nombre_categoria"),
                rs.getString("descripcion_categoria"),
                rs.getString("estado_categoria")));
    }

    public int crear(CategoriaAlimentoRequest r, String estado) {
        return insertar("""
                INSERT INTO categorias_alimentos (nombre_categoria, descripcion_categoria, estado_categoria)
                VALUES (?, ?, ?) RETURNING id_categoria
                """, limpiar(r.nombreCategoria()), limpiar(r.descripcionCategoria()), estado);
    }

    public void actualizar(int id, CategoriaAlimentoRequest r, String estado) {
        jdbc.update("""
                UPDATE categorias_alimentos SET nombre_categoria = ?, descripcion_categoria = ?, estado_categoria = ?
                WHERE id_categoria = ?
                """, limpiar(r.nombreCategoria()), limpiar(r.descripcionCategoria()), estado, id);
    }
}
