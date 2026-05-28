package com.example.copilotlearning.dto;

import com.example.copilotlearning.model.Task;
import com.example.copilotlearning.model.Task.TaskPriority;
import com.example.copilotlearning.model.Task.TaskStatus;
import java.time.LocalDateTime;

/**
 * Record de respuesta con toda la información de una tarea.
 * Incluye un método factory estático para mapear desde la entidad.
 */
public record TaskResponse(
        Long id,
        String title,
        String description,
        TaskStatus status,
        TaskPriority priority,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    /**
     * Convierte una entidad Task a un TaskResponse.
     * Patrón factory method para encapsular la lógica de mapeo.
     */
    public static TaskResponse fromEntity(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
