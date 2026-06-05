# Instrucciones Generales del Proyecto - Copilot AI Learning

## Contexto del Proyecto

Este es un proyecto didáctico de Spring Boot 3.3 con Java 21 que implementa una API REST
para gestión de tareas. Su propósito principal es enseñar a usar las herramientas de IA
de GitHub Copilot (Agents, Instructions, Skills, Hooks).

## Convenciones de Código

### Java 21

- Usar Records para DTOs (inmutables, concisos)
- Preferir `var` para variables locales cuando el tipo es obvio
- Usar pattern matching con `instanceof` cuando sea apropiado
- Usar switch expressions con pattern matching
- Preferir `sealed` interfaces para jerarquías cerradas

### Spring Boot

- Inyección por constructor (nunca `@Autowired` en campos)
- Campos de dependencias como `private final`
- Organización por feature (no por capa)
- Logging con SLF4J parametrizado
- Validación con Jakarta Validation (JSR-380)
- Manejo global de excepciones con `@RestControllerAdvice`

### Naming

- Clases: PascalCase (`TaskService`, `CreateTaskRequest`)
- Métodos: camelCase, verbos descriptivos (`createTask`, `findByStatus`)
- Constantes: UPPER_SNAKE_CASE
- Paquetes: lowercase sin guiones

### Testing

- Tests unitarios con JUnit 5 + Mockito + AssertJ
- Tests de integración con `@SpringBootTest` + MockMvc
- Nombres de test: `should<Resultado>When<Condición>()`
- Un assert lógico por test

### API REST

- Endpoints: sustantivos en plural (`/api/tasks`)
- Respuestas: siempre usar `ResponseEntity<T>` con status codes apropiados
- Errores: formato consistente con mensaje, código y timestamp
- Validación: en la capa controller con `@Valid`
