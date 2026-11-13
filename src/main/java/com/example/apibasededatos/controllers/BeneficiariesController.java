package com.example.apibasededatos.controllers;

import com.example.apibasededatos.dto.CreateBeneficiaryRequest;
import com.example.apibasededatos.models.beneficiaries;
import com.example.apibasededatos.services.BeneficiariesService;
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
@RequestMapping("/api/beneficiaries")
public class BeneficiariesController {

    private final BeneficiariesService service;

    public BeneficiariesController(BeneficiariesService service) {
        this.service = service;
    }

    @GetMapping
    public List<beneficiaries> obtenerTodos() {
        return service.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<beneficiaries> obtenerPorId(@PathVariable int id) {
        beneficiaries encontrado = service.obtenerPorId(id);
        return encontrado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(encontrado);
    }

    @PostMapping
    public ResponseEntity<beneficiaries> crear(@RequestBody CreateBeneficiaryRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<beneficiaries> actualizar(@PathVariable int id, @RequestBody CreateBeneficiaryRequest request) {
        beneficiaries actualizado = service.actualizar(id, request);
        return actualizado == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        return service.eliminar(id) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}