# Microservices Architecture — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is a microservices architecture? How does it differ from a monolithic architecture?**

**A:**
*   **Microservices Architecture:** An architectural style where a single application is built as a suite of small, independent services. Each service runs in its own process, manages its own database, and communicates via lightweight protocols (HTTP/REST, gRPC, or message queues). They are independently deployable, scalable, and can be written in different programming languages.
*   **Monolithic Architecture:** All software components (UI, business logic, data access) are packaged and deployed together as a single executable or deployable unit sharing a single database. Scaling the system requires scaling the entire application.

---

**Q2. What are the key principles that define a microservice?**

**A:**
*   **Single Responsibility:** Each service has a narrow, well-defined business capability.
*   **Independent Deployability:** Updating a service should not require redeploying other services.
*   **Decentralized Data Management:** Each service owns its database (no shared database).
*   **Decentralized Governance:** Teams can choose the best technology stack for their specific service.
*   **Failure Isolation (Fault Tolerant):** A failure in one service should not crash the entire application.

---

**Q3. What is the single responsibility principle as applied to microservices?**

**A:** At the service level, SRP means a microservice should model a single cohesive business domain or subdomain (a Bounded Context in Domain-Driven Design). It should have only "one reason to change" based on business requirements. For example, the `PaymentService` is only responsible for processing transactions; it does not manage user profiles or generate invoices.

---

**Q4. What are the advantages and disadvantages of microservices?**

**A:**
*   *Advantages:*
    *   **Independent Scaling:** Scale only the resource-heavy services (e.g. video processing vs user profiles).
    *   **Faster Time-to-Market:** Smaller teams work on separate services independently without deployment blockages.
    *   **Fault Isolation:** Memory leaks or crashes in one service do not take down the entire system.
    *   **Technology Flexibility:** Use Python for ML services and Java/Go for high-throughput APIs.
*   *Disadvantages:*
    *   **Operational Complexity:** Harder to deploy, monitor, and manage (requires CI/CD, container orchestration).
    *   **Network Latency & Reliability:** Distributed systems introduce remote network calls, increasing latency and failure rates.
    *   **Data Consistency:** Resolving queries or maintaining transactions across multiple databases is complex.
    *   **Testing Complexity:** Integration and end-to-end testing become significantly harder.

---

**Q5. When would you choose a monolith over microservices?**

**A:** Choose a monolith when:
*   The application is in its early startup phase (MVP), and domain boundaries are not yet clear.
*   The team is small and lacks experience in distributed system operations (Docker, K8s, DevOps).
*   Low latency between modules is a critical requirement.
*   The workload is simple and can easily scale vertically on a single server, keeping deployment simple.

---

**Q6. What is the strangler fig pattern for migrating from a monolith to microservices?**

**A:** The Strangler Fig pattern is a migration strategy where you incrementally replace monolithic functionalities with microservices. An API Gateway or interceptor routes traffic. For new features or refactored modules, traffic is redirected to the new microservice. Over time, the monolith's surface area shrinks (is "strangled") until it can be fully decommissioned without a high-risk "big bang" rewrite.

---

**Q7. What is bounded context in Domain-Driven Design and how does it map to a microservice?**

**A:** A Bounded Context is a boundary within which a domain model applies. Inside the boundary, all terms, definitions, and code have a specific, unambiguous meaning.
*   *Mapping:* Ideally, a single microservice maps to one Bounded Context. For example, "Product" in the *Inventory Context* represents stock levels and dimensions, while "Product" in the *Sales Context* represents pricing, discounts, and description. Keeping them in separate microservices prevents model pollution.

---

**Q8. What is service cohesion and service coupling? How do they affect microservice design?**

**A:**
*   **Service Cohesion:** The degree to which the functions within a single microservice belong together. High cohesion is desirable — a service should do one thing and do it completely.
*   **Service Coupling:** The degree of direct dependency between microservices. Low coupling is desirable. High coupling (e.g., Service A must call Service B for every database read, or they share a database) makes independent deployments impossible and increases failure rates.

---

## Communication

**Q9. What is synchronous vs asynchronous communication in microservices?**

**A:**
*   **Synchronous Communication:** The client/calling service sends a request and blocks, waiting for a real-time response from the target service before continuing (e.g. REST over HTTP, gRPC).
*   **Asynchronous Communication:** The calling service sends a message (event) to a broker (e.g. RabbitMQ, Kafka) and immediately resumes its work without waiting for a response. The target service processes the message at its own pace.

---

**Q10. When would you use REST vs gRPC vs message queues for inter-service communication?**

**A:**
*   **REST (HTTP/JSON):** Best for public-facing client-to-service communication or simple, standard integrations where ease of debugging and universal compatibility are prioritized.
*   **gRPC (HTTP/2 / Protobuf):** Best for internal, low-latency, high-throughput service-to-service communication. Binary serialization and multiplexed streams make it much faster than REST.
*   **Message Queues (AMQP/Kafka):** Best for decoupled, asynchronous, event-driven workflows, background processing, and fan-out distribution.

---

**Q11. What is the difference between an event-driven architecture and a request-response architecture?**

**A:**
*   **Request-Response:** A directed, synchronous communication style. Service A explicitly tells Service B: "Do X and tell me the result." High temporal coupling.
*   **Event-Driven:** An indirect, asynchronous communication style. Service A publishes an event: "OrderCreated." It does not know or care who consumes it. Downstream services listen to this event and trigger their own workflows. Highly decoupled.

---

**Q12. What is choreography vs orchestration in microservices? What are the trade-offs?**

**A:**
*   **Orchestration:** A central controller (the orchestrator) directs the workflow, explicitly calling each participant service and coordinating the steps (like a conductor in an orchestra).
    *   *Trade-off:* Clear visibility of the workflow in one place, but the orchestrator becomes a single point of failure and bottleneck (tight coupling).
*   **Choreography:** Each service listens to events and triggers its own next steps independently (like dancers responding to music). There is no central controller.
    *   *Trade-off:* Highly decoupled and scalable, but difficult to track the overall system state or debug workflows.

---

**Q13. What is a message broker? Name two popular ones and explain when you'd use each.**

**A:** A message broker is an intermediary application that receives, stores, and routes asynchronous messages between services.
*   **RabbitMQ:** A traditional message broker supporting AMQP. Excellent for complex routing rules (exchanges, routing keys) and guaranteed, fine-grained queue delivery.
*   **Apache Kafka:** A distributed event streaming platform. Best for massive throughput, log replayability, and event sourcing. It stores messages in partitioned, append-only logs.

---

**Q14. What is an API Gateway? What responsibilities does it typically handle?**

**A:** An API Gateway is a single entry point for all client requests. It sits in front of microservices and routes requests to appropriate downstream services.
*   *Responsibilities:* Routing/reverse proxying, authentication/authorization checking, SSL termination, rate limiting, request/response transformation, and basic logging/analytics.

---

**Q15. What is the difference between an API Gateway and a reverse proxy?**

**A:**
*   **Reverse Proxy (e.g. Nginx, HAProxy):** A simple network utility that forwards client requests to backend servers, providing load balancing and SSL termination.
*   **API Gateway (e.g. Kong, Apigee, AWS API Gateway):** A specialized application-level reverse proxy that is "API-aware". It handles identity validation, rate-limiting policies, request routing based on API versions, telemetry, and security integration.

---

**Q16. What is a service mesh (e.g., Istio, Linkerd)? What problems does it solve?**

**A:** A service mesh is a dedicated infrastructure layer for managing service-to-service communication. It runs a network proxy (sidecar, e.g., Envoy) alongside each microservice container.
*   *Problems solved:* Offloads network concerns from application code, including: mutual TLS (mTLS) encryption, service discovery, load balancing, circuit breaking, retries, and distributed tracing telemetry.

---

## Data Management

**Q17. What is the "database per service" pattern? Why is it important?**

**A:** It is the requirement that each microservice owns its own database, and no other service can access it directly. Any data access must go through the service's public API.
*   *Importance:* Ensures loose coupling. If services shared a database, a schema change in Service A could break Service B. It also allows choosing the best database engine (SQL or NoSQL) for each service's specific requirements.

---

**Q18. What is the Saga pattern? Explain choreography-based vs orchestration-based sagas.**

**A:** The Saga pattern manages distributed transactions across multiple microservices via a sequence of local transactions. Each step updates a service database and triggers the next step. If a step fails, the Saga executes **compensating transactions** to undo the changes.
*   **Choreography-based:** Services publish and listen to events to progress the transaction. Simple, but hard to trace.
*   **Orchestration-based:** A central orchestrator class calls services sequentially and handles rollback triggers if any step fails. Easier to manage for complex workflows.

---

**Q19. What is the CQRS pattern? How does it work in a microservices context?**

**A:** Command Query Responsibility Segregation (CQRS) separates read and write operations into different models and databases.
*   *Microservices Context:* A service handling writes (e.g. `OrderService` using PostgreSQL) publishes events whenever data changes. A separate read service consumes these events and updates a read-optimized view database (e.g. Elasticsearch for fast search) to handle complex user queries.

---

**Q20. What is Event Sourcing and how does it complement CQRS?**

**A:**
*   **Event Sourcing:** Instead of storing only the current state of an entity, the application stores the complete history of state changes as a sequence of immutable events in an "event store" (e.g. `OrderCreated`, `OrderShipped`, `OrderDelivered`). Current state is rebuilt by replaying these events.
*   *Complements CQRS:* Event Sourcing works perfectly with CQRS because the event store acts as the write database (write-only append), and events are projected in real-time to update the CQRS read databases.

---

**Q21. What is the two-phase commit problem in distributed transactions?**

**A:** Two-Phase Commit (2PC) is a blocking protocol used to guarantee consistency across distributed databases. It involves a coordinator asking all databases to "prepare" to commit, and then sending the "commit" command if all agree.
*   *Problem:* It is highly vulnerable to network failures. If the coordinator or a database crashes during the commit phase, resources remain locked indefinitely, degrading system availability. It does not scale well in microservices.

---

**Q22. How do you handle eventual consistency across microservices?**

**A:**
1.  **Idempotent Event Consumers:** Ensure that if a message is delivered multiple times due to retries, the consumer handles it safely without duplicating actions.
2.  **Transactional Outbox Pattern:** Save the business entity and the outgoing event in the same local database transaction. A separate relay publisher reads from the outbox table and publishes events, preventing data-event mismatch.
3.  **Compensating Transactions:** Implement sagas to undo changes if later steps fail.

---

## Resilience & Fault Tolerance

**Q23. What is the Circuit Breaker pattern? How does it prevent cascading failures?**

**A:** The Circuit Breaker pattern wraps remote calls to a service. If the remote service fails repeatedly, the circuit breaker trips (opens) and immediately returns errors (or fallback data) for subsequent calls without hitting the failing service. This gives the failing service time to recover and prevents the calling service from tying up threads, stopping cascading failures across the system.

---

**Q24. What are the three states of a circuit breaker?**

**A:**
1.  **Closed:** Normal operation. Requests are forwarded to the remote service.
2.  **Open:** Remote service is failing. Requests fail immediately, bypassing the remote call. A timer starts.
3.  **Half-Open:** After the timer expires, the breaker allows a limited number of trial requests through. If they succeed, the circuit closes; if they fail, the circuit returns to the open state.

---

**Q25. What is the Bulkhead pattern?**

**A:** Named after the partitions in a ship's hull that prevent the whole ship from sinking if one compartment floods. In microservices, it means isolating resources (like thread pools, memory, or CPU) for different downstream calls. If the thread pool for calling `ServiceA` becomes exhausted due to latency, it does not affect the thread pool calling `ServiceB`.

---

**Q26. What is retry with exponential backoff? What are the risks?**

**A:**
*   **Retry with Exponential Backoff:** If a call fails, the client waits for a short period, then retries. The wait time increases exponentially with each failure (e.g. 1s, 2s, 4s, 8s).
*   *Risks:* If multiple clients retry simultaneously, they can create a **thundering herd problem** (DDoS-ing the failing backend). To mitigate this, add **jitter** (randomized delays) to the wait time.

---

**Q27. What is the Timeout pattern and why is it important?**

**A:** The Timeout pattern sets a maximum wait limit on remote API calls. If the target service does not respond within the limit, the connection is closed, and an error is returned. It is important because without timeouts, calling threads can block indefinitely waiting for slow services, eventually exhausting the application's thread pool.

---

**Q28. What is a fallback strategy? How does it relate to circuit breakers?**

**A:** A fallback is a backup plan executed when a remote call fails or a circuit breaker is open. Instead of throwing an error, the service returns cached data, default values, or executes local alternative logic.

---

**Q29. What is chaos engineering? How is it used to validate resilience?**

**A:** Chaos Engineering is the practice of intentionally injecting failures into production systems (e.g., stopping VM instances, cutting network connections, introducing latency) to verify that the application can self-heal, failover, and maintain availability without user-visible impact. (e.g. Netflix's Chaos Monkey).

---

## Service Discovery & Load Balancing

**Q30. What is service discovery? What is client-side vs server-side discovery?**

**A:** Service discovery is the mechanism of dynamically detecting the network location (IP, port) of microservice instances.
*   **Client-Side Discovery:** The calling client queries the Service Registry (e.g. Eureka) directly to get a list of healthy instances, performs load balancing, and routes the request.
*   **Server-Side Discovery:** The client calls a load balancer. The load balancer queries the Service Registry, routes the request to a healthy instance, and returns the response (e.g. AWS ALB, Kubernetes Service).

---

**Q31. What is Consul, Eureka, or etcd used for in microservices?**

**A:** They are used as **Service Registries** and key-value stores. Microservice instances register their IP/port with them during startup and send periodic heartbeats. Other services query them to find endpoints.

---

**Q32. What is a sidecar proxy? How does it relate to a service mesh?**

**A:** A sidecar proxy is a helper container (like Envoy) running alongside the main application container in the same Pod. It intercepts all inbound and outbound network traffic. A service mesh is composed of these sidecars acting as the data plane, controlled by a central control plane.

---

**Q33. How does a load balancer differ from a service registry?**

**A:**
*   **Service Registry:** A database storing the live addresses of healthy service instances.
*   **Load Balancer:** A routing device/software that distributes incoming traffic across multiple server instances based on algorithms (round-robin, least connections). It queries the service registry to know which servers are currently healthy.

---

## Security

**Q34. How do you handle authentication and authorization across microservices?**

**A:**
1.  **Edge Authentication:** The API Gateway validates incoming credentials (OAuth2/OpenID) and authenticates the user.
2.  **Downstream Propagation:** The Gateway attaches a cryptographically signed token (like a JWT) containing user identity and roles to the request headers.
3.  **Service Authorization:** Downstream services extract the JWT, verify its signature, and check user roles locally to authorize operations.

---

**Q35. What is JWT and how is it used in a microservices environment?**

**A:** JSON Web Token (JWT) is a compact, URL-safe means of representing claims between two parties.
*   *Microservices Use:* Used to pass user context securely. Because it is signed (using a public/private key pair), microservices can verify the token's authenticity independently without making remote calls to an auth server.

---

**Q36. What is OAuth 2.0 and the role of an Authorization Server?**

**A:** OAuth 2.0 is an industry-standard authorization framework. The **Authorization Server** is responsible for validating client/user identities and issuing tokens (Access Tokens, ID Tokens) that clients present to microservice APIs to gain access.

---

**Q37. What is mTLS and why is it used?**

**A:** Mutual TLS (mTLS) requires both the client and server to authenticate each other's certificates before establishing a connection. In microservices, it prevents man-in-the-middle attacks and ensures only authorized services can communicate with each other inside the internal network.

---

**Q38. What is the principle of zero trust networking?**

**A:** "Never trust, always verify." It assumes that threats exist both inside and outside the network boundary. No service is trusted based simply on its presence inside the private subnet; every request must be authenticated, authorized, and encrypted.

---

## Observability

**Q39. What is distributed tracing? Name two tools used for it.**

**A:** Distributed tracing tracks the lifecycle of a request as it flows through multiple microservices, mapping the latency and call path.
*   *Tools:* OpenTelemetry, Zipkin, Jaeger.

---

**Q40. What is the difference between logging, metrics, and traces?**

**A:** The three pillars of observability:
*   **Logs:** Discrete, timestamped text records of events (e.g. database query failed). Good for debugging.
*   **Metrics:** Aggregated numerical measurements of system behavior over time (e.g. CPU %, request rate). Good for alerting.
*   **Traces:** End-to-end request journeys across services. Good for identifying bottlenecks.

---

**Q41. What is correlation ID and why is it important?**

**A:** A Correlation ID is a unique UUID generated at the API Gateway when a client request arrives. It is propagated through the headers of all downstream HTTP/gRPC calls. It is important because it allows developers to aggregate and filter logs from dozens of different service containers to see the full path of a single client transaction.

---

**Q42. How do you aggregate logs from dozens of microservices?**

**A:** Use a centralized logging stack (e.g. EFK - Elasticsearch, Fluentd, Kibana, or Grafana Loki). A log shipper daemon (like Filebeat) runs on each node, collects stdout/stderr from containers, and streams them to a central indexing engine (Elasticsearch) where they can be queried.

---

**Q43. What is the difference between a health check endpoint and a readiness probe?**

**A:**
*   **Liveness Probe (Health Check):** Verifies if the service process is alive. If it fails, the orchestrator (Kubernetes) restarts the container.
*   **Readiness Probe:** Verifies if the service is ready to accept incoming traffic (e.g., has finished loading caches and established db connections). If it fails, the service is removed from the load balancer target pool, but not restarted.

---

## Deployment & Operations

**Q44. What is the twelve-factor app methodology? How does it apply to microservices?**

**A:** A set of 12 best practices for building cloud-native, declarative, and scalable applications. For microservices: it enforces keeping a single codebase per service, storing configuration in environment variables, treating backing services (DBs) as attached resources, keeping build/run stages separate, and running services as stateless, disposable processes.

---

**Q45. What is a canary deployment? How do you implement it?**

**A:** A deployment strategy where you roll out a new service version to a tiny subset of users (e.g. 5%) first.
*   *Implementation:* Configure the API Gateway or Service Mesh (e.g. Istio routing rule) to route 5% of traffic to the new "canary" instances and 95% to production. Monitor error rates and latency. If stable, increase to 100%.

---

**Q46. What is blue-green deployment and how does it achieve zero-downtime releases?**

**A:** Runs two identical production environments (Blue and Green). At any time, only one (e.g., Blue) is active and receiving live traffic. You deploy the new release to the inactive (Green) environment and test it. Once verified, you switch the router/load balancer to point to Green. If issues occur, you roll back immediately by switching traffic back to Blue.

---

**Q47. How do you handle schema migrations in a microservices deployment?**

**A:** Use the **Expand and Contract (Parallel Run)** pattern:
1.  **Expand:** Add new columns/tables to the database without deleting old ones. Modify the service to write to both the old and new columns.
2.  **Migrate:** Run a background script to copy old data to new structures.
3.  **Contract:** Update the service to read/write only from the new structure. Drop the old columns/tables.
This ensures old container instances running during a rolling update do not crash when they see schema changes.

---

**Q48. What is a feature flag and how does it help in microservice releases?**

**A:** Feature flags (toggles) allow enabling or disabling application features at runtime without redeploying code. It helps decouple deployment (pushing code to production) from release (making features visible to users), enabling safer testing and fast rollbacks.

---

**Q49. How do you version a microservice API without breaking consumers?**

**A:**
1.  **URI Path Versioning:** `/api/v1/users` vs `/api/v2/users`. Simple and clear.
2.  **Header/Media Type Versioning:** Clients specify the version in the `Accept` header.
3.  **Grace Period:** Maintain older version endpoints concurrently, monitor usage, deprecate slowly, and communicate sunset timelines to clients.

---

**Q50. What is idempotency and why is it critical for microservices?**

**A:** Idempotency means that executing an operation multiple times produces the same result as executing it once.
*   *Criticality:* In microservices, network requests can timeout. A client might retry a request (e.g., `ChargePayment`). If the API is not idempotent (e.g., using idempotency keys), the user might be charged twice.

---
