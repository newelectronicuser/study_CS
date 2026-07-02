# Microservices Architecture — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is a microservices architecture and how does it differ from a monolith?**

**A:** A monolith is a single deployable unit containing all application logic. A microservices architecture decomposes the application into small, independently deployable services, each responsible for a specific business capability. Each service has its own process, database, and deployment lifecycle.

| Aspect | Monolith | Microservices |
|---|---|---|
| Deployment | Single unit | Independent per service |
| Scaling | Scale everything | Scale individual services |
| Technology | Single stack | Polyglot |
| Fault isolation | Single failure can crash all | Failures are contained |
| Complexity | Simpler initially | Distributed systems complexity |

---

**Q2. What is the strangler fig pattern?**

**A:** A migration strategy for moving from a monolith to microservices incrementally. New functionality is built as microservices, and existing monolith features are gradually migrated out, "strangling" the monolith over time until it can be retired. A facade or API gateway routes requests to either the monolith or the new service.

---

**Q3. What is bounded context in DDD?**

**A:** A bounded context is an explicit boundary within which a domain model applies. Each bounded context has its own ubiquitous language and model — the same word (e.g., "Order") may mean different things in different contexts (Order in Sales vs Order in Fulfillment). Bounded contexts map naturally to microservices boundaries.

---

## Communication

**Q4. When would you use REST vs gRPC vs message queues?**

**A:**
- **REST**: Human-readable, widely understood, good for public APIs and browser clients. JSON over HTTP.
- **gRPC**: Binary protocol (Protobuf), strongly typed, bi-directional streaming, lower latency. Best for internal service-to-service communication.
- **Message queues (Kafka, RabbitMQ)**: Asynchronous, decoupled, durable. Best for event-driven workflows, high-throughput processing, or where producer and consumer can't be online simultaneously.

---

**Q5. What is choreography vs orchestration?**

**A:**
- **Choreography**: Services react to events without a central coordinator. Each service publishes events and others subscribe. Loosely coupled but harder to trace the overall workflow.
- **Orchestration**: A central orchestrator (e.g., a workflow service) directs other services to perform tasks. Easier to understand the flow but the orchestrator becomes a coupling point.

Choreography: order-placed event triggers inventory, shipping, notification independently. Orchestration: order service explicitly calls each downstream service in sequence.

---

**Q6. What is an API Gateway?**

**A:** An API Gateway is the single entry point for all client requests. It handles: routing, authentication/authorization, rate limiting, request aggregation, SSL termination, logging, and monitoring. Examples: Kong, AWS API Gateway, nginx, Traefik.

Without it, each service would need to implement these cross-cutting concerns independently.

---

**Q7. What is a service mesh and what problems does it solve?**

**A:** A service mesh (Istio, Linkerd) is a dedicated infrastructure layer for service-to-service communication. It handles: mTLS (encryption), traffic management (canary, retries, timeouts), observability (distributed traces, metrics), and circuit breaking — all without application code changes (via sidecar proxies like Envoy). It's most valuable in large clusters with many services.

---

## Data Management

**Q8. What is the Saga pattern?**

**A:** The Saga pattern manages distributed transactions as a sequence of local transactions. If any step fails, compensating transactions undo the previous steps.

- **Choreography-based Saga**: Services emit events; each listens for events to proceed or trigger compensation.
- **Orchestration-based Saga**: A Saga orchestrator commands each participant service; handles compensations centrally.

Example: Order → Reserve Inventory → Charge Payment → Notify. If Payment fails → Release Inventory → Cancel Order.

---

**Q9. What is CQRS?**

**A:** Command Query Responsibility Segregation separates the write model (Command) from the read model (Query). Commands mutate state; Queries read state. They can use different data stores optimized for each use case. Enables scaling reads and writes independently, and allows highly optimized read models (denormalized views).

---

**Q10. What is Event Sourcing?**

**A:** Instead of storing current state, Event Sourcing stores the sequence of events that led to the current state. State is reconstructed by replaying events. Benefits: full audit log, temporal queries, event-driven integration. Trade-off: complexity, eventual consistency, snapshot management for performance.

---

**Q11. How do you handle eventual consistency across microservices?**

**A:**
1. Design for idempotency so duplicate event processing is safe.
2. Use Sagas with compensating transactions for failure scenarios.
3. Use outbox pattern to atomically save state and publish events.
4. Communicate to users that operations may be "processing" (optimistic UI).
5. Use correlation IDs to trace across services and detect inconsistencies.

---

## Resilience

**Q12. What is the Circuit Breaker pattern?**

**A:** A circuit breaker wraps a remote call and monitors failures. Three states:
- **Closed**: Normal operation. Requests pass through.
- **Open**: Failure threshold exceeded. Requests fail immediately with a fallback (no actual call made).
- **Half-Open**: After a timeout, a test request is allowed. If it succeeds, the circuit closes; if it fails, it opens again.

Prevents cascading failures: if Service B is down, the circuit breaker stops Service A from repeatedly timing out, freeing resources for other work.

---

**Q13. What is the Bulkhead pattern?**

**A:** Isolate resources (thread pools, connection pools) for different downstream services. If Service B consumes all threads in the shared pool, Service C calls are not impacted. Named after ship bulkheads that prevent flooding from spreading. Implemented via separate `ThreadPoolBulkhead` in Resilience4j.

---

**Q14. What is retry with exponential backoff and what are the risks?**

**A:** On transient failures, retry after increasing delays (1s, 2s, 4s, 8s...) with jitter (random delay added) to avoid synchronized retry storms (thundering herd). Risk: if the upstream is overloaded, retries make it worse. Combine with circuit breakers to stop retrying when the service is clearly down.

---

## Security

**Q15. How do you handle authentication and authorization across microservices?**

**A:** Common approaches:
1. **Centralized Auth Server** (OAuth 2.0 / OpenID Connect): Issues JWTs. Each service validates the token independently (no central auth call per request).
2. **API Gateway validates tokens** before routing to internal services.
3. **Service-to-service auth**: mTLS or short-lived service tokens.

Never pass raw credentials between services; use signed tokens with claims.

---

**Q16. What is mTLS and why is it used?**

**A:** Mutual TLS means both the client and server authenticate each other with certificates, not just the server authenticating itself. In microservices, this ensures that service A can verify it's actually talking to service B (not a compromised internal service). Service meshes automate certificate rotation and enforcement.

---

## Observability

**Q17. What is distributed tracing?**

**A:** Distributed tracing follows a request across multiple services, assigning a `traceId` that propagates with each inter-service call. Each service creates a **span** (start time, duration, metadata). Collected traces reveal bottlenecks and failure points across the entire call chain. Tools: Jaeger, Zipkin, AWS X-Ray, Google Cloud Trace.

---

**Q18. What is the three pillars of observability?**

**A:**
- **Logs**: Textual records of discrete events. Good for debugging.
- **Metrics**: Aggregated numeric measurements over time (CPU, request rate, error rate). Good for alerting and dashboards.
- **Traces**: End-to-end request flow across services. Good for latency analysis and root cause analysis.

All three together give full system visibility.

---

## API Versioning & Deployment

**Q19. How do you version a microservice API without breaking consumers?**

**A:** Options:
- **URL path versioning**: `/v1/orders`, `/v2/orders` — explicit, easy to route.
- **Header versioning**: `Accept: application/vnd.api+json;version=2` — cleaner URLs but harder to test.
- **Backward-compatible changes**: Add optional fields, never remove or rename. Prefer additive changes.
- **Consumer-driven contract testing** (Pact): Consumers define what they need; providers verify compliance before deployment.

---

**Q20. What is the outbox pattern?**

**A:** A pattern to reliably publish events alongside database writes without a distributed transaction. Write both the domain data and the event to an "outbox" table in the same local DB transaction. A separate process reads the outbox table and publishes events to the message broker. This guarantees atomicity: either both are committed or neither is.

---
