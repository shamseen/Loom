# Loom — Project Roadmap

## What is Loom?
A personal knowledge management system built as a microservices project.
Entries (notes, concepts, quotes, diary entries) are connected by typed Strands,
forming a knowledge graph that reveals relationships across thematic groupings.

---

## Domain Model

### Core Objects

**Entry** — the atomic unit of knowledge
- `id` — Long
- `title` — String
- `body` — String
- `type` — EntryType enum (NOTE, CONCEPT, QUOTE, DIARY, DEFINITION)
- `createdAt` — timestamp
- `updatedAt` — timestamp
- `spoolIds` — List\<Long\> (nullable, reserved for future Spool implementation)

**Strand** — a typed relationship between two Entries
- `id` — Long
- `sourceEntryId` — Long
- `targetEntryId` — Long
- `type` — RelationshipType enum (RELATES_TO, ORIGINATED_FROM, SEE_ALSO, IS_PART_OF)
- `createdAt` — timestamp
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

### v0.1 Microservices
- `entry-service` — CRUD for Entries (Spools stubbed via spoolIds field)
- `strand-service` — CRUD for Strands between Entries

### Future Microservices
- `search-service` — full text search across Entries, filter by type/tag/date
- `export-service` — generates formatted output (markdown, PDF) from queries
- `spool-service` — manages Spool containers and membership
- API Gateway — single entry point routing to all services

### Tech Stack
| Layer | Technology |
|---|---|
| Language | Java 24 |
| Framework | Spring Boot 3.5.13 |
| Build tool | Maven |
| ORM | Spring Data JPA |
| Database (dev) | H2 in-memory |
| Database (prod) | PostgreSQL (one per service) |
| Containerization | Docker (Phase 4) |
| Orchestration | Kubernetes (Phase 5) |
| CI/CD | GitLab CI/CD (Phase 6) |

---

## v0.1 — entry-service

### Endpoints
- [ ] `GET /entries` — get all entries
- [ ] `GET /entries/{id}` — get one entry
- [ ] `POST /entries` — create an entry
- [ ] `POST /entries/batch` — create multiple entries
- [ ] `PUT /entries/{id}` — update an entry (upsert)
- [ ] `DELETE /entries/{id}` — delete an entry

### Data Model Notes
- `spoolIds` field present but unused — reserved for Spool implementation
- `type` field uses EntryType enum

### Future Improvements
- [ ] Error handling — 404 for missing entries
- [ ] Input validation — @Valid, @NotNull etc.
- [ ] Data initializer — auto-seed on startup
- [ ] DTOs — separate API model from database model
- [ ] Pagination — page/size params for large datasets

---

## v0.1 — strand-service

### Endpoints
- [ ] `GET /strands` — get all strands
- [ ] `GET /strands/{id}` — get one strand
- [ ] `GET /strands/entry/{entryId}` — get all strands connected to an entry
- [ ] `POST /strands` — create a strand
- [ ] `DELETE /strands/{id}` — delete a strand

### Data Model Notes
- `sourceEntryId` and `targetEntryId` reference Entry ids (cross-service)
- `type` field uses RelationshipType enum
- Relationship abstract base class — deferred, implement when JPA inheritance strategy decided

### Inter-service Communication
- strand-service needs to validate that Entry ids exist in entry-service
- First example of inter-service communication in this project
- Implementation: RestTemplate or WebClient (to be decided)

---

## Learning Phases

### Phase 1 — Microservices Concepts ✅
- [x] Monolith vs microservices
- [x] Why microservices exist
- [x] Tradeoffs
- [x] Synchronous vs asynchronous communication
- [x] Strangler Fig Pattern

### Phase 2 — Spring Boot (in progress)
- [x] Project structure and annotations
- [x] Dependency injection and IoC
- [x] JPA and H2
- [x] Lombok
- [x] Full CRUD REST API (product-service)
- [ ] entry-service
- [ ] strand-service
- [ ] Switch H2 to PostgreSQL (local install)

### Phase 3 — Microservices Patterns
- [ ] Inter-service communication (entry and strand)
- [ ] RestTemplate vs WebClient
- [ ] Service discovery (Eureka)
- [ ] API Gateway (Spring Cloud Gateway)
- [ ] Circuit breaker (Resilience4j)
- [ ] Database per service pattern
- [ ] Maven concepts

### Phase 4 — Docker
- [ ] Dockerfile for each service
- [ ] Docker Compose — run all services locally
- [ ] Switch PostgreSQL to Docker container
- [ ] Container networking

### Phase 5 — Kubernetes
- [ ] Pods, nodes, clusters, deployments
- [ ] kubectl
- [ ] Deployment YAML files
- [ ] Services and ingress
- [ ] ConfigMaps and Secrets
- [ ] Scaling and health checks
- [ ] Local Kubernetes with minikube or Docker Desktop

### Phase 6 — CI/CD
- [ ] GitLab CI/CD overview
- [ ] .gitlab-ci.yml configuration
- [ ] Pipeline stages: build, test, package, deploy
- [ ] Monorepo pipeline — only build changed services
- [ ] Deploy to Kubernetes from pipeline

---

## Design Decisions Log

| Decision | Choice | Reason |
|---|---|---|
| Repo structure | Monorepo | Easier for solo learning |
| IDE | VS Code | Familiar, Spring Extension Pack |
| Spool implementation | Deferred | Adds complexity without v0.1 value |
| Knot implementation | Deferred | Need real data before designing |
| Relationship base class | Planned, not yet implemented | Need to learn JPA inheritance strategies first |
| Strand naming | Strand (not Fiber, not Thread) | Most natural for domain |
| Fork/cluster modeling | Pairwise Strands for now | Sufficient until Knot is needed |
| Database (dev) | H2 then PostgreSQL local | H2 for learning, PostgreSQL for persistence |
| Update behavior | Upsert | Simpler for now, 404 to be added later |

---

## Open Questions
- JPA inheritance strategy for Relationship hierarchy: SINGLE_TABLE, TABLE_PER_CLASS, or JOINED?
- Should strand-service call entry-service to validate Entry ids, or trust the client?
- Bidirectional vs unidirectional Strands — should A to B and B to A be the same Strand or different?