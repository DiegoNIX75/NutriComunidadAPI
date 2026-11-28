package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.DetalleRecetaRequest;
import com.nutricomunidad.dto.RecetaActualizacionRequest;
import com.nutricomunidad.dto.RecetaRequest;
import com.nutricomunidad.dto.RecetaResponse;
import com.nutricomunidad.models.Receta;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.RecetaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    private final RecetaService service;

    public RecetaController(RecetaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Receta> listar(@RequestParam(required = false) String buscar,
                               @RequestParam(required = false) Integer idAlimento) {
        return service.listar(buscar, idAlimento);
    }

    @GetMapping("/{id}")
    public RecetaResponse obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public RecetaResponse crear(@Valid @RequestBody RecetaRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public RecetaResponse actualizar(@PathVariable int id, @Valid @RequestBody RecetaActualizacionRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }

    @PostMapping("/{id}/ingredientes")
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public RecetaResponse agregarIngrediente(@PathVariable int id, @Valid @RequestBody DetalleRecetaRequest request) {
        return service.agregarIngrediente(id, request);
    }

    @PutMapping("/{id}/ingredientes/{idDetalle}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public RecetaResponse actualizarIngrediente(@PathVariable int id, @PathVariable int idDetalle,
                                                @Valid @RequestBody DetalleRecetaRequest request) {
        return service.actualizarIngrediente(id, idDetalle, request);
    }

    @DeleteMapping("/{id}/ingredientes/{idDetalle}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public void eliminarIngrediente(@PathVariable int id, @PathVariable int idDetalle) {
        service.eliminarIngrediente(id, idDetalle);
    }
}
