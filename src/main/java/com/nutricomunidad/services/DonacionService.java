package com.nutricomunidad.services;

import com.nutricomunidad.dto.DetalleDonacionRequest;
import com.nutricomunidad.dto.DonacionActualizacionRequest;
import com.nutricomunidad.dto.DonacionRequest;
import com.nutricomunidad.dto.DonacionResponse;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.DetalleDonacion;
import com.nutricomunidad.models.Donacion;
import com.nutricomunidad.repositories.AlimentoRepository;
import com.nutricomunidad.repositories.ComercioRepository;
import com.nutricomunidad.repositories.DetalleDonacionRepository;
import com.nutricomunidad.repositories.DonacionRepository;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.security.Roles;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Donaciones y sus detalles. Toda operación que modifica detalles recalcula cantidad_total
 * dentro de la misma transacción.
 */
@Service
public class DonacionService {

    private final DonacionRepository donaciones;
    private final DetalleDonacionRepository detalles;
    private final AlimentoRepository alimentos;
    private final ComercioRepository comercios;
    private final AccesoService acceso;

    public DonacionService(DonacionRepository donaciones, DetalleDonacionRepository detalles,
                           AlimentoRepository alimentos, ComercioRepository comercios, AccesoService acceso) {
        this.donaciones = donaciones;
        this.detalles = detalles;
        this.alimentos = alimentos;
        this.comercios = comercios;
        this.acceso = acceso;
    }

    public List<Donacion> listar(String estado, Integer idComercio) {
        return donaciones.listar(new Filtro()
                .igualSinMayusculas("estado_donacion", estado)
                .condicion("""
                        EXISTS (SELECT 1 FROM detalle_donacion dd
                                WHERE dd.id_donacion = donaciones.id_donacion AND dd.id_comercio = ?)
                        """, idComercio));
    }

    public DonacionResponse obtener(int id) {
        Donacion donacion = donaciones.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Donación", id));
        return DonacionResponse.de(donacion, detalles.listarPorDonacion(id));
    }

    @Transactional
    public DonacionResponse crear(DonacionRequest r, UsuarioToken usuario) {
        r.detalles().forEach(detalle -> validarDetalle(detalle, usuario));
        LocalDate fecha = r.fechaDonacion() == null ? LocalDate.now() : r.fechaDonacion();
        int id = donaciones.crear(fecha, r.estadoDonacion(), r.observacionesDonacion());
        r.detalles().forEach(detalle -> detalles.crear(id, detalle));
        donaciones.recalcularTotal(id);
        return obtener(id);
    }

    public DonacionResponse actualizar(int id, DonacionActualizacionRequest r, UsuarioToken usuario) {
        exigirAcceso(id, usuario);
        donaciones.actualizar(id, r.fechaDonacion(), r.estadoDonacion(), r.observacionesDonacion());
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!donaciones.eliminar(id)) {
            throw Errores.noEncontrado("Donación", id);
        }
    }

    @Transactional
    public DonacionResponse agregarDetalle(int idDonacion, DetalleDonacionRequest r, UsuarioToken usuario) {
        exigirAcceso(idDonacion, usuario);
        validarDetalle(r, usuario);
        detalles.crear(idDonacion, r);
        donaciones.recalcularTotal(idDonacion);
        return obtener(idDonacion);
    }

    @Transactional
    public DonacionResponse actualizarDetalle(int idDonacion, int idDetalle, DetalleDonacionRequest r,
                                              UsuarioToken usuario) {
        DetalleDonacion actual = obtenerDetalleDe(idDonacion, idDetalle);
        acceso.exigirComercio(usuario, actual.idComercio());
        validarDetalle(r, usuario);
        detalles.actualizar(idDetalle, r);
        donaciones.recalcularTotal(idDonacion);
        return obtener(idDonacion);
    }

    @Transactional
    public void eliminarDetalle(int idDonacion, int idDetalle, UsuarioToken usuario) {
        DetalleDonacion actual = obtenerDetalleDe(idDonacion, idDetalle);
        acceso.exigirComercio(usuario, actual.idComercio());
        detalles.eliminar(idDetalle);
        donaciones.recalcularTotal(idDonacion);
    }

    private DetalleDonacion obtenerDetalleDe(int idDonacion, int idDetalle) {
        asegurarExiste(idDonacion);
        return detalles.buscarPorId(idDetalle)
                .filter(detalle -> detalle.idDonacion() == idDonacion)
                .orElseThrow(() -> Errores.noEncontrado("Detalle de la donación " + idDonacion, idDetalle));
    }

    private void validarDetalle(DetalleDonacionRequest r, UsuarioToken usuario) {
        Errores.validarReferencia(alimentos.existe(r.idAlimento()), "El alimento", r.idAlimento());
        Errores.validarReferencia(comercios.existe(r.idComercio()), "El comercio", r.idComercio());
        acceso.exigirComercio(usuario, r.idComercio());
    }

    /** Un Comercio sólo puede consultar o modificar donaciones que incluyen productos de su comercio. */
    public void exigirAcceso(int idDonacion, UsuarioToken usuario) {
        asegurarExiste(idDonacion);
        if (AccesoService.tieneRol(usuario, Roles.COMERCIO)) {
            int propio = acceso.idComercioOSinResultados(usuario);
            boolean incluye = detalles.listarPorDonacion(idDonacion).stream().anyMatch(d -> d.idComercio() == propio);
            if (!incluye) {
                throw Errores.prohibido("Solo puedes acceder a las donaciones de tu comercio");
            }
        }
    }

    private void asegurarExiste(int id) {
        if (!donaciones.existe(id)) {
            throw Errores.noEncontrado("Donación", id);
        }
    }
}
