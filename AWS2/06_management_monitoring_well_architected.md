# Management and Monitoring

Observability and governance are essential for maintaining large-scale cloud environments.

## 1. Amazon CloudWatch
The primary monitoring service for AWS resources and applications.

| Component | Usage |
| :--- | :--- |
| **Metrics** | Performance data (CPU, Mem, Disk, etc.). Retention: 15 months. |
| **Logs** | Centralized application and system logs. Uses **Log Groups** and **Log Streams**. |
| **Alarms** | Triggers actions based on metric thresholds (e.g., scale out if CPU > 70%). |
| **Dashboards** | Visualizing metrics and alarms in a single view. |

## 2. Amazon CloudTrail
Records every API call made in your AWS account (Who, When, What, Where).

> [!IMPORTANT]
> **Monitoring vs. Auditing**: 
> - **CloudWatch** is for **Performance and Health** (Is the app running fast enough?).
> - **CloudTrail** is for **Governance and Security** (Who deleted that S3 bucket?).

## 3. Amazon EventBridge (formerly CloudWatch Events)
A serverless event bus that makes it easy to connect applications using data from your own apps, integrated SaaS apps, and AWS services.
- **Rule-based routing**: Trigger a Lambda if an EC2 instance state changes to "Stopped."
- **Scheduled Events**: Run a task every hour (Cron-like behavior).

## 4. Resource Governance
- **AWS Config**: Records and evaluates the configuration of your AWS resources. Use it to ensure compliance (e.g., "Alert me if any S3 bucket becomes public").
- **AWS Systems Manager (SSM)**: Centralized management for EC2 (Patching, Parameter Store, Session Manager).
- **Service Quotas**: Manage and request increases for your AWS account limits.

## 5. Well-Architected Framework: Senior Perspective
In a senior interview, don't just list the pillars. Discuss the **Trade-offs**.

### The Cost-Reliability Trade-off
A system that is 99.999% available (multi-region, active-active) is significantly more expensive than a system that is 99.9% available (single region, Multi-AZ). As an architect, you must decide if the business value justifies the cost.

### The Performance-Cost Trade-off
Using Provisioned IOPS (io2) for every volume provides the best performance but is wastefully expensive for non-critical workloads. Use gp3 and monitor burst balance instead.

> [!TIP]
> **Parameter Store vs. AppConfig**: 
> - **Parameter Store**: Good for simple configuration and secrets.
> - **AppConfig**: Good for dynamic configuration that requires validation and gradual rollout (like feature flags).

> [!WARNING]
> **Default Metrics**: 
> Basic monitoring (5-minute intervals) is free. **Detailed Monitoring** (1-minute intervals) is a paid feature. CloudWatch does NOT collect Memory usage or Disk space by default; you must install the **CloudWatch Agent** on your instances for these.
