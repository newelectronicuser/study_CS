# Design Patterns and Resilience

In a distributed system, failures are inevitable. Resilience patterns help your system handle these failures gracefully.

## 1. API Gateway Pattern
A single entry point for all clients.
- **Responsibilities**: Request routing, API composition, Authentication, Rate limiting, Caching, Metrics.
- **BFF (Backends for Frontends)**: A variation where you have different gateways for different clients (e.g., Mobile BFF vs. Web BFF) to optimize the data payload for each device.

## 2. Circuit Breaker Pattern
Prevents a failure in one service from cascading to others.
- **States**:
    - **Closed**: Requests pass through normally.
    - **Open**: Requests fail immediately (fallback is returned).
    - **Half-Open**: A limited number of requests are allowed to see if the service has recovered.
- **Why?**: Avoids hanging resources (threads) waiting for a timed-out service.

## 3. Bulkhead Pattern
Isolates elements of an application into pools so that if one fails, the others will continue to function.
- **Example**: Using different thread pools or connection pools for different services. If the "Orders" service pool is exhausted, the "Product Catalog" service remains unaffected.

## 4. Sidecar Pattern
Deploy components of an application as a separate process or container alongside the main application.
- **Use case**: Logging, monitoring, security (mTLS), configuration.
- **Service Mesh**: A infrastructure layer that uses sidecar proxies (like Envoy) to manage service-to-service communication.

## 5. Retry Pattern
Automatically retries a failed operation that is likely to succeed if retried.
- **Best Practice**: Use **Exponential Backoff** and **Jitter** (random delay) to avoid overwhelming the system with simultaneous retries (Thundering Herd problem).

## 6. Strangler Fig Pattern
A strategy for modernizing a monolithic application by gradually replacing specific functionalities with new microservices. Over time, the new system "strangles" the old one until it can be decommissioned.

> [!IMPORTANT]
> **Idempotency** is crucial for retries. Ensuring that an operation (like a payment or order creation) can be safely repeated without creating duplicate side effects is a fundamental requirement.
