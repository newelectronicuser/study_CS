# Databases (RDS, DynamoDB, ElastiCache)

AWS offers a variety of database engines optimized for different workloads (Relational, NoSQL, In-Memory).

## 1. Amazon RDS (Relational Database Service)
Supports MySQL, PostgreSQL, MariaDB, Oracle, SQL Server, and Amazon Aurora.

> [!IMPORTANT]
> **Multi-AZ vs Read Replicas**: 
> - **Multi-AZ**: Synchronous replication to a standby instance in a different AZ. For **High Availability** and Disaster Recovery. (Used for failover).
> - **Read Replicas**: Asynchronous replication. For **Scalability** of read-heavy workloads. (Offloads read traffic).

### Amazon Aurora
AWS-native, MySQL/PostgreSQL-compatible relational database.
- 5x faster than standard MySQL.
- Auto-scaling storage (up to 128TB).
- Storage is replicated 6-ways across 3 AZs.

## 2. Amazon DynamoDB (NoSQL)
A key-value and document database that delivers single-digit millisecond performance at any scale.

### Key Concepts
- **Partitions**: Data is distributed across partitions based on the **Partition Key**.
- **LSI (Local Secondary Index)**: Same partition key, different sort key. Can only be created at table creation time.
- **GSI (Global Secondary Index)**: Different partition key and different sort key. Can be created/deleted anytime.
- **WCU / RCU**: Provisioned throughput (Write/Read Capacity Units). Can also use "On-Demand" mode.

### Advanced Features
- **TTL (Time to Live)**: Automatically delete expired items (no extra cost).
- **Streams**: Capture changes to items (Insert/Update/Delete) and trigger a Lambda.
- **DAX (DynamoDB Accelerator)**: In-memory cache for DynamoDB, reducing latency to microseconds.

## 3. Amazon ElastiCache (In-Memory)
- **Redis**: Supports sub-millisecond latency, data structures, and persistence. (Best for complex caching).
- **Memcached**: Simple key-value store. (Best for simple caching).

## 4. Architectural Comparison

| Feature | RDS | DynamoDB | ElastiCache |
| :--- | :--- | :--- | :--- |
| **Model** | Relational (SQL) | NoSQL | In-Memory |
| **Scaling** | Vertical (Write), Horizontal (Read) | Seamless Horizontal | Primary + Replicas |
| **Schema** | Rigid | Flexible | Key-Value |
| **Consistency** | Strong | Eventual (Default), Strong (Optional) | Eventual |

## 5. Database Scenarios

### Scenario A: Real-time Leaderboard for a mobile game
**Solution**: **ElastiCache for Redis** using "Sorted Sets" (ZSETs).

### Scenario B: Massive scale IoT data (billions of events)
**Solution**: **DynamoDB**. It handles high-velocity writes and scales horizontally better than RDS.

### Scenario C: Complex JOINs across multiple tables
**Solution**: **RDS (Aurora)**. Relational databases are optimized for complex queries and normalization.

> [!TIP]
> **DynamoDB Keys**: 
> Use a high-cardinality Partition Key (e.g., `user_id` instead of `status`) to avoid "Hot Partitions" that can lead to throttling.

> [!WARNING]
> **RDS Patching**: 
> While RDS handles OS patching, there is still a "Maintenance Window." If you use Multi-AZ, the failover happens during the window, resulting in minimal downtime.
