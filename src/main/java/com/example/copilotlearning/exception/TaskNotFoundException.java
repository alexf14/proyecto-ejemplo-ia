package com.example.copilotlearning.exception;

/**
 * Excepción lanzada cuando no se encuentra un recurso solicitado.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super("No se encontró la tarea con ID: " + id);
    }
}
