---
name: generate-crud
description: 'Skill para generar automáticamente un CRUD completo de Spring Boot (Entity, DTO Records, Repository, Service, Controller y Tests) a partir del nombre de una entidad y sus campos. Invoca con /generate-crud seguido del nombre de la entidad.'
license: MIT
compatibility: 'Java 21+, Spring Boot 3.x, Maven o Gradle'
metadata:
  version: "1.0"
argument-hint: 'Nombre de la entidad y sus campos, ej: "Product name:String price:Double category:String"'
---

# Generate CRUD Skill

Genera un CRUD completo de Spring Boot a partir de una especificación de entidad.

## Cómo invocar

```
/generate-crud Product name:String price:Double category:String active:Boolean
```

## Qué genera

A partir del input anterior, esta skill genera:

1. **Entity** - Clase JPA con anotaciones
2. **DTO Request** - Record para crear/actualizar
3. **DTO Response** - Record con factory method `fromEntity()`
4. **Repository** - Interface JPA con queries derivadas
5. **Service** - Lógica de negocio con transacciones
6. **Controller** - Endpoints REST completos
7. **Tests** - Tests unitarios y de integración

## Reglas de generación

### Entity

```java
@Entity
@Table(name = "products")  // nombre en plural, snake_case
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campos del input con anotaciones de validación
    @Column(nullable = false)
    private String name;

    private Double price;

    // Timestamps automáticos
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { this.createdAt = LocalDateTime.now(); }
}
```

### DTO Request (Record)

```java
public record CreateProductRequest(
    @NotBlank String name,
    @NotNull @Positive Double price,
    String category,
    Boolean active
) {
    public CreateProductRequest {
        if (active == null) active = true;
    }
}
```

### DTO Response (Record con factory)

```java
public record ProductResponse(Long id, String name, Double price, ...) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(product.getId(), product.getName(), ...);
    }
}
```

### Repository

```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Generar queries por cada campo String
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByCategory(String category);
}
```

### Service (patrón del proyecto)

- Constructor injection
- `@Transactional(readOnly = true)` en la clase
- `@Transactional` en métodos de escritura
- Logging SLF4J
- Excepciones personalizadas para 404

### Controller (convenciones REST)

- Base path: `/api/{entidad-en-plural}`
- Status codes correctos (201 para create, 204 para delete)
- Validación con `@Valid`
- ResponseEntity en todos los métodos

### Tests

- **Unitarios**: Service con Mockito, patrón AAA
- **Integración**: Controller con MockMvc
- Casos: happy path, not found, validation error

## Estructura de archivos generados

```
src/main/java/com/example/copilotlearning/
├── model/Product.java
├── dto/CreateProductRequest.java
├── dto/UpdateProductRequest.java
├── dto/ProductResponse.java
├── repository/ProductRepository.java
├── service/ProductService.java
├── controller/ProductController.java
└── exception/ProductNotFoundException.java

src/test/java/com/example/copilotlearning/
├── service/ProductServiceTest.java
└── controller/ProductControllerTest.java
```
