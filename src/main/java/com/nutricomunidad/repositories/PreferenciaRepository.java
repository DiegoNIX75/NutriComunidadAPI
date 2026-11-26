package com.nutricomunidad.repositories;

import static com.nutricomunidad.dto.Textos.limpiar;

import com.nutricomunidad.dto.PreferenciaRequest;
import com.nutricomunidad.models.Preferencia;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PreferenciaRepository extends BaseRepository<Preferencia> {

    public PreferenciaRepository(JdbcTemplate jdbc) {
        super(jdbc, "preferencias", "id_preferencias", (rs, fila) -> new Preferencia(
                rs.getInt("id_preferencias"),
                rs.getString("tipo_preferencia"),
                rs.getString("descripcion_preferencia"),
                rs.getString("observaciones_preferencia"),
                rs.getInt("id_beneficiario")));
    }

    public int crear(PreferenciaRequest r) {
        return insertar("""
                INSERT INTO preferencias (tipo_preferencia, descripcion_preferencia, observaciones_preferencia, id_beneficiario)
                VALUES (?, ?, ?, ?) RETURNING id_preferencias
                """, valores(r));
    }

    public void actualizar(int id, PreferenciaRequest r) {
        jdbc.update("""
                UPDATE preferencias SET tipo_preferencia = ?, descripcion_preferencia = ?,
                    observaciones_preferencia = ?, id_beneficiario = ?
                WHERE id_preferencias = ?
                """, conId(valores(r), id));
    }

    private static Object[] valores(PreferenciaRequest r) {
        return new Object[] {limpiar(r.tipoPreferencia()), limpiar(r.descripcionPreferencia()),
                limpiar(r.observacionesPreferencia()), r.idBeneficiario()};
    }
}
