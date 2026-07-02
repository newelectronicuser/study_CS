# NoSQL — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

**Q1. What is NoSQL? How does it differ from relational databases?**

**A:** NoSQL ("Not Only SQL") refers to non-relational database management systems designed for horizontal scalability, high performance, and schema flexibility.
*   *Differences:*
    *   **Data Model:** RDBMS uses structured tables with rows, columns, and foreign key relations. NoSQL uses key-value, document, wide-column, or graph models.
    *   **Schema:** RDBMS is strictly schema-on-write; NoSQL is schema-less or schema-on-read.
    *   **Scaling:** RDBMS scales vertically (more CPU/RAM on one server); NoSQL scales horizontally (adds more servers to a distributed cluster).
    *   **Transactions:** RDBMS supports full ACID transactions. NoSQL usually relaxes ACID in favor of eventual consistency (BASE model) to achieve higher availability and partition tolerance.

---

**Q2. What are the four main categories of NoSQL databases? Give an example for each.**

**A:**
1.  **Document Stores:** Store semi-structured data as JSON/BSON documents. *Example:* MongoDB, CouchDB.
2.  **Key-Value Stores:** Store simple key-value pairs. Highly performant for lookups. *Example:* Redis, DynamoDB.
3.  **Wide-Column (Column-Family) Stores:** Store data in rows containing dynamic columns, optimized for high write scale and scanning range queries. *Example:* Apache Cassandra, ScyllaDB, HBase.
4.  **Graph Databases:** Store entities as "nodes" and connections as "edges" with properties. Optimized for relationship-heavy data. *Example:* Neo4j, Amazon Neptune.

---

**Q3. What is the CAP theorem? Explain each property.**

**A:** The CAP theorem states that a distributed data store can simultaneously provide at most two of the following three guarantees in the event of a network partition:
*   **Consistency (C):** Every read returns the most recent write or an error.
*   **Availability (A):** Every non-failing node returns a non-error response (but not necessarily the latest write).
*   **Partition Tolerance (P):** The system continues to operate despite network communication failures (partitions) between nodes.

In practice, because physical networks *will* drop packets (requiring Partition Tolerance), a distributed database must choose between **Consistency (CP)** or **Availability (AP)** when a partition occurs.

---

**Q4. What is the BASE model and how does it compare to ACID?**

**A:** BASE is the transaction model used by many NoSQL databases to allow scale:
*   **Basically Available (BA):** The system guarantees availability; there will be a response, even if some nodes fail.
*   **Soft State (S):** The state of the data may change over time without input due to synchronization delays.
*   **Eventual Consistency (E):** The system will become consistent after a period where no new updates are made.

*Comparison:* ACID guarantees immediate, strict consistency and isolation at the cost of blocking concurrent operations. BASE prioritizes performance and availability, accepting temporary inconsistencies.

---

**Q5. What is eventual consistency? When is it acceptable?**

**A:** Eventual consistency is a consistency model where updates made to one database node eventually propagate to all replicas. During this sync window, reads from different nodes may return stale data.
*   *Acceptable:* Use cases where real-time accuracy is not critical (e.g., social media likes, feed posts, shopping cart count updates, user profile changes).
*   *Unacceptable:* High-risk scenarios (e.g., bank account balances, inventory reservation during checkout, healthcare medical logs).

---

**Q6. When would you choose NoSQL over SQL for a project?**

**A:** Choose NoSQL when:
*   You have massive scale requirements (terabytes/petabytes) with high write/read throughput requiring horizontal scaling.
*   Your data structure is highly dynamic, unstructured, or semi-structured (schema changes frequently).
*   Your queries are simple key-value or single-entity document lookups (no complex, multi-table SQL `JOIN`s needed).
*   You need to deploy a multi-region, distributed database that operates on cheap commodity hardware.

---

**Q7. What are the trade-offs between consistency and availability in distributed NoSQL systems?**

**A:**
*   **Choosing Consistency (CP):** The database blocks or rejects reads/writes on partitioned nodes to prevent stale or conflicting data from being written. Trade-off: High latency or service downtime during network hiccups.
*   **Choosing Availability (AP):** The database allows reads and writes on all nodes during a partition. Trade-off: Nodes will drift out of sync, requiring conflict resolution (like Last-Write-Wins or Vector Clocks) later.

---

**Q8. What is a schema-less database? What are the challenges of operating one at scale?**

**A:** A database that does not enforce a rigid data structure or column definition at the database level. Documents in the same collection can have entirely different fields.
*   *Challenges at Scale:*
    *   **Data Quality:** Data validation is pushed to the application code; bad or corrupted formats can easily slip in.
    *   **Schema Evolution:** Over time, old records may miss fields that newer records have, requiring complex handling in code.
    *   **Indexing:** Queries might search for fields that only exist in some documents, making indexing less efficient.

---

## Document Stores (MongoDB)

**Q9. What is a document in MongoDB? How is it stored internally?**

**A:** A document is a basic unit of data in MongoDB, equivalent to a row in an RDBMS. It is represented as a JSON-like structure of key-value pairs. Internally, MongoDB stores documents in **BSON** (Binary JSON), which extends JSON to support extra data types (like Date, Binary, ObjectId) and makes parsing much faster.

---

**Q10. What is a collection in MongoDB vs a table in SQL?**

**A:** A **Collection** in MongoDB is a grouping of documents, equivalent to a **Table** in SQL.
*   *Key difference:* Tables have a fixed schema with pre-defined columns and data types. Collections do not enforce structure; they can contain documents of different shapes, though for practical reasons they usually group documents of the same model.

---

**Q11. What are embedded documents vs referenced documents? When do you use each?**

**A:**
*   **Embedded Documents (Denormalization):** Storing related data directly inside a single document (nested arrays/objects).
    *   *Use:* One-to-one or one-to-many relationships where the child data is always read with the parent (e.g. shipping address in a user profile). Provides fast reads since it requires only a single I/O operation.
*   **Referenced Documents (Normalization):** Storing related data in separate collections and linking them using an identifier (like `ObjectId`).
    *   *Use:* Many-to-many relationships, or when child data grows boundlessly (exceeding MongoDB's 16MB document size limit), or when the child data needs to be queried independently.

---

**Q12. How does indexing work in MongoDB? What types of indexes are supported?**

**A:** MongoDB uses B-Trees to build indexes. Types of indexes:
*   **Single Field Index:** On a single attribute.
*   **Compound Index:** On multiple fields (order matters, leftmost prefix rule applies).
*   **Multikey Index:** Indexes array fields (creates an index entry for every element in the array).
*   **Geospatial Index:** For coordinate maps (2dsphere).
*   **Text Index:** Supports search queries in string fields.
*   **TTL Index:** Automatically deletes documents after a certain period.

---

**Q13. What is the aggregation pipeline in MongoDB? Give an example.**

**A:** The aggregation pipeline is a framework for data processing and transformation. Documents pass through a multi-stage pipeline where they are filtered, grouped, and sorted.

```js
// Aggregation Example: Calculate total sales per product for completed orders
db.orders.aggregate([
  { $match: { status: "completed" } },
  { $group: { _id: "$productId", totalRevenue: { $sum: "$amount" } } },
  { $sort: { totalRevenue: -1 } }
]);
```

---

**Q14. What is the `$lookup` stage and how does it perform JOINs?**

**A:** The `$lookup` stage performs an left outer join to another collection in the same database.

```js
// Join orders with customers collection
db.orders.aggregate([
  {
    $lookup: {
      from: "customers",
      localField: "customerId",
      foreignField: "_id",
      as: "customerDetails"
    }
  }
]);
```
*Note:* `$lookup` cannot be executed on sharded collections efficiently and can cause major performance issues if done frequently on large collections.

---

**Q15. What is sharding in MongoDB? How does it distribute data?**

**A:** Sharding is MongoDB's method for horizontal scaling. It distributes data across a cluster of servers (shards).
*   *Components:*
    *   **Shard Key:** A field present in every document that determines which shard stores it.
    *   **Mongos:** Router that intercepts client queries and routes them to the correct shard(s).
    *   **Config Servers:** Store metadata about the cluster configuration and shard ranges.

---

**Q16. What is a replica set in MongoDB and how does it provide high availability?**

**A:** A Replica Set is a group of `mongod` instances that maintain the same dataset.
*   *Roles:*
    *   **Primary Node:** Receives all write operations.
    *   **Secondary Nodes:** Replicate the Primary's operations (oplog) to keep their datasets in sync.
*   *Failover:* If the Primary goes offline, the remaining secondaries hold an election and choose a new Primary automatically (usually in seconds).

---

**Q17. What is the oplog? How does it support replication?**

**A:** The **oplog** (operations log) is a capped collection in MongoDB that records a rolling history of all write operations on the Primary database. Secondary nodes continuously query the Primary's oplog and apply those operations to their own data in the exact same order to maintain synchronization.

---

**Q18. What is write concern and read preference in MongoDB?**

**A:**
*   **Write Concern:** Describes the level of acknowledgment requested from MongoDB for a write operation. e.g., `w: 1` (acknowledge once Primary writes), `w: majority` (acknowledge only when a majority of replica set nodes write to disk - protects against rollback).
*   **Read Preference:** Configures where client read operations are routed. e.g., `primary` (default, ensures fresh data), `secondary` (allows reading stale data from replicas for read scaling), `nearest` (routes to the lowest network latency node).

---

## Key-Value Stores (Redis)

**Q19. What is Redis and what are its primary use cases?**

**A:** Redis (Remote Dictionary Server) is an open-source, in-memory, key-value data structure store. It is extremely fast (sub-millisecond operations) because it runs in RAM.
*   *Primary Use Cases:* Database caching, session management, real-time message queuing (pub/sub), leaderboards, and rate limiters.

---

**Q20. What data structures does Redis support natively?**

**A:** Redis supports Strings, Lists, Hashes, Sets, Sorted Sets (with scores), Bitmaps, HyperLogLogs, Geospatial indexes, and Streams.

---

**Q21. How does Redis handle persistence (RDB vs AOF)?**

**A:**
*   **RDB (Redis Database):** Performs point-in-time snapshots of your dataset at specified intervals. Fast startup and minimal overhead, but data written between snapshots is lost in a crash.
*   **AOF (Append Only File):** Logs every write operation received by the server. It is highly durable (can sync to disk on every write), but files are larger and startup is slower.

---

**Q22. What is Redis pub/sub? When would you use it vs a message queue?**

**A:** Redis Pub/Sub is a publisher-subscriber model where messages are pushed directly to active subscribers.
*   *Redis Pub/Sub:* Fire-and-forget. If a subscriber is offline, they miss the message completely. Use for real-time notifications, chat rooms, or live analytics.
*   *Message Queue (like SQS/RabbitMQ):* Durable. Messages are stored on disk/queues until a consumer processes them. Use for task processing, financial transactions, or guaranteed delivery.

---

**Q23. How does Redis clustering work?**

**A:** Redis Cluster distributes data automatically across multiple Redis nodes using **sharding**.
*   *Mechanism:* The cluster has 16,384 logical hash slots. Keys are hashed (CRC16) and mapped to a slot. Each master node is responsible for a subset of these hash slots. It supports master-slave replication to ensure high availability.

---

**Q24. What is a Redis pipeline and why does it improve performance?**

**A:** Pipelining allows sending multiple Redis commands to the server in a single batch without waiting for the individual responses. This drastically improves performance by reducing network round-trip time (RTT) overhead.

---

**Q25. What is TTL in Redis and how do you implement cache eviction policies?**

**A:** TTL (Time-To-Live) sets an expiration time on a key, after which Redis deletes it. When memory is full, Redis applies eviction policies to free up space:
*   `noeviction` (returns errors on writes).
*   `allkeys-lru` (evicts least recently used keys).
*   `volatile-lru` (evicts LRU keys with an active TTL).
*   `allkeys-lfu` (evicts least frequently used keys).

---

**Q26. What is the difference between `SET` and `SETNX` in Redis?**

**A:**
*   `SET key value`: Sets the key to the value, overwriting any existing value.
*   `SETNX key value` (Set if Not eXists): Sets the key to the value only if the key does not already exist. Used as a primitive to implement distributed locks.

---

## Wide-Column Stores (Cassandra)

**Q27. What is Apache Cassandra and what is its primary strength?**

**A:** Apache Cassandra is an open-source, distributed, wide-column NoSQL database. Its primary strength is **linear scalability and high write throughput** with zero single point of failure (peer-to-peer ring architecture).

---

**Q28. What is the partition key and clustering key in Cassandra?**

**A:**
*   **Partition Key:** Determines which node in the Cassandra ring stores the row data (hashed using Murmur3Partitioner).
*   **Clustering Key:** Determines the physical sorting order of the columns/rows *within* that partition. Together, they form the Primary Key.

---

**Q29. How does Cassandra achieve high write throughput?**

**A:** Cassandra avoids locks and random disk I/O. It writes data sequentially:
1.  Writes the update to the **CommitLog** on disk (for crash recovery).
2.  Writes the update to the **Memtable** in memory.
3.  Periodically flushes Memtables to immutable files on disk called **SSTables**.
4.  **Compaction** runs in the background to merge SSTables and remove old records.

---

**Q30. What is a gossip protocol and how does Cassandra use it?**

**A:** A Gossip Protocol is a decentralized peer-to-peer communication protocol. Nodes in a Cassandra cluster exchange state metadata (e.g. node health, topology changes) by gossiping with a small number of random neighbor nodes every second, allowing the entire cluster to converge on state knowledge without a master coordinator.

---

**Q31. What is a tombstone in Cassandra and why can it cause performance issues?**

**A:** Because SSTables are immutable, Cassandra cannot delete data in place. Instead, it writes a deletion marker called a **tombstone**.
*   *Performance Issue:* During a read query, Cassandra must scan both active data and tombstones. If there are millions of tombstones, reads become extremely slow, consume excessive memory, and can trigger out-of-memory crashes.

---

**Q32. Why should you model tables in Cassandra by query patterns rather than data relationships?**

**A:** Cassandra does not support relational JOINs. To run queries efficiently at scale, you must design one table per query pattern (Query-driven modeling). This involves intentionally duplicating (denormalizing) data across multiple tables so that each query can retrieve all the data it needs from a single partition.

---

**Q33. What is the replication factor and consistency level in Cassandra?**

**A:**
*   **Replication Factor (RF):** The total number of nodes in the cluster that will store copies of a data partition.
*   **Consistency Level (CL):** The number of replica nodes that must acknowledge a read or write operation before it is considered successful. e.g., `LOCAL_QUORUM` requires confirmation from a majority of replicas in the local datacenter.

---

## Graph Databases

**Q34. What is a graph database? Name two popular ones.**

**A:** A graph database is a database that stores data as nodes (entities) and edges (relationships) instead of tables or documents.
*   *Popular examples:* Neo4j, Amazon Neptune.

---

**Q35. What problems are best solved with a graph database?**

**A:** Problems involving highly interconnected data and complex relationships:
*   Social network connections (friend-of-friend lookups).
*   Fraud detection networks (identifying loops or shared credentials).
*   Recommendation engines (people who bought X also bought Y).
*   Identity resolution and lineage tracking.

---

**Q36. What is a Cypher query? Give a basic example.**

**A:** Cypher is Neo4j's graph query language that uses visual, ASCII-art patterns to represent paths.

```cypher
// Find friends of Alice who like "NoSQL"
MATCH (user:User {name: "Alice"})-[:FRIEND]->(f:User)-[:LIKES]->(topic:Topic {name: "NoSQL"})
RETURN f.name;
```

---

## Design & Production

**Q37. How do you handle schema migrations in a NoSQL database?**

**A:**
1.  **Lazy Migration:** Update application code to handle both old and new formats. When reading old documents, fill in missing fields with defaults. When writing, save them in the new schema.
2.  **Background Migration Scripts:** Run batch scripts to traverse collections, update documents to the new schema format, and save them.
3.  **Schema Versioning:** Save a `schema_version: N` field in documents. The application uses this field to map the document to the correct parsing logic.

---

**Q38. What is a hot partition and how do you avoid it?**

**A:** A hot partition occurs when a disproportionate volume of write or read traffic targets a single partition key/node (e.g. using a date or country code with high skew).
*   *Avoidance:* Use a composite key, or add a random salt value (e.g. `key_0`, `key_1`) to distribute data across multiple partitions.

---

**Q39. How do you implement pagination in a document store?**

**A:** Avoid using `skip()` because it requires the database to scan all previous records. Instead, use **Range Queries (Cursor Pagination)**:

```js
// Find next 20 posts after a specific timestamp/ID
db.posts.find({ createdAt: { $lt: lastSeenTimestamp } }).sort({ createdAt: -1 }).limit(20);
```

---

**Q40. How do you ensure data consistency across multiple NoSQL collections?**

**A:**
1.  **Document Redesign:** Embed the data to handle the update atomically within a single document.
2.  **Transactions:** Use database-provided multi-document transactions (e.g., MongoDB sessions).
3.  **Outbox Pattern / Eventual Consistency:** Write updates to an outbox and publish events to reconcile state asynchronously.

---

**Q41. How would you model a time-series dataset in a NoSQL database?**

**A:** Use the **Bucket Pattern**. Instead of storing one document per event, group events by time windows (e.g., hourly or daily buckets) inside a single document to reduce index size and optimize disk reads.

```js
// Hourly bucket example
{
  sensorId: 101,
  day: "2026-07-02",
  hour: 12,
  readings: [
    { minute: 1, temp: 22.4 },
    { minute: 2, temp: 22.5 }
  ]
}
```

---

**Q42. What is multi-document transaction support in MongoDB? When should you use it?**

**A:** MongoDB supports ACID transactions across multiple documents and collections. It uses a two-phase commit protocol.
*   *When to use:* Only when transaction boundaries span multiple independent collections (e.g. transferring funds between accounts). Avoid for general operations, as transactions introduce significant lock overhead and impact cluster scaling.

---

**Q43. How do you back up and restore a NoSQL database in production?**

**A:**
*   **MongoDB:** Use `mongodump` and `mongorestore` for logical backups, or take filesystem snapshots (LVM/EBS) on secondary nodes while pausing writes (`db.fsyncLock()`).
*   **Redis:** Copy the `.rdb` snapshot file to secure object storage (S3/GCS).
*   **Cloud NoSQL (DynamoDB/Spanner):** Enable native continuous backups and point-in-time recovery (PITR).

---
