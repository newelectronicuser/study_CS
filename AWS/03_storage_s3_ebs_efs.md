# Storage: S3, EBS & EFS 💾

> Choose the right storage type for your performance and durability needs.

---

## 🌎 S3 (Simple Storage Service) - Object Storage

S3 stores data as **objects** within **buckets**. It is highly durable (99.911% durability).

### 🏷️ Storage Classes
| Class | Durability | Availability | Best For |
| :--- | :---: | :---: | :--- |
| **Standard** | 11 nines | 99.99% | General purpose, frequent access. |
| **Intelligent-Tiering** | 11 nines | 99.9% | Unknown or changing access patterns. |
| **Standard-IA** | 11 nines | 99.9% | Infrequent access but needs rapid access. |
| **One Zone-IA** | 11 nines | 99.5% | Lower cost, non-critical data (stored in 1 AZ). |
| **Glacier Instant** | 11 nines | 99.9% | Retrieval in milliseconds, archive. |
| **Glacier Deep Archive** | 11 nines | 99.99% | Cheapest. 12-hour retrieval. |

### 🔑 Key Features
- **Versioning**: Protect against accidental deletion.
- **Lifecycle Policies**: Move objects automatically between classes or delete them.
- **Replication**: CRR (Cross-Region) or SRR (Same-Region).
- **Security**: Public Access Block, Bucket Policies, ACLs, Encryption (SSE-S3, SSE-KMS, SSE-C).

---

## 🏗️ EBS (Elastic Block Store)

Network-attached storage for **individual** EC2 instances.

- **Types**:
  - **gp2/gp3**: SSD, general purpose.
  - **io1/io2**: Provisioned IOPS (Highest performance).
  - **st1**: Throughput Optimized HDD (Big Data, Log processing).
  - **sc1**: Cold HDD (Infrequent access).
- **Snapshots**: Point-in-time backups stored in S3.
- **Multi-Attach**: Attach `io1/io2` to multiple instances in the *same* AZ.

---

## 📂 EFS (Elastic File System)

Managed network file system (NFS) that can be shared across **hundreds** of EC2 instances.

- **Compatibility**: Linux only.
- **Scaling**: Scales automatically to petabytes.
- **Availability**: Regional service (Accessible across AZs).

---

## ⚖️ Storage Comparison

| Feature | S3 | EBS | EFS |
| :--- | :--- | :--- | :--- |
| **Type** | Object | Block | File (POSIX) |
| **Speed** | High Latency (ms) | Low Latency (us) | Moderate Latency |
| **Scaling** | Unlimited | Capacity fixed in advance | Scales automatically |
| **Sharing** | Web-accessible URLs | Only 1 instance (or multi-attach) | Hundreds of instances |
| **Scenario** | Static assets, backups | OS drive, DB storage | Shared home dirs, WordPress |

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | Hosting a static website | **S3** (Zero server management). |
| 2 | High-performance DB on EC2 | **EBS** (Provisioned IOPS). |
| 3 | Shared storage across 50 EC2s | **EFS**. |
| 4 | Data retrieval takes 12 hours | **Glacier Deep Archive**. |
| 5 | Accidental deletion protection | **S3 Versioning** + **MFA Delete**. |

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
