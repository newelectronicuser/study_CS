# Microservices Architecture — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

1. What is a microservices architecture? How does it differ from a monolithic architecture?
2. What are the key principles that define a microservice?
3. What is the single responsibility principle as applied to microservices?
4. What are the advantages and disadvantages of microservices?
5. When would you choose a monolith over microservices?
6. What is the strangler fig pattern for migrating from a monolith to microservices?
7. What is bounded context in Domain-Driven Design and how does it map to a microservice?
8. What is service cohesion and service coupling? How do they affect microservice design?

---

## Communication

9. What is synchronous vs asynchronous communication in microservices?
10. When would you use REST vs gRPC vs message queues for inter-service communication?
11. What is the difference between an event-driven architecture and a request-response architecture?
12. What is choreography vs orchestration in microservices? What are the trade-offs?
13. What is a message broker? Name two popular ones and explain when you'd use each.
14. What is an API Gateway? What responsibilities does it typically handle?
15. What is the difference between an API Gateway and a reverse proxy?
16. What is a service mesh (e.g., Istio, Linkerd)? What problems does it solve?

---

## Data Management

17. What is the "database per service" pattern? Why is it important?
18. What is the Saga pattern? Explain choreography-based vs orchestration-based sagas.
19. What is the CQRS pattern? How does it work in a microservices context?
20. What is Event Sourcing and how does it complement CQRS?
21. What is the two-phase commit problem in distributed transactions?
22. How do you handle eventual consistency across microservices?

---

## Resilience & Fault Tolerance

23. What is the Circuit Breaker pattern? How does it prevent cascading failures?
24. What are the three states of a circuit breaker?
25. What is the Bulkhead pattern?
26. What is retry with exponential backoff? What are the risks?
27. What is the Timeout pattern and why is it important?
28. What is a fallback strategy? How does it relate to circuit breakers?
29. What is chaos engineering? How is it used to validate microservice resilience?

---

## Service Discovery & Load Balancing

30. What is service discovery? What is the difference between client-side and server-side discovery?
31. What is Consul, Eureka, or etcd used for in microservices?
32. What is a sidecar proxy? How does it relate to a service mesh?
33. How does a load balancer differ from a service registry?

---

## Security

34. How do you handle authentication and authorization across microservices?
35. What is JWT and how is it used in a microservices environment?
36. What is OAuth 2.0 and the role of an Authorization Server in microservices?
37. What is mTLS and why is it used for service-to-service communication?
38. What is the principle of zero trust networking?

---

## Observability

39. What is distributed tracing? Name two tools used for it.
40. What is the difference between logging, metrics, and traces (the three pillars of observability)?
41. What is correlation ID and why is it important in microservices?
42. How do you aggregate logs from dozens of microservices?
43. What is the difference between a health check endpoint and a readiness probe?

---

## Deployment & Operations

44. What is the twelve-factor app methodology? How does it apply to microservices?
45. What is a canary deployment? How do you implement it for a microservice?
46. What is blue-green deployment and how does it achieve zero-downtime releases?
47. How do you handle schema migrations in a microservices deployment?
48. What is a feature flag and how does it help in microservice releases?
49. How do you version a microservice API without breaking consumers?
50. What is idempotency and why is it critical for microservices?

---
