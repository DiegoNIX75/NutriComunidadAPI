package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.InventarioRequest;
import com.nutricomunidad.models.Inventario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InventarioRepository extends BaseRepository<Inventario> {

    public InventarioRepository(JdbcTemplate jdbc) {
        super(jdbc, "inventario", "id_inventario", (rs, fila) -> new Inventario(
                rs.getInt("id_inventario"),
                decimal(rs, "cantidad_disponible"),
                fecha(rs, "fecha_actualizacion"),
                rs.getString("estado_inventario"),
                rs.getInt("id_donacion"),
                rs.getInt("id_organizacion")));
    }

    public int crear(InventarioRequest r) {
        return insertar("""
                INSERT INTO inventario (cantidad_disponible, fecha_actualizacion, estado_inventario, id_donacion, id_organizacion)
                VALUES (?, CURRENT_DATE, ?, ?, ?) RETURNING id_inventario
                """, r.cantidadDisponible(), limpiar(r.estadoInventario()), r.idDonacion(), r.idOrganizacion());
    }

    public void actualizar(int id, InventarioRequest r) {
        jdbc.update("""
                UPDATE inventario SET cantidad_disponible = ?, fecha_actualizacion = CURRENT_DATE,
                    estado_inventario = ?, id_donacion = ?, id_organizacion = ?
                WHERE id_inventario = ?
                """, r.cantidadDisponible(), limpiar(r.estadoInventario()), r.idDonacion(), r.idOrganizacion(), id);
    }

    /** Registra la fecha de la última modificación (por ejemplo, al cambiar sus lotes). */
    public void marcarActualizado(int id) {
        jdbc.update("UPDATE inventario SET fecha_actualizacion = CURRENT_DATE WHERE id_inventario = ?", id);
    }
}
