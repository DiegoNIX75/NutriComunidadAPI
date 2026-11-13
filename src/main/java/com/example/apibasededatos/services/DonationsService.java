package com.example.apibasededatos.services;

import com.example.apibasededatos.dto.CreateDonationRequest;
import com.example.apibasededatos.models.donations;
import com.example.apibasededatos.structures.LinkedStack;
import com.example.apibasededatos.structures.SinglyLinkedList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationsService {

    private final SinglyLinkedList<donations> listaDonations = new SinglyLinkedList<>();
    private final LinkedStack<donations> pilaEliminadas = new LinkedStack<>();
    private int contadorId = 1;

    public DonationsService() {
        crear(new CreateDonationRequest("09/11/2026", 20.0, "En almacén", "Manzana de Frutería La Huerta"));
        crear(new CreateDonationRequest("08/11/2026", 12.0, "Por vencer", "Pan de caja de Panadería San José"));
        crear(new CreateDonationRequest("08/11/2026", 40.0, "Entregado", "Frijol negro de Súper Ahorro"));
    }

    public List<donations> obtenerTodos() {
        return listaDonations.toList();
    }

    public donations obtenerPorId(int id) {
        return listaDonations.find(d -> d.id() == id);
    }

    public donations crear(CreateDonationRequest request) {
        donations nueva = construir(contadorId++, request);
        listaDonations.add(nueva);
        return nueva;
    }

    public donations actualizar(int id, CreateDonationRequest request) {
        donations actualizada = construir(id, request);
        return listaDonations.replace(d -> d.id() == id, actualizada) ? actualizada : null;
    }

    public boolean eliminar(int id) {
        donations eliminada = listaDonations.remove(d -> d.id() == id);
        if (eliminada == null) {
            return false;
        }
        pilaEliminadas.push(eliminada);
        return true;
    }

    public donations restaurarUltimaEliminada() {
        donations restaurada = pilaEliminadas.pop();
        if (restaurada != null) {
            listaDonations.add(restaurada);
        }
        return restaurada;
    }

    public List<donations> obtenerEliminadas() {
        return pilaEliminadas.toList();
    }

    private donations construir(int id, CreateDonationRequest r) {
        return new donations(id, r.donationDate(), r.totalQuantity(), r.status(), r.observations());
    }
}