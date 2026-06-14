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
 * Unit tests for the service layer.
 * The repository is MOCKED with Mockito, so these tests are fast and do
 * not touch any real database. They check the service logic in isolation.
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository repository; // fake repository

    @InjectMocks
    private TaskService service; // service with the mock injected

    @Test
    void createTask_savesAndReturnsTask() {
        Task input = new Task("Learn CI/CD", false);
        when(repository.save(input)).thenReturn(input);

        Task result = service.createTask(input);

        assertThat(result.getTitle()).isEqualTo("Learn CI/CD");
        verify(repository, times(1)).save(input);
    }

    @Test
    void getAllTasks_returnsEverythingFromRepository() {
        List<Task> tasks = List.of(new Task("A", false), new Task("B", true));
        when(repository.findAll()).thenReturn(tasks);

        List<Task> result = service.getAllTasks();

        assertThat(result).hasSize(2);
        verify(repository, times(1)).findAll();
    }

    @Test
    void deleteTask_callsRepositoryWithId() {
        service.deleteTask(42L);

        // The service has no return value here, so we verify it delegated correctly.
        verify(repository, times(1)).deleteById(42L);
    }
}
