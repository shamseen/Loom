# Loom — Project Roadmap

## What is Loom?
A personal knowledge management system with a graph-based knowledge structure.
Entries (notes, concepts, quotes, diary entries) are connected by typed Strands,
forming a knowledge graph that reveals relationships across thematic groupings.

Loom is open source and self-hostable. Anyone can deploy their own instance.
The engine is a reusable library — developers can build their own adapters on top of it.

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
- `strandType` — StrandType enum (CONTRADICTS, DEFINES, EVIDENCE_OF, EVOLVED_FROM, PART_OF, RELATES_TO, TANGENT)
- Extends: `Relationship` (abstract base class — future)
- Note: directionality implied by sourceEntryId → targetEntryId
- Note: `isBidirectional` field deferred — add after MVP when real usage data informs which types need it
- Note: user-defined Strand types deferred — ship with hardcoded preliminary types for MVP

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

### Hexagonal Architecture (Ports and Adapters)

The engine contains all business logic. Adapters (API, integrations) sit on top of it.
The engine has no knowledge of how it is accessed.

```
[loom-api]    [loom-integration-*]    (future adapters)
      \               /
       \             /
        [loom-engine]
              |
         [PostgreSQL]
```

### MVP Module Structure

```
Loom/
├── loom-engine/          ← shared library (models + business logic + repositories)
├── loom-api/             ← REST API microservice (controllers only)
├── loom-web/             ← React/TypeScript frontend (graph visualization UI)
├── docker-compose.yml    ← orchestrates all services for local development
└── pom.xml               ← parent POM (Java modules only)
```

### Future Modules (start as libraries, extract to microservices when needed)

```
├── loom-export/          ← export library → microservice when scale demands
├── loom-search/          ← search library → microservice when scale demands
├── loom-integration-*/   ← third-party adapters (Notion, Obsidian etc.)
```

### What Lives Where

**`loom-engine` (library — not deployable):**
- `LoomObject` — abstract base class
- `Visibility` — enum (in core/)
- `StrandType` — enum (in strand/)
- `LoomEntry`, `Strand` — domain models
- `Spool` — domain model (when implemented)
- All service classes (business logic)
- All repository interfaces (data access)
- No web layer — no controllers, no Spring Web dependency

**`loom-api` (microservice — deployable):**
- Controllers only — thin REST adapter
- `application.properties` — server config, database config
- Spring Boot main class
- Depends on `loom-engine`
- No business logic — delegates entirely to engine services

**`loom-web` (frontend service):**
- React + TypeScript
- Graph visualization (D3.js, Cytoscape.js, or React Flow)
- Calls `loom-api` via REST
- Own `package.json` and build toolchain
- Not part of Maven build — own Dockerfile

### Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 24 |
| Framework | Spring Boot 3.5.13 |
| Build tool | Maven (multi-module) |
| ORM | Spring Data JPA |
| Database (dev) | H2 in-memory |
| Database (prod) | PostgreSQL (Docker container) |
| Containerization | Docker |
| Orchestration | Kubernetes |
| CI/CD | GitLab CI/CD |
| Frontend | React + TypeScript |
| Graph visualization | TBD (D3.js / Cytoscape.js / React Flow) |

---

## MVP Endpoints — loom-api

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
- [ ] Error handling — 404 for missing resources, global exception handler
- [ ] Input validation — @Valid, @NotNull etc.
- [ ] Data initializer — auto-seed on startup
- [ ] DTOs — separate API response model from database model
- [ ] Pagination — page/size params for large datasets

---

## Learning Phases

### Phase 1 — Microservices and Architecture Concepts ✅
- [x] Monolith vs microservices
- [x] Why microservices exist and when they are not appropriate
- [x] Synchronous vs asynchronous communication
- [x] Strangler Fig Pattern
- [x] Hexagonal Architecture (Ports and Adapters)
- [x] Library vs microservice distinction
- [x] Maven multi-module projects

### Phase 2 — Spring Boot + Maven Multi-Module (in progress)
- [x] Spring Boot project structure and annotations
- [x] Dependency injection and IoC
- [x] JPA and H2
- [x] Lombok
- [x] Full CRUD REST API (practice project)
- [x] Set up Maven multi-module structure (parent POM, loom-engine, loom-api)
- [x] Domain models — LoomObject, LoomEntry, Strand (plain Java, no annotations)
- [ ] Add Lombok annotations to domain models
- [ ] Add JPA annotations to domain models
- [ ] Build repositories — EntryRepository, StrandRepository
- [ ] Build services — EntryService, StrandService
- [ ] Build loom-api — controllers only, depends on loom-engine
- [ ] Switch H2 to PostgreSQL (Docker container)
- [ ] Full CRUD for entries and strands working end-to-end

### Phase 3 — Docker
- [ ] What Docker is and why it exists
- [ ] Images vs containers
- [ ] Write Dockerfile for loom-api
- [ ] Write Dockerfile for loom-web
- [ ] Switch PostgreSQL to Docker container
- [ ] Docker Compose — run loom-api + loom-web + PostgreSQL together
- [ ] Container networking — how services find each other
- [ ] Environment variables and configuration management

### Phase 4 — Frontend (loom-web)
- [ ] Project setup — React + TypeScript + Vite
- [ ] API client — calling loom-api REST endpoints
- [ ] Entry CRUD UI
- [ ] Strand CRUD UI
- [ ] Graph visualization — rendering entries and strands as a visual network
- [ ] Graph library selection (D3.js / Cytoscape.js / React Flow)

### Phase 5 — Kubernetes
- [ ] What Kubernetes is and why it exists
- [ ] Pods, nodes, clusters, deployments
- [ ] kubectl
- [ ] Deployment YAML files for loom-api and loom-web
- [ ] Services and ingress
- [ ] ConfigMaps and Secrets
- [ ] Health checks — liveness and readiness probes
- [ ] Local Kubernetes with minikube or Docker Desktop

### Phase 6 — CI/CD
- [ ] GitLab CI/CD overview
- [ ] .gitlab-ci.yml configuration
- [ ] Pipeline stages: build, test, package, deploy
- [ ] Build Docker images in pipeline
- [ ] Deploy to Kubernetes from pipeline
- [ ] Monorepo pipeline — only build changed modules

### Phase 7 — Quarkus + ActiveMQ (second app)
- [ ] What Quarkus is and how it differs from Spring Boot
- [ ] Quarkus project setup
- [ ] ActiveMQ concepts — queues, topics, producers, consumers
- [ ] Producing and consuming messages
- [ ] Potential integration with Loom via loom-integration module

### Phase 8 — Microservices Patterns (when second service is added to Loom)
- [ ] Inter-service communication — REST with WebClient
- [ ] Service discovery — Eureka or Kubernetes native
- [ ] API Gateway — Spring Cloud Gateway
- [ ] Circuit breaker — Resilience4j
- [ ] Database per service pattern
- [ ] Eventual consistency

---

## Design Decisions Log

| Decision | Choice | Reason |
|---|---|---|
| Architecture | Hexagonal (Ports and Adapters) | Engine reusable by any adapter; clean separation of business logic from delivery |
| Repo structure | Monorepo | Single source of truth, coordinated releases, Docker handles toolchain differences |
| Module structure | Maven multi-module | loom-engine as library, loom-api as microservice |
| loom-engine scope | Models + services + repositories | Business logic must travel with models for Loom to be useful as a library |
| loom-api scope | Controllers only | Thin adapter — no business logic, delegates to engine |
| Future features (export, search) | Start as libraries, extract to microservices when scale demands | Strangler Fig applied internally |
| Frontend | Separate loom-web module in same repo | Same monorepo, Docker handles Node/Java toolchain difference |
| Database | H2 for dev, PostgreSQL in Docker for prod | H2 for fast local iteration, PostgreSQL for persistence |
| Update behavior | Upsert | Simpler for now, 404 to be added later |
| Entry body field | body (not definition) | More generic — not all entries are definitional |
| Entry type field | String (user-defined) | Fixed enum too rigid for personal knowledge domain |
| Strand directionality | Implied by sourceEntryId → targetEntryId, no explicit field for MVP | isBidirectional deferred until real usage data informs which types need it |
| Strand types | Hardcoded enum for MVP, user-defined extensibility deferred | Premature flexibility before core is stable is a trap |
| StrandType values | CONTRADICTS, DEFINES, EVIDENCE_OF, EVOLVED_FROM, PART_OF, RELATES_TO, TANGENT | Covers main relationship categories without over-engineering |
| Strand immutability | Delete and recreate to change | Relationships should be intentional; no partial updates |
| Spool implementation | Deferred | Adds complexity without MVP value |
| Knot implementation | Deferred | Need real data before designing hyperedge model |
| Relationship base class | Planned, not yet implemented | Need to decide JPA inheritance strategy first |
| Open source model | Self-hosted, single-tenant | Each user deploys their own instance; no shared infrastructure |
| Package naming | app.loom | Personal project, no real domain ownership implied |

---

## Open Questions
- JPA inheritance strategy for Relationship hierarchy: SINGLE_TABLE, TABLE_PER_CLASS, or JOINED?
- Bidirectional Strands — store once and query both directions, or store twice?
- Graph visualization library — D3.js, Cytoscape.js, or React Flow?
- Should loom-web live in this repo or a separate repo as the project grows?

## Future Strand Improvements (post-MVP)
- `isBidirectional` field — add per StrandType once real usage data informs which types need it
- User-defined Strand types — name, optional description, directionality flag
- Strand type categories — hierarchical, associative, definitional, tension, evidential
- Strand type description field — context for user-defined types that may lose meaning over time