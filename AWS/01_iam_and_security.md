# IAM & Security 🔐

> Identity controls and security services to protect your infrastructure.

---

## 🆔 IAM (Identity & Access Management)

IAM is a **Global** service. It does not require a region selection.

| Entity | Description | Usage |
| :--- | :--- | :--- |
| **User** | Represents a person or service (Application). | End users, CLI access, Long-term credentials. |
| **Group** | A collection of IAM users. | Assign permissions to multiple users at once (e.g., "Admins"). |
| **Role** | An identity with specific permissions, not associated with a specific person. | Temporary credentials for Services (EC2, Lambda) or Cross-account access. |
| **Policy** | Code (JSON) that defines permissions. | Can be **Identity-based** or **Resource-based** (e.g., S3 Bucket Policy). |

### IAM Best Practices
1. **Least Privilege**: Only give the permissions necessary to perform a task.
2. **Use Roles for Services**: Never store hardcoded AWS credentials on EC2 instances; use Instance Profiles (Roles).
3. **MFA**: Always enable Multi-Factor Authentication for the root user and privileged users.
4. **Rotate Keys**: Regularly rotate access keys.

---

## 🛡️ Security Services

| Service | Purpose | Key Features |
| :--- | :--- | :--- |
| **KMS** | Key Management Service. | Manage encryption keys (CMKs). Integrates with most AWS services. |
| **Secrets Manager** | Store and rotate secrets (DB passwords, API keys). | Automatic rotation, integration with RDS. |
| **ACM** | AWS Certificate Manager. | Provision and manage SSL/TLS certificates (Free for public certs). |
| **Shield** | DDoS protection. | **Standard** (Free) vs **Advanced** (Paid, includes specialized support). |
| **WAF** | Web Application Firewall. | Filter requests based on IP, Headers, or malicious patterns (SQLi, XSS). |
| **GuardDuty** | Intelligent threat detection. | Uses Machine Learning to find anomalies in CloudTrail, VPC Flow Logs, DNS logs. |
| **Inspector** | Automated security assessment for EC2/ECR. | Checks for vulnerabilities and deviations from best practices. |

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | EC2 needs to access S3 | Assign an **IAM Role** (Instance Profile) to the EC2 instance. |
| 2 | Authenticate users from an external app | Use **Cognito** (User Pools for Auth, Identity Pools for AWS access). |
| 3 | Restrict access from a specific IP | Use **Security Groups** (Stateful) or **NACLs** (Stateless) or **WAF**. |
| 4 | Automatically rotate DB passwords | Use **AWS Secrets Manager**. |
| 5 | Shared responsibility for IAM | **Customer** is responsible for creating and managing users/policies. |

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
