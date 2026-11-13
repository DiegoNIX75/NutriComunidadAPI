package com.example.apibasededatos.structures;

import java.util.ArrayList;
import java.util.List;

public class LinkedStack<T> {

    private Node<T> top;
    private int size;

    public void push(T value) {
        Node<T> node = new Node<>(value);
        node.next = top;
        top = node;
        size++;
    }

    public T pop() {
        if (top == null) {
            return null;
        }
        T value = top.value;
        top = top.next;
        size--;
        return value;
    }

    public T peek() {
        return top == null ? null : top.value;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public List<T> toList() {
        List<T> result = new ArrayList<>();
        for (Node<T> current = top; current != null; current = current.next) {
            result.add(current.value);
        }
        return result;
    }
}