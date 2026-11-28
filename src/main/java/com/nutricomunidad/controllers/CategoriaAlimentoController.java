package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.CategoriaAlimentoRequest;
import com.nutricomunidad.models.Alimento;
import com.nutricomunidad.models.CategoriaAlimento;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AlimentoService;
import com.nutricomunidad.services.CategoriaAlimentoService;
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
@RequestMapping("/api/categorias")
public class CategoriaAlimentoController {

    private final CategoriaAlimentoService service;
    private final AlimentoService alimentos;

    public CategoriaAlimentoController(CategoriaAlimentoService service, AlimentoService alimentos) {
        this.service = service;
        this.alimentos = alimentos;
    }

    @GetMapping
    public List<CategoriaAlimento> listar(@RequestParam(required = false) String estado,
                                          @RequestParam(required = false) String buscar) {
        return service.listar(estado, buscar);
    }

    @GetMapping("/{id}")
    public CategoriaAlimento obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @GetMapping("/{id}/alimentos")
    public List<Alimento> alimentos(@PathVariable int id) {
        service.obtener(id);
        return alimentos.listar(id, null, null);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public CategoriaAlimento crear(@Valid @RequestBody CategoriaAlimentoRequest request) {
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public CategoriaAlimento actualizar(@PathVariable int id, @Valid @RequestBody CategoriaAlimentoRequest request) {
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }
}
