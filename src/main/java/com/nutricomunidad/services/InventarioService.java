package com.nutricomunidad.services;

import com.nutricomunidad.dto.InventarioRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Inventario;
import com.nutricomunidad.repositories.DonacionRepository;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.InventarioRepository;
import com.nutricomunidad.repositories.OrganizacionRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    private final InventarioRepository inventarios;
    private final DonacionRepository donaciones;
    private final OrganizacionRepository organizaciones;

    public InventarioService(InventarioRepository inventarios, DonacionRepository donaciones,
                             OrganizacionRepository organizaciones) {
        this.inventarios = inventarios;
        this.donaciones = donaciones;
        this.organizaciones = organizaciones;
    }

    public List<Inventario> listar(Integer idOrganizacion, Integer idDonacion, String estado) {
        return inventarios.listar(new Filtro()
                .igual("id_organizacion", idOrganizacion)
                .igual("id_donacion", idDonacion)
                .igualSinMayusculas("estado_inventario", estado));
    }

    public java.util.Optional<Inventario> buscar(int id) {
        return inventarios.buscarPorId(id);
    }

    public Inventario obtener(int id) {
        return inventarios.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Inventario", id));
    }

    public Inventario crear(InventarioRequest r) {
        validarReferencias(r);
        return obtener(inventarios.crear(r));
    }

    public Inventario actualizar(int id, InventarioRequest r) {
        asegurarExiste(id);
        validarReferencias(r);
        inventarios.actualizar(id, r);
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!inventarios.eliminar(id)) {
            throw Errores.noEncontrado("Inventario", id);
        }
    }

    public void asegurarExiste(int id) {
        if (!inventarios.existe(id)) {
            throw Errores.noEncontrado("Inventario", id);
        }
    }

    private void validarReferencias(InventarioRequest r) {
        Errores.validarReferencia(donaciones.existe(r.idDonacion()), "La donación", r.idDonacion());
        Errores.validarReferencia(organizaciones.existe(r.idOrganizacion()), "La organización", r.idOrganizacion());
    }
}
