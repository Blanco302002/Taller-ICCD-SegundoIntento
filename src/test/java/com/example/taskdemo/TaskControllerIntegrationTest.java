package com.example.taskdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests: they boot the full Spring context and exercise the
 * whole stack (HTTP request -> controller -> service -> repository -> H2).
 *
 * - @SpringBootTest loads the real application context.
 * - @AutoConfigureMockMvc gives us MockMvc to fire fake HTTP requests.
 * - @ActiveProfiles("test") uses the isolated test database.
 * - @Transactional rolls back the DB changes after each test, so tests
 *   stay independent and start from a clean state.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository repository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getTasks_returnsTasksFromDatabase() throws Exception {
        // Arrange: put one task directly in the H2 database.
        repository.save(new Task("Existing task", false));

        // Act + Assert: GET /tasks should return it through the full stack.
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Existing task"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void postTask_savesTaskToDatabase() throws Exception {
        Task newTask = new Task("Write integration test", true);
        String json = objectMapper.writeValueAsString(newTask);

        // POST /tasks should create the task and echo it back with an id.
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Write integration test"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}
