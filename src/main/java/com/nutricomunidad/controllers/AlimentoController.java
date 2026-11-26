package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.AlimentoRequest;
import com.nutricomunidad.models.Alimento;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AlimentoService;
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
@RequestMapping("/api/alimentos")
public class AlimentoController {

    private final AlimentoService service;

    public AlimentoController(AlimentoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Alimento> listar(@RequestParam(required = false) Integer idCategoria,
                                 @RequestParam(required = false) String estado,
                                 @RequestParam(required = false) String buscar) {
        return service.listar(idCategoria, estado, buscar);
    }

    @GetMapping("/{id}")
    public Alimento obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Alimento crear(@Valid @RequestBody AlimentoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Alimento actualizar(@PathVariable int id, @Valid @RequestBody AlimentoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }
}
