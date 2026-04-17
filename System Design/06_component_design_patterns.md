# Component Design Patterns

Specific solutions for common challenges in distributed systems.

## 1. Unique ID Generation
In a distributed system, you cannot rely on an auto-incrementing database ID.
- **UUID**: 128-bit globally unique ID. (Pros: Simple, No coordination needed. Cons: Large, Not strictly sortable).
- **Snowflake (Twitter)**: 64-bit ID. Consists of:
    - **Timestamp**: (41 bits) - Allows sorting by time.
    - **Machine ID**: (10 bits) - Prevents collisions between different nodes.
    - **Sequence ID**: (12 bits) - Prevents collisions within the same millisecond.

## 2. Rate Limiting
Protecting your services from being overwhelmed by too many requests.
- **Token Bucket**: Tokens are added at a fixed rate. Requests consume a token. Best for handling bursts of traffic.
- **Leaky Bucket**: Requests enter a queue and are processed at a fixed rate. Smoothes out traffic.
- **Fixed Window**: Simple, but "peaks" at the boundary of two windows can double the allowed rate.
- **Sliding Window Log/Counter**: Most accurate but more memory/CPU intensive.

## 3. Distributed Locking
Ensuring that only one process can access a resource across multiple nodes.
- **Redlock (Redis)**: Acquired using a specific TTL and secret.
- **Zookeeper**: Uses "ephemeral nodes" to manage leadership and locks.
- **Etcd**: Used by Kubernetes for configuration and locking.

## 4. Bloom Filters
A space-efficient probabilistic data structure used to test whether an element is a member of a set.
- **Result**: Either "definitely not in the set" or "possibly in the set."
- **Use cases**: Checking if a username is taken, filtering out malicious URLs, preventing unnecessary database lookups.

## 5. Circuit Breaker
If Service B is struggling, Service A "opens" the circuit and immediately returns an error or fallback value instead of waiting for Service B to time out.
- **States**: 
    - **Closed**: Requests flow normally.
    - **Open**: Requests fail immediately.
    - **Half-Open**: A few requests are allowed through to see if Service B has recovered.

## 6. API Gateway
A server that acts as an API front-end, receives API requests, enforces throttling and security policies, passes requests to the back-end service, and then passes the response back to the requester.

> [!TIP]
> **Idempotency Keys**: 
> When a client performs a POST request (like a payment), they should send a unique `Idempotency-Key` header. The server stores this key; if the client retries the request due to a network error, the server can identify the duplicate and return the previous result without processing the payment again.
