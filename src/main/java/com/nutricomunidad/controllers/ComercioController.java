package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.ComercioRequest;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.errors.Errores;
import com.nutricomunidad.models.Comercio;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.ComercioService;
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

@RestController
@RequestMapping("/api/comercios")
public class ComercioController {

    private static final String SIN_REGISTRO = "Tu cuenta aún no tiene un comercio registrado";

    private final ComercioService service;
    private final AccesoService acceso;

    public ComercioController(ComercioService service, AccesoService acceso) {
        this.service = service;
        this.acceso = acceso;
    }

    @GetMapping
    public List<Comercio> listar(@RequestParam(required = false) String buscar,
                                 @RequestParam(required = false) String tipo,
                                 @RequestParam(required = false) String municipio) {
        return service.listar(buscar, tipo, municipio);
    }

    @GetMapping("/mio")
    @RequiereRol(Roles.COMERCIO)
    public Comercio mio(@RequestAttribute("usuario") UsuarioToken usuario) {
        return acceso.comercioDe(usuario).orElseThrow(() -> Errores.noEncontrado(SIN_REGISTRO));
    }

    @PutMapping("/mio")
    @RequiereRol(Roles.COMERCIO)
    public Comercio actualizarMio(@Valid @RequestBody ComercioRequest request,
                                  @RequestAttribute("usuario") UsuarioToken usuario) {
        return service.actualizar(mio(usuario).idComercio(), request);
    }

    @GetMapping("/{id}")
    public Comercio obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public Comercio crear(@Valid @RequestBody ComercioRequest request,
                          @RequestAttribute("usuario") UsuarioToken usuario) {
        Integer propietario = acceso.propietarioParaAlta(usuario, Roles.COMERCIO, acceso.comercioDe(usuario).isPresent());
        return service.crear(request, propietario);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.COMERCIO})
    public Comercio actualizar(@PathVariable int id, @Valid @RequestBody ComercioRequest request,
                               @RequestAttribute("usuario") UsuarioToken usuario) {
        acceso.exigirComercio(usuario, id);
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }
}
