# AWS Global Infrastructure & Shared Responsibility ☁️

> The foundation of AWS: How it's built and who is responsible for what.

---

## 🏗️ Global Infrastructure

| Component | Definition | Notes |
| :--- | :--- | :--- |
| **Regions** | Physical locations around the world where AWS clusters data centers. | Each Region is independent and isolated. |
| **Availability Zones (AZs)** | One or more discrete data centers with redundant power, networking, and connectivity in an AWS Region. | All AZs in a Region are interconnected with high-bandwidth, low-latency networking. |
| **Edge Locations** | Content Delivery Network (CDN) endpoints for CloudFront. | Used to cache content for lower latency to end users. |
| **Local Zones** | Extension of an AWS Region to place compute, storage, and database services closer to end users. | Useful for sub-millisecond latency requirements. |

---

## 🤝 Shared Responsibility Model

A critical interview topic. AWS is responsible for security **OF** the cloud, and the customer is responsible for security **IN** the cloud.

### AWS Responsibility (Security OF the Cloud)
- **Physical security** of data centers.
- **Hardware and software infrastructure** (Virtualization layer).
- **Network infrastructure** (Edge locations, Regions, AZs).
- **Managed Services** (e.g., S3, RDS, Lambda patches at the OS level).

### Customer Responsibility (Security IN the Cloud)
- **Data Encryption** (Client-side and Server-side).
- **Network Security** (Security Groups, ACLs).
- **IAM** (Identity and Access Management).
- **Operating System** (For Unmanaged services like EC2).
- **Application security** and code.

---

## 🏢 AWS Organizations & Billing

- **Consolidated Billing**: One bill for all accounts, volume discounts.
- **Service Control Policies (SCPs)**: Restrict what services/actions can be used across accounts (even by the root user).
- **Tagging**: Essential for cost allocation and automation.

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | High Availability (HA) | Deploy across multiple **Availability Zones**. |
| 2 | Disaster Recovery (DR) | Deploy across multiple **Regions**. |
| 3 | Least Latency for Users | Use **CloudFront** (Edge Locations) or **Local Zones**. |
| 4 | Who patches the EC2 OS? | **The Customer**. |
| 5 | Who patches the RDS OS? | **AWS**. |
| 6 | Root User best practice | Don't use it! Create an IAM user and enable MFA immediately. |

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
