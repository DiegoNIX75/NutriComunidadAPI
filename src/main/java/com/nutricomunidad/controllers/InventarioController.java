package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.InventarioRequest;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.models.Inventario;
import com.nutricomunidad.models.Lote;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.InventarioService;
import com.nutricomunidad.services.LoteService;
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
@RequestMapping("/api/inventarios")
public class InventarioController {

    private final InventarioService service;
    private final LoteService lotes;
    private final AccesoService acceso;

    public InventarioController(InventarioService service, LoteService lotes, AccesoService acceso) {
        this.service = service;
        this.lotes = lotes;
        this.acceso = acceso;
    }

    @GetMapping
    public List<Inventario> listar(@RequestParam(required = false) Integer idOrganizacion,
                                   @RequestParam(required = false) Integer idDonacion,
                                   @RequestParam(required = false) String estado,
                                   @RequestAttribute("usuario") UsuarioToken usuario) {
        if (AccesoService.tieneRol(usuario, Roles.ORGANIZACION)) {
            idOrganizacion = acceso.idOrganizacionOSinResultados(usuario);
        }
        return service.listar(idOrganizacion, idDonacion, estado);
    }

    @GetMapping("/{id}")
    public Inventario obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @GetMapping("/{id}/lotes")
    public List<Lote> lotes(@PathVariable int id) {
        service.asegurarExiste(id);
        return lotes.listar(id, null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Inventario crear(@Valid @RequestBody InventarioRequest request,
                            @RequestAttribute("usuario") UsuarioToken usuario) {
        acceso.exigirOrganizacion(usuario, request.idOrganizacion());
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Inventario actualizar(@PathVariable int id, @Valid @RequestBody InventarioRequest request,
                                 @RequestAttribute("usuario") UsuarioToken usuario) {
        acceso.exigirOrganizacion(usuario, service.obtener(id).idOrganizacion());
        acceso.exigirOrganizacion(usuario, request.idOrganizacion());
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }
}
