# AWS & GCP — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Cloud Fundamentals

**Q1. What is the difference between IaaS, PaaS, and SaaS?**

**A:**
- **IaaS (Infrastructure as a Service)**: Raw compute, networking, and storage. You manage the OS and above. Example: EC2, GCP Compute Engine.
- **PaaS (Platform as a Service)**: Managed platform for deploying applications. Provider manages OS, runtime, scaling. Example: AWS Elastic Beanstalk, GCP App Engine, Cloud Run.
- **SaaS (Software as a Service)**: Complete software delivered over the internet. No infrastructure management. Example: Gmail, Salesforce.

---

**Q2. What is the shared responsibility model?**

**A:** Cloud security responsibilities are divided:
- **Provider is responsible for**: Security **of** the cloud — physical hardware, global network, hypervisor, managed service security.
- **Customer is responsible for**: Security **in** the cloud — OS patching, IAM configuration, data encryption, application code, network configuration, compliance.

Example: AWS manages the RDS database engine patches; you manage who has access to the database and whether encryption is enabled.

---

**Q3. What is a VPC and what components make it up?**

**A:** A Virtual Private Cloud is a logically isolated network in the cloud. Components:
- **Subnets**: IP ranges within the VPC (public or private)
- **Route Tables**: Control traffic routing
- **Internet Gateway**: Connects public subnets to the internet
- **NAT Gateway**: Lets private subnets initiate outbound internet traffic
- **Security Groups**: Stateful firewall at the instance level
- **Network ACLs**: Stateless firewall at the subnet level

---

**Q4. What is the difference between a Security Group and a Network ACL?**

**A:**
| | Security Group | Network ACL |
|---|---|---|
| Level | Instance/ENI | Subnet |
| Stateful? | ✅ Yes (return traffic auto-allowed) | ❌ No (must explicitly allow both directions) |
| Rules | Allow only | Allow and Deny |
| Evaluation | All rules evaluated | Rules evaluated in number order; first match wins |

---

**Q5. What are the key IAM concepts?**

**A:**
- **User**: An individual identity with long-term credentials.
- **Group**: A collection of users sharing the same permissions.
- **Role**: An identity with temporary credentials assumed by services, EC2 instances, Lambda, or federated users. No long-term credentials.
- **Policy**: A JSON document defining permissions (`Allow`/`Deny` on `Actions` for `Resources`).
- **Service Account (GCP)**: The GCP equivalent of an IAM Role — an identity for applications rather than humans.

---

## Compute

**Q6. What are the EC2 instance purchasing options?**

**A:**
- **On-Demand**: Pay per second/hour. No commitment. Most expensive. Best for unpredictable workloads.
- **Reserved Instances**: 1 or 3-year commitment. Up to 72% discount. Best for steady-state workloads.
- **Savings Plans**: Like Reserved but more flexible (can change instance type/region). Commit to $/hour spend.
- **Spot Instances**: Unused EC2 capacity at up to 90% discount. Can be interrupted with 2-minute warning. Best for fault-tolerant batch workloads.

---

**Q7. What is AWS Lambda and what are its limitations?**

**A:** Lambda is a serverless compute service — run code without provisioning servers. You pay per invocation and duration. Limitations:
- **Timeout**: Max 15 minutes per invocation
- **Memory**: Up to 10 GB
- **Ephemeral storage**: 512 MB–10 GB `/tmp`
- **Cold starts**: First invocation after inactivity has latency overhead
- **Concurrency limits**: Soft limit of 1000 concurrent executions per region (can be raised)
- Not suited for long-running tasks or stateful workloads

---

**Q8. What is Google Cloud Run and how does it differ from Cloud Functions?**

**A:** Both are serverless but:
- **Cloud Run**: Runs any containerized application. More control (any language, runtime, port). Scales to zero. Suited for APIs and microservices.
- **Cloud Functions**: Event-driven, function-level. Simpler but less flexible. Specific runtimes only (Node, Python, Go, Java, etc.).

Cloud Run is preferred for custom containers or complex dependencies; Cloud Functions for simple event handlers.

---

## Storage

**Q9. What is the difference between S3, EBS, and EFS?**

**A:**
| | S3 | EBS | EFS |
|---|---|---|---|
| Type | Object storage | Block storage (volume) | File system (NFS) |
| Access | Via HTTP/API | Mounted to one EC2 instance | Mounted to many EC2 instances simultaneously |
| Persistence | Independent | Tied to AZ | Multi-AZ |
| Use case | Backups, static assets, data lake | Boot volumes, databases | Shared file access, CMS |

---

**Q10. What are S3 storage classes?**

**A:**
- **S3 Standard**: Frequent access. High availability.
- **S3 Intelligent-Tiering**: Auto-moves objects between tiers based on access patterns.
- **S3 Standard-IA**: Infrequent access. Lower cost, retrieval fee.
- **S3 One Zone-IA**: Single AZ. Cheaper, lower resilience.
- **S3 Glacier Instant Retrieval**: Archive with ms retrieval.
- **S3 Glacier Flexible Retrieval**: Archive, minutes to hours retrieval.
- **S3 Glacier Deep Archive**: Lowest cost, 12-hour retrieval.

---

## Databases

**Q11. What is Amazon Aurora and how does it differ from standard RDS?**

**A:** Aurora is a MySQL/PostgreSQL-compatible database rebuilt for the cloud. Key differences:
- Storage auto-scales up to 128 TB; replicated 6 ways across 3 AZs automatically.
- Up to 15 low-latency read replicas.
- Up to 5× faster than MySQL, 3× faster than PostgreSQL (AWS claim).
- Aurora Serverless: capacity scales automatically; good for variable workloads.
- Higher cost than standard RDS but provides more managed HA.

---

**Q12. What is DynamoDB and what are its key design considerations?**

**A:** DynamoDB is a fully managed serverless NoSQL key-value and document database. Key considerations:
- **Primary key**: Partition key (hash key) + optional sort key (range key). Choose a high-cardinality partition key to distribute data evenly.
- **Hot partitions**: Monotonically increasing keys (timestamps) cause hot partitions — use random suffixes or UUID.
- **Capacity modes**: On-demand (pay per request) or provisioned (set RCU/WCU — can use auto-scaling).
- **Global Secondary Indexes**: Support alternate query patterns.
- **Single-table design**: Combine multiple entity types in one table for efficiency.

---

**Q13. What is Cloud Spanner?**

**A:** Cloud Spanner is Google's globally distributed, strongly consistent relational database. Unique because it provides:
- Horizontal scaling across regions (virtually unlimited capacity)
- Full SQL support with ACID transactions globally
- External consistency (true global linearizability) using TrueTime (atomic clocks + GPS)

Suited for global, high-throughput financial systems, gaming leaderboards, or any use case requiring global consistency with SQL.

---

## Networking

**Q14. What is an Application Load Balancer vs a Network Load Balancer in AWS?**

**A:**
- **ALB (Layer 7)**: HTTP/HTTPS aware. Routes based on path, host, headers, query strings. Supports WebSockets, gRPC, authentication. Best for web applications and microservices.
- **NLB (Layer 4)**: TCP/UDP traffic. Extremely low latency and high throughput. Preserves client IP. Best for gaming, IoT, real-time streaming, or when ALB latency is unacceptable.

---

**Q15. What routing policies does AWS Route 53 support?**

**A:**
- **Simple**: Single resource, no health checks.
- **Weighted**: Split traffic by percentage (canary, A/B testing).
- **Latency-based**: Route to the lowest-latency region.
- **Failover**: Primary/secondary, health-check-based.
- **Geolocation**: Route by user's geographic location.
- **Geoproximity**: Route based on geographic distance (with bias).
- **Multi-value answer**: Return multiple healthy records.

---

## Messaging

**Q16. What is the difference between SQS Standard and FIFO queues?**

**A:**
| | Standard | FIFO |
|---|---|---|
| Delivery | At-least-once (duplicates possible) | Exactly-once processing |
| Order | Best-effort | Strictly ordered |
| Throughput | Unlimited | 300 msg/s (3000 with batching) |
| Use case | High throughput, order doesn't matter | Financial transactions, ordered workflows |

---

**Q17. How does SNS fan-out with SQS work?**

**A:** A single SNS topic publishes a message. Multiple SQS queues are subscribed to that topic. When a message is published to SNS, it is automatically delivered to all subscribed queues simultaneously. Each downstream service (consumer) reads from its own SQS queue independently. Enables decoupled, scalable event distribution without direct service-to-service coupling.

---

## Monitoring

**Q18. What is AWS CloudWatch?**

**A:** CloudWatch is AWS's monitoring and observability service. It collects:
- **Metrics**: CPU, memory, request counts, latency from AWS services and custom code.
- **Logs**: Log ingestion and querying (CloudWatch Logs Insights).
- **Alarms**: Threshold-based alerts that can trigger Auto Scaling, SNS notifications, or Lambda.
- **Dashboards**: Visualization of metrics.
- **Container Insights / Application Insights**: Enhanced monitoring for EKS/ECS.

---

## Cost Optimization

**Q19. How do you optimize cloud costs without sacrificing reliability?**

**A:**
1. **Right-size instances**: Use CloudWatch/Recommender to find underutilized resources.
2. **Reserved Instances / Committed Use**: For predictable workloads.
3. **Spot/Preemptible for batch**: Use interruption-tolerant architecture.
4. **S3 Intelligent-Tiering**: Auto-move cold objects to cheaper storage.
5. **Delete unused resources**: Unattached EBS volumes, idle load balancers, old snapshots.
6. **Autoscaling**: Scale down in off-hours.
7. **Turn off dev/test environments** on weekends (Cloud Scheduler / EventBridge).
8. **Set budget alerts** to catch unexpected spend early.

---

**Q20. What is GKE Autopilot vs Standard mode?**

**A:**
- **Standard**: You manage node pools (instance types, sizes, count). More control, more ops work.
- **Autopilot**: GKE manages nodes entirely. You only define Pods. Pay per Pod resource requests (not per node). Enforces security best practices (no privileged containers, etc.). Simpler operations; may be more expensive for dense workloads with custom node requirements.

---
