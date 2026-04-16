# Databases: RDS, DynamoDB & ElastiCache 📊

> Structured, semi-structured, and cached data solutions.

---

## 🐘 RDS (Relational Database Service) - SQL

Managed SQL database service supporting Aurora, PostgreSQL, MySQL, MariaDB, Oracle, and SQL Server.

### 🛡️ High Availability & Performance
| Feature | Multi-AZ Deployment | Read Replicas |
| :--- | :--- | :--- |
| **Purpose** | High Availability (HA) / Disaster Recovery. | Scalability / Performance. |
| **Replication** | Synchronous | Asynchronous |
| **Usage** | One active DB, one standby (Failover point). | Read-only copies for heavy read workloads. |
| **Backups** | Backups taken from the Standby (no perf hit). | Can be promoted to independent DB. |

### ⚡ Amazon Aurora
- AWS-optimized relational database.
- **5x faster** than standard MySQL, **3x faster** than PostgreSQL.
- Storage: Auto-scaling (up to 128TB), replicated 6 times across 3 AZs.
- **Aurora Serverless**: For infrequent, intermittent, or unpredictable workloads.

---

## ⚡ DynamoDB - NoSQL

Fully managed, serverless, key-value and document database that delivers single-digit millisecond performance at any scale.

- **Primary Keys**:
  - **Partition Key (PK)**: Determines the partition (sharding).
  - **Sort Key (SK)**: (Optional) Orders data within the partition.
- **Scaling**:
  - **Provisioned**: You specify RCU (Read Capacity) and WCU (Write Capacity).
  - **On-Demand**: Paid per request (expensive for high load, good for unknown load).
- **DAX**: In-memory cache for DynamoDB (10x performance boost for reads).
- **Global Tables**: Multi-region, fully managed, multi-active replication.

---

## 🚀 ElastiCache (Caching)

In-memory data store used to improve application performance by caching frequent data.

- **Redis**: Multi-AZ with Auto-Failover, Data Persistence, Pub/Sub, Complex data types.
- **Memcached**: Simple, multi-threaded, non-persistent. Use for simple caching.

---

## 🏛️ Redshift (Data Warehouse)

- **OLAP** (Online Analytical Processing) as opposed to RDS/Aurora (OLTP).
- Columnar storage (Optimized for complex queries).
- PB-scale data warehousing.

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | Scaling heavy read traffic on SQL | Add **Read Replicas**. |
| 2 | High Availability for SQL | Enable **Multi-AZ**. |
| 3 | Serverless NoSQL with millisecond latency | **DynamoDB**. |
| 4 | Improve DynamoDB read latency (us) | Use **DAX** (DynamoDB Accelerator). |
| 5 | Migrating large amounts of data to AWS | Use **AWS Snowball** or **AWS DMS** (Database Migration Service). |

---

## 📁 Notes Index

| File | Topics |
| :--- | :--- |
| `00_global_infrastructure_and_shared_responsibility.md` | Regions, AZs, Shared Responsibility, Billing |
| `01_iam_and_security.md` | IAM, KMS, Shield, WAF, GuardDuty |
| `02_compute_ec2_lambda_ecs.md` | EC2, Lambda, Auto Scaling, ELB, ECS/EKS |
| `03_storage_s3_ebs_efs.md` | S3, EBS, EFS, Glacier |
| `04_networking_vpc_route53_cloudfront.md` | VPC, Route 53, CloudFront |
| `05_databases_rds_dynamodb_elasticache.md` | RDS, DynamoDB, ElastiCache, Redshift |
| `06_management_monitoring_well_architected.md` | CloudWatch, CloudTrail, Well-Architected Framework |
