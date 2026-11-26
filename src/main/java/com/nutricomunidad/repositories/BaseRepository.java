package com.nutricomunidad.repositories;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 * Operaciones comunes (listar, buscar por id, existe, eliminar) para tablas con llave
 * primaria entera. Las subclases definen el INSERT/UPDATE de cada entidad.
 */
public abstract class BaseRepository<T> {

    protected final JdbcTemplate jdbc;
    private final String tabla;
    private final String columnaId;
    private final RowMapper<T> mapper;

    protected BaseRepository(JdbcTemplate jdbc, String tabla, String columnaId, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.tabla = tabla;
        this.columnaId = columnaId;
        this.mapper = mapper;
    }

    /** Consulta base; se sobrescribe cuando la entidad necesita JOIN. */
    protected String select() {
        return "SELECT * FROM " + tabla;
    }

    public List<T> listar(Filtro filtro) {
        return jdbc.query(select() + filtro.where() + " ORDER BY " + columnaId, mapper, filtro.parametros());
    }

    public List<T> listar() {
        return listar(new Filtro());
    }

    public Optional<T> buscarPorId(int id) {
        return jdbc.query(select() + " WHERE " + columnaId + " = ?", mapper, id).stream().findFirst();
    }

    public boolean existe(Integer id) {
        if (id == null) {
            return false;
        }
        Integer total = jdbc.queryForObject(
                "SELECT COUNT(*) FROM " + tabla + " WHERE " + columnaId + " = ?", Integer.class, id);
        return total != null && total > 0;
    }

    public boolean eliminar(int id) {
        return jdbc.update("DELETE FROM " + tabla + " WHERE " + columnaId + " = ?", id) > 0;
    }

    /** Ejecuta un INSERT ... RETURNING id y devuelve la llave generada. */
    protected int insertar(String sql, Object... parametros) {
        Integer id = jdbc.queryForObject(sql, Integer.class, parametros);
        if (id == null) {
            throw new IllegalStateException("La base de datos no devolvió la llave generada");
        }
        return id;
    }

    /** Agrega el id al final de los parámetros (para las sentencias UPDATE ... WHERE id = ?). */
    protected static Object[] conId(Object[] valores, int id) {
        return conValor(valores, id);
    }

    /** Agrega un valor al final de los parámetros. */
    protected static Object[] conValor(Object[] valores, Object valor) {
        Object[] resultado = Arrays.copyOf(valores, valores.length + 1);
        resultado[valores.length] = valor;
        return resultado;
    }

    protected static LocalDate fecha(ResultSet rs, String columna) throws SQLException {
        return rs.getObject(columna, LocalDate.class);
    }

    protected static Integer entero(ResultSet rs, String columna) throws SQLException {
        return rs.getObject(columna, Integer.class);
    }

    protected static BigDecimal decimal(ResultSet rs, String columna) throws SQLException {
        return rs.getBigDecimal(columna);
    }
}
