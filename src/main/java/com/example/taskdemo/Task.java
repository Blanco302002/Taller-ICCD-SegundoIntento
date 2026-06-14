package com.example.taskdemo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * La única entidad de dominio de esta demo.
 * Cada Task se guarda como una fila en la tabla "task" de la base H2.
 */
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // la base autoincrementa el id
    private Long id;

    private String title;

    private boolean completed;

    // JPA necesita un constructor sin argumentos.
    public Task() {
    }

    public Task(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    // --- Getters y setters (los usa Spring para mapear JSON <-> objeto) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
