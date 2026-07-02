# REST API & Idempotency — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## REST Fundamentals

**Q1. What is REST and what are its six architectural constraints?**

**A:** REST (Representational State Transfer) is an architectural style for distributed hypermedia systems. The six constraints:
1. **Client-Server**: Separation of UI concerns from data storage concerns.
2. **Stateless**: Each request must contain all information needed. Server holds no session state.
3. **Cacheable**: Responses must define themselves as cacheable or non-cacheable.
4. **Uniform Interface**: Consistent resource identification, manipulation through representations, self-descriptive messages.
5. **Layered System**: Client can't tell if it's connected to the origin server or an intermediary (load balancer, cache).
6. **Code on Demand** (optional): Servers can extend client functionality by sending executable code (e.g., JavaScript).

---

**Q2. What are the standard HTTP methods and their semantics?**

**A:**
| Method | Semantics | Safe? | Idempotent? |
|---|---|---|---|
| GET | Retrieve resource | ✅ | ✅ |
| HEAD | Like GET, headers only | ✅ | ✅ |
| POST | Create/process | ❌ | ❌ |
| PUT | Replace entire resource | ❌ | ✅ |
| PATCH | Partial update | ❌ | ✅ (if absolute) |
| DELETE | Remove resource | ❌ | ✅ |
| OPTIONS | Describe allowed methods | ✅ | ✅ |

---

**Q3. What is the difference between `PUT` and `PATCH`?**

**A:** `PUT` replaces the entire resource. Omitting a field means it's removed or reset to default. `PATCH` applies a partial update — only the specified fields are changed, others remain untouched. `PUT` requires the client to send the full representation; `PATCH` sends only the delta.

Example: Updating only an email address — `PATCH` sends `{"email": "new@email.com"}` while `PUT` must send the entire user object.

---

## Idempotency (Core Topic)

**Q4. What is idempotency in HTTP?**

**A:** An operation is idempotent if making the same request multiple times produces the same result as making it once. The state of the server is the same after one or more identical requests. This is critical for safe retries in distributed systems where "did my request get processed?" is unclear.

---

**Q5. Which HTTP methods are idempotent and which are not?**

**A:**
- **Idempotent**: `GET`, `HEAD`, `PUT`, `DELETE`, `OPTIONS` — calling them multiple times has the same effect as calling once.
- **Not idempotent**: `POST` — creating an order twice creates two orders.
- **PATCH**: Idempotent if the update is absolute (set field to value X), NOT idempotent if relative (increment by 1).

---

**Q6. Is `DELETE` idempotent? Justify.**

**A:** Yes, `DELETE` is idempotent. The first `DELETE /orders/123` removes the order. The second `DELETE /orders/123` returns `404 Not Found` — but the **server state** is the same (the order is still gone). The HTTP spec defines idempotency in terms of server state, not response codes. Clients can safely retry `DELETE`.

---

**Q7. What is the difference between idempotency and safety?**

**A:**
- **Safe**: The operation has no side effects on server state. Read-only. (GET, HEAD, OPTIONS)
- **Idempotent**: Multiple identical requests have the same server-side effect as one. (GET, PUT, DELETE)

All safe methods are idempotent, but not all idempotent methods are safe. `DELETE` is idempotent but not safe (it modifies state). `POST` is neither safe nor idempotent.

---

**Q8. Why is idempotency critical in distributed systems?**

**A:** In distributed systems, network failures make it impossible to know if a request was received and processed, partially processed, or not received at all. Without idempotency, retry logic can cause duplicate operations (double charges, double orders). With idempotency, clients can safely retry any number of times and the outcome is correct regardless.

---

**Q9. What is an idempotency key and how do you implement it?**

**A:** An idempotency key is a unique client-generated identifier (UUID) sent with a request (usually as a header: `Idempotency-Key: uuid`). The server:
1. Checks if the key exists in a persistent store (DB or Redis).
2. If yes, returns the stored response without re-executing.
3. If no, executes the operation, stores the result against the key, and returns the response.

This makes `POST` effectively idempotent.

---

**Q10. How does Stripe implement idempotency for payment APIs?**

**A:** Stripe accepts an `Idempotency-Key` header on any `POST` request. The key is stored with the request parameters and the result for 24 hours. If the same key is sent again with the same parameters, Stripe returns the cached response. If the same key is used with different parameters, Stripe returns a 422 error — preventing accidental parameter variation. This model is widely adopted as best practice.

---

**Q11. What are the challenges of implementing idempotency with database writes?**

**A:**
1. **Atomicity**: The idempotency key storage and the actual operation must be atomic (same transaction). Otherwise a crash between them creates inconsistency.
2. **Concurrency**: Two parallel requests with the same key must not both execute the operation (use DB unique constraint or distributed lock on the key).
3. **Key expiry**: Keys stored forever waste space; expired keys allow duplicate execution after expiry.
4. **Distributed systems**: If the service is replicated, the idempotency store must be shared (Redis or DB, not in-memory).

---

**Q12. What is the relationship between idempotency and at-least-once delivery?**

**A:** Message queues like Kafka and SQS guarantee at-least-once delivery — a message may be delivered more than once (no exactly-once guarantee without special configuration). If message consumers are not idempotent, duplicate delivery causes incorrect state (double charges, etc.). Idempotent consumers deduplicate using a message ID, making at-least-once semantically equivalent to exactly-once.

---

## API Design

**Q13. What are the strategies for API versioning?**

**A:**
- **URL path**: `/v1/users`, `/v2/users` — explicit, easy to test, but violates "URL identifies resource" principle.
- **Header**: `Accept: application/vnd.myapi.v2+json` — clean URLs, harder to test in browser.
- **Query param**: `/users?version=2` — simple but pollutes the query string.
- **Subdomain**: `v2.api.example.com` — good for major API changes.

Best practice: URL versioning for public APIs (simple and visible); header versioning for internal APIs.

---

**Q14. What is cursor-based pagination?**

**A:** Cursor-based pagination uses an opaque pointer (cursor) to the position in the dataset, rather than offset/page numbers. The client sends the cursor from the last page to get the next page.

```
GET /posts?limit=20&cursor=eyJpZCI6MTAwfQ==
Response: { "data": [...], "next_cursor": "eyJpZCI6MTIwfQ==" }
```

Benefits: consistent results even if data is inserted/deleted between requests; O(1) server-side cost vs O(n) for OFFSET.

---

**Q15. What is rate limiting and how do you implement it?**

**A:** Rate limiting caps the number of requests a client can make in a time window. Algorithms:
- **Token Bucket**: Tokens refill at a fixed rate; each request consumes a token. Allows bursts.
- **Leaky Bucket**: Requests processed at a fixed rate; excess is queued or dropped.
- **Fixed Window Counter**: Count requests in a fixed time window. Simple but allows bursts at window boundaries.
- **Sliding Window**: More accurate; no boundary burst issue.

Return `429 Too Many Requests` with `Retry-After` header when limits are exceeded.

---

## Security

**Q16. What is JWT and what are its three components?**

**A:** JSON Web Token is a compact, URL-safe token for transmitting claims between parties. Three parts separated by `.`:
1. **Header**: Token type and signing algorithm (`{"alg": "HS256", "typ": "JWT"}`).
2. **Payload**: Claims (user ID, roles, expiry). Not encrypted — only base64-encoded. **Never store sensitive data here.**
3. **Signature**: HMAC or RSA signature of `header.payload`, ensuring the token wasn't tampered with.

---

**Q17. What is CORS and how do you configure it?**

**A:** Cross-Origin Resource Sharing is a browser security mechanism that restricts cross-origin HTTP requests. The browser sends a preflight `OPTIONS` request; the server responds with allowed origins, methods, and headers.

Key headers: `Access-Control-Allow-Origin`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Headers`, `Access-Control-Max-Age`.

Never use `Access-Control-Allow-Origin: *` for authenticated APIs — use a whitelist of trusted origins.

---

## Performance

**Q18. How does HTTP caching work with `ETag` and `Cache-Control`?**

**A:**
- `Cache-Control: max-age=3600` — client/proxy caches the response for 1 hour.
- `ETag: "abc123"` — server's version fingerprint of the resource. Client sends `If-None-Match: "abc123"` on the next request. If unchanged, server returns `304 Not Modified` (no body) — saves bandwidth.
- `Last-Modified` + `If-Modified-Since` works similarly with timestamps.

Cache invalidation strategy: short max-age for mutable resources; long max-age + content-hashed URLs for immutable assets.

---

**Q19. What is the OWASP API Security Top 10?**

**A:** Key items:
1. Broken Object Level Authorization (BOLA/IDOR) — user accesses another user's data
2. Broken Authentication
3. Broken Object Property Level Authorization — mass assignment, excessive data exposure
4. Unrestricted Resource Consumption — no rate limiting
5. Broken Function Level Authorization — horizontal/vertical privilege escalation
6. Unrestricted Access to Sensitive Business Flows
7. Server-Side Request Forgery (SSRF)
8. Security Misconfiguration
9. Improper Inventory Management — undocumented endpoints
10. Unsafe Consumption of APIs

---

**Q20. How do you design an idempotent order creation API?**

**A:** Sample design:
- Client generates a UUID before the request.
- `POST /orders` with header `Idempotency-Key: <uuid>` and order body.
- Server checks Redis/DB for this key:
  - **Found**: Return the stored response immediately.
  - **Not found**: Begin processing, store key + "processing" state atomically (use DB transaction with unique constraint on key), create order, update key to "completed" with full response.
- Return `201 Created` with the order data.
- Set key TTL to 24-48 hours.
- Use the outbox pattern to publish order events atomically.

---
