package com.nutricomunidad.services;

import com.nutricomunidad.dto.PreferenciaRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Preferencia;
import com.nutricomunidad.repositories.BeneficiarioRepository;
import com.nutricomunidad.repositories.Filtro;
import com.nutricomunidad.repositories.PreferenciaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PreferenciaService {

    private final PreferenciaRepository preferencias;
    private final BeneficiarioRepository beneficiarios;

    public PreferenciaService(PreferenciaRepository preferencias, BeneficiarioRepository beneficiarios) {
        this.preferencias = preferencias;
        this.beneficiarios = beneficiarios;
    }

    public List<Preferencia> listar(Integer idBeneficiario, String tipo) {
        return preferencias.listar(new Filtro()
                .igual("id_beneficiario", idBeneficiario)
                .igualSinMayusculas("tipo_preferencia", tipo));
    }

    public Preferencia obtener(int id) {
        return preferencias.buscarPorId(id).orElseThrow(() -> Errores.noEncontrado("Preferencia", id));
    }

    public Preferencia crear(PreferenciaRequest r) {
        Errores.validarReferencia(beneficiarios.existe(r.idBeneficiario()), "El beneficiario", r.idBeneficiario());
        return obtener(preferencias.crear(r));
    }

    public Preferencia actualizar(int id, PreferenciaRequest r) {
        if (!preferencias.existe(id)) {
            throw Errores.noEncontrado("Preferencia", id);
        }
        Errores.validarReferencia(beneficiarios.existe(r.idBeneficiario()), "El beneficiario", r.idBeneficiario());
        preferencias.actualizar(id, r);
        return obtener(id);
    }

    public void eliminar(int id) {
        if (!preferencias.eliminar(id)) {
            throw Errores.noEncontrado("Preferencia", id);
        }
    }
}
