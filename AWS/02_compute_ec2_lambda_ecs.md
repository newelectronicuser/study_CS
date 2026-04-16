# Compute: EC2, Lambda & Containers 🚀

> Choose the right compute power for your workload.

---

## 💻 EC2 (Elastic Compute Cloud)

EC2 provides resizable compute capacity in the cloud.

### 💰 Pricing Options (Essential for Exam/Interview)
| Option | Best For | Discount |
| :--- | :--- | :--- |
| **On-Demand** | Short-term, unpredictable workloads. Low cost/flexibility. | None |
| **Reserved Instances (RI)** | Steady-state, predictable usage (1-3 years). | Up to 75% |
| **Savings Plans** | Flexible usage across EC2, Lambda, Fargate. | Similar to RI |
| **Spot Instances** | Fault-tolerant, interruptible tasks (e.g., Batch jobs). | Up to 90% |
| **Dedicated Hosts** | Compliance or custom license requirements (BYOL). | Most Expensive |

### 🛠️ Instance Types (Summary)
- **T/M**: General Purpose (Balanced).
- **C**: Compute Optimized (High-performance CPUs).
- **R/X**: Memory Optimized (Large datasets in memory).
- **I/D/H**: Storage Optimized (High-speed local storage).
- **G/P**: Accelerated Computing (GPU/Machine Learning).

---

## ⚡ Lambda (Serverless)

Run code without provisioning or managing servers. You pay only for the compute time you consume.

- **Limit**: Execution time up to **15 minutes**.
- **Cold Start**: Delay when a new instance is initialized.
- **Triggers**: S3, DynamoDB, API Gateway, EventBridge, etc.
- **Scaling**: AWS handles scaling automatically (concurrency).

---

## 📦 Containers (ECS & EKS)

| Service | Description | Type |
| :--- | :--- | :--- |
| **ECS** | Amazon's own container orchestration service. | Managed |
| **EKS** | Managed Kubernetes service. | Managed |
| **Fargate** | Serverless engine for ECS and EKS. | Serverless |
| **ECR** | Elastic Container Registry (Docker Repo). | Managed |

---

## ⚖️ Scalability: ELB & ASG

### Elastic Load Balancer (ELB)
- **ALB (Application)**: Layer 7 (HTTP/HTTPS). Routing based on paths (`/api`) or hostnames.
- **NLB (Network)**: Layer 4 (TCP/UDP). Ultra-high performance, static IPs.
- **GLB (Gateway)**: Deploy and manage 3rd party virtual appliances (Firewalls).

### Auto Scaling Group (ASG)
- **Desired/Min/Max Capacity**: Defines the fleet size.
- **Scaling Policies**: 
  - **Dynamic**: Based on metrics (e.g., CPU > 70%).
  - **Predictive**: Uses ML to forecast demand.
  - **Scheduled**: Based on time (e.g., Friday high traffic).

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | Run a simple script once a day | **AWS Lambda** (Scheduled via EventBridge). |
| 2 | High-performance DB on EC2 | Use **Storage Optimized** (I-type) or **EBS Provisioned IOPS**. |
| 3 | Need a static IP for a load balancer | Use **Network Load Balancer (NLB)**. |
| 4 | Microservices architecture | Use **ALB** with **ECS** or **EKS**. |
| 5 | Shared responsibility for Lambda | **AWS** manages OS/Runtime. **Customer** manages Code/IAM. |

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
