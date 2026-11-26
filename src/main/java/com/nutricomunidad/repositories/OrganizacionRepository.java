package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.OrganizacionRequest;
import com.nutricomunidad.models.Organizacion;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrganizacionRepository extends BaseRepository<Organizacion> {

    public OrganizacionRepository(JdbcTemplate jdbc) {
        super(jdbc, "organizaciones", "id_organizacion", (rs, fila) -> new Organizacion(
                rs.getInt("id_organizacion"),
                rs.getString("nombre_organizacion"),
                rs.getString("tipo_organizacion"),
                rs.getString("responsable_organizacion"),
                rs.getString("telefono_organizacion"),
                rs.getString("correo_organizacion"),
                rs.getString("calle_organizacion"),
                rs.getString("numero_exterior_organizacion"),
                rs.getString("numero_interior_organizacion"),
                rs.getString("colonia_organizacion"),
                rs.getString("codigo_postal_organizacion"),
                rs.getString("municipio_organizacion"),
                rs.getString("estado_organizacion"),
                entero(rs, "id_usuario")));
    }

    public int crear(OrganizacionRequest r, Integer idUsuario) {
        return insertar("""
                INSERT INTO organizaciones (nombre_organizacion, tipo_organizacion, responsable_organizacion,
                    telefono_organizacion, correo_organizacion, calle_organizacion, numero_exterior_organizacion,
                    numero_interior_organizacion, colonia_organizacion, codigo_postal_organizacion,
                    municipio_organizacion, estado_organizacion, id_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_organizacion
                """, conValor(valores(r), idUsuario));
    }

    public Optional<Organizacion> buscarPorUsuario(int idUsuario) {
        return listar(new Filtro().igual("id_usuario", idUsuario)).stream().findFirst();
    }

    public void actualizar(int id, OrganizacionRequest r) {
        jdbc.update("""
                UPDATE organizaciones SET nombre_organizacion = ?, tipo_organizacion = ?, responsable_organizacion = ?,
                    telefono_organizacion = ?, correo_organizacion = ?, calle_organizacion = ?,
                    numero_exterior_organizacion = ?, numero_interior_organizacion = ?, colonia_organizacion = ?,
                    codigo_postal_organizacion = ?, municipio_organizacion = ?, estado_organizacion = ?
                WHERE id_organizacion = ?
                """, conId(valores(r), id));
    }

    private static Object[] valores(OrganizacionRequest r) {
        return new Object[] {limpiar(r.nombreOrganizacion()), limpiar(r.tipoOrganizacion()),
                limpiar(r.responsableOrganizacion()), limpiar(r.telefonoOrganizacion()), limpiar(r.correoOrganizacion()),
                limpiar(r.calleOrganizacion()), limpiar(r.numeroExteriorOrganizacion()),
                limpiar(r.numeroInteriorOrganizacion()), limpiar(r.coloniaOrganizacion()),
                limpiar(r.codigoPostalOrganizacion()), limpiar(r.municipioOrganizacion()), limpiar(r.estadoOrganizacion())};
    }
}
