package com.nutricomunidad.services;

import com.nutricomunidad.dto.LoteRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Lote;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.InventarioRepository;
import com.nutricomunidad.repositories.LoteRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Lotes de un inventario. Al modificarlos se actualiza la fecha del inventario correspondiente. */
@Service
public class LoteService {

    private final LoteRepository lotes;
    private final InventarioRepository inventarios;

    public LoteService(LoteRepository lotes, InventarioRepository inventarios) {
        this.lotes = lotes;
        this.inventarios = inventarios;
    }

    /** caducaAntesDe permite consultar los lotes que caducan en o antes de una fecha. */
    public List<Lote> listar(Integer idInventario, LocalDate caducaAntesDe) {
        return lotes.listar(new Filtro()
                .igual("id_inventario", idInventario)
                .menorOIgual("fecha_caducidad", caducaAntesDe));
    }

    public Lote obtener(int id) {
        return lotes.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Lote", id));
    }

    @Transactional
    public Lote crear(LoteRequest r) {
        validar(r);
        int id = lotes.crear(r);
        inventarios.marcarActualizado(r.idInventario());
        return obtener(id);
    }

    @Transactional
    public Lote actualizar(int id, LoteRequest r) {
        Lote actual = obtener(id);
        validar(r);
        lotes.actualizar(id, r);
        inventarios.marcarActualizado(r.idInventario());
        if (actual.idInventario() != r.idInventario()) {
            inventarios.marcarActualizado(actual.idInventario());
        }
        return obtener(id);
    }

    @Transactional
    public void eliminar(int id) {
        Lote actual = obtener(id);
        lotes.eliminar(id);
        inventarios.marcarActualizado(actual.idInventario());
    }

    private void validar(LoteRequest r) {
        Errores.validarReferencia(inventarios.existe(r.idInventario()), "El inventario", r.idInventario());
        if (r.cantidadActual().compareTo(r.cantidadInicial()) > 0) {
            throw Errores.peticionInvalida("cantidadActual no puede ser mayor que cantidadInicial");
        }
        if (r.fechaCaducidad() != null && r.fechaCaducidad().isBefore(r.fechaRecepcion())) {
            throw Errores.peticionInvalida("fechaCaducidad no puede ser anterior a fechaRecepcion");
        }
        if (r.fechaConsumo() != null && r.fechaConsumo().isBefore(r.fechaRecepcion())) {
            throw Errores.peticionInvalida("fechaConsumo no puede ser anterior a fechaRecepcion");
        }
    }
}
