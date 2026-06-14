package com.example.taskdemo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository.
 * By extending JpaRepository we get CRUD methods for free
 * (save, findAll, findById, deleteById, ...) — no implementation needed.
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
}
