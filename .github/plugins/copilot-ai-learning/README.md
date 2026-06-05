# Copilot AI Learning Plugin

Plugin didáctico que agrupa agents, skills y comandos para aprender a desarrollar con
GitHub Copilot en proyectos Java Spring Boot.

## Instalación

```bash
# Usando Copilot CLI
copilot plugin install copilot-ai-learning@tu-usuario
```

## Qué Incluye

### Agents

| Agent | Descripción |
|-------|-------------|
| `java-architect` | Revisa arquitectura Java, sugiere patrones SOLID |
| `test-writer` | Genera tests JUnit 5 + Mockito + AssertJ |
| `code-reviewer` | Busca bugs, vulnerabilidades y problemas de rendimiento |
| `mcp-expert` | Experto en crear MCP servers con Spring Boot |

### Skills (Slash Commands)

| Comando | Descripción |
|---------|-------------|
| `/copilot-ai-learning:generate-crud` | Genera CRUD completo (Entity, DTO, Repo, Service, Controller, Tests) |
| `/copilot-ai-learning:setup-mcp-server` | Scaffold de un MCP Server con Spring Boot |

## Compatibilidad

- Java 21+
- Spring Boot 3.x
- Maven o Gradle

## Licencia

MIT
