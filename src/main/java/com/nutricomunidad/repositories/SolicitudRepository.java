package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.SolicitudRequest;
import com.nutricomunidad.models.Solicitud;
import java.time.LocalDate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SolicitudRepository extends BaseRepository<Solicitud> {

    public SolicitudRepository(JdbcTemplate jdbc) {
        super(jdbc, "solicitudes", "id_solicitud", (rs, fila) -> new Solicitud(
                rs.getInt("id_solicitud"),
                fecha(rs, "fecha_solicitud"),
                rs.getString("motivo"),
                entero(rs, "numero_integrantes"),
                rs.getString("prioridad"),
                rs.getString("estado_solicitud"),
                rs.getInt("id_organizacion"),
                rs.getInt("id_beneficiario")));
    }

    public int crear(SolicitudRequest r, LocalDate fecha) {
        return insertar("""
                INSERT INTO solicitudes (fecha_solicitud, motivo, numero_integrantes, prioridad, estado_solicitud,
                    id_organizacion, id_beneficiario)
                VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id_solicitud
                """, valores(r, fecha));
    }

    public void actualizar(int id, SolicitudRequest r, LocalDate fecha) {
        jdbc.update("""
                UPDATE solicitudes SET fecha_solicitud = ?, motivo = ?, numero_integrantes = ?, prioridad = ?,
                    estado_solicitud = ?, id_organizacion = ?, id_beneficiario = ?
                WHERE id_solicitud = ?
                """, conId(valores(r, fecha), id));
    }

    public void actualizarEstado(int id, String estado) {
        jdbc.update("UPDATE solicitudes SET estado_solicitud = ? WHERE id_solicitud = ?", estado, id);
    }

    private static Object[] valores(SolicitudRequest r, LocalDate fecha) {
        return new Object[] {fecha, limpiar(r.motivo()), r.numeroIntegrantes(), limpiar(r.prioridad()),
                limpiar(r.estadoSolicitud()), r.idOrganizacion(), r.idBeneficiario()};
    }
}
