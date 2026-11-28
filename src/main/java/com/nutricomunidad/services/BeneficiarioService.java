package com.nutricomunidad.services;

import com.nutricomunidad.dto.BeneficiarioRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Beneficiario;
import com.nutricomunidad.repositories.BeneficiarioRepository;
import com.nutricomunidad.repositories.Filtro;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BeneficiarioService {

    private final BeneficiarioRepository beneficiarios;

    public BeneficiarioService(BeneficiarioRepository beneficiarios) {
        this.beneficiarios = beneficiarios;
    }

    public List<Beneficiario> listar(String buscar, String municipio) {
        return beneficiarios.listar(new Filtro()
                .contiene(buscar, "nombre_beneficiario", "ap", "am", "correo")
                .igualSinMayusculas("municipio", municipio));
    }

    public Beneficiario obtener(int id) {
        return beneficiarios.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Beneficiario", id));
    }

    public Beneficiario crear(BeneficiarioRequest r, Integer idUsuario) {
        return obtener(beneficiarios.crear(r, idUsuario));
    }

    public Beneficiario actualizar(int id, BeneficiarioRequest r) {
        asegurarExiste(id);
        beneficiarios.actualizar(id, r);
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!beneficiarios.eliminar(id)) {
            throw Errores.noEncontrado("Beneficiario", id);
        }
    }

    /** Lanza 404 si el beneficiario de la ruta no existe. */
    public void asegurarExiste(int id) {
        if (!beneficiarios.existe(id)) {
            throw Errores.noEncontrado("Beneficiario", id);
        }
    }
}
