package com.example.apibasededatos.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SinglyLinkedList<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;

    public void add(T value) {
        Node<T> node = new Node<>(value);
        if (head == null) {
            head = node;
        } else {
            tail.next = node;
        }
        tail = node;
        size++;
    }

    public T find(Predicate<T> condition) {
        for (Node<T> current = head; current != null; current = current.next) {
            if (condition.test(current.value)) {
                return current.value;
            }
        }
        return null;
    }

    public boolean replace(Predicate<T> condition, T newValue) {
        for (Node<T> current = head; current != null; current = current.next) {
            if (condition.test(current.value)) {
                current.value = newValue;
                return true;
            }
        }
        return false;
    }

    public T remove(Predicate<T> condition) {
        Node<T> previous = null;
        for (Node<T> current = head; current != null; previous = current, current = current.next) {
            if (condition.test(current.value)) {
                if (previous == null) {
                    head = current.next;
                } else {
                    previous.next = current.next;
                }
                if (current == tail) {
                    tail = previous;
                }
                size--;
                return current.value;
            }
        }
        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public List<T> toList() {
        List<T> result = new ArrayList<>();
        for (Node<T> current = head; current != null; current = current.next) {
            result.add(current.value);
        }
        return result;
    }
}