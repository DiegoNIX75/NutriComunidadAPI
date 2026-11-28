package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.DetalleRecetaRequest;
import com.nutricomunidad.models.DetalleReceta;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DetalleRecetaRepository extends BaseRepository<DetalleReceta> {

    public DetalleRecetaRepository(JdbcTemplate jdbc) {
        super(jdbc, "detalle_recetas", "id_detalle_receta", (rs, fila) -> new DetalleReceta(
                rs.getInt("id_detalle_receta"),
                decimal(rs, "cantidad"),
                rs.getString("unidad_medida"),
                rs.getInt("id_alimento"),
                rs.getInt("id_receta"),
                rs.getString("nombre_alimento")));
    }

    @Override
    protected String select() {
        return """
                SELECT d.*, a.nombre_alimento
                FROM detalle_recetas d
                JOIN alimentos a ON a.id_alimento = d.id_alimento
                """;
    }

    public List<DetalleReceta> listarPorReceta(int idReceta) {
        return listar(new Filtro().igual("d.id_receta", idReceta));
    }

    public int crear(int idReceta, DetalleRecetaRequest r) {
        return insertar("""
                INSERT INTO detalle_recetas (cantidad, unidad_medida, id_alimento, id_receta)
                VALUES (?, ?, ?, ?) RETURNING id_detalle_receta
                """, r.cantidad(), limpiar(r.unidadMedida()), r.idAlimento(), idReceta);
    }

    public void actualizar(int id, DetalleRecetaRequest r) {
        jdbc.update("""
                UPDATE detalle_recetas SET cantidad = ?, unidad_medida = ?, id_alimento = ?
                WHERE id_detalle_receta = ?
                """, r.cantidad(), limpiar(r.unidadMedida()), r.idAlimento(), id);
    }
}
