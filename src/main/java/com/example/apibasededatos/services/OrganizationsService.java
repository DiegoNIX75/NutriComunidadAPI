package com.example.apibasededatos.services;

import com.example.apibasededatos.dto.CreateOrganizationRequest;
import com.example.apibasededatos.models.organizations;
import com.example.apibasededatos.structures.SinglyLinkedList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrganizationsService {

    private final SinglyLinkedList<organizations> listaOrganizations = new SinglyLinkedList<>();
    private int contadorId = 1;

    public OrganizationsService() {
        crear(new CreateOrganizationRequest("Banco de Alimentos Norte", "Banco de alimentos", "Laura Méndez", "5544556677",
                "laura.mendez@banconorte.org", "Av. Universidad", "300", "", "Narvarte", "03020", "Benito Juárez", "Ciudad de México"));
        crear(new CreateOrganizationRequest("Comedor Esperanza", "Comedor comunitario", "Carlos Díaz", "5544556688",
                "contacto@comedoresperanza.org", "Calle Allende", "27", "", "Obrera", "06800", "Cuauhtémoc", "Ciudad de México"));
        crear(new CreateOrganizationRequest("Casa Hogar Luz", "Casa hogar", "Patricia Soto", "5544556699",
                "info@casahogarluz.org", "Av. Zaragoza", "150", "", "Moctezuma", "15530", "Venustiano Carranza", "Ciudad de México"));
    }

    public List<organizations> obtenerTodos() {
        return listaOrganizations.toList();
    }

    public organizations obtenerPorId(int id) {
        return listaOrganizations.find(o -> o.id() == id);
    }

    public organizations crear(CreateOrganizationRequest request) {
        organizations nueva = construir(contadorId++, request);
        listaOrganizations.add(nueva);
        return nueva;
    }

    public organizations actualizar(int id, CreateOrganizationRequest request) {
        organizations actualizada = construir(id, request);
        return listaOrganizations.replace(o -> o.id() == id, actualizada) ? actualizada : null;
    }

    public boolean eliminar(int id) {
        return listaOrganizations.remove(o -> o.id() == id) != null;
    }

    private organizations construir(int id, CreateOrganizationRequest r) {
        return new organizations(id, r.nameORG(), r.typeOfORG(), r.manager(), r.phoneNumber(), r.mail(),
                r.street(), r.exteriorNumber(), r.interiorNumber(), r.neighborhood(), r.postalCode(),
                r.municipality(), r.state());
    }
}
