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
 * Tests de integración: levantan el contexto completo de Spring y ejercitan
 * todo el stack (request HTTP -> controller -> service -> repositorio -> H2).
 *
 * - @SpringBootTest carga el contexto real de la aplicación.
 * - @AutoConfigureMockMvc nos da MockMvc para disparar requests HTTP simuladas.
 * - @ActiveProfiles("test") usa la base de datos de test aislada (en memoria).
 * - @Transactional hace rollback de los cambios en la base después de cada
 *   test, así los tests quedan independientes y arrancan en un estado limpio.
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
    void getTasks_devuelveLasTareasDeLaBase() throws Exception {
        // Preparación: metemos una tarea directo en la base H2.
        repository.save(new Task("Tarea existente", false));

        // Acción + verificación: GET /tasks debería devolverla por todo el stack.
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Tarea existente"))
                .andExpect(jsonPath("$[0].completed").value(false));
    }

    @Test
    void postTask_guardaLaTareaEnLaBase() throws Exception {
        Task newTask = new Task("Escribir test de integración", true);
        String json = objectMapper.writeValueAsString(newTask);

        // POST /tasks debería crear la tarea y devolverla con un id.
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Escribir test de integración"))
                .andExpect(jsonPath("$.completed").value(true));
    }
}
