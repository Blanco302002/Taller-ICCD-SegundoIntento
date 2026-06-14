package com.example.taskdemo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller exposing CRUD endpoints under /tasks.
 * @CrossOrigin lets the static frontend (opened from a file or another port)
 * call this API from the browser without CORS errors.
 */
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    // GET /tasks  -> list all tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return service.getAllTasks();
    }

    // POST /tasks -> create a new task
    @PostMapping
    public Task createTask(@RequestBody Task task) {
        return service.createTask(task);
    }

    // PUT /tasks/{id} -> update an existing task (404 if it doesn't exist)
    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        return service.updateTask(id, task)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /tasks/{id} -> delete a task
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        service.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
