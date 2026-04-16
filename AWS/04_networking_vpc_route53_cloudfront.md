# Networking: VPC, Route 53 & CloudFront 🌐

> The plumbing of AWS: How resources communicate and how users reach them.

---

## ☁️ VPC (Virtual Private Cloud)

A private, isolated section of the AWS Cloud where you can launch resources.

### 🧩 Core Components
| Component | Description |
| :--- | :--- |
| **Subnet** | A range of IP addresses in your VPC. (Public vs Private). |
| **IGW** | Internet Gateway. Enables communication between the VPC and the Internet. |
| **NAT Gateway** | Managed service for Private subnets to access the Internet (IPv4). One-way traffic only. |
| **VPC Peering** | Connect two VPCs together privately using AWS network. |
| **VPC Endpoints** | Access AWS services (S3, DynamoDB) without leaving the private network. |

### 🛡️ Security: Security Groups vs NACLs
| Feature | Security Group (SG) | Network ACL (NACL) |
| :--- | :--- | :--- |
| **Level** | Instance Level | Subnet Level |
| **Stateful?** | ✅ Yes (Response traffic automatic) | ❌ No (Need to allow outbound explicitly) |
| **Rules** | Allow rules only | Allow and Deny rules |
| **Evaluation** | All rules evaluated | Rules evaluated in order (numbered) |

---

## 🚦 Route 53 (DNS)

A highly available and scalable Cloud DNS web service.

### 🗺️ Routing Policies
1. **Simple**: One resource (e.g., an IP address).
2. **Weighted**: Send % of traffic to different resources (Can be used for A/B testing).
3. **Latency**: Route based on the lowest latency for the user.
4. **Failover**: Active-Passive for Disaster Recovery.
5. **Geolocation**: Route based on the user's location.
6. **Multi-value Answer**: Up to 8 healthy records returned randomly.

---

## ⚡ CloudFront (CDN)

Speeds up distribution of your static and dynamic web content to end users via **Edge Locations**.

- **Origin**: S3 bucket, HTTP server, or Load Balancer.
- **Cache**: Cached at edge locations based on TTL (Time-To-Live).
- **Security**: 
  - **OAI / OAC**: Ensure users can only access S3 content via CloudFront.
  - Integration with **WAF** and **Shield**.
  - Signed URLs / Signed Cookies for private content.

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | Private subnet EC2 needs to download updates | Add a **NAT Gateway** in a Public Subnet. |
| 2 | Connect two VPCs in different accounts | Use **VPC Peering** or **Transit Gateway**. |
| 3 | Static IP for a load balancer | Use **Network Load Balancer (NLB)**. |
| 4 | Block a specific malicious IP | Use **Network ACL (NACL)** (SG only allows "Allow" rules). |
| 5 | Shared responsibility for VPC | **AWS** manages physical network. **Customer** manages subnets, route tables, and security. |

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
