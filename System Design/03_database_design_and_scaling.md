# Database Design and Scaling

Choosing the right database and scaling it correctly is the foundation of a robust system.

## 1. SQL vs. NoSQL
- **SQL (Relational)**: Structured data, ACID compliance, complex joins. Best for fintech, user profiles, and where data integrity is paramount. (e.g., PostgreSQL, MySQL).
- **NoSQL (Non-Relational)**: Flexible schema, horizontal scaling, high performance for specific access patterns.
    - **Key-Value**: (Redis, DynamoDB) - Caching, sessions.
    - **Document**: (MongoDB) - Content management, blogs.
    - **Wide-column**: (Cassandra, HBase) - Time-series data, huge datasets.
    - **Graph**: (Neo4j) - Social networks, fraud detection.

## 2. Scaling Databases
### Replication (Read Scaling)
Creating copies of the database to handle more read traffic.
- **Leader-Follower (Master-Slave)**: All writes go to the Leader. Data is replicated to Followers. Reads can go to any node.
- **Multi-Leader**: Multiple nodes can accept writes (good for geographically distributed systems).

### Sharding (Write Scaling)
Splitting data across multiple database instances (shards) based on a "shard key".
- **Horizontal Partitioning**: Splitting a large table into smaller tables based on rows.
- **Consistent Hashing**: A technique to minimize data movement when a new shard is added or removed.

## 3. ACID vs. BASE
- **ACID (SQL focus)**: Atomicity, Consistency, Isolation, Durability. Focus on data integrity.
- **BASE (NoSQL focus)**: Basically Available, Soft state, Eventual consistency. Focus on availability and performance.

## 4. Database Optimization
- **Indexing**: Speeds up reads but slows down writes (as indices must be updated).
- **Denormalization**: Intentionally adding redundant data to avoid expensive JOINs in SQL.
- **Read Replicas**: Offloading read traffic from the main leader.

## 5. CAP Theorem Choices
- **HBase/MongoDB**: Usually CP (Consistency and Partition Tolerance).
- **Cassandra/DynamoDB**: Usually AP (Availability and Partition Tolerance).

> [!WARNING]
> **Shard Key Selection**: 
> Choosing a bad shard key (like a monotonically increasing ID) can lead to "hotspots," where one shard handles all the traffic while others are idle.

> [!TIP]
> **Replication vs. Partitioning**:
> - **Replication** is for **Availability** and **Read Scaling**.
> - **Partitioning/Sharding** is for **Write Scaling** and **Storage Capacity**.
