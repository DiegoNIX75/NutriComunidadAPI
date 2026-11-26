package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.LoteRequest;
import com.nutricomunidad.models.Lote;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoteRepository extends BaseRepository<Lote> {

    public LoteRepository(JdbcTemplate jdbc) {
        super(jdbc, "lotes", "id_lote", (rs, fila) -> new Lote(
                rs.getInt("id_lote"),
                rs.getString("numero_lote"),
                fecha(rs, "fecha_recepcion"),
                fecha(rs, "fecha_caducidad"),
                fecha(rs, "fecha_consumo"),
                decimal(rs, "cantidad_inicial"),
                decimal(rs, "cantidad_actual"),
                rs.getInt("id_inventario")));
    }

    public int crear(LoteRequest r) {
        return insertar("""
                INSERT INTO lotes (numero_lote, fecha_recepcion, fecha_caducidad, fecha_consumo, cantidad_inicial,
                    cantidad_actual, id_inventario)
                VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_lote
                """, valores(r));
    }

    public void actualizar(int id, LoteRequest r) {
        jdbc.update("""
                UPDATE lotes SET numero_lote = ?, fecha_recepcion = ?, fecha_caducidad = ?, fecha_consumo = ?,
                    cantidad_inicial = ?, cantidad_actual = ?, id_inventario = ?
                WHERE id_lote = ?
                """, conId(valores(r), id));
    }

    private static Object[] valores(LoteRequest r) {
        return new Object[] {limpiar(r.numeroLote()), r.fechaRecepcion(), r.fechaCaducidad(), r.fechaConsumo(),
                r.cantidadInicial(), r.cantidadActual(), r.idInventario()};
    }
}
