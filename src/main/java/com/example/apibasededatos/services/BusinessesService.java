package com.example.apibasededatos.services;

import com.example.apibasededatos.dto.CreateBusinessRequest;
import com.example.apibasededatos.models.businesses;
import com.example.apibasededatos.structures.SinglyLinkedList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessesService {

    private final SinglyLinkedList<businesses> listaBusinesses = new SinglyLinkedList<>();
    private int contadorId = 1;

    public BusinessesService() {
        crear(new CreateBusinessRequest("Frutería La Huerta", "La Huerta S.A. de C.V.", "Frutería", "5512340001",
                "contacto@lahuerta.com", "Calle Morelos", "12", "", "Centro", "06000", "Cuauhtémoc", "Ciudad de México"));
        crear(new CreateBusinessRequest("Panadería San José", "Panificadora San José S.A.", "Panadería", "5512340002",
                "ventas@sanjose.com", "Av. Juárez", "88", "", "Juárez", "06600", "Cuauhtémoc", "Ciudad de México"));
        crear(new CreateBusinessRequest("Súper Ahorro", "Comercializadora Ahorro S.A.", "Supermercado", "5512340003",
                "info@superahorro.com", "Av. Tlalpan", "450", "", "Portales", "03300", "Benito Juárez", "Ciudad de México"));
    }

    public List<businesses> obtenerTodos() {
        return listaBusinesses.toList();
    }

    public businesses obtenerPorId(int id) {
        return listaBusinesses.find(b -> b.id() == id);
    }

    public businesses crear(CreateBusinessRequest request) {
        businesses nuevo = construir(contadorId++, request);
        listaBusinesses.add(nuevo);
        return nuevo;
    }

    public businesses actualizar(int id, CreateBusinessRequest request) {
        businesses actualizado = construir(id, request);
        return listaBusinesses.replace(b -> b.id() == id, actualizado) ? actualizado : null;
    }

    public boolean eliminar(int id) {
        return listaBusinesses.remove(b -> b.id() == id) != null;
    }

    private businesses construir(int id, CreateBusinessRequest r) {
        return new businesses(id, r.businessName(), r.legalName(), r.typeOfBusiness(), r.phoneNumber(), r.mail(),
                r.street(), r.exteriorNumber(), r.interiorNumber(), r.neighborhood(), r.postalCode(),
                r.municipality(), r.state());
    }
}