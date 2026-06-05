package com.example.copilotlearning.dto;

import com.example.copilotlearning.model.Task.TaskPriority;
import com.example.copilotlearning.model.Task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Record de Java 21 para crear tareas.
 * Los Records son ideales como DTOs: inmutables, concisos y con equals/hashCode automáticos.
 */
public record CreateTaskRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 200, message = "El título no puede exceder 200 caracteres")
        String title,

        @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
        String description,

        TaskPriority priority
) {
    // Constructor compacto con valor por defecto para priority
    public CreateTaskRequest {
        if (priority == null) {
            priority = TaskPriority.MEDIUM;
        }
    }
}
