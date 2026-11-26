package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.EntregaRequest;
import com.nutricomunidad.dto.EstadoRequest;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.models.Entrega;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.EntregaService;
import com.nutricomunidad.services.SolicitudService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Entregas. Un Beneficiario sólo ve las de sus solicitudes; una Organización sólo ve y registra las
 * de solicitudes dirigidas a su organización.
 */
@RestController
@RequestMapping("/api/entregas")
@RequiereRol({Roles.ADMIN, Roles.ORGANIZACION, Roles.BENEFICIARIO})
public class EntregaController {

    private final EntregaService service;
    private final SolicitudService solicitudes;
    private final AccesoService acceso;

    public EntregaController(EntregaService service, SolicitudService solicitudes, AccesoService acceso) {
        this.service = service;
        this.solicitudes = solicitudes;
        this.acceso = acceso;
    }

    @GetMapping
    public List<Entrega> listar(@RequestParam(required = false) String estado,
                                @RequestParam(required = false) Integer idSolicitud,
                                @RequestAttribute("usuario") UsuarioToken usuario) {
        Integer idBeneficiario = AccesoService.tieneRol(usuario, Roles.BENEFICIARIO)
                ? acceso.idBeneficiarioOSinResultados(usuario) : null;
        Integer idOrganizacion = AccesoService.tieneRol(usuario, Roles.ORGANIZACION)
                ? acceso.idOrganizacionOSinResultados(usuario) : null;
        return service.listar(estado, idSolicitud, idBeneficiario, idOrganizacion);
    }

    @GetMapping("/{id}")
    public Entrega obtener(@PathVariable int id, @RequestAttribute("usuario") UsuarioToken usuario) {
        Entrega entrega = service.obtener(id);
        exigirAccesoSolicitud(entrega.idSolicitud(), usuario);
        return entrega;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Entrega crear(@Valid @RequestBody EntregaRequest request, @RequestAttribute("usuario") UsuarioToken usuario) {
        exigirAccesoSolicitud(request.idSolicitud(), usuario);
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Entrega actualizar(@PathVariable int id, @Valid @RequestBody EntregaRequest request,
                              @RequestAttribute("usuario") UsuarioToken usuario) {
        obtener(id, usuario);
        exigirAccesoSolicitud(request.idSolicitud(), usuario);
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Entrega cambiarEstado(@PathVariable int id, @Valid @RequestBody EstadoRequest request,
                                 @RequestAttribute("usuario") UsuarioToken usuario) {
        obtener(id, usuario);
        return service.cambiarEstado(id, request.estado());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }

    /** Si la solicitud no existe, el servicio responde 400/404 como de costumbre. */
    private void exigirAccesoSolicitud(Integer idSolicitud, UsuarioToken usuario) {
        if (idSolicitud == null) {
            return;
        }
        solicitudes.buscar(idSolicitud).ifPresent(s -> {
                    acceso.exigirBeneficiario(usuario, s.idBeneficiario());
                    acceso.exigirOrganizacion(usuario, s.idOrganizacion());
                });
    }
}
