package com.example.apibasededatos.services;

import com.example.apibasededatos.models.donations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DonationsService {

    private final List<donations> listaDonations = new ArrayList<>();
    private int contadorId = 1;


    public List<donations> obtenerTodos() {
        return listaDonations;
    }


    public donations obtenerPorId(int id) {
        for (donations d : listaDonations) {
            if (d.id() == id) {
                return d;
            }
        }
        return null;
    }


    public donations crear(donations nuevaDonacion) {
        donations donacionConId = new donations(
                contadorId++,
                nuevaDonacion.donationDate(),
                nuevaDonacion.totalQuantity(),
                nuevaDonacion.status(),
                nuevaDonacion.observations()
        );
        listaDonations.add(donacionConId);
        return donacionConId;
    }


    public donations actualizar(int id, donations datos) {
        for (int i = 0; i < listaDonations.size(); i++) {
            if (listaDonations.get(i).id() == id) {
                donations actualizada = new donations(
                        id,
                        datos.donationDate(),
                        datos.totalQuantity(),
                        datos.status(),
                        datos.observations()
                );
                listaDonations.set(i, actualizada);
                return actualizada;
            }
        }
        return null;
    }


    public boolean eliminar(int id) {
        return listaDonations.removeIf(d -> d.id() == id);
    }
}
