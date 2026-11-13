package com.example.apibasededatos.structures;

class Node<T> {
    T value;
    Node<T> next;

    Node(T value) {
        this.value = value;
    }
}