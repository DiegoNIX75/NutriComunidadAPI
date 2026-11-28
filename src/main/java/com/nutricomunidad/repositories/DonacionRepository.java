package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.models.Donacion;
import java.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DonacionRepository extends BaseRepository<Donacion> {

    public DonacionRepository(JdbcTemplate jdbc) {
        super(jdbc, "donaciones", "id_donacion", (rs, fila) -> new Donacion(
                rs.getInt("id_donacion"),
                fecha(rs, "fecha_donacion"),
                decimal(rs, "cantidad_total"),
                rs.getString("estado_donacion"),
                rs.getString("observaciones_donacion")));
    }

    public int crear(LocalDate fecha, String estado, String observaciones) {
        return insertar("""
                INSERT INTO donaciones (fecha_donacion, estado_donacion, observaciones_donacion)
                VALUES (?, ?, ?) RETURNING id_donacion
                """, fecha, limpiar(estado), limpiar(observaciones));
    }

    public void actualizar(int id, LocalDate fecha, String estado, String observaciones) {
        jdbc.update("""
                UPDATE donaciones SET fecha_donacion = ?, estado_donacion = ?, observaciones_donacion = ?
                WHERE id_donacion = ?
                """, fecha, limpiar(estado), limpiar(observaciones), id);
    }

    /** Recalcula cantidad_total como la suma de las cantidades de los detalles. */
    public void recalcularTotal(int id) {
        jdbc.update("""
                UPDATE donaciones
                SET cantidad_total = COALESCE(
                    (SELECT SUM(cantidad_detalle_donacion) FROM detalle_donacion WHERE id_donacion = ?), 0)
                WHERE id_donacion = ?
                """, id, id);
    }
}
