package com.example.apibasededatos.controllers;

import com.example.apibasededatos.dto.CreateDonationRequest;
import com.example.apibasededatos.models.donations;
import com.example.apibasededatos.services.DonationsService;
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
@RequestMapping("/api/donations")
public class DonationsController {

    private final DonationsService service;

    public DonationsController(DonationsService service) {
        this.service = service;
    }

    @GetMapping
    public List<donations> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<donations> obtenerPorId(@PathVariable int id) {
        donations encontrado = service.obtenerPorId(id);
        return encontrado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(encontrado);
    }

    @PostMapping
    public ResponseEntity<donations> crear(@RequestBody CreateDonationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<donations> actualizar(@PathVariable int id, @RequestBody CreateDonationRequest request) {
        donations actualizado = service.actualizar(id, request);
        return actualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/deleted")
    public List<donations> obtenerEliminadas() {
        return service.obtenerEliminadas();
    }

    @PostMapping("/restore")
    public ResponseEntity<donations> restaurarUltimaEliminada() {
        donations restaurada = service.restaurarUltimaEliminada();
        return restaurada == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(restaurada);
    }
}
