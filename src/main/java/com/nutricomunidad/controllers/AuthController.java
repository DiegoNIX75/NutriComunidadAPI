package com.nutricomunidad.controllers;

import com.nutricomunidad.dto.CambioContrasenaRequest;
import com.nutricomunidad.dto.LoginRequest;
import com.nutricomunidad.dto.LoginResponse;
import com.nutricomunidad.dto.Mensaje;
import com.nutricomunidad.dto.PerfilRequest;
import com.nutricomunidad.dto.RegistroRequest;
import com.nutricomunidad.dto.UsuarioResponse;
import com.nutricomunidad.dto.UsuarioToken;
import com.nutricomunidad.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse registrar(@Valid @RequestBody RegistroRequest request) {
        return authService.registrar(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/yo")
    public UsuarioResponse yo(@RequestAttribute("usuario") UsuarioToken usuario) {
        return authService.yo(usuario.id());
    }

    @PutMapping("/yo")
    public UsuarioResponse actualizarPerfil(@RequestAttribute("usuario") UsuarioToken usuario,
                                            @Valid @RequestBody PerfilRequest request) {
        return authService.actualizarPerfil(usuario.id(), request);
    }

    @PutMapping("/yo/contrasena")
    public Mensaje cambiarContrasena(@RequestAttribute("usuario") UsuarioToken usuario,
                                     @Valid @RequestBody CambioContrasenaRequest request) {
        authService.cambiarContrasena(usuario.id(), request);
        return new Mensaje("Contraseña actualizada correctamente");
    }
}
