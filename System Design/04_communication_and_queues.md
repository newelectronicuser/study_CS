# Communication and Queues

How different parts of a system talk to each other determines its latency, reliability, and complexity.

## 1. Protocols
- **REST (HTTP 1.1/2)**: Standard for web APIs. JSON-based, stateless, and human-readable.
- **gRPC (HTTP 2)**: Binary-based (Protobuf), high performance, strongly typed. Best for internal service-to-service communication.
- **WebSockets**: Full-duplex communication over a single TCP connection. Best for real-time apps (chat, live notifications).
- **GraphQL**: Allows clients to request exactly the data they need. Reduces over-fetching.

## 2. Proxies
- **Forward Proxy**: Sits in front of the **client**. Used for anonymity, filtering, or caching for the user.
- **Reverse Proxy**: Sits in front of the **server** (e.g., Nginx). Used for load balancing, SSL termination, and caching.

## 3. Message Queues (Asynchronous)
Used to decouple components and handle bursts in traffic.
- **Pub/Sub**: Producers send messages to a topic; all subscribers to that topic receive the message.
- **Push vs. Pull**:
    - **Kafka**: Pull-based. Consumers pull data at their own pace. Excellent for high-throughput stream processing.
    - **RabbitMQ**: Push-based. The broker pushes messages to consumers. Best for complex routing and task distribution.

## 4. Key Concepts in Messaging
- **Idempotency**: Ensuring that performing an operation multiple times has the same effect as doing it once. (Crucial for message retries).
- **Dead Letter Queue (DLQ)**: A queue for messages that cannot be processed successfully after several attempts.
- **Guaranteed Delivery**:
    - **At most once**: Messages may be lost but never duplicated.
    - **At least once**: Messages are never lost but may be duplicated. (Preferred).
    - **Exactly once**: The most complex/expensive guarantee.

## 5. Polling vs. WebSockets vs. SSE
| Method | Type | Use Case |
| :--- | :--- | :--- |
| **Short Polling** | Client repeatedly calls server. | Simple, but high overhead. |
| **Long Polling** | Server holds request until data is available. | Better than short polling, still high overhead. |
| **WebSockets** | Bi-directional, long-lived. | Real-time (Chat, Gaming). |
| **SSE** | Server-to-client unidirectional. | Live scores, Twitter feeds. |

> [!IMPORTANT]
> **Messaging for Resilience**: 
> If Service A calls Service B synchronously, and Service B is down, Service A fails too. If Service A puts a message in a queue, it can continue its work while Service B processes the message whenever it comes back up.
