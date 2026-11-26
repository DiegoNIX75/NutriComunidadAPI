package com.nutricomunidad.services;

import com.nutricomunidad.dto.SolicitudRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Solicitud;
import com.nutricomunidad.repositories.BeneficiarioRepository;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.OrganizacionRepository;
import com.nutricomunidad.repositories.SolicitudRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class SolicitudService {

    private final SolicitudRepository solicitudes;
    private final BeneficiarioRepository beneficiarios;
    private final OrganizacionRepository organizaciones;

    public SolicitudService(SolicitudRepository solicitudes, BeneficiarioRepository beneficiarios,
                            OrganizacionRepository organizaciones) {
        this.solicitudes = solicitudes;
        this.beneficiarios = beneficiarios;
        this.organizaciones = organizaciones;
    }

    public List<Solicitud> listar(String estado, String prioridad, Integer idBeneficiario, Integer idOrganizacion) {
        return solicitudes.listar(new Filtro()
                .igualSinMayusculas("estado_solicitud", estado)
                .igualSinMayusculas("prioridad", prioridad)
                .igual("id_beneficiario", idBeneficiario)
                .igual("id_organizacion", idOrganizacion));
    }

    public java.util.Optional<Solicitud> buscar(int id) {
        return solicitudes.buscarPorId(id);
    }

    public Solicitud obtener(int id) {
        return solicitudes.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Solicitud", id));
    }

    public Solicitud crear(SolicitudRequest r) {
        validarReferencias(r);
        LocalDate fecha = r.fechaSolicitud() == null ? LocalDate.now() : r.fechaSolicitud();
        return obtener(solicitudes.crear(r, fecha));
    }

    public Solicitud actualizar(int id, SolicitudRequest r) {
        Solicitud actual = obtener(id);
        validarReferencias(r);
        LocalDate fecha = r.fechaSolicitud() == null ? actual.fechaSolicitud() : r.fechaSolicitud();
        solicitudes.actualizar(id, r, fecha);
        return obtener(id);
    }

    public Solicitud cambiarEstado(int id, String estado) {
        asegurarExiste(id);
        solicitudes.actualizarEstado(id, estado.trim());
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!solicitudes.eliminar(id)) {
            throw Errores.noEncontrado("Solicitud", id);
        }
    }

    public void asegurarExiste(int id) {
        if (!solicitudes.existe(id)) {
            throw Errores.noEncontrado("Solicitud", id);
        }
    }

    private void validarReferencias(SolicitudRequest r) {
        Errores.validarReferencia(beneficiarios.existe(r.idBeneficiario()), "El beneficiario", r.idBeneficiario());
        Errores.validarReferencia(organizaciones.existe(r.idOrganizacion()), "La organización", r.idOrganizacion());
    }
}
