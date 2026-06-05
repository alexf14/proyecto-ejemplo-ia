# 🤖 Copilot AI Learning - Proyecto Didáctico

> Aprende a usar **GitHub Copilot Agents, Instructions, Skills, Hooks, Agentic Workflows, Plugins y MCP Servers** con un proyecto real de Spring Boot + Java 21.

## 📋 ¿Qué es este proyecto?

Este proyecto es una **API REST de gestión de tareas** construida con Spring Boot 3.3 y Java 21.
Su objetivo principal NO es la aplicación en sí, sino servir como **laboratorio de aprendizaje**
para todas las herramientas de IA de GitHub Copilot.

## 🏗️ Estructura del Proyecto

```
copilot-ai-learning/
├── 📂 .github/
│   ├── 📄 copilot-instructions.md              ← Instrucciones globales del proyecto
│   ├── 📂 agents/
│   │   ├── 📄 java-architect.agent.md          ← Agente: Arquitecto Java
│   │   ├── 📄 test-writer.agent.md             ← Agente: Escritor de tests
│   │   ├── 📄 code-reviewer.agent.md           ← Agente: Revisor de código
│   │   └── 📄 mcp-expert.agent.md              ← Agente: Experto MCP
│   ├── 📂 instructions/
│   │   ├── 📄 java21.instructions.md           ← Instrucciones: Features Java 21
│   │   ├── 📄 springboot-api.instructions.md   ← Instrucciones: APIs REST
│   │   └── 📄 mcp-server.instructions.md       ← Instrucciones: MCP Servers
│   ├── 📂 skills/
│   │   ├── 📂 generate-crud/
│   │   │   └── 📄 SKILL.md                     ← Skill: Generador de CRUD
│   │   └── 📂 setup-mcp-server/
│   │       └── 📄 SKILL.md                     ← Skill: Scaffold MCP Server
│   ├── 📂 hooks/
│   │   └── 📂 pre-commit-check/
│   │       ├── 📄 hooks.json                   ← Configuración del hook
│   │       └── 📄 check-conventions.sh         ← Script de verificación
│   ├── 📂 workflows/                           ← ⚡ NUEVO: Agentic Workflows
│   │   ├── 📄 daily-stale-tasks-report.md      ← Informe diario de tareas
│   │   ├── 📄 pr-code-quality-check.md         ← Revisión automática de PRs
│   │   └── 📄 spring-boot-dependency-updater.md← Actualización de dependencias
│   ├── 📂 plugins/                             ← 🔌 NUEVO: Plugin del proyecto
│   │   └── 📂 copilot-ai-learning/
│   │       ├── 📄 README.md
│   │       └── 📂 .github/plugin/
│   │           └── 📄 plugin.json
│   └── 📂 mcp/                                ← 🌐 NUEVO: MCP Configuration
│       └── 📄 mcp.json                        ← Servers MCP disponibles
├── 📂 docs/
│   └── 📄 GUIA-COPILOT-AI.md                  ← Guía completa (español)
├── 📂 src/                                      ← Código fuente Java
├── 📄 pom.xml                                  ← Dependencias Maven
└── 📄 README.md                                ← Este archivo
```

## 🧩 Herramientas de IA incluidas

| Herramienta | Cantidad | Descripción |
|-------------|:--------:|-------------|
| 🤖 Agents | 4 | Arquitecto Java, Test Writer, Code Reviewer, MCP Expert |
| 📐 Instructions | 3 | Java 21, Spring Boot API, MCP Server |
| 🎯 Skills | 2 | Generate CRUD, Setup MCP Server |
| 🪝 Hooks | 1 | Verificador de convenciones |
| ⚡ Workflows | 3 | Stale tasks report, PR quality, Dep updater |
| 🔌 Plugins | 1 | Plugin del proyecto completo |
| 🌐 MCP Config | 1 | Configuración de MCP servers |

## 🚀 Inicio Rápido

### Prerrequisitos

- Java 21+
- Maven 3.9+
- VS Code con GitHub Copilot

### Ejecutar la aplicación

```bash
cd copilot-ai-learning
mvn clean package
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080/api/tasks`

### Endpoints disponibles

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/tasks` | Listar todas las tareas |
| GET | `/api/tasks/{id}` | Obtener tarea por ID |
| POST | `/api/tasks` | Crear nueva tarea |
| PUT | `/api/tasks/{id}` | Actualizar tarea |
| DELETE | `/api/tasks/{id}` | Eliminar tarea |
| GET | `/api/tasks/status/{status}` | Filtrar por estado |
| GET | `/api/tasks/search?keyword=X` | Buscar por título |

### Ejemplo de uso

```bash
# Crear una tarea
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Aprender Copilot", "description": "Estudiar agents y skills", "priority": "HIGH"}'
```

## 📚 Documentación de Herramientas Copilot

Para la guía completa de cada herramienta, consulta:
👉 **[docs/GUIA-COPILOT-AI.md](docs/GUIA-COPILOT-AI.md)**

## 🛠️ Stack Tecnológico

- **Java 21** - Records, Pattern Matching, Sealed Classes
- **Spring Boot 3.3** - Framework web
- **Spring Data JPA** - Persistencia
- **H2 Database** - Base de datos en memoria
- **JUnit 5 + Mockito + AssertJ** - Testing
- **Jakarta Validation** - Validación de inputs
- **MCP Java SDK** - Model Context Protocol (para el skill de MCP)

## 📄 Licencia

MIT - Uso libre para aprendizaje.
