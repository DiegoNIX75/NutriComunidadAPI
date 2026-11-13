package com.example.apibasededatos.services;

import com.example.apibasededatos.dto.CreateUserRequest;
import com.example.apibasededatos.models.users;
import com.example.apibasededatos.structures.SinglyLinkedList;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

    private final SinglyLinkedList<users> listaUsers = new SinglyLinkedList<>();
    private int contadorId = 1;

    public UsersService() {
        crear(new CreateUserRequest("comercio", "comercio@nutricomunidad.test", "comercio123"));
        crear(new CreateUserRequest("organizacion", "organizacion@nutricomunidad.test", "org123"));
        crear(new CreateUserRequest("beneficiario", "beneficiario@nutricomunidad.test", "bene123"));
    }

    public List<users> obtenerTodos() {
        return listaUsers.toList();
    }

    public users obtenerPorId(int id) {
        return listaUsers.find(u -> u.id() == id);
    }

    public users crear(CreateUserRequest request) {
        users nuevo = new users(contadorId++, request.name(), request.mail(), request.password());
        listaUsers.add(nuevo);
        return nuevo;
    }

    public users actualizar(int id, CreateUserRequest request) {
        users actualizado = new users(id, request.name(), request.mail(), request.password());
        return listaUsers.replace(u -> u.id() == id, actualizado) ? actualizado : null;
    }

    public boolean eliminar(int id) {
        return listaUsers.remove(u -> u.id() == id) != null;
    }
}