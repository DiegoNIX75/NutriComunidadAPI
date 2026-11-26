package com.nutricomunidad.repositories;

import com.nutricomunidad.models.Alergia;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

/** Tabla con llave compuesta (id_preferencias, id_alimento, id_beneficiario). */
@Repository
public class AlergiaRepository {

    private static final RowMapper<Alergia> MAPPER = (rs, fila) -> new Alergia(
            rs.getInt("id_preferencias"),
            rs.getInt("id_alimento"),
            rs.getInt("id_beneficiario"),
            rs.getString("nombre_alimento"));

    private final JdbcTemplate jdbc;

    public AlergiaRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Alergia> listarPorBeneficiario(int idBeneficiario) {
        return jdbc.query("""
                SELECT al.id_preferencias, al.id_alimento, al.id_beneficiario, a.nombre_alimento
                FROM alergias al
                JOIN alimentos a ON a.id_alimento = al.id_alimento
                WHERE al.id_beneficiario = ?
                ORDER BY a.nombre_alimento
                """, MAPPER, idBeneficiario);
    }

    public boolean existe(int idPreferencias, int idAlimento, int idBeneficiario) {
        Integer total = jdbc.queryForObject("""
                SELECT COUNT(*) FROM alergias
                WHERE id_preferencias = ? AND id_alimento = ? AND id_beneficiario = ?
                """, Integer.class, idPreferencias, idAlimento, idBeneficiario);
        return total != null && total > 0;
    }

    public void crear(int idPreferencias, int idAlimento, int idBeneficiario) {
        jdbc.update("INSERT INTO alergias (id_preferencias, id_alimento, id_beneficiario) VALUES (?, ?, ?)",
                idPreferencias, idAlimento, idBeneficiario);
    }

    public boolean eliminar(int idPreferencias, int idAlimento, int idBeneficiario) {
        return jdbc.update("""
                DELETE FROM alergias WHERE id_preferencias = ? AND id_alimento = ? AND id_beneficiario = ?
                """, idPreferencias, idAlimento, idBeneficiario) > 0;
    }
}
