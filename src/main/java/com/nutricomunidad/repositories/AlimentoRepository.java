package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.AlimentoRequest;
import com.nutricomunidad.models.Alimento;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AlimentoRepository extends BaseRepository<Alimento> {

    public AlimentoRepository(JdbcTemplate jdbc) {
        super(jdbc, "alimentos", "id_alimento", (rs, fila) -> new Alimento(
                rs.getInt("id_alimento"),
                rs.getString("nombre_alimento"),
                rs.getString("descripcion"),
                rs.getString("unidad_medida"),
                decimal(rs, "calorias_por_porcion"),
                decimal(rs, "proteinas"),
                decimal(rs, "carbohidratos"),
                decimal(rs, "grasas"),
                rs.getString("estado_alimento"),
                rs.getInt("id_categoria"),
                rs.getString("nombre_categoria")));
    }

    @Override
    protected String select() {
        return """
                SELECT a.*, c.nombre_categoria
                FROM alimentos a
                JOIN categorias_alimentos c ON c.id_categoria = a.id_categoria
                """;
    }

    public int crear(AlimentoRequest r, String estado) {
        return insertar("""
                INSERT INTO alimentos (nombre_alimento, descripcion, unidad_medida, calorias_por_porcion, proteinas,
                    carbohidratos, grasas, estado_alimento, id_categoria)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_alimento
                """, valores(r, estado));
    }

    public void actualizar(int id, AlimentoRequest r, String estado) {
        jdbc.update("""
                UPDATE alimentos SET nombre_alimento = ?, descripcion = ?, unidad_medida = ?, calorias_por_porcion = ?,
                    proteinas = ?, carbohidratos = ?, grasas = ?, estado_alimento = ?, id_categoria = ?
                WHERE id_alimento = ?
                """, conId(valores(r, estado), id));
    }

    private static Object[] valores(AlimentoRequest r, String estado) {
        return new Object[] {limpiar(r.nombreAlimento()), limpiar(r.descripcion()), limpiar(r.unidadMedida()),
                r.caloriasPorPorcion(), r.proteinas(), r.carbohidratos(), r.grasas(), estado, r.idCategoria()};
    }
}
