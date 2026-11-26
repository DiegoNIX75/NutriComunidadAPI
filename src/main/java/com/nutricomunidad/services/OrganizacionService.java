package com.nutricomunidad.services;

import com.nutricomunidad.dto.OrganizacionRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Organizacion;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.OrganizacionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class OrganizacionService {

    private final OrganizacionRepository organizaciones;

    public OrganizacionService(OrganizacionRepository organizaciones) {
        this.organizaciones = organizaciones;
    }

    public List<Organizacion> listar(String buscar, String tipo, String municipio) {
        return organizaciones.listar(new Filtro()
                .contiene(buscar, "nombre_organizacion", "responsable_organizacion")
                .igualSinMayusculas("tipo_organizacion", tipo)
                .igualSinMayusculas("municipio_organizacion", municipio));
    }

    public Organizacion obtener(int id) {
        return organizaciones.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Organización", id));
    }

    public Organizacion crear(OrganizacionRequest r, Integer idUsuario) {
        return obtener(organizaciones.crear(r, idUsuario));
    }

    public Organizacion actualizar(int id, OrganizacionRequest r) {
        asegurarExiste(id);
        organizaciones.actualizar(id, r);
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!organizaciones.eliminar(id)) {
            throw Errores.noEncontrado("Organización", id);
        }
    }

    public void asegurarExiste(int id) {
        if (!organizaciones.existe(id)) {
            throw Errores.noEncontrado("Organización", id);
        }
    }
}
