package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.OrganizacionRequest;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Inventario;
import com.nutricomunidad.models.Organizacion;
import com.nutricomunidad.models.Solicitud;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.InventarioService;
import com.nutricomunidad.services.OrganizacionService;
import com.nutricomunidad.services.SolicitudService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizaciones")
public class OrganizacionController {

    private static final String SIN_REGISTRO = "Tu cuenta aún no tiene una organización registrada";

    private final OrganizacionService service;
    private final SolicitudService solicitudes;
    private final InventarioService inventarios;
    private final AccesoService acceso;

    public OrganizacionController(OrganizacionService service, SolicitudService solicitudes,
                                  InventarioService inventarios, AccesoService acceso) {
        this.service = service;
        this.solicitudes = solicitudes;
        this.inventarios = inventarios;
        this.acceso = acceso;
    }

    @GetMapping
    public List<Organizacion> listar(@RequestParam(required = false) String buscar,
                                     @RequestParam(required = false) String tipo,
                                     @RequestParam(required = false) String municipio) {
        return service.listar(buscar, tipo, municipio);
    }

    @GetMapping("/mio")
    @RequiereRol(Roles.ORGANIZACION)
    public Organizacion mia(@RequestAttribute("usuario") UsuarioToken usuario) {
        return acceso.organizacionDe(usuario).orElseThrow(() -> Errores.noEncontrado(SIN_REGISTRO));
    }

    @PutMapping("/mio")
    @RequiereRol(Roles.ORGANIZACION)
    public Organizacion actualizarMia(@Valid @RequestBody OrganizacionRequest request,
                                      @RequestAttribute("usuario") UsuarioToken usuario) {
        return service.actualizar(mia(usuario).idOrganizacion(), request);
    }

    @GetMapping("/{id}")
    public Organizacion obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Organizacion crear(@Valid @RequestBody OrganizacionRequest request,
                              @RequestAttribute("usuario") UsuarioToken usuario) {
        Integer propietario = acceso.propietarioParaAlta(usuario, Roles.ORGANIZACION,
                acceso.organizacionDe(usuario).isPresent());
        return service.crear(request, propietario);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Organizacion actualizar(@PathVariable int id, @Valid @RequestBody OrganizacionRequest request,
                                   @RequestAttribute("usuario") UsuarioToken usuario) {
        acceso.exigirOrganizacion(usuario, id);
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }

    @GetMapping("/{id}/solicitudes")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public List<Solicitud> solicitudes(@PathVariable int id, @RequestAttribute("usuario") UsuarioToken usuario) {
        acceso.exigirOrganizacion(usuario, id);
        service.asegurarExiste(id);
        return solicitudes.listar(null, null, null, id);
    }

    @GetMapping("/{id}/inventarios")
    public List<Inventario> inventarios(@PathVariable int id) {
        service.asegurarExiste(id);
        return inventarios.listar(id, null, null);
    }
}
