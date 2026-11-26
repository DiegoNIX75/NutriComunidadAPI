package com.nutricomunidad.services;

import com.nutricomunidad.dto.AlergiaRequest;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Alergia;
import com.nutricomunidad.models.Preferencia;
import com.nutricomunidad.repositories.AlergiaRepository;
import com.nutricomunidad.repositories.AlimentoRepository;
import com.nutricomunidad.repositories.PreferenciaRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AlergiaService {

    private final AlergiaRepository alergias;
    private final PreferenciaRepository preferencias;
    private final AlimentoRepository alimentos;
    private final BeneficiarioService beneficiarios;

    public AlergiaService(AlergiaRepository alergias, PreferenciaRepository preferencias,
                          AlimentoRepository alimentos, BeneficiarioService beneficiarios) {
        this.alergias = alergias;
        this.preferencias = preferencias;
        this.alimentos = alimentos;
        this.beneficiarios = beneficiarios;
    }

    public List<Alergia> listar(int idBeneficiario) {
        beneficiarios.asegurarExiste(idBeneficiario);
        return alergias.listarPorBeneficiario(idBeneficiario);
    }

    public List<Alergia> agregar(int idBeneficiario, AlergiaRequest r) {
        beneficiarios.asegurarExiste(idBeneficiario);
        Preferencia preferencia = preferencias.buscarPorId(r.idPreferencias())
                .orElseThrow(() -> Errores.peticionInvalida("La preferencia con id " + r.idPreferencias() + " no existe"));
        if (preferencia.idBeneficiario() != idBeneficiario) {
            throw Errores.peticionInvalida("La preferencia indicada no pertenece al beneficiario");
        }
        Errores.validarReferencia(alimentos.existe(r.idAlimento()), "El alimento", r.idAlimento());
        if (alergias.existe(r.idPreferencias(), r.idAlimento(), idBeneficiario)) {
            throw Errores.conflicto("La alergia ya está registrada para este beneficiario");
        }
        alergias.crear(r.idPreferencias(), r.idAlimento(), idBeneficiario);
        return alergias.listarPorBeneficiario(idBeneficiario);
    }

    public void eliminar(int idBeneficiario, int idPreferencias, int idAlimento) {
        if (!alergias.eliminar(idPreferencias, idAlimento, idBeneficiario)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "La alergia indicada no existe");
        }
    }
}
