# Architecture and Patterns

High-level organizational patterns that define how a system is built and maintained.

## 1. Monolith vs. Microservices
- **Monolith**: All logic is in a single deployment unit. (Simple to start, hard to scale/maintain).
- **Microservices**: Independent services that talk over the network. (Complex orchestration, easy to scale individual parts).

## 2. Event-Driven Architecture (EDA)
Instead of direct API calls, services communicate by emitting and consuming events.
- **Sagas**: Managing distributed transactions across microservices.
    - **Choreography**: Each service produces an event that triggers the next service. (Decentralized).
    - **Orchestration**: A central "Saga Manager" coordinates the flow. (Centralized).
- **Event Sourcing**: Instead of storing current state, store the entire history of events. State is reconstructed by replaying events.
- **CQRS (Command Query Responsibility Segregation)**: Separates the "write" model (Command) from the "read" model (Query). Best for complex domains where reads and writes have very different performance needs.

## 3. Communication Patterns
- **Synchronous**: Request-Response (REST/gRPC).
- **Asynchronous**: Fire-and-forget (Queues/Pub-Sub).

## 4. Service Mesh and Sidecars
- **Sidecar**: A helper process that runs alongside the main service (e.g., handling logging, security, or proxying).
- **Service Mesh (Istio/Linkerd)**: A dedicated infrastructure layer for handling service-to-service communication, providing security, observability, and traffic management without changing the application code.

## 5. Deployment Strategies
- **Blue-Green**: Routing traffic between two identical production environments.
- **Canary**: Gradually rolling out a change to a small subset of users.
- **Rolling Update**: Replacing instances one by one.

## 6. Serverless Architecture
Building applications without managing servers (FaaS - Function as a Service).
- **Pros**: Automatic scaling, pay-as-you-go, no maintenance.
- **Cons**: "Cold starts," execution time limits, limited control over the environment.

> [!IMPORTANT]
> **Don't Start with Microservices**: 
> Microservices introduce "distributed system overhead" (network latency, eventual consistency, complex debugging). Many successful companies (like Instagram/Shopify) started as monoliths and only split when organizational or scaling needs required it.
