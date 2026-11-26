package com.nutricomunidad.services;

import com.nutricomunidad.dto.EntregaRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Entrega;
import com.nutricomunidad.repositories.EntregaRepository;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.SolicitudRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class EntregaService {

    private final EntregaRepository entregas;
    private final SolicitudRepository solicitudes;

    public EntregaService(EntregaRepository entregas, SolicitudRepository solicitudes) {
        this.entregas = entregas;
        this.solicitudes = solicitudes;
    }

    public List<Entrega> listar(String estado, Integer idSolicitud) {
        return listar(estado, idSolicitud, null, null);
    }

    /** idBeneficiario / idOrganizacion filtran por la solicitud asociada a cada entrega. */
    public List<Entrega> listar(String estado, Integer idSolicitud, Integer idBeneficiario, Integer idOrganizacion) {
        return entregas.listar(new Filtro()
                .igualSinMayusculas("estado", estado)
                .igual("id_solicitud", idSolicitud)
                .condicion("""
                        EXISTS (SELECT 1 FROM solicitudes s
                                WHERE s.id_solicitud = entregas.id_solicitud AND s.id_beneficiario = ?)
                        """, idBeneficiario)
                .condicion("""
                        EXISTS (SELECT 1 FROM solicitudes s
                                WHERE s.id_solicitud = entregas.id_solicitud AND s.id_organizacion = ?)
                        """, idOrganizacion));
    }

    public Entrega obtener(int id) {
        return entregas.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Entrega", id));
    }

    public Entrega crear(EntregaRequest r) {
        Errores.validarReferencia(solicitudes.existe(r.idSolicitud()), "La solicitud", r.idSolicitud());
        return obtener(entregas.crear(r));
    }

    public Entrega actualizar(int id, EntregaRequest r) {
        asegurarExiste(id);
        Errores.validarReferencia(solicitudes.existe(r.idSolicitud()), "La solicitud", r.idSolicitud());
        entregas.actualizar(id, r);
        return obtener(id);
    }

    public Entrega cambiarEstado(int id, String estado) {
        asegurarExiste(id);
        entregas.actualizarEstado(id, estado.trim());
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!entregas.eliminar(id)) {
            throw Errores.noEncontrado("Entrega", id);
        }
    }

    private void asegurarExiste(int id) {
        if (!entregas.existe(id)) {
            throw Errores.noEncontrado("Entrega", id);
        }
    }
}
