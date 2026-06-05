package com.example.copilotlearning.service;

import com.example.copilotlearning.dto.CreateTaskRequest;
import com.example.copilotlearning.dto.UpdateTaskRequest;
import com.example.copilotlearning.exception.TaskNotFoundException;
import com.example.copilotlearning.model.Task;
import com.example.copilotlearning.model.Task.TaskPriority;
import com.example.copilotlearning.model.Task.TaskStatus;
import com.example.copilotlearning.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para TaskService usando Mockito.
 * Demuestra buenas prácticas de testing:
 * - Aislamiento con mocks
 * - AssertJ para aserciones legibles
 * - Prueba de casos positivos y excepciones
 */
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    private Task sampleTask;

    @BeforeEach
    void setUp() {
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setTitle("Tarea de prueba");
        sampleTask.setDescription("Descripción de prueba");
        sampleTask.setStatus(TaskStatus.PENDING);
        sampleTask.setPriority(TaskPriority.HIGH);
    }

    @Test
    void shouldCreateTask() {
        var request = new CreateTaskRequest("Nueva tarea", "Descripción", TaskPriority.HIGH);
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        var response = taskService.createTask(request);

        assertThat(response.title()).isEqualTo("Tarea de prueba");
        assertThat(response.priority()).isEqualTo(TaskPriority.HIGH);
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldGetTaskById() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));

        var response = taskService.getTaskById(1L);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Tarea de prueba");
    }

    @Test
    void shouldThrowWhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(99L))
                .isInstanceOf(TaskNotFoundException.class);
    }

    @Test
    void shouldUpdateTask() {
        var request = new UpdateTaskRequest("Título actualizado", null, TaskStatus.IN_PROGRESS, null);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        taskService.updateTask(1L, request);

        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void shouldGetAllTasks() {
        when(taskRepository.findAll()).thenReturn(List.of(sampleTask));

        var tasks = taskService.getAllTasks();

        assertThat(tasks).hasSize(1);
        assertThat(tasks.getFirst().title()).isEqualTo("Tarea de prueba");
    }
}
