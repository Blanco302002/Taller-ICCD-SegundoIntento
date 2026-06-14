package com.example.taskdemo;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Business logic layer. Sits between the controller (HTTP) and the
 * repository (database). Keeping logic here makes it easy to unit test
 * with a mocked repository.
 */
@Service
public class TaskService {

    private final TaskRepository repository;

    // Spring injects the repository automatically (constructor injection).
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task createTask(Task task) {
        return repository.save(task);
    }

    /**
     * Updates an existing task. Returns the saved task, or empty if no
     * task with that id exists.
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
