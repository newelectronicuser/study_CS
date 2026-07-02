# DBMS — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Fundamentals

**Q1. What is a DBMS? How does it differ from a file system?**

**A:**
*   **DBMS (Database Management System):** A software suite designed to store, retrieve, define, and manage data in databases while ensuring security, integrity, and concurrency control.
*   *Differences from File System:*
    *   **Data Redundancy:** File systems have high redundancy and inconsistency; DBMS minimizes it via normalization.
    *   **Access:** File system access is complex, requiring custom code; DBMS uses a declarative query language (SQL).
    *   **Concurrency:** File systems lack robust simultaneous access locks; DBMS uses lock managers and MVCC.
    *   **Transactions:** DBMS guarantees ACID transactions; file systems do not.

---

**Q2. What are the different types of database models?**

**A:**
*   **Relational Model:** Organizes data into tables of rows and columns linked by keys (most common).
*   **Hierarchical Model:** Organizes data in a tree-like parent-child structure (1:N relationship).
*   **Network Model:** Organizes data in graph-like structures allowing multiple parents (M:N relationship).
*   **Object-Oriented Model:** Stores data as objects rather than relations, binding code and data together.

---

**Q3. What is a data model? What is the difference between conceptual, logical, and physical data models?**

**A:** A data model defines how data is structured, stored, and manipulated.
*   **Conceptual Model:** High-level design showing *what* data is stored and the relationships between entities (technology-independent, typically ER diagrams).
*   **Logical Model:** Defines the structure of data elements and relationships (e.g., tables, columns, primary/foreign keys) without specifying database engine specifics.
*   **Physical Model:** The concrete database implementation, defining actual tables, column data types, indexes, partitions, and storage locations (database-engine dependent).

---

**Q4. What is the three-schema architecture?**

**A:** A framework to separate user applications from the physical database:
1.  **External Level (View Schema):** Describes how end users view the data (individual views).
2.  **Conceptual Level (Logical Schema):** Defines the structural design of the entire database (tables, fields, relationships, constraints).
3.  **Internal Level (Physical Schema):** Describes the physical storage structure and access paths (indexes, blocks, files).

---

**Q5. What is data independence? Distinguish between logical and physical data independence.**

**A:** Data independence is the ability to modify schema definitions at one level without forcing modifications at higher levels.
*   **Logical Data Independence:** The ability to change the conceptual schema (e.g., adding a table column) without changing the external schema or application code.
*   **Physical Data Independence:** The ability to change the physical schema (e.g., adding an index, moving files to another disk) without changing the conceptual or logical schema.

---

**Q6. What is DDL vs DML vs DCL vs TCL in SQL?**

**A:**
*   **DDL (Data Definition Language):** Defines database structure (e.g., `CREATE`, `ALTER`, `DROP`, `TRUNCATE`).
*   **DML (Data Manipulation Language):** Manipulates database records (e.g., `SELECT`, `INSERT`, `UPDATE`, `DELETE`).
*   **DCL (Data Control Language):** Controls permissions and security (e.g., `GRANT`, `REVOKE`).
*   **TCL (Transaction Control Language):** Manages transactions (e.g., `COMMIT`, `ROLLBACK`, `SAVEPOINT`).

---

**Q7. What is a database schema?**

**A:** A database schema is the formal, skeletal structure representing the logical configuration of the entire database, defining tables, views, fields, data types, indexes, and primary/foreign key relationships.

---

## ER Modeling

**Q8. What is an Entity-Relationship (ER) diagram? What are the components?**

**A:** An ER diagram is a graphical tool used to design databases conceptually.
*   *Components:*
    *   **Entity:** A real-world object (represented by a rectangle).
    *   **Attribute:** A property of an entity (represented by an ellipse).
    *   **Relationship:** Associations between entities (represented by a diamond).

---

**Q9. What is the difference between a strong entity and a weak entity?**

**A:**
*   **Strong Entity:** Has a primary key and can exist independently of other entities in the system.
*   **Weak Entity:** Cannot be uniquely identified by its own attributes alone and depends on a strong parent entity to exist. Its primary key is formed by combining the parent's primary key with a partial discriminator attribute (represented by a double rectangle).

---

**Q10. What is cardinality in an ER diagram?**

**A:** Cardinality defines the numerical relationships between entities:
*   **One-to-One (1:1):** One manager manages one department.
*   **One-to-Many (1:N):** One department employs many employees.
*   **Many-to-Many (M:N):** Many students enroll in many courses.

---

**Q11. What is a participation constraint (total vs partial)?**

**A:**
*   **Total Participation:** Every entity in the set must participate in the relationship (represented by a double line). e.g., Every `Employee` *must* work for a `Department`.
*   **Partial Participation:** Entities in the set may or may not participate in the relationship. e.g., Not every `Employee` manages a `Department`.

---

**Q12. How do you convert an ER diagram to relational tables?**

**A:**
1.  Convert strong entities into tables.
2.  Convert weak entities into tables, including the parent's primary key as a foreign key.
3.  For 1:N relationships, place the primary key of the "1" side as a foreign key in the "N" side table.
4.  For M:N relationships, create a new mapping table containing the primary keys of both participating tables.

---

**Q13. What is generalization vs specialization in ER modeling?**

**A:**
*   **Generalization:** A bottom-up approach where multiple low-level entities with common attributes are combined into a single high-level entity (e.g. merging `Car` and `Truck` into `Vehicle`).
*   **Specialization:** A top-down approach where a high-level entity is split into multiple low-level sub-entities based on distinguishing features (e.g. splitting `Employee` into `Developer` and `Manager`).

---

## Normalization

**Q14. What is normalization and why is it important?**

**A:** Normalization is the process of structuring relational tables to reduce data redundancy and eliminate update, insertion, and deletion anomalies. It is important because it ensures data integrity and saves disk storage space.

---

**Q15. Explain 1NF, 2NF, 3NF, and BCNF with examples.**

**A:**
*   **1NF (First Normal Form):** Every attribute value must be atomic (no arrays or multi-valued attributes).
*   **2NF (Second Normal Form):** Must be in 1NF, and all non-key attributes must be fully functionally dependent on the primary key (eliminates partial dependency on composite keys).
*   **3NF (Third Normal Form):** Must be in 2NF, and no non-key attribute should be transitively dependent on the primary key (no non-key attribute determines another non-key attribute).
*   **BCNF (Boyce-Codd Normal Form):** A stronger version of 3NF. For every functional dependency $X \rightarrow Y$, $X$ must be a super key.

---

**Q16. What is a functional dependency?**

**A:** A functional dependency $X \rightarrow Y$ describes a relationship between attributes where the value of $X$ uniquely determines the value of $Y$. If two rows have the same value for $X$, they must have the same value for $Y$.

---

**Q17. What is a partial dependency and how does it violate 2NF?**

**A:** A partial dependency occurs when a non-prime attribute depends on only a *part* of a composite primary key.
*   *Example:* If primary key is `(StudentID, CourseID)` and `StudentName` only depends on `StudentID`, it is a partial dependency. This violates 2NF because `StudentName` should be split into a separate `Students` table.

---

**Q18. What is a transitive dependency and how does it violate 3NF?**

**A:** A transitive dependency occurs when $A \rightarrow B$ and $B \rightarrow C$, meaning $A \rightarrow C$ transitively through $B$.
*   *Example:* `BookID -> AuthorID` and `AuthorID -> AuthorName`. Here, `AuthorName` is transitively dependent on `BookID`. This violates 3NF, and `AuthorName` must be moved to an `Authors` table.

---

**Q19. What is the difference between 3NF and BCNF?**

**A:** BCNF is stricter than 3NF. In 3NF, for a dependency $X \rightarrow Y$, $Y$ can be a prime attribute (part of a candidate key) even if $X$ is not a super key. BCNF forbids this: $X$ **must** be a super key for any functional dependency $X \rightarrow Y$.

---

**Q20. What are 4NF and 5NF? When do they apply?**

**A:**
*   **4NF:** Must be in BCNF and contain no multi-valued dependencies.
*   **5NF (Project-Join Normal Form):** Relates to splitting tables such that they can be reconstructed by joining smaller tables without introducing spurious data. Applied only to complex multi-key mapping structures.

---

**Q21. When and why would you denormalize a database?**

**A:** Denormalization is the process of intentionally adding redundancy to a database.
*   *Why:* To improve read query performance. Highly normalized databases require joining many tables, which is CPU-expensive. Denormalization stores pre-joined or aggregated data directly to speed up search routines, at the cost of slower writes and higher storage usage.

---

## Relational Algebra & Calculus

**Q22. What is relational algebra? Name the fundamental operators.**

**A:** Relational algebra is a procedural query language that defines a set of operations on relations to produce a new relation.
*   *Fundamental Operators:* Selection ($\sigma$), Projection ($\pi$), Union ($\cup$), Set Difference ($-$), Cartesian Product ($\times$), and Rename ($\rho$).

---

**Q23. What is the difference between selection ($\sigma$) and projection ($\pi$)?**

**A:**
*   **Selection ($\sigma$):** A horizontal operation that filters rows matching a predicate condition (similar to `WHERE` in SQL).
*   **Projection ($\pi$):** A vertical operation that selects specific columns/attributes, discarding the rest (similar to `SELECT column1, column2` in SQL).

---

**Q24. What is the difference between natural join, equi-join, and theta join?**

**A:**
*   **Theta Join:** Joins tables based on an arbitrary comparison predicate (using operators like $<, >, \leq, \geq, \neq, =$).
*   **Equi-Join:** A specific type of Theta Join that uses only the equality operator ($=$).
*   **Natural Join:** An Equi-Join that automatically joins tables on all columns with identical names and automatically removes duplicate join columns from the result schema.

---

**Q25. What is the Cartesian product in relational algebra?**

**A:** The Cartesian product ($R \times S$) combines every row of relation $R$ with every row of relation $S$. If $R$ has $n$ rows and $S$ has $m$ rows, the result has $n \times m$ rows.

---

**Q26. What is relational calculus and how does it differ from relational algebra?**

**A:**
*   **Relational Algebra:** Procedural (tells the system *how* to retrieve the data through operations).
*   **Relational Calculus:** Non-procedural/declarative (tells the system *what* data to retrieve using predicate logic formulas, leaving the execution steps to the engine).

---

## Transactions & Concurrency Control

**Q27. What are the ACID properties? Define each.**

**A:**
*   **Atomicity:** All operations in a transaction succeed, or the entire transaction is rolled back (All-or-Nothing).
*   **Consistency:** A transaction must transition the database from one valid state to another, maintaining all schema constraints.
*   **Isolation:** Transactions executing concurrently must not interfere with each other; they must behave as if executing sequentially.
*   **Durability:** Once committed, transaction updates are written to non-volatile storage and will persist even in a system crash.

---

**Q28. What is a transaction? What is a transaction log?**

**A:**
*   **Transaction:** A logical unit of database processing consisting of one or more SQL statements executed as a single package.
*   **Transaction Log (WAL):** An append-only log file on disk recording all database modifications before they are applied to the database pages, enabling crash recovery.

---

**Q29. What are transaction isolation levels? List them in order of strictness.**

**A:** Defined by the SQL standard to balance performance and consistency:
1.  **Read Uncommitted (Weakest):** Allows dirty reads. No read locks.
2.  **Read Committed:** Prevents dirty reads. Read locks are released immediately after read.
3.  **Repeatable Read:** Prevents dirty and non-repeatable reads. Holds read locks until transaction completion.
4.  **Serializable (Strictest):** Prevents all anomalies including phantoms by locking ranges of keys or tables.

---

**Q30. What is a dirty read, non-repeatable read, and phantom read?**

**A:**
*   **Dirty Read:** Transaction A reads data modified by Transaction B that has not been committed yet. If B rolls back, A's data is invalid.
*   **Non-Repeatable Read:** Transaction A reads a row. Transaction B updates that row and commits. Transaction A reads the same row again and sees different values.
*   **Phantom Read:** Transaction A executes a query matching a range condition. Transaction B inserts a new row matching that range and commits. Transaction A runs the query again and sees a new "phantom" row.

---

**Q31. What is a schedule in DBMS? What is a serializable schedule?**

**A:**
*   **Schedule:** A chronological sequence of execution steps (read, write, commit, abort) of concurrent transactions.
*   **Serializable Schedule:** A concurrent schedule that produces the exact same database state as some serial (one-by-one) execution of those transactions.

---

**Q32. What is conflict serializability? What is view serializability?**

**A:**
*   **Conflict Serializability:** A schedule is conflict serializable if it can be transformed into a serial schedule by swapping non-conflicting operations (reads/writes of different variables or concurrent reads of same variable).
*   **View Serializability:** A broader definition of serializability checking if the read-write view matches a serial schedule (handles blind writes). Testing view serializability is NP-complete.

---

**Q33. What is a precedence graph?**

**A:** A directed graph used to test conflict serializability. Nodes represent transactions, and edges represent conflicting operations (e.g. $T_1$ writes $x$, then $T_2$ reads $x$). If the graph has **no cycles**, the schedule is conflict serializable.

---

**Q34. What is two-phase locking (2PL)? What is strict 2PL?**

**A:**
*   **2PL:** A protocol guaranteeing serializability. It has two phases:
    1.  *Growing Phase:* Transactions acquire locks, cannot release any.
    2.  *Shrinking Phase:* Transactions release locks, cannot acquire any.
*   **Strict 2PL:** Requires that all exclusive (write) locks held by a transaction must be released only after the transaction commits or aborts, preventing cascading rollbacks.

---

**Q35. What is a deadlock in DBMS? How is it resolved?**

**A:** A state where two transactions are blocked waiting for locks held by each other.
*   *Resolution:* The DBMS runs a deadlock detection engine checking the "wait-for" graph for cycles. If a cycle is detected, it selects a transaction (the "victim") and aborts it, releasing its locks.

---

**Q36. What is timestamp-based concurrency control?**

**A:** A lock-free concurrency protocol. Each transaction receives a unique timestamp upon starting. Read and write operations check variable timestamp limits. If a transaction attempts to write a variable that has already been read by a newer transaction, it is aborted and restarted.

---

**Q37. What is optimistic concurrency control (OCC)?**

**A:** Assumes lock conflicts are rare. Transactions execute in three phases:
1.  **Read:** Read and write changes to local workspace.
2.  **Validate:** Check if other transactions modified variables concurrently.
3.  **Write (Commit):** If no conflict, write changes to DB. If conflict, abort and restart.

---

**Q38. What is MVCC? Which databases use it?**

**A:** Multi-Version Concurrency Control. Instead of locking rows on updates, the database stores multiple versions of a row. Readers see a snapshot version of data consistent with their start timestamp, completely avoiding read locks and allowing readers to never block writers, and vice versa.
*   *Databases:* PostgreSQL, MySQL (InnoDB), Oracle.

---

## Indexing & Query Processing

**Q39. What is an index? What is dense vs sparse index?**

**A:** An index is a data structure that speeds up data retrieval operations at the cost of additional storage and slower writes.
*   **Dense Index:** Contains an index record for every single search key value in the data file.
*   **Sparse Index:** Contains index records for only some search key values (usually block pointers). Faster to load but requires scanning data blocks.

---

**Q40. What is a B-Tree and a B+ Tree? Why is B+ Tree preferred for database indexes?**

**A:**
*   **B-Tree:** A self-balancing search tree where internal nodes store both keys and data pointers.
*   **B+ Tree:** An extension of B-Tree where keys are stored in internal nodes, but **all data pointers are stored in leaf nodes**. Leaf nodes are also linked sequentially as a doubly linked list.
*   *Why Preferred:* Because internal nodes do not store data, they are smaller, meaning more keys fit in a single disk block (higher fan-out). This reduces disk I/O depth. Additionally, the linked leaf nodes make range queries extremely fast (linear scan instead of tree traversals).

---

**Q41. What is a hash index? When is it better than a B+ Tree?**

**A:** A hash index uses a hash function to map search keys to buckets.
*   *Comparison:* Hash indexes offer $O(1)$ lookup times, which is faster than B+ Trees ($O(\log n)$) for exact matches (`WHERE id = 5`). However, they do not support range queries (`WHERE age > 18`) or sorting, making B+ Trees the default for general indexing.

---

**Q42. What is a bitmap index and when is it used?**

**A:** A bitmap index uses bit arrays (0s and 1s) to represent membership of values. Used for columns with low cardinality (few unique values, e.g. `Gender`, `MaritalStatus`) in data warehousing systems where queries combine multiple filters using fast bitwise AND/OR operations.

---

**Q43. What is query optimization? What is a query execution plan?**

**A:**
*   **Query Optimization:** The process of parsing an SQL query and choosing the most efficient execution strategy (e.g., deciding which index to scan, which join algorithm to use).
*   **Query Execution Plan:** The concrete tree of operations generated by the optimizer showing step-by-step how the query will be executed.

---

**Q44. What is a cost-based optimizer vs a rule-based optimizer?**

**A:**
*   **Rule-based:** Follows static, hardcoded heuristics (e.g., "always use index if available").
*   **Cost-based (Modern):** Evaluates multiple plans, calculates a cost score for each based on table statistics (cardinality, row size, disk I/O, CPU cycles), and selects the lowest-cost plan.

---

**Q45. What is join ordering and why does it matter?**

**A:** Join ordering determines the sequence in which multiple tables are joined. In a 3-table join (`A JOIN B JOIN C`), joining `A` and `B` first might yield a massive intermediate dataset, whereas joining `B` and `C` first might filter out 99% of rows immediately. Proper ordering dramatically reduces CPU and RAM usage.

---

**Q46. What are the different join algorithms?**

**A:**
*   **Nested Loop Join:** For each row in outer table, scan the inner table. Best for small tables.
*   **Hash Join:** Builds a hash table in memory for the smaller table, then scans the larger table matching keys. Highly efficient for large tables.
*   **Sort-Merge Join:** Sorts both tables on join keys, then merges them in a single pass. Best if data is already indexed/sorted on join keys.

---

## Recovery & Reliability

**Q47. What is database recovery? What failures exist?**

**A:** Database recovery restores the database to a consistent state after a crash.
*   *Failures:* Transaction failures (aborts), System crash (power loss), Disk failure (media corruption).

---

**Q48. What is a checkpoint in DBMS?**

**A:** A checkpoint is a recovery optimization marker. Periodically, the DBMS flushes all dirty in-memory pages to disk and writes a `CHECKPOINT` record to the log. During recovery, the engine only needs to scan the log back to the last checkpoint, drastically reducing recovery startup times.

---

**Q49. What is write-ahead logging (WAL)?**

**A:** The requirement that log records describing database modifications must be written and flushed to non-volatile disk storage (the log file) **before** the actual database pages are written to the data files. This guarantees durability and undo capability during crashes.

---

**Q50. What is the ARIES recovery algorithm?**

**A:** A widely used crash recovery algorithm consisting of three phases:
1.  **Analysis:** Scan log forward from the last checkpoint to identify active transactions and dirty pages.
2.  **Redo:** Reapply all logged changes (including aborted transactions) to restore the state at crash time.
3.  **Undo:** Roll back changes of all transactions that were active (not committed) at the crash time by scanning the log backward.

---

**Q51. What is the difference between undo logging and redo logging?**

**A:**
*   **Undo Log:** Contains the *old* values of updated data. Used to roll back changes of incomplete transactions.
*   **Redo Log:** Contains the *new* values of updated data. Used to reapply changes of committed transactions that were not flushed to disk before the crash.

---

**Q52. What is shadow paging?**

**A:** An alternative to log-based recovery. The database maintains two page tables: a current page table and a shadow page table. Writes are written to new, independent disk pages. On commit, the current page table pointer is updated to point to the new pages. On crash, the database simply discards the current page table and restores the shadow table, making recovery instantaneous without scanning logs.

---

## Distributed Databases

**Q53. What is a distributed database?**

**A:** A database system where physical storage is distributed across multiple network nodes (servers).
*   *Advantages:* High availability, scalability, low latency (geographical distribution).
*   *Challenges:* Network partition failures, consistency maintenance, distributed deadlock resolution.

---

**Q54. What is data fragmentation?**

**A:** Dividing a table into smaller portions to store across different nodes:
*   **Horizontal (Sharding):** Splitting rows (e.g. users with ID 1-1000 on Node A, 1001-2000 on Node B).
*   **Vertical:** Splitting columns (e.g. personal info on Node A, transaction history on Node B).
*   **Mixed:** Combining both.

---

**Q55. What is data replication and what consistency models exist?**

**A:** Storing copies of the same data across multiple nodes for availability.
*   *Models:*
    *   **Strong Consistency:** All read operations see the latest committed writes immediately.
    *   **Eventual Consistency:** Replicas sync asynchronously. Reads might return stale data temporarily, but all nodes eventually converge to the same state.

---

**Q56. What is the CAP theorem?**

**A:** A distributed system can guarantee at most two out of three properties simultaneously:
*   **Consistency (C):** Every read receives the most recent write or an error.
*   **Availability (A):** Every non-failing node returns a non-error response (without guarantee of containing the latest write).
*   **Partition Tolerance (P):** The system continues to operate despite network partition errors.
In practice, because network partitions cannot be prevented (must choose P), databases must choose between Consistency (CP) or Availability (AP).

---

**Q57. What is a two-phase commit (2PC)?**

**A:** A protocol coordinating commits across distributed database nodes.
*   *Phase 1 (Prepare):* Coordinator asks nodes if they are ready to commit. Nodes write to log and reply yes/no.
*   *Phase 2 (Commit):* If all say yes, coordinator sends commit. If any say no/times out, coordinator sends rollback.
*   *Limitations:* It is blocking; if coordinator crashes in Phase 2, locks are held indefinitely, causing latency.

---

**Q58. What is a distributed query?**

**A:** A query that accesses data located across multiple physical nodes of a distributed database. The query optimizer must break it down into local sub-queries, coordinate execution, and merge results over the network.

---
