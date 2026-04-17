# 02 Compute: EC2, Lambda, ECS, EKS

## Core Concepts

### EC2 (Elastic Compute Cloud)
Virtual machines in the cloud.
*   **Instance Types:** Optimized for Compute (C), Memory (R), Storage (I/D), or General Purpose (M/T).
*   **Purchasing Options:**
    *   **On-Demand:** Pay by the second. Best for short-term, unpredictable workloads.
    *   **Savings Plans / Reserved Instances (RI):** Up to 72% discount for 1/3 year commitment.
    *   **Spot Instances:** Up to 90% discount. AWS can reclaim with a 2-minute warning. Best for stateless, fault-tolerant batch jobs.

### Lambda (Serverless)
Event-driven, short-lived functions.
*   **Properties:** No servers to manage, sub-second scaling, pay-per-request + duration.
*   **Limits:** 15-minute timeout, 10GB RAM max, 1000 concurrent executions (soft limit).

### Containers (ECS vs. EKS)
*   **ECS (Elastic Container Service):** AWS-native container orchestrator. Simpler, integrates deeply with AWS.
*   **EKS (Elastic Kubernetes Service):** Managed Kubernetes. Best for multi-cloud strategies or high-complexity K8s requirements.
*   **Fargate:** Serverless compute engine for containers. You don't manage the underlying EC2 instances.

---

## Interview Deep Dives

### 1. "How do you handle Lambda Cold Starts?"
**Answer:**
*   **What it is:** The latency incurred when AWS initializes a new execution environment.
*   **Mitigation:**
    1.  **Provisioned Concurrency:** Keeps environments "warm" and ready (costs more).
    2.  **Optimize Code:** Minimize package size, use lighter languages (Go/Python > Java/C#), and move initialization code outside the handler.
    3.  **Choose right RAM:** Higher memory also grants more CPU, potentially reducing init time.

### 2. "When would you prefer ECS over EKS for a production application?"
**Answer:**
*   **Scale of Operations:** If the team is small and doesn't want to manage Kubernetes complexity (control plane, namespaces, RBAC), ECS is the better choice.
*   **Integration:** ECS integrates seamlessly with ALB, IAM roles for tasks, and CloudWatch without extra configuration.
*   **Cost:** ECS control plane is free; EKS has a per-cluster hourly charge.

### 3. "Explain EC2 Placement Groups."
**Answer:**
*   **Cluster:** Instances close together in one AZ (Low latency/High throughput).
*   **Partition:** Instances distributed across logical partitions (reduces correlated failures; good for Hadoop/Cassandra).
*   **Spread:** Each instance on distinct hardware (maximum reliability for a small number of critical instances).

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Lambda** | Spiky traffic, Microservices | Cold starts, 15m timeout, limited control over environment. |
| **Fargate** | Standard APIs, steady load | Slower scaling than Lambda, higher minimum cost. |
| **EC2** | High-perf computing, Legacy apps | High management overhead (patching, scaling setup). |

---

## Common Interview Scenarios

### Scenario: "You have a batch processing job that takes 30 minutes. Which compute service do you choose?"
**Troubleshooting/Design:**
1.  **Not Lambda:** It has a 15-minute timeout.
2.  **EC2 Spot:** If the job is stateless and can be resumed, Spot is the most cost-effective.
3.  **Fargate/ECS:** If you want containerized isolation and don't want to manage the underlying server patching.

### Scenario: "A Lambda function needs to access a private RDS instance in a VPC."
**Key Details:**
*   Assign the Lambda to the **Private Subnets** of the VPC.
*   Ensure the **Security Group** of RDS allows ingress from the Lambda's Security Group.
*   **Warning:** Attaching a Lambda to a VPC used to cause long cold starts (ENI attachment), but "AWS Hyperplane" has mostly fixed this. Still, ensure the VPC has enough IP addresses in the subnets.
