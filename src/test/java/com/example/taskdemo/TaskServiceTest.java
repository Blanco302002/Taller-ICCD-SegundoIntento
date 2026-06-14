package com.example.taskdemo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios de la capa de servicio.
 * El repositorio se SIMULA con Mockito, así que estos tests son rápidos y no
 * tocan ninguna base de datos real. Prueban la lógica del servicio aislada.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository; // repositorio falso (mock)

    @InjectMocks
    private TaskService service; // servicio con el mock inyectado

    @Test
    void createTask_guardaYDevuelveLaTarea() {
        Task input = new Task("Aprender CI/CD", false);
        when(repository.save(input)).thenReturn(input);

        Task result = service.createTask(input);

        assertThat(result.getTitle()).isEqualTo("Aprender CI/CD");
        verify(repository, times(1)).save(input);
    }

    @Test
    void getAllTasks_devuelveTodoLoDelRepositorio() {
        List<Task> tasks = List.of(new Task("A", false), new Task("B", true));
        when(repository.findAll()).thenReturn(tasks);

        List<Task> result = service.getAllTasks();

        assertThat(result).hasSize(2);
        verify(repository, times(1)).findAll();
    }

    @Test
    void deleteTask_llamaAlRepositorioConElId() {
        service.deleteTask(42L);

        // Acá el servicio no devuelve nada, así que verificamos que delegó bien.
        verify(repository, times(1)).deleteById(42L);
    }
}
