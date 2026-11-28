package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.BeneficiarioRequest;
import com.nutricomunidad.models.Beneficiario;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class BeneficiarioRepository extends BaseRepository<Beneficiario> {

    public BeneficiarioRepository(JdbcTemplate jdbc) {
        super(jdbc, "beneficiarios", "id_beneficiario", (rs, fila) -> new Beneficiario(
                rs.getInt("id_beneficiario"),
                rs.getString("nombre_beneficiario"),
                rs.getString("ap"),
                rs.getString("am"),
                rs.getString("telefono"),
                rs.getString("correo"),
                entero(rs, "numero_integrantes"),
                rs.getString("calle"),
                rs.getString("numero_exterior"),
                rs.getString("numero_interior"),
                rs.getString("colonia"),
                rs.getString("codigo_postal"),
                rs.getString("municipio"),
                rs.getString("estado_beneficiario"),
                entero(rs, "id_usuario")));
    }

    /** idUsuario es la cuenta propietaria (null cuando lo registra otra persona, p. ej. una organización). */
    public int crear(BeneficiarioRequest r, Integer idUsuario) {
        return insertar("""
                INSERT INTO beneficiarios (nombre_beneficiario, ap, am, telefono, correo, numero_integrantes,
                    calle, numero_exterior, numero_interior, colonia, codigo_postal, municipio, estado_beneficiario,
                    id_usuario)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_beneficiario
                """, conValor(valores(r), idUsuario));
    }

    public Optional<Beneficiario> buscarPorUsuario(int idUsuario) {
        return listar(new Filtro().igual("id_usuario", idUsuario)).stream().findFirst();
    }

    public void actualizar(int id, BeneficiarioRequest r) {
        jdbc.update("""
                UPDATE beneficiarios SET nombre_beneficiario = ?, ap = ?, am = ?, telefono = ?, correo = ?,
                    numero_integrantes = ?, calle = ?, numero_exterior = ?, numero_interior = ?, colonia = ?,
                    codigo_postal = ?, municipio = ?, estado_beneficiario = ?
                WHERE id_beneficiario = ?
                """, conId(valores(r), id));
    }

    private static Object[] valores(BeneficiarioRequest r) {
        return new Object[] {limpiar(r.nombreBeneficiario()), limpiar(r.ap()), limpiar(r.am()), limpiar(r.telefono()),
                limpiar(r.correo()), r.numeroIntegrantes(), limpiar(r.calle()), limpiar(r.numeroExterior()),
                limpiar(r.numeroInterior()), limpiar(r.colonia()), limpiar(r.codigoPostal()), limpiar(r.municipio()),
                limpiar(r.estadoBeneficiario())};
    }
}
