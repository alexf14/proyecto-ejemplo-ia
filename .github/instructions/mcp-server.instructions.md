---
description: 'Convenciones para desarrollar MCP Servers con Java y Spring Boot: definición de Tools, Resources, Prompts y configuración de transporte.'
applyTo: '**/mcp/**/*.java, **/tools/**/*.java, **/prompts/**/*.java'
---

# Instrucciones para MCP Server Development

## ¿Qué es MCP?

Model Context Protocol (MCP) es un estándar abierto que permite a los agentes de IA
conectarse con herramientas, datos y servicios externos de forma segura y estructurada.

## Conceptos Clave

### Tools (Herramientas)

Son **acciones** que el agente puede ejecutar. Equivalen a funciones/métodos.

```java
// ✅ CORRECTO - Tool bien definida con descripción clara
@McpTool(
    name = "create_task",
    description = "Crea una nueva tarea en el sistema de gestión"
)
public Mono<CallToolResult> createTask(
    @McpParam(description = "Título de la tarea (max 200 chars)", required = true)
    String title,
    @McpParam(description = "Prioridad: LOW, MEDIUM, HIGH, CRITICAL", required = false)
    String priority
) { /* implementación */ }

// ❌ INCORRECTO - Sin descripciones útiles
@McpTool(name = "ct")
public Mono<CallToolResult> ct(String t, String p) { /* ... */ }
```

### Resources (Recursos)

Son **datos de solo lectura** que el agente puede consultar.

```java
// ✅ CORRECTO - URI descriptiva y tipo MIME definido
@McpResource(
    uri = "tasks://active-count",
    name = "active-task-count",
    description = "Número actual de tareas activas (no completadas/canceladas)",
    mimeType = "application/json"
)
public Mono<ReadResourceResult> getActiveCount() {
    long count = taskRepository.countByStatusNotIn(List.of(COMPLETED, CANCELLED));
    return Mono.just(ReadResourceResult.text("tasks://active-count",
        """
        {"activeCount": %d, "timestamp": "%s"}
        """.formatted(count, Instant.now())));
}
```

### Prompts (Plantillas de Prompt)

Son **templates reutilizables** que el agente ofrece al usuario.

```java
// ✅ CORRECTO - Prompt con argumentos documentados
@McpPrompt(
    name = "sprint-review",
    description = "Genera contenido para una review de sprint"
)
public Mono<GetPromptResult> sprintReview(
    @McpParam(description = "Número del sprint", required = true)
    String sprintNumber,
    @McpParam(description = "Período: 'last-week', 'last-2-weeks'", required = false)
    String period
) {
    // Construir el prompt con contexto
}
```

## Convenciones de Naming

| Elemento | Formato | Ejemplo |
|----------|---------|---------|
| Tool name | snake_case | `query_tasks`, `create_task` |
| Resource URI | scheme://path | `tasks://list`, `config://db` |
| Resource name | kebab-case | `task-list`, `db-config` |
| Prompt name | kebab-case | `sprint-review`, `bug-report` |
| Parámetros | camelCase en Java | `sprintNumber`, `taskStatus` |

## Estructura de Proyecto MCP

```
src/main/java/com/example/mcp/
├── config/
│   └── McpServerConfig.java      # Beans de configuración
├── tools/
│   ├── QueryTools.java            # Tools de lectura
│   └── MutationTools.java         # Tools de escritura
├── resources/
│   └── DataResources.java         # Resources expuestos
└── prompts/
    └── ReportPrompts.java         # Templates de prompts
```

## Manejo de Errores en MCP

```java
// ✅ CORRECTO - Errores informativos para el agente
@McpTool(name = "get_task", description = "Obtiene una tarea por su ID")
public Mono<CallToolResult> getTask(@McpParam(description = "ID numérico de la tarea") String taskId) {
    try {
        long id = Long.parseLong(taskId);
        var task = taskService.getById(id);
        return Mono.just(CallToolResult.text(toJson(task)));
    } catch (NumberFormatException e) {
        return Mono.just(CallToolResult.error("El ID debe ser un número válido, recibido: " + taskId));
    } catch (TaskNotFoundException e) {
        return Mono.just(CallToolResult.error("No existe tarea con ID " + taskId));
    }
}
```

## Seguridad

- NUNCA exponer operaciones destructivas sin confirmación.
- Validar TODOS los inputs del agente (pueden contener prompt injection).
- Limitar el scope de las tools al mínimo necesario.
- Usar `@McpParam(required = true)` para parámetros obligatorios.
- No incluir secrets en las respuestas de Resources.
