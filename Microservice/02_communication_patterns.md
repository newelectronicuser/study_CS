# Communication Patterns

Microservices must interact with each other. Choosing the right communication strategy is vital for performance and reliability.

## 1. Synchronous Communication
The sender sends a request and waits for a response from the receiver.
- **REST (HTTP/JSON)**: The most common. Lightweight, easy to understand, and browser-friendly. Pros: Standardization. Cons: Verbose, no native streaming.
- **gRPC (HTTP2/Protobuf)**: Uses Protocol Buffers for binary serialization. Pros: High performance, strongly typed, bidirectional streaming. Cons: Not browser-friendly without a proxy.

## 2. Asynchronous Communication
The sender sends a message and doesn't wait for an immediate response.
- **Messaging (Message Brokers)**: Uses a broker like **RabbitMQ** or **Apache Kafka**.
- **Patterns**:
    - **Point-to-Point**: Message sent to one specific queue.
    - **Publish/Subscribe (Pub/Sub)**: Message published to a topic, and all subscribers receive it.

### Why Asynchronous?
- **Decoupling**: Services don't need to know about each other.
- **Resilience**: If a receiver is down, the message stays in the queue.
- **Scalability**: Can handle spikes in traffic by processing messages at their own pace.

## 3. Service Discovery
In a dynamic microservices environment, instances come and go with changing IP addresses.
- **Service Registry (e.g., Eureka, Consul)**: A database containing the network locations of service instances.
- **Patterns**:
    - **Client-Side Discovery**: Client queries the registry to find a service instance.
    - **Server-Side Discovery**: Client makes a request via a Load Balancer, which queries the registry.

## 4. Load Balancing
- **Client-Side**: The client (or a library like Netflix Ribbon) chooses which instance to send the request to.
- **Server-Side**: A dedicated hardware or software load balancer (like Nginx or AWS ELB) distributes traffic.

> [!IMPORTANT]
> **API Gateway vs. Load Balancer**:
> A Load Balancer just routes traffic. An **API Gateway** provides higher-level features like authentication, rate limiting, request transformation, and protocol translation.
