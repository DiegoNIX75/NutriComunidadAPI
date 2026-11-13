package com.example.apibasededatos.controllers;

import com.example.apibasededatos.dto.CreateBusinessRequest;
import com.example.apibasededatos.models.businesses;
import com.example.apibasededatos.services.BusinessesService;
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
@RequestMapping("/api/businesses")
public class BusinessesController {

    private final BusinessesService service;

    public BusinessesController(BusinessesService service) {
        this.service = service;
    }

    @GetMapping
    public List<businesses> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<businesses> obtenerPorId(@PathVariable int id) {
        businesses encontrado = service.obtenerPorId(id);
        return encontrado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(encontrado);
    }

    @PostMapping
    public ResponseEntity<businesses> crear(@RequestBody CreateBusinessRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<businesses> actualizar(@PathVariable int id, @RequestBody CreateBusinessRequest request) {
        businesses actualizado = service.actualizar(id, request);
        return actualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}