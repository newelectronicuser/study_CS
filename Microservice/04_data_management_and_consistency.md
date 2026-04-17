# Data Management and Consistency

In microservices, managing data across distributed boundaries is one of the most significant challenges.

## 1. Database per Service
Each microservice has its own private data store.
- **Why?**: Loose coupling. Each service can use the database type best suited for its needs (Polyglot Persistence).
- **Challenge**: Difficult to perform cross-service joins and maintain data consistency.

## 2. Distributed Transactions and Saga Pattern
Traditional ACID transactions (2PC - Two-Phase Commit) don't scale well in microservices. The **Saga Pattern** is used to manage long-running business transactions via a sequence of local transactions.
- **Choreography**: Each service produces and listens to events from other services. No central controller. (Decoupled but hard to track).
- **Orchestration**: A central "Orchestrator" directs the participants on which local transactions to execute. (Easier to monitor, but centralizes logic).

## 3. CQRS (Command Query Responsibility Segregation)
Separates read and write operations into different models.
- **Command Side**: Handles creates/updates (optimized for writes).
- **Query Side**: Handles reads (optimized for complex queries, often uses a different database like Elasticsearch or a materialized view).
- **Why?**: Allows independent scaling of read and write performance.

## 4. Event Sourcing
Instead of storing only the current state of an object, you store a sequence of state-changing events.
- **Pros**: Full audit log, ability to "time travel" to any state, naturally fits with CQRS.
- **Cons**: High complexity, difficult to query without CQRS.

## 5. Transactional Outbox Pattern
Ensures that a database update and the publishing of a corresponding event happen atomically.
1. Service updates the business table AND inserts a record into an **Outbox table** in the same transaction.
2. A separate process (Message Relay) reads from the Outbox table and publishes to a Message Broker.

## 6. Consistency Models
- **Strong Consistency**: Data is the same across all nodes at all times. (Hard to achieve in Microservices).
- **Eventual Consistency**: All nodes will eventually contain the same data given enough time. (The standard for high-scale distributed systems).

> [!CAUTION]
> **Avoid Shared Databases**: A shared database between microservices is an "anti-pattern" that leads to tight coupling and makes independent deployments nearly impossible.
