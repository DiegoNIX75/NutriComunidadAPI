package com.example.apibasededatos.services;

import com.example.apibasededatos.dto.CreateBeneficiaryRequest;
import com.example.apibasededatos.models.beneficiaries;
import com.example.apibasededatos.structures.SinglyLinkedList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeneficiariesService {

    private final SinglyLinkedList<beneficiaries> listaBeneficiaries = new SinglyLinkedList<>();
    private int contadorId = 1;

    public BeneficiariesService() {
        crear(new CreateBeneficiaryRequest("Ana", "López", "5544556601", "ana.lopez@correo.com", 4,
                "Av. Reforma", "120", "", "Centro", "06000", "Cuauhtémoc", "Ciudad de México"));
        crear(new CreateBeneficiaryRequest("Luis", "Ruiz", "5544556602", "luis.ruiz@correo.com", 3,
                "Calle Hidalgo", "45", "2B", "Doctores", "06720", "Cuauhtémoc", "Ciudad de México"));
        crear(new CreateBeneficiaryRequest("María", "Gómez", "5544556603", "maria.gomez@correo.com", 5,
                "Av. Insurgentes", "310", "", "Roma Norte", "06700", "Cuauhtémoc", "Ciudad de México"));
    }

    public List<beneficiaries> obtenerTodos() {
        return listaBeneficiaries.toList();
    }

    public beneficiaries obtenerPorId(int id) {
        return listaBeneficiaries.find(b -> b.id() == id);
    }

    public beneficiaries crear(CreateBeneficiaryRequest request) {
        beneficiaries nuevo = construir(contadorId++, request);
        listaBeneficiaries.add(nuevo);
        return nuevo;
    }

    public beneficiaries actualizar(int id, CreateBeneficiaryRequest request) {
        beneficiaries actualizado = construir(id, request);
        return listaBeneficiaries.replace(b -> b.id() == id, actualizado) ? actualizado : null;
    }

    public boolean eliminar(int id) {
        return listaBeneficiaries.remove(b -> b.id() == id) != null;
    }

    private beneficiaries construir(int id, CreateBeneficiaryRequest r) {
        return new beneficiaries(id, r.name(), r.lastName(), r.phoneNumber(), r.mail(), r.familyMembers(),
                r.street(), r.exteriorNumber(), r.interiorNumber(), r.neighborhood(), r.postalCode(),
                r.municipality(), r.state());
    }
}