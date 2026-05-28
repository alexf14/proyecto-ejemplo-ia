---
name: setup-mcp-server
description: 'Genera la estructura completa de un MCP Server en Java con Spring Boot, incluyendo Tools, Resources y Prompts configurados para un dominio específico.'
license: MIT
compatibility: 'Java 21+, Spring Boot 3.x, MCP Java SDK 0.14+'
metadata:
  version: "1.0"
argument-hint: 'Dominio del server, ej: "task-manager" o "inventory-system"'
---

# Setup MCP Server Skill

Genera un MCP (Model Context Protocol) Server completo integrado con Spring Boot.

## Cómo invocar

```
/setup-mcp-server task-manager
```

## ¿Qué es un MCP Server?

Un MCP Server expone herramientas (Tools), datos (Resources) y plantillas de prompts
(Prompts) que los agentes de IA pueden usar para interactuar con tu aplicación.

```
┌────────────────────┐         ┌─────────────────────┐
│   GitHub Copilot   │◄──MCP──►│   Tu MCP Server     │
│   (AI Client)      │         │   (Spring Boot)     │
└────────────────────┘         └─────────┬───────────┘
                                         │
                               ┌─────────▼───────────┐
                               │  Tu base de datos   │
                               │  APIs externas      │
                               │  Lógica de negocio  │
                               └─────────────────────┘
```

## Estructura generada

```
src/main/java/com/example/mcp/
├── McpServerApplication.java        # Aplicación principal
├── config/
│   └── McpServerConfig.java         # Configuración del server MCP
├── tools/
│   ├── QueryToolHandler.java        # Tool: buscar datos
│   └── MutationToolHandler.java     # Tool: modificar datos
├── resources/
│   └── DataResourceHandler.java     # Resource: exponer datos
└── prompts/
    └── ReportPromptHandler.java     # Prompt: templates predefinidos
```

## Dependencias necesarias (pom.xml)

```xml
<!-- MCP Java SDK -->
<dependency>
    <groupId>io.modelcontextprotocol.sdk</groupId>
    <artifactId>mcp</artifactId>
    <version>0.14.1</version>
</dependency>

<!-- Para Spring Boot integration -->
<dependency>
    <groupId>io.modelcontextprotocol.sdk</groupId>
    <artifactId>mcp-spring-boot-starter</artifactId>
    <version>0.14.1</version>
</dependency>

<!-- Reactive Streams (requerido por MCP SDK) -->
<dependency>
    <groupId>io.projectreactor</groupId>
    <artifactId>reactor-core</artifactId>
</dependency>
```

## Reglas de Generación

### McpServerApplication.java

```java
@SpringBootApplication
public class McpServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(McpServerApplication.class, args);
    }
}
```

### McpServerConfig.java

```java
@Configuration
public class McpServerConfig {

    @Bean
    public McpServer mcpServer(List<ToolHandler> tools,
                               List<ResourceHandler> resources,
                               List<PromptHandler> prompts) {
        return McpServer.builder()
            .serverInfo("task-manager-mcp", "1.0.0")
            .tools(tools)
            .resources(resources)
            .prompts(prompts)
            .build();
    }
}
```

### Tool Handler (ejemplo para el dominio dado)

```java
@Component
public class QueryToolHandler implements ToolHandler {

    private final TaskService taskService;

    public QueryToolHandler(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
            .name("query_tasks")
            .description("Busca tareas por estado o palabra clave")
            .inputSchema(JsonSchema.object()
                .property("status", JsonSchema.string()
                    .description("PENDING, IN_PROGRESS, COMPLETED, CANCELLED"))
                .property("keyword", JsonSchema.string()
                    .description("Palabra clave para buscar"))
                .build())
            .build();
    }

    @Override
    public Mono<CallToolResult> handle(Map<String, Object> arguments) {
        String status = (String) arguments.get("status");
        String keyword = (String) arguments.get("keyword");
        // Implementar lógica de búsqueda
        var results = taskService.search(status, keyword);
        return Mono.just(CallToolResult.text(toJson(results)));
    }
}
```

### Resource Handler

```java
@Component
public class DataResourceHandler implements ResourceHandler {

    @Override
    public ResourceDefinition getDefinition() {
        return ResourceDefinition.builder()
            .uri("tasks://schema")
            .name("task-schema")
            .description("Esquema de datos de las tareas")
            .mimeType("application/json")
            .build();
    }

    @Override
    public Mono<ReadResourceResult> handle() {
        String schema = """
            {
                "entity": "Task",
                "fields": {
                    "id": "Long",
                    "title": "String (max 200)",
                    "description": "String (max 1000)",
                    "status": "PENDING | IN_PROGRESS | COMPLETED | CANCELLED",
                    "priority": "LOW | MEDIUM | HIGH | CRITICAL"
                }
            }
            """;
        return Mono.just(ReadResourceResult.text("tasks://schema", schema));
    }
}
```

### Prompt Handler

```java
@Component
public class ReportPromptHandler implements PromptHandler {

    @Override
    public PromptDefinition getDefinition() {
        return PromptDefinition.builder()
            .name("weekly-report")
            .description("Genera un informe semanal de tareas")
            .argument("format", "Formato: markdown, json, plain", false)
            .build();
    }

    @Override
    public Mono<GetPromptResult> handle(Map<String, String> arguments) {
        String format = arguments.getOrDefault("format", "markdown");
        var message = PromptMessage.user(
            "Genera un informe semanal de tareas en formato " + format +
            ". Incluye: resumen, tareas completadas, pendientes, bloqueadas y métricas."
        );
        return Mono.just(new GetPromptResult(List.of(message)));
    }
}
```

## Configuración del transporte

En `application.yml`:
```yaml
mcp:
  server:
    name: task-manager-mcp
    version: 1.0.0
    transport: stdio  # o 'sse' para HTTP
```

## Cómo usar el MCP Server desde Copilot

Configura en `.vscode/mcp.json` o en la configuración de VS Code:
```json
{
  "servers": {
    "task-manager": {
      "command": "java",
      "args": ["-jar", "target/mcp-server.jar"],
      "env": {
        "SPRING_PROFILES_ACTIVE": "mcp"
      }
    }
  }
}
```
