package com.nutricomunidad.services;

import com.nutricomunidad.dto.ComercioRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Comercio;
import com.nutricomunidad.repositories.ComercioRepository;
import com.nutricomunidad.repositories.Filtro;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ComercioService {

    private final ComercioRepository comercios;

    public ComercioService(ComercioRepository comercios) {
        this.comercios = comercios;
    }

    public List<Comercio> listar(String buscar, String tipo, String municipio) {
        return comercios.listar(new Filtro()
                .contiene(buscar, "nombre_comercio", "razon_social")
                .igualSinMayusculas("tipo_comercio", tipo)
                .igualSinMayusculas("municipio_comercio", municipio));
    }

    public Comercio obtener(int id) {
        return comercios.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Comercio", id));
    }

    public Comercio crear(ComercioRequest r, Integer idUsuario) {
        return obtener(comercios.crear(r, idUsuario));
    }

    public Comercio actualizar(int id, ComercioRequest r) {
        if (!comercios.existe(id)) {
            throw Errores.noEncontrado("Comercio", id);
        }
        comercios.actualizar(id, r);
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!comercios.eliminar(id)) {
            throw Errores.noEncontrado("Comercio", id);
        }
    }
}
