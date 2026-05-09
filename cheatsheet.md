# Spring Boot Cheatsheet

## Stereotype Annotations
Tell Spring what a class is so it can manage it automatically.

| Annotation | Layer | What it does |
|---|---|---|
| `@Component` | Any | Generic Spring-managed component |
| `@Service` | Business Logic | Marks a service class — business logic lives here |
| `@Repository` | Data Access | Marks a data access class — database logic lives here |
| `@RestController` | Web | Handles HTTP requests, returns JSON automatically |
| `@Controller` | Web | Handles HTTP requests, returns views (less common in microservices) |
| `@Configuration` | Config | Marks a configuration class |

> **Express analogy:** `@RestController` = route handlers, `@Service` = data services, `@Repository` = DB access layer

---

## Wiring Annotations
Tell Spring how to connect components together.

| Annotation | What it does |
|---|---|
| `@Autowired` | Inject a dependency automatically — Spring finds the right class and injects it |
| `@Bean` | Declare a Spring-managed object inside a `@Configuration` class |
| `@SpringBootApplication` | Entry point annotation — combines `@Configuration`, `@EnableAutoConfiguration`, `@ComponentScan` |

> **Jest analogy:** `@Autowired` is like dependency injection in testing — Spring injects real (or mock) dependencies so you don't call `new` yourself.

---

## Web / HTTP Annotations
Map HTTP requests to Java methods.

| Annotation | HTTP Method | What it does |
|---|---|---|
| `@RequestMapping("/path")` | Any | Base path mapping — usually on the class |
| `@GetMapping("/{id}")` | GET | Handle GET requests |
| `@PostMapping` | POST | Handle POST requests |
| `@PutMapping("/{id}")` | PUT | Handle PUT requests |
| `@DeleteMapping("/{id}")` | DELETE | Handle DELETE requests |
| `@PatchMapping("/{id}")` | PATCH | Handle PATCH requests |

### Request Data Annotations

| Annotation | What it does | Express equivalent |
|---|---|---|
| `@RequestBody` | Extract JSON body from request | `req.body` |
| `@PathVariable` | Extract variable from URL path | `req.params.id` |
| `@RequestParam` | Extract query parameter from URL | `req.query.page` |

---
## Database Annotations
Using JPA/Jakarta
| Annotation | Level | What it does |
|---|---|---|
| `@MappedSuperclass` | Class | Base class fields included in subclass tables, no own table
|`@Entity` | Class | Create table for this class |
|`@Column(...)` | Field | Customize column - nullable, name, length, data type to save |
|`@Table(name = "...")` | Class | Custom table name |
|`@Id` | Field | Primary key |
|`@GeneratedValue` | Field | Auto-generate primary key |
|`@Enumerated(EnumType.STRING)` | Field | Store enum as str not int |
|`@ElementCollection` | Field | Store collection of simple values |
|`@PrePersist` | Method | Runs before first save (INSERT) |
|`@PreUpdate` | Method | Runs before every UPDATE |

## Quick Code Reference

### Basic REST Controller
```java
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        return productService.update(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.delete(id);
    }
}
```

### Basic Service
```java
@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
}
```

### Basic Repository
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // JpaRepository gives you findAll(), findById(), save(), delete() for free
    // Add custom queries here if needed
}
```

### Basic Model (with Lombok)
```java
@Entity
@Data                    // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor       // Lombok: generates no-args constructor
@AllArgsConstructor      // Lombok: generates all-args constructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Double price;
}
```

---

## Project Structure

```
my-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/company/myservice/
│   │   │       ├── MyServiceApplication.java   ← entry point (@SpringBootApplication)
│   │   │       ├── controller/
│   │   │       │   └── ProductController.java  ← HTTP layer (@RestController)
│   │   │       ├── service/
│   │   │       │   └── ProductService.java     ← business logic (@Service)
│   │   │       ├── repository/
│   │   │       │   └── ProductRepository.java  ← data access (@Repository)
│   │   │       └── model/
│   │   │           └── Product.java            ← data model (@Entity)
│   │   └── resources/
│   │       └── application.properties          ← configuration (DB url, port, etc.)
│   └── test/
│       └── java/                               ← tests live here
└── pom.xml                                     ← Maven dependencies
```

### Layer Responsibilities

| Layer | Annotation | Responsibility | Express equivalent |
|---|---|---|---|
| Controller | `@RestController` | Receive HTTP requests, return responses | Route handlers |
| Service | `@Service` | Business logic, orchestration | Data services / business logic |
| Repository | `@Repository` | Database access only | Mongoose models / DB queries |
| Model | `@Entity` | Data structure / database table | Mongoose schema |

---

## application.properties Quick Reference

```properties
# Server port (default is 8080)
server.port=8080

# H2 in-memory database (for development/learning)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.h2.console.enabled=true

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Application name (important for microservices)
spring.application.name=product-service
```

---

## pom.xml Key Dependencies

```xml
<!-- Spring Web — REST APIs -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Data JPA — database access -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- H2 — in-memory database for development -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Lombok — reduces boilerplate -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Spring Boot Test — testing support -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

---

## Key Concepts Summary

| Concept | What it means |
|---|---|
| **IoC Container** | Spring manages object creation and wiring — you don't call `new` |
| **Dependency Injection** | Spring injects dependencies into your classes automatically |
| **Auto-configuration** | Spring Boot configures itself based on what's on the classpath |
| **Embedded server** | Tomcat runs inside your app — no external server setup needed |
| **JPA** | Java Persistence API — standard way to interact with databases in Java |
| **Lombok** | Library that generates boilerplate (getters, setters, constructors) via annotations |