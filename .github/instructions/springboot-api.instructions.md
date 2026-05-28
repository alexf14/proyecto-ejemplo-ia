---
description: 'Convenciones para desarrollar APIs REST con Spring Boot 3.x: estructura de controllers, manejo de errores, validación y respuestas HTTP.'
applyTo: '**/*Controller.java, **/*Service.java, **/dto/*.java'
---

# Instrucciones para Spring Boot REST APIs

## Estructura del Controller

```java
@RestController
@RequestMapping("/api/recurso")  // plural, lowercase
public class RecursoController {

    private final RecursoService recursoService;  // private final

    // Inyección por constructor (sin @Autowired)
    public RecursoController(RecursoService recursoService) {
        this.recursoService = recursoService;
    }

    @GetMapping
    public ResponseEntity<List<RecursoResponse>> getAll() {
        return ResponseEntity.ok(recursoService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(recursoService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RecursoResponse> create(@Valid @RequestBody CreateRequest req) {
        RecursoResponse created = recursoService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecursoResponse> update(@PathVariable Long id, @Valid @RequestBody UpdateRequest req) {
        return ResponseEntity.ok(recursoService.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        recursoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
```

## Status Codes HTTP

| Operación | Éxito | Error común |
|-----------|-------|-------------|
| GET /recursos | 200 OK | - |
| GET /recursos/{id} | 200 OK | 404 Not Found |
| POST /recursos | 201 Created | 400 Bad Request |
| PUT /recursos/{id} | 200 OK | 404 Not Found, 400 Bad Request |
| DELETE /recursos/{id} | 204 No Content | 404 Not Found |

## Manejo Global de Errores

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Records para respuestas de error (inmutables y concisos)
    record ErrorResponse(String message, int status, LocalDateTime timestamp) {}

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        var error = new ErrorResponse(ex.getMessage(), 404, LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        // Mapear errores de validación a un formato legible
    }
}
```

## Validación con Jakarta Validation

```java
public record CreateTaskRequest(
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "Máximo 200 caracteres")
    String title,

    @Size(max = 1000)
    String description,

    @NotNull
    TaskPriority priority
) {}
```

## Transacciones en el Servicio

```java
@Service
@Transactional(readOnly = true)  // lectura por defecto
public class TaskService {

    @Transactional  // escritura para operaciones que modifican
    public TaskResponse createTask(CreateTaskRequest request) {
        // ...
    }
}
```

## Logging

```java
private static final Logger logger = LoggerFactory.getLogger(MiClase.class);

// ✅ CORRECTO - Parametrizado (lazy evaluation)
logger.info("Tarea {} creada por usuario {}", taskId, userId);

// ❌ INCORRECTO - Concatenación (siempre evalúa)
logger.info("Tarea " + taskId + " creada por usuario " + userId);
```
