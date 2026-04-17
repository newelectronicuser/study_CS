# Observability in Microservices

In a distributed system, you cannot simply log into a machine to see what's wrong. Observability is the ability to understand the internal state of a system based on its external outputs.

## 1. The Three Pillars
### Metrics (e.g., Prometheus, Grafana)
Aggregation of data over a period of time (e.g., Error rate, Request latency, CPU usage). Used for alerting and performance monitoring.

### Logging (e.g., ELK Stack - Elasticsearch, Logstash, Kibana)
Discrete events that happen in the system.
- **Centralized Logging**: Logs from all services must be sent to a single location to be searchable.
- **Structured Logging**: Using JSON instead of plain text for easier parsing.

### Tracing (e.g., Jaeger, Zipkin, OpenTelemetry)
Following a single request as it travels across multiple microservices.
- **Correlation ID**: A unique ID generated at the API Gateway and passed in the headers of all downstream requests.

## 2. Health Checks
- **Liveness Probe**: Tells the orchestrator (like Kubernetes) if the container is still alive. If it fails, the container is restarted.
- **Readiness Probe**: Tells the orchestrator if the service is ready to accept traffic.

## 3. Top-Level Metrics (GOLDEN SIGNALS)
1.  **Latency**: Time it takes to service a request.
2.  **Traffic**: How much demand is being placed on the system.
3.  **Errors**: The rate of requests that fail.
4.  **Saturation**: How "full" your service is (CPU, Memory, I/O).

## 4. Log Aggregation Challenges
- **Volume**: Microservices can generate terabytes of logs daily.
- **Serialization**: Ensuring all services use a consistent log format.
- **Privacy**: Ensuring PII (Personally Identifiable Information) is not logged.

## 5. Service Mesh for Observability
A Service Mesh (like Istio) can automatically generate metrics and traces for all service-to-service traffic without changing the application code.

> [!TIP]
> **Tracing is your best friend** during debugging. Without it, finding which microservice in a long chain caused a latency spike or an error is nearly impossible.
