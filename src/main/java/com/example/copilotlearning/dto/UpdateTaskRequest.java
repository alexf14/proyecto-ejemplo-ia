package com.example.copilotlearning.dto;

import com.example.copilotlearning.model.Task.TaskPriority;
import com.example.copilotlearning.model.Task.TaskStatus;
import jakarta.validation.constraints.Size;

/**
 * Record para actualizar tareas. Todos los campos son opcionales.
 */
public record UpdateTaskRequest(
        @Size(max = 200, message = "El título no puede exceder 200 caracteres")
        String title,

        @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
        String description,

        TaskStatus status,

        TaskPriority priority
) {}
