# Storage (S3, EBS, EFS)

AWS provides diverse storage options for different data access patterns (Object, Block, and File).

## 1. Amazon S3 (Object Storage)
The most widely used AWS service for storing and retrieving any amount of data.

> [!IMPORTANT]
> **Consistency Model**: 
> As of Dec 2020, S3 offers **Strong Read-after-Write Consistency** for ALL objects. This means if you write a new object or overwrite an existing one, any subsequent read will return the updated data immediately.

### Performance Optimization
- **Prefixes**: For high-request rates (>3,500/s), use a random prefix (hash) or unique partition names to avoid "Hot Partition" issues.
- **Multipart Upload**: Use for objects > 100MB; required for objects > 5GB.
- **Transfer Acceleration**: Uses Edge Locations to speed up uploads over long distances.

### Storage Classes
- **Standard**: High availability, fast access.
- **Intelligent-Tiering**: Automatically moves data between tiers based on access patterns (Best for unpredictable data).
- **Glacier Deep Archive**: Lowest cost; retrieval time 12-48 hours.

## 2. Amazon EBS (Block Storage)
Persistent storage volumes for EC2 instances. 
- **gp3 (General Purpose SSD)**: Balanced price/performance. You can scale IOPS independently from capacity.
- **io2 (Provisioned IOPS SSD)**: Highest performance for mission-critical databases.
- **EBS Snapshots**: Point-in-time backups stored in S3. Use **Incremental Backups** to save costs.

## 3. Amazon EFS (File Storage)
Managed network file system (NFS) that can be mounted on **multiple** EC2 instances or Lambda functions simultaneously.
- **Linux only** (Use FSx for Windows).
- Scalable and highly available across AZs.
- Supports "Frequent Access" and "Infrequent Access" (IA) tiers.

## 4. Architectural Comparison

| Feature | S3 | EBS | EFS |
| :--- | :--- | :--- | :--- |
| **Type** | Object | Block | File (NFS) |
| **Connectivity** | Public API (HTTP) | Single EC2 (Private) | Multiple EC2/Lambda (Private) |
| **Scalability** | Unlimited | Max 16TiB per vol | Unlimited (Pay as you use) |
| **Best For** | Static assets, Data Lakes | OS Drive, Databases | Shared media, Home dirs |

## 5. Storage Scenarios

### Scenario A: Shared configuration files across 100 EC2 instances
**Solution**: **EFS**. Unlike EBS, EFS can be attached to many instances at once.

### Scenario B: Ultra-low latency database storage
**Solution**: **Instance Store** (if data can be lost on stop) or **EBS (io2)** (if persistence is required).

### Scenario C: Long-term archival for compliance (7 years)
**Solution**: **S3 Glacier Deep Archive**. Use **Lifecycle Policies** to automate the move from Standard to Glacier.

> [!TIP]
> **S3 Object Lock**: 
> Prevents an object from being deleted or overwritten for a fixed amount of time. Excellent for compliance and protecting against ransomware.

> [!WARNING]
> **EBS vs AZ**: 
> EBS volumes are tied to a specific **Availability Zone**. To move an EBS volume to another AZ, you must take a snapshot and recreate it in the target AZ.
