# Management, Monitoring & Well-Architected 🛠️

> Operations, compliance, and architectural best practices.

---

## 🔍 Monitoring vs. Auditing

| Service | Category | Purpose |
| :--- | :--- | :--- |
| **CloudWatch** | **Monitoring** | Performance: Metrics, Logs (Apps/OS), Alarms (Actions on thresholds). |
| **CloudTrail** | **Auditing** | Compliance: Records API calls (Who, What, When, Where). |
| **AWS Config** | **Inventory/Compliance** | Resource history, configuration changes, and compliance auditing. |
| **EventBridge** | **Automation** | Serverless event bus (e.g., "If EC2 stops, trigger Lambda"). |

### CloudWatch Details
- **Metrics**: Data about performance (CPU, Network In/Out).
- **Logs**: Application/OS level logs (Requires an agent for EC2).
- **Alarms**: Trigger notifications (SNS) or Auto Scaling actions.
- **Dashboards**: Visual representation of metrics across accounts.

---

## 🏗️ AWS Well-Architected Framework

The framework provides a consistent approach for customers and partners to evaluate architectures.

### 🏛️ The 6 Pillars
1. **Operational Excellence**: Running and monitoring systems to deliver business value.
2. **Security**: Protecting information, systems, and assets.
3. **Reliability**: Ensuring workload performs its intended function correctly and consistently.
4. **Performance Efficiency**: Using computing resources efficiently to meet requirements.
5. **Cost Optimization**: Eliminating unneeded costs or sub-optimal resources.
6. **Sustainability**: Minimizing the environmental impacts of running cloud workloads.

---

## 🏗️ Infrastructure as Code (IaC)

- **CloudFormation**: Declarative way to outline your infrastructure (JSON/YAML).
- **AWS CDK**: Define infrastructure using familiar programming languages (JS, TS, Python, Java, C#).
- **AWS Elastic Beanstalk**: Platform as a Service (PaaS). Deploy and scale web apps without worrying about the underlying infrastructure.

---

## 🎯 Interview Gotchas

| # | Scenario | Correct Choice / Fact |
| :--- | :--- | :--- |
| 1 | Who made an API call 2 days ago? | **CloudTrail**. |
| 2 | EC2 CPU usage is high; what service? | **CloudWatch**. |
| 3 | History of a Security Group rule change | **AWS Config**. |
| 4 | Deploy the same stack across 10 regions | **CloudFormation** (StackSets). |
| 5 | Shared responsibility for Monitoring | **AWS** manages monitoring infrastructure. **Customer** sets up alarms and logging logic. |

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
