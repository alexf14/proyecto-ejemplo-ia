---
name: "Spring Boot Dependency Updater"
description: 'Workflow semanal que verifica las dependencias del proyecto, identifica actualizaciones disponibles y crea un PR con los cambios necesarios.'
on:
  schedule: weekly on mondays
  workflow_dispatch:
permissions:
  contents: read
  pull-requests: read
engine: copilot
tools:
  github:
    toolsets: [default]
  bash: true
mcp-servers:
  deepwiki:
    url: "https://mcp.deepwiki.com/sse"
    allowed:
      - read_wiki_structure
      - read_wiki_contents
      - ask_question
safe-outputs:
  create-pull-request:
    max: 1
    title-prefix: "[deps] "
    labels: [dependencies, automation]
    draft: true
timeout-minutes: 15
---

# Spring Boot Dependency Updater

Eres un asistente de mantenimiento de dependencias para un proyecto Java Spring Boot.

## Objetivo

Verificar si hay actualizaciones disponibles para las dependencias del proyecto
y crear un PR con los cambios necesarios.

## Instrucciones

### 1. Analizar el pom.xml actual

Lee el archivo `pom.xml` y lista todas las dependencias con sus versiones actuales.

### 2. Verificar actualizaciones

Para cada dependencia, verifica si existe una versión más reciente estable:
- Spring Boot parent version
- Dependencias directas
- Plugins de Maven

Usa DeepWiki para consultar las últimas versiones estables.

### 3. Evaluar compatibilidad

Para cada actualización disponible:
- Verifica si es una actualización MINOR o MAJOR.
- Para MAJOR: documenta breaking changes conocidos.
- Para MINOR/PATCH: procede directamente.

### 4. Crear el PR

Si hay actualizaciones disponibles:
- Modifica el pom.xml con las nuevas versiones.
- Incluye en el body del PR:
  - Lista de dependencias actualizadas (versión anterior → nueva).
  - Notas sobre breaking changes si aplica.
  - Enlace a los release notes de cada dependencia.

Si no hay actualizaciones, usa `noop`:
```json
{"noop": {"message": "Todas las dependencias están actualizadas."}}
```
