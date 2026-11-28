package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.EntregaRequest;
import com.nutricomunidad.models.Entrega;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EntregaRepository extends BaseRepository<Entrega> {

    public EntregaRepository(JdbcTemplate jdbc) {
        super(jdbc, "entregas", "id_entrega", (rs, fila) -> new Entrega(
                rs.getInt("id_entrega"),
                fecha(rs, "fecha_entrega"),
                rs.getString("responsable"),
                rs.getString("estado"),
                rs.getString("observaciones"),
                rs.getInt("id_solicitud")));
    }

    public int crear(EntregaRequest r) {
        return insertar("""
                INSERT INTO entregas (fecha_entrega, responsable, estado, observaciones, id_solicitud)
                VALUES (?, ?, ?, ?, ?) RETURNING id_entrega
                """, valores(r));
    }

    public void actualizar(int id, EntregaRequest r) {
        jdbc.update("""
                UPDATE entregas SET fecha_entrega = ?, responsable = ?, estado = ?, observaciones = ?, id_solicitud = ?
                WHERE id_entrega = ?
                """, conId(valores(r), id));
    }

    public void actualizarEstado(int id, String estado) {
        jdbc.update("UPDATE entregas SET estado = ? WHERE id_entrega = ?", estado, id);
    }

    private static Object[] valores(EntregaRequest r) {
        return new Object[] {r.fechaEntrega(), limpiar(r.responsable()), limpiar(r.estado()),
                limpiar(r.observaciones()), r.idSolicitud()};
    }
}
