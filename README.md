# Loom — Project Roadmap

## What is Loom?
A personal knowledge management system built as a microservices project.
Entries (notes, concepts, quotes, diary entries) are connected by typed Strands,
forming a knowledge graph that reveals relationships across thematic groupings.

---

## Domain Model

### Core Objects

**LoomObject** — abstract base for all Loom domain objects
- `id` — String (object-prefixed UUID e.g. `entry_550e8400-...`)
- `createdAt` — ZonedDateTime (UTC)
- `updatedAt` — ZonedDateTime (UTC)
- `visibility` — `Visibility` enum (PUBLIC, PRIVATE, UNLISTED)

**LoomEntry** — the atomic unit of knowledge; e.g. note, concept, quote, or any discrete thought
- `title` — String
- `body` — String
- `entryType` — String (user-defined e.g. NOTE, CONCEPT, QUOTE, DIARY, DEFINITION)
- `spoolIds` — List\<String\> (nullable, reserved for future Spool implementation)

**Strand** — a typed relationship between two Entries
- `sourceEntryId` — String
- `targetEntryId` — String
- `type` — RelationshipType enum (RELATES_TO, ORIGINATED_FROM, SEE_ALSO, IS_PART_OF)
- `direction` — Direction enum (UNIDIRECTIONAL, BIDIRECTIONAL)
- Extends: `Relationship` (abstract base class — future)

**Relationship** *(abstract — future)*
- Abstract base class for all relational objects
- Common fields: id, type, createdAt
- Implementations: Strand (2 entries), Knot (N entries)

**Knot** *(deferred — future)*
- A single relationship connecting more than 2 Entries simultaneously
- Hyperedge in graph theory terms
- Will extend Relationship

**Spool** *(deferred — future)*
- A thematic container/grouping for Entries
- Entries can belong to multiple Spools
- Strands can cross Spool boundaries — this is the key feature

---

## Architecture

### Current — Single Microservice
`loom-app` is a single Spring Boot microservice containing all core knowledge graph functionality. Structured as a proper microservice from the start — containerized, deployable independently — even though it is currently the only service.

### Module Structure
```
Loom/
├── loom-core/          ← shared library (domain models, not deployable)
│   └── pom.xml
├── loom-app/           ← core knowledge graph microservice
│   └── pom.xml
└── pom.xml             ← parent POM declaring both modules
```

### Future Microservices
Additional sibling services added as new capabilities are needed:
- `loom-search` — full text search across Entries, filter by type/date
- `loom-export` — generates formatted output (markdown, PDF) from queries
- `loom-integration` — connectors to external systems, mobile, browser extension
- API Gateway — single entry point routing to all services (when multiple services exist)

### What Lives Where

**`loom-core` (shared library):**
- `LoomObject` — abstract base class
- `Visibility` — enum
- `LoomEntry` — domain model
- `Strand` — domain model
- `Spool` — domain model (when implemented)
- Shared DTOs, exceptions, constants (when needed)

**`loom-app` (deployable microservice):**
- Controllers
- Services
- Repositories
- Application configuration
- Depends on loom-core

### Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 24 |
| Framework | Spring Boot 3.5.13 |
| Build tool | Maven (multi-module) |
| ORM | Spring Data JPA |
| Database (dev) | H2 in-memory |
| Database (prod) | PostgreSQL |
| Containerization | Docker |
| Orchestration | Kubernetes |
| CI/CD | GitLab CI/CD |

---

## v0.1 — loom-app Endpoints

### Entries
- [ ] `GET /entries` — get all entries
- [ ] `GET /entries/{id}` — get one entry
- [ ] `POST /entries` — create an entry
- [ ] `POST /entries/batch` — create multiple entries
- [ ] `PUT /entries/{id}` — update an entry (upsert)
- [ ] `DELETE /entries/{id}` — delete an entry

### Strands
- [ ] `GET /strands` — get all strands
- [ ] `GET /strands/{id}` — get one strand
- [ ] `GET /strands/entry/{entryId}` — get all strands connected to an entry
- [ ] `POST /strands` — create a strand
- [ ] `DELETE /strands/{id}` — delete a strand

### Future Improvements
- [ ] Error handling — 404 for missing resources
- [ ] Input validation — @Valid, @NotNull etc.
- [ ] Data initializer — auto-seed on startup
- [ ] DTOs — separate API model from database model
- [ ] Pagination — page/size params for large datasets

---

## Learning Phases

### Phase 1 — Spring Boot + Maven Multi-Module (in progress)
- [ ] Maven concepts deep dive
- [ ] Set up Maven multi-module structure (loom-core + loom-app)
- [ ] Dependency injection and IoC
- [ ] JPA and H2
- [ ] Lombok
- [ ] Wire up JPA in loom-app
- [ ] Full CRUD for entries
- [ ] Full CRUD for strands
- [ ] Switch H2 to PostgreSQL

### Phase 2 — Microservices Concepts ✅
- [ ] Monolith vs microservices
- [ ] Why microservices exist
- [ ] Tradeoffs
- [ ] Synchronous vs asynchronous communication
- [ ] Strangler Fig Pattern
- [ ] When microservices are and are not appropriate

### Phase 3 — Docker
- [ ] What Docker is and why it exists
- [ ] Images vs containers
- [ ] Write Dockerfile for loom-app
- [ ] Build and run Docker image locally
- [ ] Switch PostgreSQL to Docker container
- [ ] Docker Compose — run loom-app + PostgreSQL together
- [ ] Container networking basics
- [ ] Environment variables and configuration management

### Phase 4 — Kubernetes
- [ ] What Kubernetes is and why it exists
- [ ] Pods, nodes, clusters, deployments
- [ ] kubectl
- [ ] Deployment YAML files for loom-app
- [ ] Services and ingress
- [ ] ConfigMaps and Secrets
- [ ] Health checks — liveness and readiness probes
- [ ] Local Kubernetes with minikube or Docker Desktop

### Phase 5 — CI/CD
- [ ] GitLab CI/CD overview
- [ ] .gitlab-ci.yml configuration
- [ ] Pipeline stages: build, test, package, deploy
- [ ] Build Docker image in pipeline
- [ ] Deploy to Kubernetes from pipeline
- [ ] Monorepo pipeline — only build changed modules

### Phase 6 — Microservices Patterns (when second service is added)
- [ ] Inter-service communication — REST with WebClient
- [ ] Service discovery — Eureka or Kubernetes native
- [ ] API Gateway — Spring Cloud Gateway
- [ ] Circuit breaker — Resilience4j
- [ ] Database per service pattern
- [ ] Eventual consistency

### Phase 7 — Quarkus + ActiveMQ (separate app)
- [ ] What Quarkus is and how it differs from Spring Boot
- [ ] Quarkus project setup
- [ ] ActiveMQ concepts — queues, topics, producers, consumers
- [ ] Producing messages
- [ ] Consuming messages
- [ ] Integration with Loom (loom-integration service)

---

## Design Decisions Log

| Decision | Choice | Reason |
|---|---|---|
| Repo structure | Monorepo | Easier for solo learning, all Loom services together |
| Architecture | Single microservice (loom-app) | No need for multiple services at current scope |
| Module structure | Maven multi-module (loom-core + loom-app) | Clean separation of domain models from app logic |
| Domain models | Live in loom-core | Reusable when sibling services are added |
| IDE | VS Code | Familiar, Spring Extension Pack |
| Spool implementation | Deferred | Adds complexity without v0.1 value |
| Knot implementation | Deferred | Need real data before designing |
| Relationship base class | Planned, not yet implemented | Need to learn JPA inheritance strategies first |
| Strand naming | Strand (not Fiber, not Thread) | Most natural for domain |
| Fork/cluster modeling | Pairwise Strands for now | Sufficient until Knot is needed |
| Database (dev) | H2 then PostgreSQL in Docker | H2 for learning, PostgreSQL via Docker for persistence |
| Update behavior | Upsert | Simpler for now, 404 to be added later |
| Entry body field | body (not definition) | More generic — not all entries are definitional |
| Entry type field | String (user-defined) | Fixed enum too rigid for personal knowledge domain |
| Strand direction | Direction enum (UNI/BIDIRECTIONAL) | Some relationships are hierarchical, others mutual |
| Communication between future services | REST (WebClient) | Loom data is tightly coupled, synchronous makes sense |

---

## Open Questions
- JPA inheritance strategy for Relationship hierarchy: SINGLE_TABLE, TABLE_PER_CLASS, or JOINED?
- Bidirectional vs unidirectional Strands — query concern or storage concern?
- Should future loom-search service call loom-app REST API or share loom-core models directly?
