package com.nutricomunidad.services;

import com.nutricomunidad.dto.CategoriaAlimentoRequest;
import com.nutricomunidad.dto.Textos;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.CategoriaAlimento;
import com.nutricomunidad.repositories.CategoriaAlimentoRepository;
import com.nutricomunidad.repositories.Filtro;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaAlimentoService {

    private static final String ESTADO_POR_DEFECTO = "Activo";

    private final CategoriaAlimentoRepository categorias;

    public CategoriaAlimentoService(CategoriaAlimentoRepository categorias) {
        this.categorias = categorias;
    }

    public List<CategoriaAlimento> listar(String estado, String buscar) {
        return categorias.listar(new Filtro()
                .igualSinMayusculas("estado_categoria", estado)
                .contiene(buscar, "nombre_categoria"));
    }

    public CategoriaAlimento obtener(int id) {
        return categorias.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Categoría", id));
    }

    public CategoriaAlimento crear(CategoriaAlimentoRequest r) {
        return obtener(categorias.crear(r, Textos.oPorDefecto(r.estadoCategoria(), ESTADO_POR_DEFECTO)));
    }

    public CategoriaAlimento actualizar(int id, CategoriaAlimentoRequest r) {
        CategoriaAlimento actual = obtener(id);
        categorias.actualizar(id, r, Textos.oPorDefecto(r.estadoCategoria(), actual.estadoCategoria()));
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!categorias.eliminar(id)) {
            throw Errores.noEncontrado("Categoría", id);
        }
    }
}
