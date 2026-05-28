# 📖 Guía Completa: GitHub Copilot AI Tools

> Guía didáctica para entender y usar Agents, Instructions, Skills, Hooks, Workflows,
> Plugins y MCP Servers de GitHub Copilot en un proyecto Java Spring Boot.

---

## 📑 Índice

1. [Visión General](#-visión-general)
2. [Copilot Instructions (Instrucciones Globales)](#-copilot-instructions)
3. [Agents (Agentes)](#-agents-agentes)
4. [Instructions (Instrucciones de Archivo)](#-instructions-instrucciones-de-archivo)
5. [Skills (Habilidades)](#-skills-habilidades)
6. [Hooks (Ganchos de Eventos)](#-hooks-ganchos-de-eventos)
7. [Agentic Workflows (Flujos Agénticos)](#-agentic-workflows-flujos-agénticos)
8. [Plugins (Paquetes Instalables)](#-plugins-paquetes-instalables)
9. [MCP Servers (Model Context Protocol)](#-mcp-servers-model-context-protocol)
10. [Flujo de Trabajo Completo](#-flujo-de-trabajo-completo)
11. [Recursos Adicionales](#-recursos-adicionales)

---

## 🌐 Visión General

GitHub Copilot ofrece varias herramientas para personalizar y potenciar la experiencia de
desarrollo con IA. Cada una tiene un propósito diferente:

| Herramienta | ¿Qué es? | ¿Cuándo usarla? |
|-------------|-----------|-----------------|
| **Copilot Instructions** | Reglas globales del proyecto | Siempre (se aplican a todo el repo) |
| **Agents** | Asistentes especializados | Cuando necesitas un experto en algo concreto |
| **Instructions** | Reglas por tipo de archivo | Cuando escribes código de un tipo específico |
| **Skills** | Tareas complejas predefinidas | Para automatizar procesos repetitivos |
| **Hooks** | Automatizaciones por eventos | Para validaciones o acciones automáticas |
| **Agentic Workflows** | Automatización con IA en GitHub Actions | Para tareas recurrentes del repo (triage, reports) |
| **Plugins** | Paquetes instalables de agents+skills | Para distribuir y compartir configuraciones |
| **MCP Servers** | Conexión de IA con herramientas externas | Para que Copilot acceda a APIs, DBs, servicios |

### Analogía simple

Imagina un equipo de cocina:
- **Copilot Instructions** = Las reglas generales de la cocina (higiene, orden, horarios)
- **Agents** = Los chefs especializados (pastelero, parrillero, salsero)
- **Instructions** = Las recetas específicas para cada plato
- **Skills** = Los procesos automatizados (la máquina de pasta, el horno programado)
- **Hooks** = Los controles de calidad automáticos (termómetro que avisa si algo se quema)
- **Workflows** = El turno nocturno del restaurante (limpieza, inventario, pedidos automáticos)
- **Plugins** = Un kit completo de herramientas que compras en una tienda profesional
- **MCP Servers** = Los proveedores externos (carnicero, pescadero) conectados por teléfono

---

## 📋 Copilot Instructions

### ¿Qué son?

Son instrucciones **globales** que Copilot aplica SIEMPRE que trabaja en tu proyecto.
Se definen en `.github/copilot-instructions.md` y afectan a todas las sugerencias.

### Ubicación en este proyecto

```
.github/copilot-instructions.md
```

### ¿Para qué sirven?

- Definir las convenciones generales del proyecto.
- Establecer el stack tecnológico esperado.
- Indicar patrones de código preferidos.
- Configurar estilos de naming y estructura.

### ¿Cómo funcionan?

Copilot lee este archivo automáticamente cuando trabajas en el repositorio.
No necesitas invocarlas manualmente; están siempre activas.

### Estructura recomendada

```markdown
# Nombre del Proyecto

## Contexto
Breve descripción del proyecto y su propósito.

## Convenciones de Código
- Regla 1
- Regla 2

## Stack Tecnológico
- Java 21
- Spring Boot 3.x

## Patrones
- Inyección por constructor
- Records para DTOs
```

### Ejemplo real (este proyecto)

Nuestro archivo define:
- Usar Java 21 con Records para DTOs
- Inyección por constructor (nunca `@Autowired` en campos)
- Logging con SLF4J parametrizado
- Tests con patrón `should<Resultado>When<Condición>()`

### 💡 Mejores prácticas

1. **Sé específico**: "Usar Records para DTOs" es mejor que "Usar features modernas".
2. **Incluye ejemplos**: Muestra el patrón esperado vs el que quieres evitar.
3. **No te excedas**: Mantén el archivo bajo 2000 palabras.
4. **Actualiza regularmente**: Las convenciones evolucionan con el proyecto.

---

## 🤖 Agents (Agentes)

### ¿Qué son?

Son **asistentes especializados** que puedes invocar para tareas específicas.
Cada agente tiene un rol definido, herramientas disponibles y un modelo de IA asignado.

### Ubicación en este proyecto

```
.github/agents/
├── java-architect.agent.md
├── test-writer.agent.md
└── code-reviewer.agent.md
```

### ¿Para qué sirven?

- **java-architect**: Revisa tu arquitectura, sugiere patrones SOLID, valida diseño.
- **test-writer**: Genera tests completos siguiendo las convenciones del proyecto.
- **code-reviewer**: Busca bugs, vulnerabilidades y problemas de rendimiento.

### ¿Cómo se usan?

En VS Code con Copilot Chat, invoca al agente por su nombre:

```
@java-architect ¿Este servicio cumple con SOLID?
@test-writer Genera tests para TaskService
@code-reviewer Revisa los cambios de mi último commit
```

### Anatomía de un archivo .agent.md

```markdown
---
name: 'Nombre Legible del Agente'
description: 'Descripción de qué hace el agente'
tools:
  - codebase     # Acceso al código del proyecto
  - terminal     # Puede ejecutar comandos
model: gpt-4o    # Modelo de IA a usar
---

# Título del Agente

Eres [rol del agente]. Tu misión es [objetivo principal].

## Reglas
1. Regla uno
2. Regla dos

## Formato de respuesta
Cómo debe responder el agente.
```

### Campos del frontmatter

| Campo | Obligatorio | Descripción |
|-------|:-----------:|-------------|
| `name` | ✅ | Nombre legible (se muestra en la UI) |
| `description` | ✅ | Qué hace el agente (max ~200 caracteres) |
| `tools` | Recomendado | Herramientas disponibles: `codebase`, `terminal`, `web` |
| `model` | Recomendado | Modelo de IA: `gpt-4o`, `claude-sonnet`, etc. |

### 💡 Mejores prácticas para Agents

1. **Un agente, un rol**: No mezcles revisión de código con generación de tests.
2. **Instrucciones claras**: Sé explícito sobre cómo debe responder.
3. **Formato de salida**: Define cómo quieres las respuestas (markdown, código, tablas).
4. **Ejemplos**: Incluye ejemplos de input/output esperado.
5. **Limitaciones**: Indica qué NO debe hacer el agente.

### Ejemplo detallado: Java Architect Agent

```
@java-architect Revisa la clase TaskService

Respuesta esperada:
✅ CORRECTO: Inyección por constructor con campo private final
✅ CORRECTO: Transacciones configuradas correctamente
🔴 PROBLEMA: Falta validación de null en updateTask
   📁 Archivo: service/TaskService.java:67
   💡 Sugerencia: Añadir Objects.requireNonNull() o validación previa
```

---

## 📐 Instructions (Instrucciones de Archivo)

### ¿Qué son?

Son reglas de código que se aplican **solo a archivos que coincidan con un patrón**.
A diferencia de Copilot Instructions (globales), estas son específicas por tipo de archivo.

### Ubicación en este proyecto

```
.github/instructions/
├── java21.instructions.md          # Se aplica a **/*.java
└── springboot-api.instructions.md  # Se aplica a *Controller.java, *Service.java, dto/*.java
```

### ¿Para qué sirven?

- Definir convenciones específicas por tipo de archivo.
- Enseñar a Copilot patrones de código para tu stack.
- Mostrar con ejemplos qué es correcto y qué evitar.
- Asegurar consistencia en el código generado.

### ¿Cómo funcionan?

Copilot aplica estas instrucciones **automáticamente** cuando estás editando un archivo
que coincide con el patrón `applyTo`. No necesitas invocarlas.

### Anatomía de un archivo .instructions.md

```markdown
---
description: 'Descripción de las instrucciones'
applyTo: '**/*.java'  # Patrón glob de archivos afectados
---

# Título

## Sección 1
Contenido con ejemplos...
```

### Campos del frontmatter

| Campo | Obligatorio | Descripción |
|-------|:-----------:|-------------|
| `description` | ✅ | Qué enseñan estas instrucciones |
| `applyTo` | ✅ | Patrón glob: `**/*.java`, `**/dto/*.java`, etc. |

### Patrones glob comunes

| Patrón | Se aplica a |
|--------|-------------|
| `**/*.java` | Todos los archivos Java |
| `**/*Controller.java` | Solo Controllers |
| `**/test/**/*.java` | Solo archivos de test |
| `**/*.java, **/*.kt` | Java y Kotlin |
| `**/dto/*.java` | Solo DTOs |

### 💡 Mejores prácticas para Instructions

1. **Usa ejemplos ✅/❌**: Muestra qué hacer y qué NO hacer.
2. **Sé práctico**: Incluye snippets de código copiar/pegar.
3. **Aplica a lo justo**: No pongas `**/*` si solo aplica a Java.
4. **Complementa, no repitas**: No repitas lo que ya está en copilot-instructions.md.
5. **Actualiza con el stack**: Si migras de Java 17 a 21, actualiza los ejemplos.

### Diferencia con Copilot Instructions

| Aspecto | copilot-instructions.md | .instructions.md |
|---------|------------------------|-------------------|
| Alcance | Todo el proyecto | Solo archivos que coincidan |
| Activación | Siempre activas | Solo al editar archivos matching |
| Cantidad | 1 archivo | Múltiples, por tipo |
| Propósito | Reglas generales | Reglas específicas por tecnología |

---

## 🎯 Skills (Habilidades)

### ¿Qué son?

Son **tareas complejas predefinidas** que el agente puede ejecutar bajo demanda.
Una skill es una carpeta con un `SKILL.md` que define un proceso completo,
opcionalmente con scripts, plantillas o datos de referencia.

### Ubicación en este proyecto

```
.github/skills/
└── generate-crud/
    └── SKILL.md    # Definición de la skill
```

### ¿Para qué sirven?

- Automatizar tareas repetitivas (generar CRUD, documentación, tests).
- Encapsular conocimiento experto en un proceso reproducible.
- Incluir recursos de referencia (templates, scripts, datos).
- Crear flujos de trabajo complejos invocables con un comando.

### ¿Cómo se usan?

Las skills se invocan con una instrucción específica:

```
/generate-crud Product name:String price:Double category:String
```

O instalándolas desde el repositorio awesome-copilot:

```bash
gh skills install github/awesome-copilot spring-boot-testing
```

### Anatomía de una Skill

```
mi-skill/
├── SKILL.md              # Definición (obligatorio)
├── assets/               # Recursos opcionales
│   └── templates/
│       └── entity.java.template
├── scripts/              # Scripts auxiliares
│   └── generate.sh
└── references/           # Documentación de referencia
    └── patterns.md
```

### Estructura del SKILL.md

```markdown
---
name: mi-skill                    # Nombre (lowercase con guiones)
description: 'Descripción de la skill (10-1024 caracteres)'
license: MIT
compatibility: 'Java 21+, Spring Boot 3.x'
metadata:
  version: "1.0"
argument-hint: 'Hint de qué argumentos espera'
---

# Nombre de la Skill

Descripción de qué hace.

## Cómo invocar
Instrucciones de uso.

## Proceso
Pasos que sigue la skill.

## Reglas
Restricciones y convenciones.
```

### Campos del frontmatter

| Campo | Obligatorio | Descripción |
|-------|:-----------:|-------------|
| `name` | ✅ | ID de la skill (lowercase, guiones, max 64 chars) |
| `description` | ✅ | Qué hace (10-1024 caracteres, en single quotes) |
| `license` | Opcional | Licencia (MIT, Apache-2.0, etc.) |
| `compatibility` | Opcional | Requisitos del entorno |
| `metadata.version` | Opcional | Versión de la skill |
| `argument-hint` | Opcional | Ejemplo de argumentos |

### 💡 Mejores prácticas para Skills

1. **Una skill, una tarea**: No mezcles generación de código con deployment.
2. **Incluye ejemplos**: Muestra input → output esperado.
3. **Define el proceso paso a paso**: El agente necesita pasos claros.
4. **Incluye validación**: Indica cómo verificar que el resultado es correcto.
5. **Referencia archivos del proyecto**: Usa la estructura actual como ejemplo.

### Diferencia Skills vs Instructions vs Agents

| Aspecto | Instructions | Agents | Skills |
|---------|-------------|--------|--------|
| Activación | Automática | Manual (`@agent`) | Manual (`/skill`) |
| Propósito | Reglas de estilo | Conversación experta | Tareas complejas |
| Complejidad | Baja | Media | Alta |
| Archivos extras | No | No | Sí (scripts, templates) |
| Output | Código inline | Respuesta en chat | Archivos generados |

---

## 🪝 Hooks (Ganchos de Eventos)

### ¿Qué son?

Son **scripts o comandos** que se ejecutan automáticamente cuando ocurre un evento
específico durante una sesión de Copilot (por ejemplo, al finalizar una sesión).

### Ubicación en este proyecto

```
.github/hooks/
└── pre-commit-check/
    ├── hooks.json              # Configuración del hook
    └── check-conventions.sh    # Script que se ejecuta
```

### ¿Para qué sirven?

- Validar convenciones de código automáticamente.
- Escanear secrets antes de commitear.
- Ejecutar linters o formatters.
- Generar documentación automática.
- Verificar que los tests pasan.

### ¿Cómo funcionan?

Los hooks se ejecutan **automáticamente** cuando ocurre el evento configurado.
No necesitas invocarlos manualmente.

### Eventos disponibles

| Evento | ¿Cuándo se ejecuta? |
|--------|---------------------|
| `sessionStart` | Al iniciar una sesión de Copilot |
| `sessionEnd` | Al finalizar una sesión de Copilot |

### Anatomía del hooks.json

```json
{
  "version": 1,
  "hooks": {
    "sessionEnd": [
      {
        "type": "command",
        "bash": ".github/hooks/pre-commit-check/check-conventions.sh",
        "cwd": ".",
        "env": {
          "CHECK_MODE": "warn",
          "PROJECT_TYPE": "spring-boot"
        },
        "timeoutSec": 30
      }
    ]
  }
}
```

### Campos de configuración

| Campo | Obligatorio | Descripción |
|-------|:-----------:|-------------|
| `version` | ✅ | Siempre `1` |
| `hooks` | ✅ | Objeto con eventos como keys |
| `type` | ✅ | Tipo de hook: `"command"` |
| `bash` | ✅ | Ruta al script a ejecutar |
| `cwd` | Opcional | Directorio de trabajo (default: `.`) |
| `env` | Opcional | Variables de entorno para el script |
| `timeoutSec` | Opcional | Timeout en segundos (default: 30) |

### 💡 Mejores prácticas para Hooks

1. **Rápidos**: No más de 30 segundos de ejecución.
2. **No destructivos**: Un hook no debe modificar código sin permiso.
3. **Informativos**: Muestra mensajes claros de qué está verificando.
4. **Configurables**: Usa variables de entorno para modos (warn vs error).
5. **Idempotentes**: Ejecutar el mismo hook varias veces da el mismo resultado.

### Ejemplo de nuestro Hook

El hook `pre-commit-check` de este proyecto verifica:

1. ✅ No hay `@Autowired` en campos (debe ser por constructor).
2. ✅ No hay `System.out.println` (debe usar SLF4J).
3. ✅ Los DTOs son Records (no clases tradicionales).
4. ✅ Los Controllers usan `ResponseEntity`.
5. ✅ Hay tests para cada clase principal.

---

## ⚡ Agentic Workflows (Flujos Agénticos)

### ¿Qué son?

Son **automatizaciones con IA que se ejecutan en GitHub Actions**. A diferencia de los
workflows clásicos (YAML con pasos determinísticos), los Agentic Workflows usan un agente
de IA (Copilot, Claude, Codex) que interpreta instrucciones en lenguaje natural.

### Ubicación en este proyecto

```
.github/workflows/
├── daily-stale-tasks-report.md           ← Informe diario de tareas estancadas
├── pr-code-quality-check.md              ← Revisión automática de PRs
└── spring-boot-dependency-updater.md     ← Actualización semanal de deps
```

### ¿Para qué sirven?

- **Triage automático**: Clasificar issues nuevos, asignar labels.
- **Informes periódicos**: Generar resúmenes diarios/semanales.
- **Revisión de PRs**: Analizar calidad de código automáticamente.
- **Mantenimiento**: Detectar dependencias desactualizadas, comentarios stale.
- **Seguridad**: Escaneos periódicos de vulnerabilidades.

### ¿Cómo funcionan?

1. Se definen en un archivo `.md` en `.github/workflows/`.
2. Se compilan a un `.lock.yml` con `gh aw compile`.
3. GitHub Actions ejecuta el workflow en el trigger definido.
4. El agente de IA lee el markdown y actúa según las instrucciones.
5. Las acciones de escritura pasan por **Safe Outputs** (guardrails de seguridad).

### Anatomía de un Agentic Workflow

```markdown
---
name: "Nombre del Workflow"
description: 'Qué hace este workflow'
on:
  schedule: daily on weekdays          # Trigger: cuándo se ejecuta
  # Opciones: schedule, issues, pull_request, workflow_dispatch, slash_command
permissions:
  contents: read                       # Permisos mínimos necesarios
  issues: read
engine: copilot                        # Motor de IA a usar
tools:
  github:
    toolsets: [default]                # Herramientas GitHub disponibles
  bash: true                           # Acceso a bash
mcp-servers:                           # MCP servers externos (opcional)
  deepwiki:
    url: "https://mcp.deepwiki.com/sse"
    allowed: ["ask_question"]
safe-outputs:                          # Acciones permitidas (guardrails)
  create-issue:
    title-prefix: "[report] "
    labels: [automation]
  add-comment:
    max: 1
  create-pull-request:
    draft: true
    title-prefix: "[ai] "
timeout-minutes: 20
---

# Instrucciones en Lenguaje Natural

El agente de IA interpreta este markdown y actúa en consecuencia.
Puedes usar variables como ${{ github.event.issue.number }}.
```

### Campos del frontmatter

| Campo | Obligatorio | Descripción |
|-------|:-----------:|-------------|
| `name` | ✅ | Nombre legible del workflow |
| `description` | ✅ | Descripción breve |
| `on` | ✅ | Trigger: `schedule`, `issues`, `pull_request`, `slash_command` |
| `permissions` | ✅ | Permisos GitHub (principio de mínimo privilegio) |
| `safe-outputs` | ✅ | Acciones de escritura permitidas |
| `engine` | Opcional | Motor IA: `copilot` (default) |
| `tools` | Opcional | Herramientas disponibles para el agente |
| `mcp-servers` | Opcional | MCP servers externos |
| `timeout-minutes` | Opcional | Timeout (default: 20) |

### Triggers disponibles

| Trigger | Ejemplo | Descripción |
|---------|---------|-------------|
| `schedule` | `daily on weekdays` | Programado (diario, semanal) |
| `issues` | `types: [opened]` | Cuando se abre/modifica un issue |
| `pull_request` | `types: [opened, synchronize]` | Cuando se abre/actualiza un PR |
| `workflow_dispatch` | - | Manual desde la UI |
| `slash_command` | `name: review` | Comando `/review` en un issue/PR |

### Safe Outputs (Guardrails de Seguridad)

Los Safe Outputs son el mecanismo de seguridad clave. El agente NO puede escribir
directamente en el repo; solo puede proponer acciones que pasan por un "gate":

```yaml
safe-outputs:
  create-issue:
    title-prefix: "[ai] "       # Prefijo obligatorio en el título
    labels: [automation]         # Labels automáticos
    close-older-issues: true     # Cierra issues previos del mismo workflow

  add-comment:
    max: 1                       # Máximo 1 comentario por ejecución

  create-pull-request:
    max: 1
    draft: true                  # Solo PRs en borrador
    title-prefix: "[ai] "
    labels: [automation]
    if-no-changes: warn          # Qué hacer si no hay cambios
```

### 💡 Mejores prácticas para Workflows

1. **Principio de mínimo privilegio**: Solo permisos `read` salvo que necesites escribir.
2. **Safe Outputs siempre**: Nunca des acceso directo de escritura al agente.
3. **Timeout corto**: 15-20 minutos máximo para evitar costes.
4. **Instrucciones claras**: El agente interpreta el markdown literal.
5. **Idempotencia**: El workflow debería poder ejecutarse múltiples veces sin problema.
6. **Variables de contexto**: Usa `${{ github.event.* }}` para datos dinámicos.

### Diferencia con GitHub Actions clásico

| Aspecto | GitHub Actions (YAML) | Agentic Workflows (MD) |
|---------|----------------------|------------------------|
| Lenguaje | YAML determinístico | Markdown con lenguaje natural |
| Lógica | Steps/Jobs predefinidos | IA interpreta instrucciones |
| Flexibilidad | Rígido pero predecible | Flexible pero no determinístico |
| Uso | CI/CD, builds, deploys | Triage, reports, mantenimiento |
| Seguridad | Permisos por token | Safe Outputs + Threat Detection |

### CLI para Agentic Workflows

```bash
# Compilar un workflow (genera .lock.yml)
gh aw compile my-workflow

# Inspeccionar MCP servers configurados
gh aw mcp inspect my-workflow

# Ver estado de los workflows
gh aw status

# Ejecutar manualmente
gh aw run my-workflow
```

---

## 🔌 Plugins (Paquetes Instalables)

### ¿Qué son?

Son **paquetes que agrupan agents, skills y comandos** relacionados alrededor de un
tema específico. Permiten distribuir y compartir configuraciones de Copilot de forma
organizada.

### Ubicación en este proyecto

```
.github/plugins/
└── copilot-ai-learning/
    ├── .github/plugin/
    │   └── plugin.json          ← Manifiesto del plugin
    └── README.md                ← Documentación del plugin
```

### ¿Para qué sirven?

- **Distribuir** configuraciones de Copilot como un paquete instalable.
- **Agrupar** agents + skills + commands que van juntos.
- **Compartir** con la comunidad o tu equipo.
- **Instalar** configuraciones de otros con un solo comando.
- **Versionar** cambios en tu configuración de IA.

### ¿Cómo se usan?

```bash
# Instalar un plugin desde awesome-copilot
copilot plugin install java-development@awesome-copilot

# Instalar un plugin externo
copilot plugin install mi-plugin@mi-usuario

# Listar plugins instalados
copilot plugin list
```

### Anatomía del plugin.json

```json
{
  "name": "mi-plugin",
  "description": "Descripción de qué hace el plugin.",
  "version": "1.0.0",
  "author": {
    "name": "Tu Nombre"
  },
  "repository": "https://github.com/usuario/repo",
  "license": "MIT",
  "keywords": ["java", "spring-boot", "testing"],
  "agents": [
    "./agents/mi-agente.agent.md"
  ],
  "skills": [
    "./skills/mi-skill"
  ]
}
```

### Campos del plugin.json

| Campo | Obligatorio | Descripción |
|-------|:-----------:|-------------|
| `name` | ✅ | ID del plugin (lowercase, guiones, = nombre de carpeta) |
| `description` | ✅ | Qué hace el plugin |
| `version` | ✅ | Versión semántica (e.g., "1.0.0") |
| `author` | Opcional | Autor con campo `name` |
| `repository` | Opcional | URL del repositorio |
| `license` | Opcional | Licencia (MIT, Apache-2.0...) |
| `keywords` | Opcional | Array de tags para búsqueda |
| `agents` | Opcional | Array de rutas a archivos .agent.md |
| `skills` | Opcional | Array de rutas a carpetas de skills |

### Plugins útiles para Java (instalables desde awesome-copilot)

| Plugin | Descripción |
|--------|-------------|
| `java-development` | Spring Boot, Quarkus, JUnit, Javadoc |
| `java-mcp-development` | Crear MCP Servers en Java con Spring Boot |
| `modernize-java` | Migrar/modernizar aplicaciones Java |
| `openapi-to-application-java-spring-boot` | Generar app desde OpenAPI spec |

### 💡 Mejores prácticas para Plugins

1. **Un tema por plugin**: `java-testing`, no `java-everything`.
2. **Keywords descriptivos**: Facilitan la búsqueda.
3. **README claro**: Tabla con los agents/skills/commands incluidos.
4. **Versionado semántico**: MAJOR para breaking changes, MINOR para features.
5. **Rutas relativas**: Los paths en `agents`/`skills` son relativos al plugin.

---

## 🌐 MCP Servers (Model Context Protocol)

### ¿Qué son?

MCP (Model Context Protocol) es un **estándar abierto** que permite a agentes de IA
conectarse con herramientas externas, bases de datos y servicios. Un MCP Server expone
capacidades que el agente puede usar de forma segura y estructurada.

### Ubicación en este proyecto

```
.github/
├── mcp/
│   └── mcp.json                       ← Configuración de MCP servers
├── instructions/
│   └── mcp-server.instructions.md     ← Convenciones para crear MCP servers
├── agents/
│   └── mcp-expert.agent.md            ← Agente experto en MCP
└── skills/
    └── setup-mcp-server/
        └── SKILL.md                   ← Skill para generar MCP servers
```

### ¿Para qué sirven?

- **Conectar Copilot con tu app**: El agente puede consultar tu base de datos.
- **Exponer APIs internas**: Sin abrir endpoints públicos.
- **Automatizar tareas**: El agente puede ejecutar acciones en tu sistema.
- **Compartir contexto**: Datos, esquemas, documentación en tiempo real.

### Conceptos clave de MCP

```
┌─────────────────────────────────────────────────────────────┐
│                    MCP SERVER                                 │
├─────────────────────────────────────────────────────────────┤
│                                                              │
│  🔧 TOOLS (Herramientas)      📦 RESOURCES (Recursos)       │
│  ─────────────────────        ────────────────────────       │
│  Acciones que el agente       Datos de solo lectura          │
│  puede EJECUTAR               que el agente puede LEER       │
│                                                              │
│  • query_tasks                • tasks://list                 │
│  • create_task                • tasks://schema               │
│  • update_status              • config://database            │
│                                                              │
│  📝 PROMPTS (Plantillas)                                     │
│  ────────────────────────                                    │
│  Templates de prompts que                                    │
│  el agente puede OFRECER                                    │
│                                                              │
│  • weekly-report                                             │
│  • bug-analysis                                              │
│  • sprint-review                                             │
│                                                              │
└─────────────────────────────────────────────────────────────┘
```

| Concepto | ¿Qué es? | Analogía |
|----------|-----------|----------|
| **Tool** | Acción ejecutable | Un botón que hace algo |
| **Resource** | Dato de solo lectura | Un panel informativo |
| **Prompt** | Template de prompt reutilizable | Una plantilla de formulario |

### Tipos de MCP Server

| Tipo | Transporte | Cuándo usarlo |
|------|-----------|---------------|
| **Stdio** | stdin/stdout | Aplicaciones locales, CLI |
| **SSE/HTTP** | HTTP Server-Sent Events | Servidores remotos, servicios web |
| **Docker** | Container con stdin/stdout | Aislamiento, reproducibilidad |

### Configuración en VS Code (.github/mcp/mcp.json)

```json
{
  "servers": {
    "mi-app-local": {
      "command": "java",
      "args": ["-jar", "target/app.jar"],
      "env": { "SPRING_PROFILES_ACTIVE": "mcp" }
    },
    "github-mcp": {
      "command": "docker",
      "args": ["run", "--rm", "-i", "ghcr.io/github/github-mcp-server"],
      "env": { "GITHUB_PERSONAL_ACCESS_TOKEN": "${GITHUB_TOKEN}" }
    },
    "servicio-remoto": {
      "url": "https://api.ejemplo.com/mcp",
      "transport": "sse"
    }
  }
}
```

### Configuración en Agentic Workflows

```yaml
mcp-servers:
  # MCP Server remoto por HTTP/SSE
  deepwiki:
    url: "https://mcp.deepwiki.com/sse"
    allowed: ["read_wiki_contents", "ask_question"]

  # MCP Server en Docker
  custom-tool:
    container: "ghcr.io/mi-org/mi-mcp-server:v1.0"
    env:
      API_KEY: "${{ secrets.API_KEY }}"
    allowed: ["*"]

  # MCP Server por comando
  local-tool:
    command: "uvx"
    args: ["mi-herramienta-mcp"]
    allowed: ["buscar", "analizar"]
```

### Crear un MCP Server con Spring Boot (ejemplo)

```java
// 1. Dependencia en pom.xml
// <groupId>io.modelcontextprotocol.sdk</groupId>
// <artifactId>mcp-spring-boot-starter</artifactId>

// 2. Definir un Tool
@Component
public class TaskQueryTool implements ToolHandler {

    private final TaskService taskService;

    public TaskQueryTool(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    public ToolDefinition getDefinition() {
        return ToolDefinition.builder()
            .name("query_tasks")
            .description("Busca tareas por estado")
            .inputSchema(JsonSchema.object()
                .property("status", JsonSchema.string()
                    .description("PENDING, IN_PROGRESS, COMPLETED"))
                .build())
            .build();
    }

    @Override
    public Mono<CallToolResult> handle(Map<String, Object> args) {
        String status = (String) args.get("status");
        var tasks = taskService.getByStatus(TaskStatus.valueOf(status));
        return Mono.just(CallToolResult.text(toJson(tasks)));
    }
}
```

### MCP Servers disponibles (GitHub Registry)

| Server | Descripción |
|--------|-------------|
| **GitHub MCP** | Acceso a repos, issues, PRs, Actions |
| **DeepWiki** | Documentación de proyectos open source |
| **Notion** | Páginas y bases de datos de Notion |
| **Sentry** | Errores y alertas de producción |
| **Slack** | Mensajes y canales de Slack |
| **Brave Search** | Búsqueda web |
| **Jupyter** | Ejecución de código y notebooks |

### 💡 Mejores prácticas para MCP

1. **Naming snake_case**: Tools en `snake_case` (`query_tasks`, no `queryTasks`).
2. **Descripciones claras**: El agente decide qué tool usar según la descripción.
3. **Validar inputs**: Los agentes pueden enviar datos inesperados.
4. **Errores informativos**: Devuelve mensajes que ayuden al agente a corregir.
5. **Mínimo privilegio**: No expongas tools destructivas sin necesidad.
6. **allowed filter**: En workflows, limita qué tools puede usar cada server.

---

### Escenario: Crear un nuevo recurso "Product" en la API

#### Paso 1: Invocar la Skill

```
/generate-crud Product name:String price:Double category:String active:Boolean
```

La skill `generate-crud` genera automáticamente todos los archivos del CRUD.

#### Paso 2: Las Instructions se activan automáticamente

Al editar los archivos `.java` generados, se aplican:
- `java21.instructions.md` → Sugiere usar Records, pattern matching, etc.
- `springboot-api.instructions.md` → Valida la estructura del Controller/Service.

#### Paso 3: Consultar al Agente Arquitecto

```
@java-architect ¿El ProductService cumple con los principios SOLID?
```

El agente revisa y da feedback sobre la arquitectura.

#### Paso 4: Generar tests con el Agente

```
@test-writer Genera tests unitarios completos para ProductService
```

El agente genera tests siguiendo el patrón AAA y las convenciones del proyecto.

#### Paso 5: Revisar con Code Reviewer

```
@code-reviewer Revisa todo el código nuevo de Product
```

El agente busca bugs, vulnerabilidades y problemas de rendimiento.

#### Paso 6: Hook se ejecuta al finalizar

Al cerrar la sesión de Copilot, el hook `pre-commit-check` verifica automáticamente
que todo el código nuevo cumple con las convenciones.

---

## 📚 Recursos Adicionales

### Repositorio de referencia

- **awesome-copilot**: https://github.com/github/awesome-copilot
  - Cientos de agents, instructions y skills de la comunidad.
  - Website: https://awesome-copilot.github.com/

### Documentación oficial

- [GitHub Copilot Agents Docs](https://docs.github.com/en/copilot)
- [Agent Skills Specification](https://agentskills.io/specification)
- [Copilot Hooks Docs](https://docs.github.com/en/copilot/how-tos/use-copilot-agents/coding-agent/use-hooks)

### Skills útiles para instalar

```bash
# Testing Spring Boot
gh skills install github/awesome-copilot spring-boot-testing

# Conocimiento del codebase
gh skills install github/awesome-copilot acquire-codebase-knowledge

# Sugerir agents relevantes
gh skills install github/awesome-copilot suggest-awesome-github-copilot-agents
```

### Plugins interesantes para Java

```bash
# Plugin de desarrollo Java completo
gh plugins install github/awesome-copilot java-development

# MCP Server para Java
gh plugins install github/awesome-copilot java-mcp-development
```

---

## 🎓 Resumen: ¿Qué herramienta uso?

| Necesito... | Uso... |
|-------------|--------|
| Que Copilot siempre siga mis reglas | `copilot-instructions.md` |
| Un experto para consultar sobre algo | Agent (`.agent.md`) |
| Reglas automáticas al escribir código | Instructions (`.instructions.md`) |
| Automatizar una tarea compleja | Skill (`SKILL.md`) |
| Verificaciones automáticas | Hook (`hooks.json`) |
| Automatizar tareas del repo con IA | Agentic Workflow (`.md` en workflows/) |
| Distribuir agents+skills como paquete | Plugin (`plugin.json`) |
| Conectar Copilot con mi app/APIs/DB | MCP Server (`mcp.json`) |

### Mapa de relaciones

```
┌─────────────────────────────────────────────────────────────────┐
│                        TU REPOSITORIO                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│  📋 copilot-instructions.md (SIEMPRE activas)                   │
│       │                                                          │
│       ├── 📐 Instructions (AUTO por tipo de archivo)             │
│       │                                                          │
│       ├── 🤖 Agents (MANUAL: @agent pregunta)                   │
│       │                                                          │
│       ├── 🎯 Skills (MANUAL: /skill argumentos)                 │
│       │                                                          │
│       ├── 🪝 Hooks (AUTO por eventos de sesión)                 │
│       │                                                          │
│       ├── ⚡ Workflows (AUTO por triggers: schedule/issues/PR)   │
│       │       │                                                  │
│       │       └── 🌐 MCP Servers (conexiones externas)          │
│       │                                                          │
│       └── 🔌 Plugins (agrupan todo lo anterior)                 │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

*Creado como proyecto didáctico. Basado en el repositorio [github/awesome-copilot](https://github.com/github/awesome-copilot) y [GitHub Agentic Workflows](https://github.github.com/gh-aw/).*
