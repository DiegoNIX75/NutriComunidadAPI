package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.DetalleDonacionRequest;
import com.nutricomunidad.models.DetalleDonacion;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DetalleDonacionRepository extends BaseRepository<DetalleDonacion> {

    public DetalleDonacionRepository(JdbcTemplate jdbc) {
        super(jdbc, "detalle_donacion", "id_detalle_donacion", (rs, fila) -> new DetalleDonacion(
                rs.getInt("id_detalle_donacion"),
                decimal(rs, "cantidad_detalle_donacion"),
                rs.getString("unidad_medida_detalle_donacion"),
                rs.getInt("id_alimento"),
                rs.getInt("id_comercio"),
                rs.getInt("id_donacion"),
                rs.getString("nombre_alimento"),
                rs.getString("nombre_comercio")));
    }

    @Override
    protected String select() {
        return """
                SELECT d.*, a.nombre_alimento, c.nombre_comercio
                FROM detalle_donacion d
                JOIN alimentos a ON a.id_alimento = d.id_alimento
                JOIN comercios c ON c.id_comercio = d.id_comercio
                """;
    }

    public List<DetalleDonacion> listarPorDonacion(int idDonacion) {
        return listar(new Filtro().igual("d.id_donacion", idDonacion));
    }

    public int crear(int idDonacion, DetalleDonacionRequest r) {
        return insertar("""
                INSERT INTO detalle_donacion (cantidad_detalle_donacion, unidad_medida_detalle_donacion,
                    id_alimento, id_comercio, id_donacion)
                VALUES (?, ?, ?, ?, ?) RETURNING id_detalle_donacion
                """, r.cantidadDetalleDonacion(), limpiar(r.unidadMedidaDetalleDonacion()), r.idAlimento(),
                r.idComercio(), idDonacion);
    }

    public void actualizar(int id, DetalleDonacionRequest r) {
        jdbc.update("""
                UPDATE detalle_donacion SET cantidad_detalle_donacion = ?, unidad_medida_detalle_donacion = ?,
                    id_alimento = ?, id_comercio = ?
                WHERE id_detalle_donacion = ?
                """, r.cantidadDetalleDonacion(), limpiar(r.unidadMedidaDetalleDonacion()), r.idAlimento(),
                r.idComercio(), id);
    }
}
