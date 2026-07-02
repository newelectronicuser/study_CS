# REST API & Idempotency — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## REST Fundamentals

**Q1. What is REST? What are the six architectural constraints of a RESTful system?**

**A:** REST (Representational State Transfer) is an architectural style for designing networked applications. It relies on a stateless, client-server, cacheable communications protocol (typically HTTP).
*   *Six Constraints:*
    1.  **Client-Server:** Decouples the user interface from the data storage.
    2.  **Stateless:** Each request from client to server must contain all the information necessary to understand and complete the request. The server does not store session context.
    3.  **Cacheable:** Responses must explicitly define themselves as cacheable or not to improve network efficiency.
    4.  **Uniform Interface:** Simplifies architecture via resource identification (URIs), resource manipulation through representations, self-descriptive messages, and hypermedia (HATEOAS).
    5.  **Layered System:** Clients cannot tell if they are connected directly to the end server or an intermediary (like load balancers or proxies).
    6.  **Code on Demand (Optional):** Servers can temporarily extend client functionality by transferring executable code (e.g. JavaScript).

---

**Q2. What is the difference between REST and RPC (e.g., gRPC, SOAP)?**

**A:**
*   **REST:** Resource-oriented. Interacts with nouns (resources) using standard verbs (HTTP methods like GET, POST, PUT, DELETE). Focuses on loose coupling and standardized communication.
*   **RPC (Remote Procedure Call):** Action/procedure-oriented. Interacts with verbs (functions) exposed by the server (e.g. `getUser()`, `deleteRecord()`). gRPC uses HTTP/2 and Protocol Buffers for fast, binary serialization, which is more performant than JSON-based REST but more tightly coupled.

---

**Q3. What is a resource in REST? How are resources identified?**

**A:** A resource is any conceptual object or entity that can be named, managed, and manipulated in the system (e.g. a User, a File, a Payment).
*   *Identification:* Resources are identified using unique, uniform locators called **URIs (Uniform Resource Identifiers)**, e.g., `https://api.example.com/v1/users/123`.

---

**Q4. What are the standard HTTP methods? What is the intended semantic of each?**

**A:**
*   `GET`: Retrieve a representation of a resource. Safe and idempotent.
*   `POST`: Create a new resource. Neither safe nor idempotent.
*   `PUT`: Replace an existing resource, or create it if it doesn't exist. Idempotent but not safe.
*   `PATCH`: Apply partial modifications to a resource. Neither safe nor idempotent (by default, though it can be made so).
*   `DELETE`: Delete a resource. Idempotent but not safe.

---

**Q5. What is the difference between `PUT` and `PATCH`?**

**A:**
*   `PUT`: Replaces the **entire** resource with the request payload. If any fields are omitted from the payload, they are typically set to null or default values.
*   `PATCH`: Applies a **partial** update to the resource. Only the fields specified in the request payload are modified, leaving other attributes unchanged.

---

**Q6. What are HTTP status codes? Give examples for 2xx, 3xx, 4xx, and 5xx ranges.**

**A:** Status codes indicate the outcome of an HTTP request:
*   `2xx` (Success): e.g., `200 OK` (generic success), `201 Created` (resource created), `204 No Content` (success, no response body).
*   `3xx` (Redirection): e.g., `301 Moved Permanently`, `304 Not Modified` (cached content is valid).
*   `4xx` (Client Errors): e.g., `400 Bad Request`, `401 Unauthorized` (no auth), `403 Forbidden` (no permission), `404 Not Found`.
*   `5xx` (Server Errors): e.g., `500 Internal Server Error`, `502 Bad Gateway`, `503 Service Unavailable` (overloaded/down).

---

**Q7. What is the difference between `401 Unauthorized` and `403 Forbidden`?**

**A:**
*   `401 Unauthorized`: Means the client has **not authenticated** (no valid credentials provided). The server doesn't know who you are.
*   `403 Forbidden`: Means the client is **authenticated, but does not have permission** to access the requested resource. The server knows who you are but denies access.

---

**Q8. What is the difference between `404 Not Found` and `410 Gone`?**

**A:**
*   `404 Not Found`: Means the resource is not available at this URI. It may exist in the future, or the client entered the wrong path.
*   `410 Gone`: A permanent error indicating the resource once existed at this URI but has been intentionally and permanently deleted, and will not return.

---

**Q9. What is content negotiation in REST?**

**A:** Content negotiation is the mechanism where the client and server agree on the representation format of the resource (e.g., JSON, XML, HTML). The client requests formats using headers like `Accept` and `Accept-Language`, and the server responds using the `Content-Type` header to match.

---

**Q10. What is HATEOAS and is it commonly implemented in practice?**

**A:** HATEOAS (Hypermedia As the Engine of Application State) is a constraint of REST. The server's response should return not only data but also hypermedia links guiding the client on what actions/transitions are dynamically available next.
*   *In Practice:* It is **rarely** fully implemented because it adds significant complexity to payload sizes and client parsers. Most clients prefer consuming static API documentation rather than parsing dynamic links.

---

## Idempotency

**Q11. What is idempotency in the context of HTTP and REST APIs?**

**A:** An API operation is idempotent if making multiple identical requests has the same side effect on the server as making a single request. The server state must not change further after the first execution, and the client should receive the same response.

---

**Q12. Which HTTP methods are idempotent by specification? Which are not?**

**A:**
*   **Idempotent:** `GET`, `HEAD`, `OPTIONS`, `PUT`, `DELETE`.
*   **Not Idempotent:** `POST`, `PATCH`.

---

**Q13. Is `POST` idempotent? Why or why not? How can you make it idempotent?**

**A:** By specification, `POST` is **not** idempotent because sending duplicate `POST` requests typically creates duplicate resources (e.g. placing two identical orders).
*   *Making it idempotent:* Introduce an **Idempotency Key** (usually a UUID) passed in the request header (`Idempotency-Key`). The server checks if it has processed this key before; if so, it skips execution and returns the cached result of the first request.

---

**Q14. Is `DELETE` idempotent? Justify your answer.**

**A:** Yes, `DELETE` is idempotent. Deleting a resource (e.g. `/users/1`) removes it. Sending a second `DELETE` request for the same user cannot delete it again (the user is already gone). The side effect on the database is identical.
*   *Note:* The HTTP status code can change (e.g. `204 No Content` for the first call, `404 Not Found` for the second), but the state of the server remains the same.

---

**Q15. What is the difference between idempotency and safety in HTTP methods?**

**A:**
*   **Safe Methods:** Methods that do not modify resources or cause side effects on the server (e.g., `GET`, `HEAD`, `OPTIONS`). They are purely read-only. All safe methods are naturally idempotent.
*   **Idempotent Methods:** Methods that can modify server state, but executing them multiple times results in the same side effect as a single execution (e.g., `PUT`, `DELETE`). Not all idempotent methods are safe.

---

**Q16. Why is idempotency critical in distributed systems and microservices?**

**A:** In distributed networks, requests can fail due to network timeouts. If a client sends a payment request and experiences a timeout, the client does not know if:
1. The request failed before reaching the server.
2. The server processed the payment, but the network failed during the response.
Without idempotency, retrying the request risks duplicate transactions (charging the user twice). Idempotency ensures safe retries.

---

**Q17. What is an idempotency key? How do you implement it on the server side?**

**A:** An Idempotency Key is a unique string (typically a UUID) sent by the client in HTTP headers.
*   *Server-Side Implementation:*
    1.  Receive request, extract the key.
    2.  Acquire a lock on the key (using Redis or DB constraints) to prevent concurrent race conditions.
    3.  Check if the key exists in a fast store (like Redis).
    4.  If it exists: Return the cached response immediately.
    5.  If it doesn't: Execute the request, save the response payload along with the key (with a TTL, e.g. 24 hours), release the lock, and return the response.

---

**Q18. How do you handle duplicate `POST` requests using an idempotency key?**

**A:**
*   **Request 1:** Executing. Lock acquired.
*   **Request 2 (Duplicate, arrives while 1 is running):** Server sees the key is locked/in-progress. It should return a `409 Conflict` (or block and poll until Request 1 finishes, then return the result).
*   **Request 3 (Duplicate, arrives after 1 completes):** Server matches the key, fetches the cached response from storage, and returns it immediately.

---

**Q19. What are the challenges of implementing idempotency with database writes?**

**A:** Dual-write problems. If you write the business data to the SQL database, but the cache saving the idempotency key fails (or vice versa), the system drifts.
*   *Solution:* Save the idempotency key and the business transaction in the **same database transaction** (using an `idempotent_keys` database table). This guarantees that either both succeed, or both roll back.

---

**Q20. How does Stripe implement idempotency? What can you learn from it?**

**A:** Stripe requires an `Idempotency-Key` header on all POST requests.
*   *Key Lessons:*
    1. Use locks to prevent concurrent race conditions.
    2. Cache both the status code and response body.
    3. Impose a TTL (Stripe keys expire after 24 hours).
    4. Verify that the request parameters match the original request; if a client retries the same key but changes the body payload, Stripe returns an error rather than the cached result.

---

**Q21. How would you design an idempotent order creation API?**

**A:**
1.  Client generates a UUID for the order.
2.  Client sends `POST /orders` with header `Idempotency-Key: <UUID>`.
3.  Server checks database table `processed_requests` for the UUID.
4.  If found, return the saved order details.
5.  If not, run a transaction: insert the order into `orders` table, insert the UUID into `processed_requests`, commit, and return `201 Created`.

---

**Q22. What happens if an idempotency key expires before the client retries?**

**A:** If the key expires (e.g. after its 24-hour TTL), the server treats the retry request as a brand new request. It will execute the code again, potentially creating a duplicate resource. Thus, the TTL must be sized to accommodate any retry window of the client.

---

**Q23. How would you store idempotency keys — in a cache or a database? What are the trade-offs?**

**A:**
*   **Cache (Redis):** Fast lookup ($O(1)$) and built-in TTL support. Low latency. Trade-off: Risk of data loss if Redis restarts (unless configured with high persistence/AOF).
*   **Database (SQL):** Strong ACID consistency. The key can be saved in the same transaction as the business model, ensuring zero drift. Trade-off: Higher write latency and DB table bloat (requires background cleanup jobs).
*   *Hybrid:* Store locks and short-term keys in Redis, but persist key execution audits in the SQL database for high-value transactions (payments).

---

**Q24. What is the relationship between idempotency and at-least-once delivery?**

**A:** At-least-once delivery guarantees that a message broker will deliver a message to a consumer, but network failures mean duplicates can occur. To prevent duplicate processing, the consumer **must be idempotent**. Event-driven architectures rely on consumer idempotency to achieve safety.

---

**Q25. What is deduplication at the consumer level?**

**A:** Deduplication is the concrete mechanism used by consumers to achieve idempotency. The consumer extracts a unique event ID from incoming messages, checks it against a database of processed IDs (e.g. in Redis or a DB unique constraint), and skips processing if the ID has already been marked as processed.

---

## API Design

**Q26. What is API versioning? What are the strategies?**

**A:** Versioning allows updating API contracts without breaking existing integrations.
*   *Strategies:*
    1.  **URI Path Versioning (Most Common):** `GET /v1/users`
    2.  **Query Parameter Versioning:** `GET /users?version=1`
    3.  **Header Versioning (Accept Header):** `Accept: application/vnd.company.v1+json`

---

**Q27. What are the trade-offs between versioning strategies?**

**A:**
*   `URI Versioning`: Easy to read and test in browsers, but pollutes paths and forces client updates.
*   `Header Versioning`: Cleaner URI design, but harder for clients to configure and test, requiring custom headers on every call.

---

**Q28. What is pagination? Compare offset-based, cursor-based, and keyset pagination.**

**A:** Pagination splits large query datasets into pages.
*   **Offset-based (`LIMIT 10 OFFSET 50`):** Simple to implement but slow for deep pages ($O(n)$ scanning) and vulnerable to drift (items shifting if rows are inserted/deleted during pagination).
*   **Keyset/Cursor-based (`WHERE id > last_seen_id LIMIT 10`):** Extremely fast ($O(1)$ lookup via index), handles dynamic inserts/deletes without duplicates or skips. Trade-off: Cannot jump directly to a random page (e.g. Page 5).

---

**Q29. How do you design a REST API for filtering and sorting?**

**A:** Use URL query parameters:
*   *Filtering:* `GET /users?status=active&role=admin`
*   *Sorting:* `GET /users?sort=-created_at,name` (prefix `-` indicates descending order).

---

**Q30. What is the difference between an API key and OAuth 2.0 for authentication?**

**A:**
*   **API Key:** A static string passed in headers. Simple, but vulnerable to leakage (lasts indefinitely unless rotated). Best for server-to-server integrations.
*   **OAuth 2.0:** A delegation framework that issues short-lived, scopes-restricted access tokens (JWTs) via an authorization flow. Much more secure; best for user-facing applications.

---

**Q31. What is rate limiting? How do you implement it in a REST API?**

**A:** Rate limiting restricts the number of requests a client can make in a given timeframe (e.g., 60 requests/minute).
*   *Implementation:* Use a middleware proxy (API Gateway, Nginx, or Redis with the Token Bucket/Leaky Bucket algorithm) to track request counts per client IP or API key, returning `429 Too Many Requests` when limits are exceeded.

---

**Q32. What is throttling and how does it differ from rate limiting?**

**A:**
*   **Rate Limiting:** Enforces hard caps on requests per time window (e.g., max 100/minute).
*   **Throttling:** Dynamically slows down request processing speeds (adds latency or restricts bandwidth) when traffic spikes, protecting backend servers from crashing without throwing errors immediately.

---

**Q33. What is CORS? How do you configure it?**

**A:** CORS (Cross-Origin Resource Sharing) is a browser security mechanism that restricts web applications from making requests to a different domain than the one that served the app.
*   *Configuration:* The server must respond with headers like `Access-Control-Allow-Origin: *` or specific origins, defining allowed headers, methods, and credentials.

---

**Q34. How do you handle long-running operations in a REST API?**

**A:** Use the **Asynchronous Request-Reply** pattern:
1.  Client submits request: Server starts background job, immediately returns `202 Accepted` with a `Location` header pointing to a status endpoint (`/tasks/123`).
2.  Client polls the status endpoint to check progress.
3.  Once finished, the status endpoint returns `303 See Other` redirecting the client to the final resource. Alternatively, the server can notify the client via a **Webhook** post call.

---

## Security

**Q35. What is SQL injection and how do REST APIs prevent it?**

**A:** SQL injection occurs when untrusted user input is concatenated directly into SQL queries, allowing attackers to execute arbitrary SQL commands.
*   *Prevention:* Use **Parameterized Queries (Prepared Statements)** where input is treated strictly as parameters, never as executable code.

---

**Q36. What is CSRF and how does it affect REST APIs?**

**A:** CSRF (Cross-Site Request Forgery) forces a logged-in browser user to execute unwanted actions on a trusted site by exploiting cookie transmission.
*   *Effect on REST:* If the API uses standard sessions/cookies for auth, it is vulnerable. If it uses custom headers (`Authorization: Bearer <token>`), it is immune because browsers do not attach custom headers automatically.

---

**Q37. What is the difference between authentication and authorization?**

**A:**
*   **Authentication (AuthN):** Verifying who the user is (e.g., login).
*   **Authorization (AuthZ):** Verifying what the authenticated user is allowed to do (checking roles/permissions).

---

**Q38. What is JWT? What are its components?**

**A:** JSON Web Token is a signed string containing data. Components:
1.  **Header:** Defines the token type and signing algorithm (e.g., HS256).
2.  **Payload:** Contains the claims (user ID, expiration, roles).
3.  **Signature:** Cryptographic verification hash ensuring the token has not been modified.

---

**Q40. How do you prevent API key leakage in client-side applications?**

**A:** Never embed sensitive private API keys directly in frontend Javascript code. Instead, route frontend requests through a backend server (a Backend-For-Frontend proxy) which appends the private key securely before calling the external API.

---

## Performance & Observability

**Q42. How does HTTP caching work?**

**A:**
*   `Cache-Control`: Tells the browser how long it can cache the response (e.g. `max-age=3600`).
*   `ETag`: A unique hash representing the resource state. The client sends `If-None-Match: <etag>` on retries. If the resource hasn't changed, the server returns `304 Not Modified`, saving bandwidth.

---

**Q44. How do you implement request tracing in a REST API?**

**A:** Generate a unique `X-Correlation-ID` header at the Gateway for every incoming request. Ensure this header is propagated down all internal microservice calls and printed in every log statement.

---

## GraphQL (Comparative)

**Q47. What is GraphQL and how does it differ from REST?**

**A:** GraphQL is a query language for APIs.
*   *Differences:*
    *   REST uses multiple endpoints; GraphQL exposes a single endpoint (`/graphql`).
    *   REST returns fixed resource shapes; GraphQL allows clients to specify the exact fields they need, preventing over-fetching and under-fetching.

---

**Q48. What is the N+1 problem in GraphQL and how is it solved?**

**A:** It occurs when resolving nested fields (e.g., fetching 100 posts, and executing a separate SQL query to fetch the author for each post, yielding 101 queries).
*   *Solution:* Use **DataLoader** utility to batch and cache downstream requests, merging them into a single `IN` query.

---

**Q49. When would you choose GraphQL over REST?**

**A:** Choose GraphQL when building complex frontends with multiple page views requiring diverse nested data, or when aggregating data from multiple backend microservices into a unified client schema.

---
