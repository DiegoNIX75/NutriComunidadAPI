package com.nutricomunidad.services;

import com.nutricomunidad.dto.DetalleRecetaRequest;
import com.nutricomunidad.dto.RecetaActualizacionRequest;
import com.nutricomunidad.dto.RecetaRequest;
import com.nutricomunidad.dto.RecetaResponse;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Receta;
import com.nutricomunidad.repositories.AlimentoRepository;
import com.nutricomunidad.repositories.DetalleRecetaRepository;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.RecetaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecetaService {

    private final RecetaRepository recetas;
    private final DetalleRecetaRepository ingredientes;
    private final AlimentoRepository alimentos;

    public RecetaService(RecetaRepository recetas, DetalleRecetaRepository ingredientes, AlimentoRepository alimentos) {
        this.recetas = recetas;
        this.ingredientes = ingredientes;
        this.alimentos = alimentos;
    }

    /** idAlimento filtra las recetas que usan ese alimento como ingrediente. */
    public List<Receta> listar(String buscar, Integer idAlimento) {
        return recetas.listar(new Filtro()
                .contiene(buscar, "nombre_receta", "descripcion")
                .condicion("""
                        EXISTS (SELECT 1 FROM detalle_recetas dr
                                WHERE dr.id_receta = recetas.id_receta AND dr.id_alimento = ?)
                        """, idAlimento));
    }

    public RecetaResponse obtener(int id) {
        Receta receta = recetas.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Receta", id));
        return RecetaResponse.de(receta, ingredientes.listarPorReceta(id));
    }

    @Transactional
    public RecetaResponse crear(RecetaRequest r) {
        List<DetalleRecetaRequest> lista = r.ingredientes() == null ? List.of() : r.ingredientes();
        lista.forEach(this::validarIngrediente);
        int id = recetas.crear(r.nombreReceta(), r.descripcion(), r.tiempoPreparacion(), r.proporciones(),
                r.costoEstimado());
        lista.forEach(ingrediente -> ingredientes.crear(id, ingrediente));
        return obtener(id);
    }

    public RecetaResponse actualizar(int id, RecetaActualizacionRequest r) {
        asegurarExiste(id);
        recetas.actualizar(id, r.nombreReceta(), r.descripcion(), r.tiempoPreparacion(), r.proporciones(),
                r.costoEstimado());
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!recetas.eliminar(id)) {
            throw Errores.noEncontrado("Receta", id);
        }
    }

    public RecetaResponse agregarIngrediente(int idReceta, DetalleRecetaRequest r) {
        asegurarExiste(idReceta);
        validarIngrediente(r);
        ingredientes.crear(idReceta, r);
        return obtener(idReceta);
    }

    public RecetaResponse actualizarIngrediente(int idReceta, int idDetalle, DetalleRecetaRequest r) {
        asegurarIngredienteDe(idReceta, idDetalle);
        validarIngrediente(r);
        ingredientes.actualizar(idDetalle, r);
        return obtener(idReceta);
    }

    public void eliminarIngrediente(int idReceta, int idDetalle) {
        asegurarIngredienteDe(idReceta, idDetalle);
        ingredientes.eliminar(idDetalle);
    }

    private void asegurarIngredienteDe(int idReceta, int idDetalle) {
        asegurarExiste(idReceta);
        boolean pertenece = ingredientes.buscarPorId(idDetalle)
                .map(detalle -> detalle.idReceta() == idReceta)
                .orElse(false);
        if (!pertenece) {
            throw Errores.noEncontrado("Ingrediente de la receta " + idReceta, idDetalle);
        }
    }

    private void validarIngrediente(DetalleRecetaRequest r) {
        Errores.validarReferencia(alimentos.existe(r.idAlimento()), "El alimento", r.idAlimento());
    }

    private void asegurarExiste(int id) {
        if (!recetas.existe(id)) {
            throw Errores.noEncontrado("Receta", id);
        }
    }
}
