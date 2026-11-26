package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.DetalleDonacionRequest;
import com.nutricomunidad.dto.DonacionActualizacionRequest;
import com.nutricomunidad.dto.DonacionRequest;
import com.nutricomunidad.dto.DonacionResponse;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.models.Donacion;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.DonacionService;
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

/** Donaciones. Un Comercio sólo ve y modifica las donaciones que incluyen productos de su comercio. */
@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    private final DonacionService service;
    private final AccesoService acceso;

    public DonacionController(DonacionService service, AccesoService acceso) {
        this.service = service;
        this.acceso = acceso;
    }

    @GetMapping
    public List<Donacion> listar(@RequestParam(required = false) String estado,
                                 @RequestParam(required = false) Integer idComercio,
                                 @RequestAttribute("usuario") UsuarioToken usuario) {
        if (AccesoService.tieneRol(usuario, Roles.COMERCIO)) {
            idComercio = acceso.idComercioOSinResultados(usuario);
        }
        return service.listar(estado, idComercio);
    }

    @GetMapping("/{id}")
    public DonacionResponse obtener(@PathVariable int id, @RequestAttribute("usuario") UsuarioToken usuario) {
        service.exigirAcceso(id, usuario);
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public DonacionResponse crear(@Valid @RequestBody DonacionRequest request,
                                  @RequestAttribute("usuario") UsuarioToken usuario) {
        return service.crear(request, usuario);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public DonacionResponse actualizar(@PathVariable int id, @Valid @RequestBody DonacionActualizacionRequest request,
                                       @RequestAttribute("usuario") UsuarioToken usuario) {
        return service.actualizar(id, request, usuario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }

    @PostMapping("/{id}/detalles")
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public DonacionResponse agregarDetalle(@PathVariable int id, @Valid @RequestBody DetalleDonacionRequest request,
                                           @RequestAttribute("usuario") UsuarioToken usuario) {
        return service.agregarDetalle(id, request, usuario);
    }

    @PutMapping("/{id}/detalles/{idDetalle}")
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public DonacionResponse actualizarDetalle(@PathVariable int id, @PathVariable int idDetalle,
                                              @Valid @RequestBody DetalleDonacionRequest request,
                                              @RequestAttribute("usuario") UsuarioToken usuario) {
        return service.actualizarDetalle(id, idDetalle, request, usuario);
    }

    @DeleteMapping("/{id}/detalles/{idDetalle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public void eliminarDetalle(@PathVariable int id, @PathVariable int idDetalle,
                                @RequestAttribute("usuario") UsuarioToken usuario) {
        service.eliminarDetalle(id, idDetalle, usuario);
    }
}
