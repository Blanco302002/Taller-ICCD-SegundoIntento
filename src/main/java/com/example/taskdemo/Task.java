package com.example.taskdemo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * The single domain entity of this demo.
 * Each Task is stored as one row in the "task" table of the H2 database.
 */
@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-increments the id
    private Long id;

    private String title;

    private boolean completed;

    // JPA requires a no-args constructor.
    public Task() {
    }

    public Task(String title, boolean completed) {
        this.title = title;
        this.completed = completed;
    }

    // --- Getters and setters (used by Spring for JSON <-> object mapping) ---

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
