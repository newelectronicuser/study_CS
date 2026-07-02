# REST API & Idempotency — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## REST Fundamentals

1. What is REST? What are the six architectural constraints of a RESTful system?
2. What is the difference between REST and RPC (e.g., gRPC, SOAP)?
3. What is a resource in REST? How are resources identified?
4. What are the standard HTTP methods? What is the intended semantic of each?
5. What is the difference between `PUT` and `PATCH`?
6. What are HTTP status codes? Give examples for 2xx, 3xx, 4xx, and 5xx ranges.
7. What is the difference between `401 Unauthorized` and `403 Forbidden`?
8. What is the difference between `404 Not Found` and `410 Gone`?
9. What is content negotiation in REST?
10. What is HATEOAS and is it commonly implemented in practice?

---

## Idempotency (Core Topic)

11. What is idempotency in the context of HTTP and REST APIs?
12. Which HTTP methods are idempotent by specification? Which are not?
13. Is `POST` idempotent? Why or why not? How can you make it idempotent?
14. Is `DELETE` idempotent? Justify your answer.
15. What is the difference between idempotency and safety in HTTP methods?
16. Why is idempotency critical in distributed systems and microservices?
17. What is an idempotency key? How do you implement it on the server side?
18. How do you handle duplicate `POST` requests using an idempotency key?
19. What are the challenges of implementing idempotency with database writes?
20. How does Stripe implement idempotency for payment APIs? What can you learn from it?
21. How would you design an idempotent order creation API?
22. What happens if an idempotency key expires before the client retries?
23. How would you store idempotency keys — in a cache or a database? What are the trade-offs?
24. What is the relationship between idempotency and at-least-once delivery in messaging systems?
25. What is deduplication at the consumer level and how does it relate to idempotency?

---

## API Design

26. What is API versioning? What are the strategies (path, header, query param)?
27. What are the trade-offs between versioning strategies?
28. What is pagination? Compare offset-based, cursor-based, and keyset pagination.
29. How do you design a REST API for filtering and sorting?
30. What is the difference between an API key and OAuth 2.0 for authentication?
31. What is rate limiting? How do you implement it in a REST API?
32. What is throttling and how does it differ from rate limiting?
33. What is CORS? How do you configure it on a REST API?
34. How do you handle long-running operations in a REST API (polling, webhooks, async)?

---

## Security

35. What is SQL injection and how do REST APIs prevent it?
36. What is CSRF and how does it affect REST APIs?
37. What is the difference between authentication and authorization in an API?
38. What is JWT? What are its components (header, payload, signature)?
39. What are the security concerns with storing JWTs in localStorage vs httpOnly cookies?
40. How do you prevent API key leakage in client-side applications?
41. What is the OWASP API Security Top 10?

---

## Performance & Observability

42. What is HTTP caching? How do `Cache-Control`, `ETag`, and `Last-Modified` headers work?
43. What is the difference between conditional GET and unconditional GET?
44. How do you implement request tracing and correlation IDs in a REST API?
45. What is an API gateway and what cross-cutting concerns does it address?
46. How do you measure and improve REST API latency?

---

## GraphQL (Comparative)

47. What is GraphQL and how does it differ from REST?
48. What is the N+1 problem in GraphQL and how is it solved?
49. When would you choose GraphQL over REST?

---
