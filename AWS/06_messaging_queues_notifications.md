# 06 Messaging: SQS, SNS, EventBridge, Kinesis

## Core Concepts

### SQS (Simple Queue Service)
Message queuing service for decoupling microservices.
*   **Standard Queue:** Best-effort ordering, at-least-once delivery, nearly unlimited throughput.
*   **FIFO Queue:** First-In-First-Out, exactly-once processing, limited throughput (300 msgs/sec, or 3000 with batching).
*   **DLQ (Dead Letter Queue):** Stores messages that failed processing after $N$ attempts.

### SNS (Simple Notification Service)
Pub/Sub messaging service.
*   **Fan-out:** One message sent to SNS can be delivered to multiple SQS queues, Lambda functions, or HTTP endpoints.
*   **Filtering:** Allows subscribers to receive only a subset of messages based on attributes.

### EventBridge (Event Bus)
Serverless event bus for building event-driven applications.
*   **Capabilities:** Schema Registry, Scheduling (Cron), and integration with 3rd party SaaS (Zendesk, Datadog).
*   **Difference from SNS:** EventBridge is better for complex routing and integrating with external/internal events; SNS is better for high-throughput fan-out.

### Kinesis (Streaming)
Real-time data streaming for big data.
*   **Data Streams:** Shard-based, manual or auto-scaling. Retains data for 24h to 365 days.
*   **Data Firehose:** Managed service to load streams into S3/Redshift/OpenSearch. Near real-time (60s buffer).

---

## Interview Deep Dives

### 1. "SQS vs. SNS: When to use which?"
**Answer:**
*   **SQS (Pull):** Use for **Decoupling** and **Buffering**. The consumer controls when and how many messages to process. Ideal for work distribution.
*   **SNS (Push):** Use for **Fan-out**. If one event needs to trigger multiple independent actions (e.g., updating a database AND sending an email), SNS pushes to all subscribers instantly.

### 2. "Explain the SQS Visibility Timeout."
**Answer:**
*   **What it is:** The period during which SQS prevents other consumers from receiving and processing a message that has already been picked up.
*   **Trade-off:** If the visibility timeout is too short, the message might be processed twice. If too long, a failed process won't be retried quickly.
*   **Best Practice:** Set it to $6 \times$ your function's timeout.

### 3. "Kinesis vs. SQS: Which for big data?"
**Answer:**
*   **SQS:** Each message is processed independently and deleted. No "replay" capability.
*   **Kinesis:** Multiple consumers can read the same stream independently. Supports "replay" by moving the shard iterator. Best for log aggregation, real-time analytics, and clickstream data.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **SQS** | Load leveling, async tasks | No ordering (standard), finite message size (256KB). |
| **SNS** | Real-time notifications | No persistence; if subscriber is down, message is lost (unless retried). |
| **EventBridge** | Cross-account events, SaaS | Higher latency ($~500$ms) compared to SNS. |
| **Kinesis** | Real-time large-scale streams | Complex handling of shards and consumers (KCL). |

---

## Common Interview Scenarios

### Scenario: "How do you ensure exactly-once processing in a high-scale environment?"
**Troubleshooting/Design:**
1.  **SQS FIFO:** Best if ordering is also required. Uses Message Deduplication IDs.
2.  **Idempotency:** The best senior answer. Design your database/logic so that processing the same message twice has no side effect (e.g., `Update if version = X` or using a unique Transaction ID).
3.  **DynamoDB:** Use a "ProcessedMessages" table to track IDs.

### Scenario: "Design a system to handle a viral spike in sign-ups."
**Key Details:**
*   **API Gateway + SQS:** Buffer the incoming requests.
*   **Lambda:** Read from SQS in batches.
*   **SNS:** Fan-out to downstream services (Email service, Analytics service).
*   **Why:** This prevents the database from being overwhelmed (backpressure) and ensures no sign-up data is lost.
