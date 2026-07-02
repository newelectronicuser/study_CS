# NoSQL — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is NoSQL and how does it differ from relational databases?**

**A:** NoSQL ("Not Only SQL") databases are designed for flexible schemas, horizontal scalability, and high availability. Unlike RDBMS with fixed schemas and ACID transactions, NoSQL databases trade some consistency guarantees for performance and scalability. They excel at handling large volumes of unstructured or semi-structured data, high write throughput, and distributed deployments.

---

**Q2. What are the four main categories of NoSQL databases?**

**A:**
- **Document stores**: Store JSON/BSON documents. Example: MongoDB, CouchDB.
- **Key-Value stores**: Simple key → value pairs. Example: Redis, DynamoDB.
- **Wide-column stores**: Column-family model optimized for writes and range scans. Example: Apache Cassandra, HBase.
- **Graph databases**: Store nodes and edges for relationship-heavy queries. Example: Neo4j, Amazon Neptune.

---

**Q3. What is the CAP theorem?**

**A:** CAP states that a distributed system can guarantee at most **two** of:
- **Consistency**: Every read returns the most recent write.
- **Availability**: Every request receives a response (not necessarily the latest data).
- **Partition Tolerance**: The system continues operating despite network partitions.

Since network partitions are unavoidable in distributed systems, you must choose between CP (consistent but may be unavailable) or AP (available but may return stale data). Examples: Cassandra (AP), HBase (CP).

---

**Q4. What is the BASE model?**

**A:** BASE is the NoSQL counterpart to ACID:
- **Basically Available**: System guarantees availability.
- **Soft state**: State may change over time even without input (due to eventual consistency).
- **Eventually Consistent**: The system will become consistent over time, but not necessarily immediately after a write.

---

**Q5. When would you choose NoSQL over SQL?**

**A:**
- High write throughput with simple access patterns (Cassandra)
- Document-heavy data with flexible/evolving schemas (MongoDB)
- Caching, sessions, leaderboards (Redis)
- Graph relationships (Neo4j)
- Truly massive scale across many commodity servers

Choose SQL when: you need strong consistency, complex queries with JOINs, multi-table transactions, and a well-defined schema.

---

## Document Stores (MongoDB)

**Q6. What are embedded documents vs referenced documents in MongoDB?**

**A:**
- **Embedded**: Nest related data inside a single document. Better for read performance (single query), ideal for data that is always accessed together.
- **Referenced**: Store related data in separate collections with ObjectId references. Better for large sub-documents, shared references, or independently queried data.

Rule of thumb: embed for "has-a" (one-to-one, one-to-few); reference for "many-to-many" or large sub-documents.

---

**Q7. What is the aggregation pipeline in MongoDB?**

**A:** The aggregation pipeline processes documents through a sequence of stages, each transforming the data. Common stages: `$match` (filter), `$group` (aggregate), `$project` (reshape), `$sort`, `$limit`, `$lookup` (join), `$unwind` (flatten arrays).

```js
db.orders.aggregate([
  { $match: { status: "completed" } },
  { $group: { _id: "$customerId", total: { $sum: "$amount" } } },
  { $sort: { total: -1 } }
])
```

---

**Q8. What is sharding in MongoDB?**

**A:** Sharding distributes data across multiple machines (shards). Each shard holds a subset of data. A **shard key** determines which shard a document goes to. A **mongos** router directs queries to the right shard. Good shard key: high cardinality, even distribution, query-aligned. Bad shard key (monotonically increasing ID) causes hot partitions.

---

**Q9. What is a replica set in MongoDB?**

**A:** A replica set is a group of mongod processes that maintain the same dataset. One node is the **primary** (accepts writes), others are **secondaries** that replicate the primary's oplog. If the primary fails, an election selects a new primary. Provides high availability and read scalability (reads from secondaries).

---

**Q10. What is write concern and read preference in MongoDB?**

**A:**
- **Write concern**: Specifies acknowledgment required before confirming a write. `w:1` (primary only), `w:majority` (majority of nodes acknowledge — safer for durability).
- **Read preference**: Where reads are routed. `primary`, `primaryPreferred`, `secondary`, `secondaryPreferred`, `nearest`. Secondary reads may return stale data.

---

## Key-Value Stores (Redis)

**Q11. What are the primary data structures in Redis?**

**A:** String, List (doubly linked list), Hash (field-value map), Set (unique members), Sorted Set (scored set for rankings), Bitmap, HyperLogLog, Stream, Geospatial index.

---

**Q12. How does Redis handle persistence (RDB vs AOF)?**

**A:**
- **RDB (Redis Database)**: Point-in-time snapshots at configured intervals. Faster restarts, but data between snapshots can be lost.
- **AOF (Append Only File)**: Logs every write operation. More durable (can replay all ops), but slower and larger file.
- Both can be used together for maximum durability with fast restart.

---

**Q13. What is TTL in Redis and what are eviction policies?**

**A:** TTL (Time-To-Live) sets an expiry on a key. After expiry, the key is automatically deleted. Eviction policies (when memory is full): `noeviction` (error), `allkeys-lru` (evict least recently used), `volatile-lru` (LRU only among keys with TTL), `allkeys-random`, `volatile-ttl` (evict key with lowest TTL).

---

## Wide-Column Stores (Cassandra)

**Q14. What is the partition key and clustering key in Cassandra?**

**A:** The **partition key** determines which node stores a row (hashed to a token ring position). The **clustering key** determines the sort order of rows within a partition. Together they form the primary key. Good partition keys have high cardinality and distribute writes evenly to avoid hot partitions.

---

**Q15. Why should you model tables in Cassandra by query patterns?**

**A:** Cassandra does not support JOINs or arbitrary ad-hoc queries efficiently. You must design a table for each specific query — denormalize and duplicate data as needed. If you have two access patterns, you may need two tables with the same data. Query-first design is the Cassandra way.

---

**Q16. What is a tombstone in Cassandra?**

**A:** When a row or cell is deleted in Cassandra, a tombstone (deletion marker) is written rather than immediately removing the data. Tombstones accumulate and are cleaned up during compaction. Too many tombstones degrade read performance because Cassandra must scan and discard them. Avoid excessive deletes or use TTL instead.

---

## Design & Production

**Q17. What is a hot partition and how do you avoid it?**

**A:** A hot partition occurs when most traffic hits a single shard/partition (e.g., using timestamp or monotonic IDs as a shard key). Avoidance strategies: use high-cardinality, randomly distributed keys (UUID, hashed user ID); add a random prefix/suffix to spread writes; use salting.

---

**Q18. How do you implement pagination in a document store like MongoDB?**

**A:** Avoid large `skip()` values (they scan from the start). Use **cursor-based pagination**: query by the `_id` or a timestamp of the last seen document:

```js
db.posts.find({ _id: { $gt: lastSeenId } }).sort({ _id: 1 }).limit(20)
```

---

**Q19. How do you ensure data consistency across multiple NoSQL collections?**

**A:** Options:
1. Use multi-document transactions (MongoDB 4.x supports ACID transactions across collections).
2. Design data to avoid cross-collection writes (embed instead of reference).
3. Use the Saga pattern (sequence of local transactions with compensating rollbacks).
4. Accept eventual consistency and design for idempotent writes.

---

**Q20. What is multi-document transaction support in MongoDB?**

**A:** Since MongoDB 4.0, transactions spanning multiple documents and collections are supported with ACID guarantees (similar to relational DBs). They use a two-phase locking mechanism and are available on replica sets; since 4.2, also on sharded clusters. Use them sparingly — they have performance overhead and lock contention.

---
