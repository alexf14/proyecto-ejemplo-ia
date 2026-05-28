---
description: 'Estándares de codificación para Java 21: uso de records, pattern matching, sealed classes, text blocks y virtual threads.'
applyTo: '**/*.java'
---

# Instrucciones para Java 21

## Records (usar siempre para DTOs)

```java
// ✅ CORRECTO - Record como DTO
public record CreateTaskRequest(
    @NotBlank String title,
    @Size(max = 1000) String description,
    TaskPriority priority
) {
    // Constructor compacto para valores por defecto
    public CreateTaskRequest {
        if (priority == null) priority = TaskPriority.MEDIUM;
    }
}

// ❌ INCORRECTO - Clase tradicional como DTO
public class CreateTaskRequest {
    private String title; // boilerplate innecesario
    // getters, setters, equals, hashCode, toString...
}
```

## Pattern Matching con instanceof

```java
// ✅ CORRECTO - Pattern matching
if (obj instanceof Task task) {
    System.out.println(task.getTitle());
}

// ❌ INCORRECTO - Cast manual
if (obj instanceof Task) {
    Task task = (Task) obj;
    System.out.println(task.getTitle());
}
```

## Switch Expressions

```java
// ✅ CORRECTO - Switch expression
String label = switch (task.getPriority()) {
    case LOW -> "Baja prioridad";
    case MEDIUM -> "Prioridad media";
    case HIGH -> "Alta prioridad";
    case CRITICAL -> "¡Urgente!";
};

// ❌ INCORRECTO - Switch statement clásico
String label;
switch (task.getPriority()) {
    case LOW: label = "Baja"; break;
    case MEDIUM: label = "Media"; break;
    // ...
}
```

## Sealed Classes (para jerarquías cerradas)

```java
// ✅ CORRECTO - Sealed interface para eventos del dominio
public sealed interface TaskEvent
    permits TaskCreatedEvent, TaskUpdatedEvent, TaskDeletedEvent {
    Long taskId();
    LocalDateTime timestamp();
}

public record TaskCreatedEvent(Long taskId, String title, LocalDateTime timestamp)
    implements TaskEvent {}
```

## Text Blocks (para strings multilínea)

```java
// ✅ CORRECTO - Text block
String query = """
    SELECT t FROM Task t
    WHERE t.status = :status
    AND t.priority = :priority
    ORDER BY t.createdAt DESC
    """;

// ❌ INCORRECTO - Concatenación
String query = "SELECT t FROM Task t " +
    "WHERE t.status = :status " +
    "AND t.priority = :priority";
```

## Inferencia de tipos con var

```java
// ✅ CORRECTO - var cuando el tipo es obvio
var tasks = taskRepository.findAll();
var response = TaskResponse.fromEntity(task);
var now = LocalDateTime.now();

// ❌ INCORRECTO - var cuando oscurece el tipo
var result = processData(input); // ¿qué tipo devuelve?
```

## Colecciones inmutables

```java
// ✅ CORRECTO - toList() inmutable (Java 16+)
var activeTaskIds = tasks.stream()
    .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
    .map(Task::getId)
    .toList();

// ❌ INCORRECTO - Collectors.toList() mutable
var activeTaskIds = tasks.stream()
    .filter(t -> t.getStatus() == TaskStatus.IN_PROGRESS)
    .map(Task::getId)
    .collect(Collectors.toList());
```
