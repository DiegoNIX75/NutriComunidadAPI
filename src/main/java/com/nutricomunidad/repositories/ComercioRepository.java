package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.ComercioRequest;
import com.nutricomunidad.models.Comercio;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ComercioRepository extends BaseRepository<Comercio> {

    public ComercioRepository(JdbcTemplate jdbc) {
        super(jdbc, "comercios", "id_comercio", (rs, fila) -> new Comercio(
                rs.getInt("id_comercio"),
                rs.getString("nombre_comercio"),
                rs.getString("razon_social"),
                rs.getString("tipo_comercio"),
                rs.getString("telefono_comercio"),
                rs.getString("correo_comercio"),
                rs.getString("calle_comercio"),
                rs.getString("numero_exterior_comercio"),
                rs.getString("numero_interior_comercio"),
                rs.getString("colonia_comercio"),
                rs.getString("codigo_postal_comercio"),
                rs.getString("municipio_comercio"),
                rs.getString("estado_comercio"),
                entero(rs, "id_usuario")));
    }

    public int crear(ComercioRequest r, Integer idUsuario) {
        return insertar("""
                INSERT INTO comercios (nombre_comercio, razon_social, tipo_comercio, telefono_comercio, correo_comercio,
                    calle_comercio, numero_exterior_comercio, numero_interior_comercio, colonia_comercio,
                    codigo_postal_comercio, municipio_comercio, estado_comercio, id_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_comercio
                """, conValor(valores(r), idUsuario));
    }

    public Optional<Comercio> buscarPorUsuario(int idUsuario) {
        return listar(new Filtro().igual("id_usuario", idUsuario)).stream().findFirst();
    }

    public void actualizar(int id, ComercioRequest r) {
        jdbc.update("""
                UPDATE comercios SET nombre_comercio = ?, razon_social = ?, tipo_comercio = ?, telefono_comercio = ?,
                    correo_comercio = ?, calle_comercio = ?, numero_exterior_comercio = ?, numero_interior_comercio = ?,
                    colonia_comercio = ?, codigo_postal_comercio = ?, municipio_comercio = ?, estado_comercio = ?
                WHERE id_comercio = ?
                """, conId(valores(r), id));
    }

    private static Object[] valores(ComercioRequest r) {
        return new Object[] {limpiar(r.nombreComercio()), limpiar(r.razonSocial()), limpiar(r.tipoComercio()),
                limpiar(r.telefonoComercio()), limpiar(r.correoComercio()), limpiar(r.calleComercio()),
                limpiar(r.numeroExteriorComercio()), limpiar(r.numeroInteriorComercio()), limpiar(r.coloniaComercio()),
                limpiar(r.codigoPostalComercio()), limpiar(r.municipioComercio()), limpiar(r.estadoComercio())};
    }
}
