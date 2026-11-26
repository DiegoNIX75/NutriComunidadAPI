package com.nutricomunidad.services;

import com.nutricomunidad.dto.AlimentoRequest;
import com.nutricomunidad.dto.Textos;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Alimento;
import com.nutricomunidad.repositories.AlimentoRepository;
import com.nutricomunidad.repositories.CategoriaAlimentoRepository;
import com.nutricomunidad.repositories.Filtro;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AlimentoService {

    private static final String ESTADO_POR_DEFECTO = "Activo";

    private final AlimentoRepository alimentos;
    private final CategoriaAlimentoRepository categorias;

    public AlimentoService(AlimentoRepository alimentos, CategoriaAlimentoRepository categorias) {
        this.alimentos = alimentos;
        this.categorias = categorias;
    }

    public List<Alimento> listar(Integer idCategoria, String estado, String buscar) {
        return alimentos.listar(new Filtro()
                .igual("a.id_categoria", idCategoria)
                .igualSinMayusculas("a.estado_alimento", estado)
                .contiene(buscar, "a.nombre_alimento", "a.descripcion"));
    }

    public Alimento obtener(int id) {
        return alimentos.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Alimento", id));
    }

    public Alimento crear(AlimentoRequest r) {
        Errores.validarReferencia(categorias.existe(r.idCategoria()), "La categoría", r.idCategoria());
        return obtener(alimentos.crear(r, Textos.oPorDefecto(r.estadoAlimento(), ESTADO_POR_DEFECTO)));
    }

    public Alimento actualizar(int id, AlimentoRequest r) {
        Alimento actual = obtener(id);
        Errores.validarReferencia(categorias.existe(r.idCategoria()), "La categoría", r.idCategoria());
        alimentos.actualizar(id, r, Textos.oPorDefecto(r.estadoAlimento(), actual.estadoAlimento()));
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!alimentos.eliminar(id)) {
            throw Errores.noEncontrado("Alimento", id);
        }
    }
}
