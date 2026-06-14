package com.example.taskdemo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de Spring Data JPA.
 * Al extender JpaRepository obtenemos los métodos CRUD gratis
 * (save, findAll, findById, deleteById, ...) sin escribir implementación.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
}
