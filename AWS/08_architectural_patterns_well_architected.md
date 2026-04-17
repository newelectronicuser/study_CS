# 08 Architectural Patterns & Well-Architected Framework

## Core Concepts

### The 6 Pillars of the Well-Architected Framework
1.  **Operational Excellence:** Automated changes, responding to events, and defining standards.
2.  **Security:** Protecting data, systems, and assets. (IAM, Encryption, Logging).
3.  **Reliability:** Ability to recover from outages (Multi-AZ, ASG, Backups).
4.  **Performance Efficiency:** Efficiently using resources (Serverless, Right-sizing).
5.  **Cost Optimization:** Avoiding unnecessary costs (Spot, Savings Plans).
6.  **Sustainability:** Minimizing the environmental impact of cloud workloads.

### Disaster Recovery (DR) Strategies
*   **Backup & Restore:** RPO=High, RTO=High. Cheapest, data simply restored from S3/Snapshots.
*   **Pilot Light:** RPO=Minutes, RTO=Minutes. Database kept live; compute is launched only during failure.
*   **Warm Standby:** RPO=Seconds, RTO=Minutes. A scaled-down version of the system is always running.
*   **Multi-site (Active-Active):** RPO=0, RTO=0. Most expensive, everything live in two or more regions.

---

## Interview Deep Dives

### 1. "How do you design for High Availability (99.99%)?"
**Answer:**
*   **Redundancy at every layer:**
    *   **DNS:** Route 53 with Health Checks.
    *   **Compute:** EC2 in Auto Scaling Groups across multiple AZs.
    *   **Data:** RDS Multi-AZ or Aurora (6-way replication).
    *   **Storage:** S3 (multi-AZ by default).
*   **Loose Coupling:** Using SQS to handle spikes and prevent cascading failures.
*   **Self-healing:** CloudWatch Alarms to restart instances or trigger Lambda scripts.

### 2. "Explain RPO vs. RTO."
**Answer:**
*   **RPO (Recovery Point Objective):** How much data can you afford to lose? (Time since last backup).
*   **RTO (Recovery Time Objective):** How long can your system be down? (Time to restore service).
*   **Trade-off:** Lower RPO/RTO requires more automation and redundant hardware, significantly increasing costs.

### 3. "Vertical vs. Horizontal Scaling: Which is better?"
**Answer:**
*   **Vertical (Scaling Up):** Increasing CPU/RAM of a single instance. Easiest but has a hard ceiling and no redundancy.
*   **Horizontal (Scaling Out):** Adding more instances. Unlimited scale and built-in redundancy, but requires a load balancer and stateless application design.

---

## Senior Level Trade-offs: Caching Strategies

| Strategy | Best For | Trade-off |
| :--- | :--- | :--- |
| **Write-through** | Data consistency | Write latency (must write to cache and DB). |
| **Lazy Loading (Cache Aside)** | High read volumes | Cache misses; stale data if not handled properly. |
| **TTL (Time to Live)** | Volatile data | Risk of stale data; must balance freshness vs. performance. |

---

## Common Interview Scenarios

### Scenario: "A monolith application is failing due to memory leaks. How do you move it to AWS securely?"
**Troubleshooting/Design:**
1.  **Containerize:** Move it to **ECS on Fargate**. This limits the impact of memory leaks to a single task which can be restarted automatically.
2.  **Horizontal Scaling:** Use **Auto Scaling Groups** based on memory triggers.
3.  **Refactor (Long term):** Break the monolith into microservices using **Lambda** and **API Gateway** for the stateless parts.

### Scenario: "You need to migrate 50TB of data from an on-premise data center to AWS in a week with slow internet."
**Key Details:**
*   **AWS Snowball Edge:** Request a physical device to transfer data.
*   **Why:** Even a 1Gbps link would take ~5 days for 50TB under perfect conditions. Snowball bypasses internet bandwidth issues.
*   **Post-migration:** Use **DataSync** to keep the on-prem and cloud data synchronized during the transition.
