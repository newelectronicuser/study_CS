# Storage and Databases

GCP provides specialized storage and database services for every type of workload, from simple object storage to globally consistent relational databases.

## 1. Cloud Storage
Object storage for unstructured data.
- **Bucket Classes**:
    - **Standard**: High frequency access.
    - **Nearline**: Access < 1 time per month. (Backup).
    - **Coldline**: Access < 1 time per 90 days. (Disaster recovery).
    - **Archive**: Access < 1 time per year. (Compliance).
- **Features**: Object Lifecycle Management, Versioning, and Retention Policies.

## 2. Cloud SQL
Managed relational database service.
- **Engines**: MySQL, PostgreSQL, SQL Server.
- **Features**: Automated backups, high availability (Regional), and read replicas.

## 3. Cloud Spanner
Google's proprietary, horizontally scalable, and globally consistent relational database.
- **Strategic Value**: Combines the benefits of SQL (JOINs, ACID) with the scalability of NoSQL.
- **TrueTime**: Uses atomic clocks and GPS to ensure global consistency without performance penalties.
- **Best For**: Financial systems, global inventories, and massive mission-critical systems.

## 4. NoSQL Databases
- **Firestore**: A scalable document database for mobile/web apps. (Part of the Firebase ecosystem).
- **Bigtable**: A high-performance NoSQL for large analytical/operational workloads. (Recommended for IoT, Fintech, and Adtech).

## 5. Storage Comparison

| Service | Type | Consistency | Scalability |
| :--- | :--- | :--- | :--- |
| **Cloud Storage** | Object | Strong | Massive |
| **Cloud SQL** | Relational | Strong | Vertical / Regional |
| **Cloud Spanner** | Relational | **Global Strong** | Horizontal / Global |
| **Bigtable** | NoSQL (Wide Column) | Eventual | Massive Horizontal |
| **Firestore** | NoSQL (Document) | Strong / Eventual | High |

## 6. Persistant Disks (PD)
Block storage for VMs.
- **Zonal PD**: Single data center.
- **Regional PD**: Synchronous replication across two zones in a region. (Best for HA without application-level replication).
- **Types**: Standard, Balanced, SSD, Extreme.

> [!IMPORTANT]
> **Spanner is Unique**: 
> In an interview, highlight that Cloud Spanner is one of the few databases in the world that can offer **99.999% availability (five nines)** and global ACID consistency at the same time.

> [!TIP]
> **Cloud SQL vs. Spanner**: 
> Use **Cloud SQL** for standard business apps that fit within a few terabytes. Use **Cloud Spanner** if you exceed the limits of a single master database or need global transactional consistency.
