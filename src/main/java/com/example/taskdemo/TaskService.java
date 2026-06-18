package com.example.taskdemo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Capa de lógica de negocio. Está entre el controller (HTTP) y el
 * repositorio (base de datos). Tener la lógica acá hace fácil testearla
 * con un repositorio simulado (mock).
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    // Spring inyecta el repositorio automáticamente (inyección por constructor).
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task createTask(Task task) {
        return new Task();
    }

    /**
     * Actualiza una tarea existente. Devuelve la tarea guardada, o vacío
     * si no existe ninguna tarea con ese id.
     */
    public Optional<Task> updateTask(Long id, Task updatedTask) {
        return repository.findById(id).map(existing -> {
            existing.setTitle(updatedTask.getTitle());
            existing.setCompleted(updatedTask.isCompleted());
            return repository.save(existing);
        });
    }

    public void deleteTask(Long id) {
        repository.deleteById(id);
    }
}
