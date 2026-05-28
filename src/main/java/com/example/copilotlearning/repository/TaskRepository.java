package com.example.copilotlearning.repository;

import com.example.copilotlearning.model.Task;
import com.example.copilotlearning.model.Task.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para la entidad Task.
 * Spring Data genera la implementación automáticamente a partir de la interfaz.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByStatus(TaskStatus status);

    List<Task> findByPriority(Task.TaskPriority priority);

    List<Task> findByTitleContainingIgnoreCase(String keyword);
}
