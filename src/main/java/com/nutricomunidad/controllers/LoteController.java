package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.LoteRequest;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.models.Lote;
import com.nutricomunidad.security.RequiereRol;
import com.nutricomunidad.security.Roles;
import com.nutricomunidad.services.AccesoService;
import com.nutricomunidad.services.InventarioService;
import com.nutricomunidad.services.LoteService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/api/lotes")
public class LoteController {

    private final LoteService service;
    private final InventarioService inventarios;
    private final AccesoService acceso;

    public LoteController(LoteService service, InventarioService inventarios, AccesoService acceso) {
        this.service = service;
        this.inventarios = inventarios;
        this.acceso = acceso;
    }

    /** Una Organización sólo gestiona lotes de los inventarios de su organización. */
    private void exigirInventarioPropio(Integer idInventario, UsuarioToken usuario) {
        if (idInventario != null) {
            inventarios.buscar(idInventario).ifPresent(i -> acceso.exigirOrganizacion(usuario, i.idOrganizacion()));
        }
    }

    @GetMapping
    public List<Lote> listar(@RequestParam(required = false) Integer idInventario,
                             @RequestParam(required = false)
                             @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate caducaAntesDe) {
        return service.listar(idInventario, caducaAntesDe);
    }

    @GetMapping("/{id}")
    public Lote obtener(@PathVariable int id) {
        return service.obtener(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Lote crear(@Valid @RequestBody LoteRequest request, @RequestAttribute("usuario") UsuarioToken usuario) {
        exigirInventarioPropio(request.idInventario(), usuario);
        return service.crear(request);
    }

    @PutMapping("/{id}")
    @RequiereRol({Roles.ADMIN, Roles.ORGANIZACION})
    public Lote actualizar(@PathVariable int id, @Valid @RequestBody LoteRequest request,
                           @RequestAttribute("usuario") UsuarioToken usuario) {
        exigirInventarioPropio(service.obtener(id).idInventario(), usuario);
        exigirInventarioPropio(request.idInventario(), usuario);
        return service.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequiereRol(Roles.ADMIN)
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }
}
