package com.example.copilotlearning.service;

import com.example.copilotlearning.dto.CreateTaskRequest;
import com.example.copilotlearning.dto.TaskResponse;
import com.example.copilotlearning.dto.UpdateTaskRequest;
import com.example.copilotlearning.exception.TaskNotFoundException;
import com.example.copilotlearning.model.Task;
import com.example.copilotlearning.model.Task.TaskStatus;
import com.example.copilotlearning.repository.TaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de lógica de negocio para tareas.
 * Aplica las mejores prácticas de Spring Boot:
 * - Inyección por constructor (inmutable)
 * - Logging con SLF4J
 * - Transacciones declarativas
 * - Separación clara entre DTOs y entidades
 */
@Service
@Transactional(readOnly = true)
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskResponse> getAllTasks() {
        logger.debug("Obteniendo todas las tareas");
        return taskRepository.findAll().stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    public TaskResponse getTaskById(Long id) {
        logger.debug("Buscando tarea con ID: {}", id);
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponse.fromEntity(task);
    }

    @Transactional
    public TaskResponse createTask(CreateTaskRequest request) {
        logger.info("Creando nueva tarea: {}", request.title());

        Task task = new Task();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setPriority(request.priority());

        Task saved = taskRepository.save(task);
        logger.info("Tarea creada con ID: {}", saved.getId());
        return TaskResponse.fromEntity(saved);
    }

    @Transactional
    public TaskResponse updateTask(Long id, UpdateTaskRequest request) {
        logger.info("Actualizando tarea con ID: {}", id);

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        if (request.title() != null) {
            task.setTitle(request.title());
        }
        if (request.description() != null) {
            task.setDescription(request.description());
        }
        if (request.status() != null) {
            task.setStatus(request.status());
        }
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }

        Task updated = taskRepository.save(task);
        return TaskResponse.fromEntity(updated);
    }

    @Transactional
    public void deleteTask(Long id) {
        logger.info("Eliminando tarea con ID: {}", id);
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }

    public List<TaskResponse> getTasksByStatus(TaskStatus status) {
        logger.debug("Buscando tareas con estado: {}", status);
        return taskRepository.findByStatus(status).stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }

    public List<TaskResponse> searchTasks(String keyword) {
        logger.debug("Buscando tareas con keyword: {}", keyword);
        return taskRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(TaskResponse::fromEntity)
                .toList();
    }
}
