# DBMS — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Fundamentals

**Q1. What is a DBMS and how does it differ from a file system?**

**A:** A DBMS (Database Management System) is software that manages organized collections of data, providing controlled access, query capabilities, concurrent access, crash recovery, and security. Unlike a file system:
- DBMS provides a query language (SQL) for complex data retrieval.
- It enforces data integrity and constraints.
- It manages concurrent access with locking/transactions.
- It provides atomicity and durability (crash recovery).
- It offers views and access control at the data level.

---

**Q2. What is the three-schema architecture?**

**A:** The ANSI/SPARC three-schema architecture separates a database into three levels:
- **External (View) level**: Individual user views of the data (what different applications see).
- **Conceptual level**: The logical structure of the entire database (tables, relationships, constraints) — independent of storage.
- **Internal (Physical) level**: How data is physically stored (file organization, indexes, compression).

This separation enables **data independence** — changes to the physical storage don't require changes to application code.

---

## ER Modeling

**Q3. What is a weak entity?**

**A:** A weak entity cannot be uniquely identified by its own attributes alone — it depends on a strong (owner) entity. Its primary key is a combination of its own partial key (discriminator) and the owner entity's primary key. Represented with a double rectangle in ER diagrams.

Example: `OrderItem` is weak — it depends on `Order`. A single order item cannot exist without its parent order.

---

**Q4. What is cardinality in ER modeling?**

**A:** Cardinality defines the numerical relationship between entity instances:
- **1:1** (One-to-One): One employee has one company car.
- **1:N** (One-to-Many): One department has many employees.
- **M:N** (Many-to-Many): Many students enroll in many courses. Implemented with a junction/bridge table.

Combined with participation constraints (total = mandatory, partial = optional), they define integrity rules.

---

## Normalization

**Q5. Explain 1NF, 2NF, 3NF, and BCNF with examples.**

**A:**
- **1NF**: Atomic values only; no repeating groups; each row unique. Violating: storing `"Java, Python"` in a skills column.
- **2NF**: 1NF + no partial dependency on the primary key. If PK is `(OrderID, ProductID)`, the `ProductName` should not depend only on `ProductID` — move it to a `Products` table.
- **3NF**: 2NF + no transitive dependency. If `Employee → Department → DeptHead`, then `DeptHead` transitively depends on `Employee`. Extract `Department(DeptID, DeptHead)`.
- **BCNF**: Every determinant must be a candidate key. Stricter than 3NF — handles anomalies 3NF misses in tables with multiple overlapping candidate keys.

---

**Q6. What is a functional dependency?**

**A:** A functional dependency `X → Y` means that for any two tuples in a relation, if they have the same value for attribute(s) X, they must have the same value for Y. X **functionally determines** Y. Example: `StudentID → StudentName` (each student ID maps to exactly one name). The basis for all normalization theory.

---

**Q7. When and why would you denormalize?**

**A:** Denormalize when read performance outweighs write consistency. Scenarios:
- **Reporting/analytics**: Pre-aggregate data in a denormalized data warehouse (star schema) to avoid complex JOINs on every query.
- **High-throughput reads**: Cache computed values directly in the row instead of joining at query time.
- **Materialized views**: Pre-compute and store JOIN results for frequently run expensive queries.

Trade-offs: data redundancy, update anomalies, more complex writes.

---

## Transactions & Concurrency Control

**Q8. What are the ACID properties?**

**A:**
- **Atomicity**: A transaction is all-or-nothing. Either all operations succeed and are committed, or all are rolled back.
- **Consistency**: A transaction brings the database from one valid state to another, respecting all defined rules and constraints.
- **Isolation**: Concurrent transactions execute as if they were serial — intermediate states are not visible to other transactions.
- **Durability**: Once a transaction commits, its changes persist even in the event of a crash (written to non-volatile storage).

---

**Q9. What is conflict serializability?**

**A:** A schedule (ordering of operations from concurrent transactions) is **conflict serializable** if it can be transformed into a serial schedule by swapping non-conflicting operations. Two operations conflict if they operate on the same data item and at least one is a write. Tested using a **precedence graph**: if the graph has a cycle, the schedule is not conflict serializable.

---

**Q10. What is two-phase locking (2PL)?**

**A:** A concurrency control protocol with two phases:
1. **Growing phase**: A transaction can acquire locks but cannot release any.
2. **Shrinking phase**: A transaction can release locks but cannot acquire new ones.

2PL guarantees conflict serializability. **Strict 2PL** holds all locks until commit — prevents dirty reads and cascading rollbacks. **Rigorous 2PL** holds all locks (read and write) until commit, ensuring recoverability.

---

**Q11. What is MVCC (Multi-Version Concurrency Control)?**

**A:** MVCC maintains multiple versions of a data item. Writers create a new version while readers see an older consistent snapshot. This allows readers and writers to proceed concurrently without blocking each other.

Used by: PostgreSQL, MySQL InnoDB, Oracle. Enables high concurrency for mixed read-write workloads. Reads see a consistent snapshot of the database at the start of the transaction (snapshot isolation). Garbage collection periodically removes old versions.

---

**Q12. What are the four transaction isolation levels and what anomalies do they prevent?**

**A:**

| Isolation Level | Dirty Read | Non-Repeatable Read | Phantom Read |
|---|---|---|---|
| Read Uncommitted | ✅ possible | ✅ possible | ✅ possible |
| Read Committed | ❌ prevented | ✅ possible | ✅ possible |
| Repeatable Read | ❌ prevented | ❌ prevented | ✅ possible |
| Serializable | ❌ prevented | ❌ prevented | ❌ prevented |

Higher isolation = more correctness, less concurrency. Most production databases default to Read Committed.

---

**Q13. What is deadlock and how is it detected and resolved?**

**A:** A deadlock occurs when two or more transactions each wait for a lock held by another in a circular chain. Conditions: mutual exclusion, hold-and-wait, no preemption, circular wait.

Detection: Build a waits-for graph. A cycle indicates deadlock. The DBMS selects a **victim** transaction (usually the one that's cheapest to abort) and rolls it back.

Prevention: Acquire locks in a consistent order; use `NOWAIT`/`SKIP LOCKED`; use timeouts.

---

## Indexing & Query Processing

**Q14. What is a B+ Tree and why is it preferred for database indexes?**

**A:** A B+ Tree is a self-balancing tree where all data pointers (leaf nodes) are at the bottom level, linked together in a doubly linked list. Internal nodes only store keys for navigation.

Why preferred:
- O(log n) search, insert, delete
- Excellent for range queries (traverse the leaf list)
- High fan-out means shallow trees even for millions of records — few disk I/Os
- All leaf nodes at the same depth — predictable performance

B-Tree stores data at all levels; B+ Tree keeps data only at leaves — makes range scans much more efficient.

---

**Q15. What is query optimization?**

**A:** Query optimization transforms a SQL query into an efficient execution plan. The optimizer:
1. Parses and validates SQL into a logical query tree.
2. Applies algebraic transformations (push selections down, reorder joins).
3. Generates multiple candidate physical plans (different join algorithms, index vs sequential scan).
4. Estimates cost of each plan using statistics (row counts, data distribution, I/O cost).
5. Selects the lowest-cost plan.

`EXPLAIN ANALYZE` shows the chosen plan and actual execution statistics.

---

**Q16. What are the different join algorithms?**

**A:**
- **Nested Loop Join**: For each row in the outer table, scan the inner table. O(n×m). Good when the inner table has an index or is small.
- **Hash Join**: Build a hash table on the smaller relation, probe it with the larger. O(n+m). Good for large, unsorted tables with equality joins.
- **Sort-Merge Join**: Sort both relations on the join key, then merge. O(n log n + m log m). Good when data is already sorted or indexes exist.

---

## Recovery

**Q17. What is write-ahead logging (WAL)?**

**A:** WAL guarantees atomicity and durability: before any database page is modified on disk, the corresponding log records are written to a persistent log first. On crash recovery, the system replays the log to redo committed transactions and undo uncommitted ones. Used by PostgreSQL (WAL segments), MySQL InnoDB (redo log), and virtually all production databases.

---

**Q18. What is a checkpoint in DBMS?**

**A:** A checkpoint is a point in the log where the DBMS guarantees that all dirty (modified) buffer pages have been flushed to disk. On recovery, the system only needs to process log records after the last checkpoint, reducing recovery time significantly. Without checkpoints, recovery would require replaying the entire log from the beginning.

---

**Q19. What is the difference between undo logging and redo logging?**

**A:**
- **Undo logging**: Records the old value (before image) of each changed item. Used to roll back an aborted transaction. The rule: write the undo log record to disk before writing the modified data page.
- **Redo logging**: Records the new value (after image). Used to replay committed transactions after a crash. The rule: write all log records to disk before committing.
- **ARIES** (used by most databases): Uses both undo and redo logs with a steal/no-force buffer management policy.

---

## Distributed Databases

**Q20. What is the CAP theorem and how does it apply to distributed databases?**

**A:** CAP states a distributed system can guarantee at most two of: Consistency (all nodes see the same data at the same time), Availability (every request gets a response), Partition Tolerance (system works despite network partitions).

Since network partitions are unavoidable in distributed systems, the real choice is **CP vs AP**:
- **CP systems** (HBase, Zookeeper, etcd): May become unavailable during partitions to preserve consistency.
- **AP systems** (Cassandra, CouchDB, DynamoDB): Remain available but may serve stale data during partitions.

---

**Q21. What is the two-phase commit (2PC) and what are its limitations?**

**A:** 2PC coordinates a distributed transaction across multiple nodes:
1. **Phase 1 (Prepare)**: Coordinator asks all participants to prepare and vote yes/no.
2. **Phase 2 (Commit/Abort)**: If all vote yes, coordinator commits. Otherwise, abort.

Limitations:
- **Blocking**: If the coordinator crashes after prepare but before commit, participants are blocked indefinitely (holding locks).
- **Single point of failure**: Coordinator failure can leave the system in an uncertain state.
- **Latency**: Two network round trips per transaction.

Alternatives: Sagas (no distributed locks), Paxos/Raft for consensus.

---

**Q22. What is data fragmentation in distributed databases?**

**A:**
- **Horizontal fragmentation**: Different rows of a table stored on different nodes (like sharding). Example: customers with ID 1-1000 on node 1, 1001-2000 on node 2.
- **Vertical fragmentation**: Different columns stored on different nodes. Example: personal info on one node, financial info on another.
- **Mixed**: Combines both.

Fragmentation enables parallel query processing and places data closer to where it's accessed, but complicates queries that need data from multiple fragments.

---
