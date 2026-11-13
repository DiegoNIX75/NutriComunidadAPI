package com.example.apibasededatos.services;

import com.example.apibasededatos.models.businesses;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BusinessesService {

    private final List<businesses> listaBusinesses = new ArrayList<>();
    private int contadorId = 1;


    public List<businesses> obtenerTodos() {
        return listaBusinesses;
    }


    public businesses obtenerPorId(int id) {
        for (businesses b : listaBusinesses) {
            if (b.id() == id) {
                return b;
            }
        }
        return null;
    }


    public businesses crear(businesses nuevoNegocio) {
        businesses negocioConId = new businesses(
                contadorId++,
                nuevoNegocio.BusinessName(),
                nuevoNegocio.LegalName(),
                nuevoNegocio.typeOfBusiness(),
                nuevoNegocio.phoneNumber(),
                nuevoNegocio.mail(),
                nuevoNegocio.street(),
                nuevoNegocio.exteriorNumber(),
                nuevoNegocio.interiorNumber(),
                nuevoNegocio.neighborhood(),
                nuevoNegocio.postalCode(),
                nuevoNegocio.municipality(),
                nuevoNegocio.state()
        );
        listaBusinesses.add(negocioConId);
        return negocioConId;
    }


    public businesses actualizar(int id, businesses datos) {
        for (int i = 0; i < listaBusinesses.size(); i++) {
            if (listaBusinesses.get(i).id() == id) {
                businesses actualizado = new businesses(
                        id,
                        datos.BusinessName(),
                        datos.LegalName(),
                        datos.typeOfBusiness(),
                        datos.phoneNumber(),
                        datos.mail(),
                        datos.street(),
                        datos.exteriorNumber(),
                        datos.interiorNumber(),
                        datos.neighborhood(),
                        datos.postalCode(),
                        datos.municipality(),
                        datos.state()
                );
                listaBusinesses.set(i, actualizado);
                return actualizado;
            }
        }
        return null;
    }


    public boolean eliminar(int id) {
        return listaBusinesses.removeIf(b -> b.id() == id);
    }
}
