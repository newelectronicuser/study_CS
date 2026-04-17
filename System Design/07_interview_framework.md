# System Design Interview Framework

A system design interview is an open-ended conversation. Having a structured framework allows you to stay calm and cover all the important bases.

## Step 1: Clarify Requirements (3-5 mins)
Never start designing immediately. Spend time understanding the scale and scope.
- **Functional Requirements**: What are the top 2-3 features? (e.g., "User can post a tweet," "User can follow others").
- **Non-Functional Requirements**: High availability? Strong consistency? Low latency?
- **Scale**: How many users? How many requests per second (QPS)? Read-to-write ratio?

## Step 2: Back-of-the-Envelope Estimation (2-5 mins)
Quantify the problem to guide your hardware/storage choices.
- **QPS**: Daily active users * average requests / seconds in a day.
- **Storage**: QPS * size of data * duration of retention.
- **Bandwidth**: QPS * size of data.

## Step 3: High-Level Design (5-10 mins)
Draw the big picture. Focus on the core workflow.
- **Components**: Client, Load Balancer, Web Servers, Databases, Caches.
- **Flow**: Trace one complete request through the system.

## Step 4: Deep Dive (15-20 mins)
This is where you show your expertise. The interviewer will likely steer you toward a specific bottleneck.
- **Database Scaling**: Sharding vs. Replication.
- **Caching**: Where to cache and what invalidation strategy to use.
- **Resilience**: How to handle failure in a specific service.
- **Data Model**: SQL or NoSQL choice and schema design.

## Step 5: Wrap Up (2-5 mins)
Summarize and mention potential future improvements.
- **Bottlenecks**: What parts of the system are still fragile?
- **Trade-offs**: Why did you choose Availability over Consistency?
- **Monitoring**: How would you identify issues in production?

---

### Common Interview Questions
- **URL Shortener**: Focusing on ID generation and storage.
- **News Feed**: Focusing on fan-out (Push vs. Pull models).
- **Chat System**: Focusing on WebSockets and message storage.
- **YouTube/Netflix**: Focusing on video ingestion, encoding, and CDNs.

> [!IMPORTANT]
> **Talk Out Loud**: 
> The interviewer is testing your **thought process**, not just the final result. Mention the trade-offs at every step (e.g., "We could use SQL for its rigid schema, but since our data is unstructured and needs to scale horizontally, I'll go with NoSQL").

> [!TIP]
> **Don't Over-engineer early**: 
> Start simple. Design for 1 million users first, then talk about how you would scale to 100 million.
