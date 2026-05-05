# 04 Databases: RDS, DynamoDB, Aurora, ElastiCache

## Core Concepts

### RDS (Relational Database Service)
Managed RDBMS (MySQL, PostgreSQL, SQL Server, Oracle, MariaDB).
*   **Multi-AZ:** Synchronous replication to a standby in a different AZ. Used for **Disaster Recovery/High Availability**. Automated failover.
*   **Read Replicas:** Asynchronous replication. Used for **Scaling Reads**.

### Amazon Aurora
AWS-native relational database (compatible with MySQL/PostgreSQL).
*   **Architecture:** Decoupled compute and storage. Storage scales automatically up to 128TB.
*   **Aurora Serverless:** Scales compute capacity (ACUs) up and down based on application demand.

### DynamoDB (NoSQL)
Key-value and document database.
*   **Scaling:** Provisioned (you set RCU/WCU) vs. On-Demand (pay-per-request).
*   **Indexes:**
    *   **LSI (Local Secondary Index):** Same Partition Key, different Sort Key. Created only at table creation.
    *   **GSI (Global Secondary Index):** Different Partition Key and Sort Key. Can be added/deleted anytime.

### ElastiCache
In-memory data store.
*   **Redis:** Supports complex data types, persistence, and pub/sub.
*   **Memcached:** Simple key-value store, multi-threaded.

---

## Interview Deep Dives

### 1. "Multi-AZ vs. Read Replicas: When do you use each?"
**Answer:**
*   **Multi-AZ:** Use for **Reliability**. It's a "hot standby." You cannot read from the standby. It protects against AZ failure or hardware failure.
*   **Read Replicas:** Use for **Scalability**. You can have up to 5/15 replicas to offload read traffic from the primary. Failover to a replica is possible but manual (unless using Aurora).

### 2. "How do you handle 'Hot Partitions' in DynamoDB?"
**Answer:**
*   **What it is:** When a large amount of traffic is directed to a single partition key, exceeding the 1000 WCU / 3000 RCU limit per partition.
*   **Mitigation:**
    1.  **Add Random Suffix:** Append a random number to the partition key (e.g., `user_id_1`, `user_id_2`) to distribute data.
    2.  **DAX (DynamoDB Accelerator):** An in-memory cache to handle heavy read traffic.
    3.  **Better Key Design:** Use high-cardinality attributes for the partition key.

### 3. "Aurora vs. Standard RDS: Why choose Aurora?"
**Answer:**
*   **Performance:** Up to 5x faster than standard MySQL.
*   **Reliability:** Data is replicated 6 times across 3 AZs.
*   **Failover:** Much faster (usually < 30s) than standard RDS Multi-AZ.
*   **Scaling:** Aurora Serverless v2 scales much more smoothly without dropping connections.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **RDS (Postgres)** | Complex joins, ACID | Vertical scaling limit; manual sharding for massive scale. |
| **DynamoDB** | Extreme scale, low latency | No joins; expensive for complex queries; strict schema modeling. |
| **Redshift** | OLAP / Data Warehousing | Not for transactional (OLTP) workloads. |

---

## Common Interview Scenarios

### Scenario: "A social media app has 90% read traffic and 10% write traffic. The RDS instance is struggling."
**Troubleshooting/Design:**
1.  **Read Replicas:** Add replicas and update the application to split read/write traffic.
2.  **Caching:** Implement **ElastiCache (Redis)** for frequently accessed data (e.g., user profiles).
3.  **Connection Pooling:** Use **RDS Proxy** to manage high connection counts from Lambda.

### Scenario: "When should you use DynamoDB over RDS?"
**Key Details:**
*   **Scalability:** If you need to handle millions of requests per second.
*   **Schema Flexibility:** If the data model is evolving or varies significantly.
*   **Persistence:** If you need single-digit millisecond latency at any scale.
*   **Operations:** If you want a truly "serverless" database with no maintenance windows.
