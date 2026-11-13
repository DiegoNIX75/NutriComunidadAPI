package com.example.apibasededatos.controllers;

import com.example.apibasededatos.dto.CreateOrganizationRequest;
import com.example.apibasededatos.models.organizations;
import com.example.apibasededatos.services.OrganizationsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationsController {

    private final OrganizationsService service;

    public OrganizationsController(OrganizationsService service) {
        this.service = service;
    }

    @GetMapping
    public List<organizations> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<organizations> obtenerPorId(@PathVariable int id) {
        organizations encontrado = service.obtenerPorId(id);
        return encontrado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(encontrado);
    }

    @PostMapping
    public ResponseEntity<organizations> crear(@RequestBody CreateOrganizationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<organizations> actualizar(@PathVariable int id, @RequestBody CreateOrganizationRequest request) {
        organizations actualizado = service.actualizar(id, request);
        return actualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
