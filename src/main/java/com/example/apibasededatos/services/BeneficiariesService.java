package com.example.apibasededatos.services;

import com.example.apibasededatos.models.beneficiaries;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeneficiariesService {

    private final List<beneficiaries> listaBeneficiaries = new ArrayList<>();
    private int contadorId = 1;

    public List<beneficiaries> obtenerTodos() {
        return listaBeneficiaries;
    }

    public beneficiaries obtenerPorId(int id) {
        for (beneficiaries b : listaBeneficiaries) {
            if (b.id() == id) {
                return b;
            }
        }
        return null;
    }

    public beneficiaries crear(beneficiaries nuevo) {
        beneficiaries conId = new beneficiaries(
                contadorId++,
                nuevo.name(),
                nuevo.lastName(),
                nuevo.phoneNumber(),
                nuevo.mail(),
                nuevo.familyMembers(),
                nuevo.street(),
                nuevo.exteriorNumber(),
                nuevo.interiorNumber(),
                nuevo.neighborhood(),
                nuevo.postalCode(),
                nuevo.municipality(),
                nuevo.state()
        );
        listaBeneficiaries.add(conId);
        return conId;
    }

    public beneficiaries actualizar(int id, beneficiaries datos) {
        for (int i = 0; i < listaBeneficiaries.size(); i++) {
            if (listaBeneficiaries.get(i).id() == id) {
                beneficiaries actualizado = new beneficiaries(
                        id,
                        datos.name(),
                        datos.lastName(),
                        datos.phoneNumber(),
                        datos.mail(),
                        datos.familyMembers(),
                        datos.street(),
                        datos.exteriorNumber(),
                        datos.interiorNumber(),
                        datos.neighborhood(),
                        datos.postalCode(),
                        datos.municipality(),
                        datos.state()
                );
                listaBeneficiaries.set(i, actualizado);
                return actualizado;
            }
        }
        return null;
    }

    public boolean eliminar(int id) {
        return listaBeneficiaries.removeIf(b -> b.id() == id);
    }
}
