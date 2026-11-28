package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.PreferenciaRequest;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.models.Preferencia;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.PreferenciaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Preferencias alimentarias. Un Beneficiario sólo gestiona las de su propio beneficiario. */
@RestController
@RequestMapping("/api/preferencias")
@RequiereRol({Roles.ADMIN, Roles.ORGANIZACION, Roles.BENEFICIARIO})
public class PreferenciaController {

    private final PreferenciaService service;
    private final AccesoService acceso;

    public PreferenciaController(PreferenciaService service, AccesoService acceso) {
        this.service = service;
        this.acceso = acceso;
    }

    @GetMapping
    public List<Preferencia> listar(@RequestParam(required = false) Integer idBeneficiario,
                                    @RequestParam(required = false) String tipo,
                                    @RequestAttribute("usuario") UsuarioToken usuario) {
        if (AccesoService.tieneRol(usuario, Roles.BENEFICIARIO)) {
            idBeneficiario = acceso.idBeneficiarioOSinResultados(usuario);
        }
        return service.listar(idBeneficiario, tipo);
    }

    @GetMapping("/{id}")
    public Preferencia obtener(@PathVariable int id, @RequestAttribute("usuario") UsuarioToken usuario) {
        Preferencia preferencia = service.obtener(id);
        acceso.exigirBeneficiario(usuario, preferencia.idBeneficiario());
        return preferencia;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Preferencia crear(@Valid @RequestBody PreferenciaRequest request,
                             @RequestAttribute("usuario") UsuarioToken usuario) {
        acceso.exigirBeneficiario(usuario, request.idBeneficiario());
        return service.crear(request);
    }

    @PutMapping("/{id}")
    public Preferencia actualizar(@PathVariable int id, @Valid @RequestBody PreferenciaRequest request,
                                  @RequestAttribute("usuario") UsuarioToken usuario) {
        obtener(id, usuario);
        acceso.exigirBeneficiario(usuario, request.idBeneficiario());
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable int id, @RequestAttribute("usuario") UsuarioToken usuario) {
        obtener(id, usuario);
        service.eliminar(id);
    }
}
