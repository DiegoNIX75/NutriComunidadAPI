package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.EstadoRequest;
import com.nutricomunidad.dto.RolAsignacionRequest;
import com.nutricomunidad.dto.UsuarioActualizacionRequest;
import com.nutricomunidad.dto.UsuarioRequest;
import com.nutricomunidad.dto.UsuarioResponse;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.UsuarioService;
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

@RestController
@RequestMapping("/api/usuarios")
@RequiereRol(Roles.ADMIN)
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) String estado,
                                        @RequestParam(required = false) Integer idRol,
                                        @RequestParam(required = false) String buscar) {
        return service.listar(estado, idRol, buscar);
    }

    @GetMapping("/{id}")
    public UsuarioResponse obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public UsuarioResponse actualizar(@PathVariable int id, @Valid @RequestBody UsuarioActualizacionRequest request) {
        return service.actualizar(id, request);
    }

    @PatchMapping("/{id}/estado")
    public UsuarioResponse cambiarEstado(@PathVariable int id, @Valid @RequestBody EstadoRequest request,
                                         @RequestAttribute("usuario") UsuarioToken actor) {
        return service.cambiarEstado(id, request.estado(), actor.id());
    }

    @PatchMapping("/{id}/rol")
    public UsuarioResponse cambiarRol(@PathVariable int id, @Valid @RequestBody RolAsignacionRequest request,
                                      @RequestAttribute("usuario") UsuarioToken actor) {
        return service.cambiarRol(id, request.idRol(), actor.id());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id, @RequestAttribute("usuario") UsuarioToken actor) {
        service.eliminar(id, actor.id());
    }
}
