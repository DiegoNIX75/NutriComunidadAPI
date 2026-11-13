package com.example.apibasededatos.services;

import com.example.apibasededatos.models.users;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersService {

    private final List<users> listaUsers = new ArrayList<>();
    private int contadorId = 1;


    public List<users> obtenerTodos() {
        return listaUsers;
    }


    public users obtenerPorId(int id) {
        for (users u : listaUsers) {
            if (u.id() == id) {
                return u;
            }
        }
        return null;
    }


    public users crear(users nuevoUsuario) {
        users usuarioConId = new users(
                contadorId++,
                nuevoUsuario.name(),
                nuevoUsuario.mail(),
                nuevoUsuario.password()
        );
        listaUsers.add(usuarioConId);
        return usuarioConId;
    }

    public users actualizar(int id, users datosActualizados) {
        for (int i = 0; i < listaUsers.size(); i++) {
            if (listaUsers.get(i).id() == id) {
                users actualizado = new users(
                        id,
                        datosActualizados.name(),
                        datosActualizados.mail(),
                        datosActualizados.password()
                );
                listaUsers.set(i, actualizado);
                return actualizado;
            }
        }
        return null;
    }


    public boolean eliminar(int id) {
        return listaUsers.removeIf(u -> u.id() == id);
    }
}
