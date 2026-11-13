package com.example.apibasededatos.services;

import com.example.apibasededatos.models.organizations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganizationsService {

    private final List<organizations> listaOrganizations = new ArrayList<>();
    private int contadorId = 1;

    public List<organizations> obtenerTodos() {
        return listaOrganizations;
    }

    public organizations obtenerPorId(int id) {
        for (organizations o : listaOrganizations) {
            if (o.id() == id) {
                return o;
            }
        }
        return null;
    }

    public organizations crear(organizations nueva) {
        organizations conId = new organizations(
                contadorId++,
                nueva.nameORG(),
                nueva.typeOfORG(),
                nueva.manager(),
                nueva.PhoneNumber(),
                nueva.mail(),
                nueva.street(),
                nueva.exteriorNumber(),
                nueva.interiorNumber(),
                nueva.neighborhood(),
                nueva.postalCode(),
                nueva.municipality(),
                nueva.state()
        );
        listaOrganizations.add(conId);
        return conId;
    }

    public organizations actualizar(int id, organizations datos) {
        for (int i = 0; i < listaOrganizations.size(); i++) {
            if (listaOrganizations.get(i).id() == id) {
                organizations actualizada = new organizations(
                        id,
                        datos.nameORG(),
                        datos.typeOfORG(),
                        datos.manager(),
                        datos.PhoneNumber(),
                        datos.mail(),
                        datos.street(),
                        datos.exteriorNumber(),
                        datos.interiorNumber(),
                        datos.neighborhood(),
                        datos.postalCode(),
                        datos.municipality(),
                        datos.state()
                );
                listaOrganizations.set(i, actualizada);
                return actualizada;
            }
        }
        return null;
    }

    public boolean eliminar(int id) {
        return listaOrganizations.removeIf(o -> o.id() == id);
    }
}
