# AWS & GCP — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Cloud Fundamentals

**Q1. What is the difference between IaaS, PaaS, and SaaS? Give examples from AWS and GCP.**

**A:**
*   **IaaS (Infrastructure as a Service):** Provides raw compute, storage, and networking resources. You manage the operating system, middleware, and application runtime.
    *   *AWS Example:* Amazon EC2 (Elastic Compute Cloud).
    *   *GCP Example:* Google Compute Engine.
*   **PaaS (Platform as a Service):** Provides a managed environment where the cloud provider manages the OS, runtime, scaling, and hardware. You only manage application code and data.
    *   *AWS Example:* AWS Elastic Beanstalk, AWS App Runner.
    *   *GCP Example:* Google App Engine, Google Cloud Run.
*   **SaaS (Software as a Service):** Complete, fully managed end-user software applications delivered over the web.
    *   *AWS Example:* Amazon WorkMail, Amazon Chime.
    *   *GCP Example:* Google Workspace (Gmail, Drive).

---

**Q2. What is a region and an availability zone (AZ) in AWS? What is a zone in GCP?**

**A:**
*   **Region:** A geographic area containing multiple physical data centers. Regions are isolated from each other to prevent cascading failures.
*   **Availability Zone (AZ) / Zone:** An isolated location within a region. Each AZ (AWS) or Zone (GCP) has redundant power, networking, and cooling. They are physically separate but connected to other zones in the same region via low-latency fiber-optic networks. Deploying across multiple zones ensures high availability and fault tolerance.

---

**Q3. What is the shared responsibility model in cloud security?**

**A:** It defines security responsibilities split between the provider and customer:
*   **Security OF the Cloud (Provider's Responsibility):** Protecting the global infrastructure (hardware, software, networking, facilities) that runs all cloud services.
*   **Security IN the Cloud (Customer's Responsibility):** Securing everything deployed inside the cloud (OS patching on VMs, configuring IAM users/roles, network access lists/firewalls, encrypting client/server data, protecting application logic/code).

---

**Q4. What is a VPC (Virtual Private Cloud)? What components make it up?**

**A:** A VPC is a logically isolated virtual network dedicated to your cloud account. Its main components are:
*   **Subnets:** Segments of a VPC's IP address range where resources (like VMs) are placed.
*   **Route Tables:** Set of rules (routes) determining where network traffic from subnets is directed.
*   **Internet Gateway (IGW):** Connects a VPC to the internet.
*   **NAT Gateway:** Allows resources in private subnets to send outbound traffic to the internet while blocking incoming traffic.
*   **Security Groups:** Stateful virtual firewalls regulating instance-level ingress/egress.
*   **Network Access Control Lists (NACLs):** Stateless, subnet-level security firewalls.

---

**Q5. What is the difference between a public subnet and a private subnet?**

**A:**
*   **Public Subnet:** A subnet whose route table has a direct route to an Internet Gateway (`0.0.0.0/0` targeting the IGW). Resources here (e.g., Load Balancers, Bastion hosts) get public IPs and are directly reachable from the internet.
*   **Private Subnet:** A subnet whose route table does *not* have a route to an Internet Gateway. Resources here only have private IPs. To reach the internet (e.g., for system updates), they must route outbound traffic through a NAT Gateway located in a public subnet.

---

**Q6. What is a NAT Gateway and why is it used in a private subnet?**

**A:** A Network Address Translation (NAT) Gateway allows resources in a private subnet to securely connect outbound to the internet (e.g., for software patches or API integrations) while preventing external hosts from initiating direct inbound connections with those private resources. It is deployed inside a public subnet with an Elastic IP and translates private source IPs to its public IP.

---

**Q7. What is a security group vs a network ACL in AWS?**

**A:**
*   **Security Group:**
    *   Acts at the **instance level** (ENI).
    *   **Stateful:** If you allow inbound traffic, outbound response traffic is automatically allowed.
    *   Supports **allow rules only** (implicit deny).
    *   Evaluates all rules before deciding to allow traffic.
*   **Network ACL (NACL):**
    *   Acts at the **subnet boundary**.
    *   **Stateless:** Outbound responses must be explicitly allowed via outbound rules.
    *   Supports **both allow and deny rules**.
    *   Evaluates rules in numerical order (first match wins).

---

**Q8. What is Cloud IAM and what are the key concepts (users, groups, roles, policies)?**

**A:** Identity and Access Management (IAM) manages secure access to cloud resources.
*   **User:** An identity with long-term credentials representing a person or service interaction.
*   **Group:** A collection of IAM users. Assigning permissions to a group applies them to all member users.
*   **Role:** An identity with temporary security credentials that can be assumed by users, applications, or services (like EC2 or GKE) that need access to specific resources.
*   **Policy:** A JSON document outlining permissions (Allows/Denies on specific API Actions for specific cloud Resources).

---

**Q9. What is the principle of least privilege in IAM?**

**A:** It is the practice of limiting user/service access permissions to the absolute minimum necessary to perform their required tasks. By granting only the specific permissions needed (e.g., read-only access to a single S3 bucket instead of administrator access to the entire account), you significantly reduce the blast radius in the event of a security breach or accidental configuration change.

---

**Q10. What is a service account in GCP and how does it differ from an IAM user in AWS?**

**A:**
*   **GCP Service Account:** An identity used by applications, VMs, or services rather than individual human users. It uses private/public key pairs for authentication and is represented by an email address.
*   **AWS IAM User:** Typically represents a human operator with long-term console credentials or access keys. For application authentication, AWS prefers using **IAM Roles** (which issue temporary credentials via STS), while GCP utilizes Service Accounts natively, mapping them to instances or service identities.

---

## Compute

**Q11. What is EC2 in AWS? What is Compute Engine in GCP?**

**A:** They are the respective IaaS virtual machine services. They allow users to provision virtual servers on demand with customizable CPU, memory, storage (SSD/HDD), network configurations, and operating systems (Linux, Windows). They support scaling, custom images (AMIs in AWS, Machine Images in GCP), and flexible pricing models.

---

**Q12. What are the EC2 instance purchasing options (On-Demand, Reserved, Spot, Savings Plans)?**

**A:**
*   **On-Demand:** Pay per second or hour with no long-term commitments. Best for short-term, unpredictable workloads.
*   **Reserved Instances (RI):** A commitment to a specific instance configuration for 1 or 3 years. Provides up to 72% discount.
*   **Savings Plans:** A highly flexible discount model offering similar savings to RIs but based on a commitment to a consistent hourly spend (e.g., $10/hr) across compute services.
*   **Spot Instances:** Bidding on unused AWS capacity for up to a 90% discount. AWS can terminate these instances with a 2-minute notice if they need the capacity back. Ideal for stateless, fault-tolerant batch jobs.

---

**Q13. What is an Auto Scaling Group (ASG) in AWS?**

**A:** An ASG manages a logical collection of EC2 instances to ensure high availability and load matching. It maintains a target number of instances by launching new ones if an instance fails (self-healing), and automatically scales the number of instances up or down based on metrics (e.g., CPU utilization, network traffic, custom CloudWatch alarms).

---

**Q14. What is AWS Lambda? What are its limitations?**

**A:** AWS Lambda is a serverless, event-driven compute service that executes code in response to triggers. It manages the underlying resources and scales automatically.
*   *Limitations:*
    *   **Execution Timeout:** Maximum of 15 minutes (900 seconds) per invocation.
    *   **Memory Limit:** 128 MB to 10,240 MB.
    *   **Ephemeral Disk Space (`/tmp`):** 512 MB to 10 GB.
    *   **Deployment Package Size:** 50 MB (zipped), 250 MB (unzipped).
    *   **Cold Starts:** Initial latency when an idle function container is spun up.

---

**Q15. What is Google Cloud Functions vs Cloud Run?**

**A:**
*   **Google Cloud Functions:** An event-driven FaaS (Function-as-a-Service) where you deploy code snippets (Python, Node.js, Go) that run in response to events (e.g., pub/sub messages, HTTP requests).
*   **Google Cloud Run:** A fully managed serverless platform that runs containerized applications. It automatically scales containers from zero to thousands, handles routing, and supports any language or binary package bundled into a container.

---

**Q16. What is a managed instance group (MIG) in GCP?**

**A:** A MIG is GCP's equivalent of an Auto Scaling Group. It is a collection of identical VM instances running the same machine template. MIGs offer automated healing (recreating failed instances), multi-zone distribution (high availability), load balancing support, and autoscaling based on CPU, load balancing capacity, or Pub/Sub metrics.

---

**Q17. What is the difference between serverless and container-based compute?**

**A:**
*   **Serverless (FaaS/Managed CaaS):** Code execution is fully managed. Developers focus purely on logic/code. Infrastructure scales down to zero when idle, and scaling happens instantly on request volume. Billing is per millisecond/execution. (e.g., AWS Lambda, GCP Cloud Run).
*   **Container-based (EKS/GCP GKE/ECS):** You run containers on pre-provisioned or managed node clusters. You manage clustering, container orchestrators, resource sizing, and network routing. While scaling is highly automated, nodes generally remain running (idle costs apply). Good for long-running, stateful, or complex microservices.

---

**Q18. What is AWS Fargate and how does it differ from ECS on EC2?**

**A:**
*   **AWS Fargate:** A serverless engine for containers (ECS or EKS). You pay per CPU and Memory resource requested by your task. You do not manage, provision, or patch any underlying EC2 instances.
*   **ECS on EC2:** You manage a cluster of EC2 instances that host your containers. You are responsible for instance sizing, OS patches, and scaling the instance cluster, but you have full OS-level control and pay only for the running EC2 instances (regardless of container density).

---

## Storage

**Q19. What is Amazon S3? What are the different storage classes?**

**A:** Amazon S3 (Simple Storage Service) is an object storage service offering high durability (11 nines) and scalability.
*   *Storage Classes:*
    *   **Standard:** Highly active, frequently accessed data.
    *   **Intelligent-Tiering:** Automatically optimizes costs for data with unknown access patterns.
    *   **Standard-Infrequent Access (IA) & One Zone-IA:** Data accessed less often, but requires milliseconds retrieval. Lower storage cost, higher retrieval fee.
    *   **Glacier Instant / Flexible / Deep Archive:** Archival storage classes with retrieval times ranging from milliseconds to 12 hours. Offers the lowest storage cost.

---

**Q20. What is the difference between S3, EBS, and EFS?**

**A:**
*   **S3 (Object Storage):** Accessible via HTTP APIs globally. Highly durable, stores unstructured objects (keys/values). Ideal for static assets, backups, and data lakes.
*   **EBS (Elastic Block Store):** High-performance block storage volumes designed for single-VM attachment (low-latency, OS boot drives, database data directories).
*   **EFS (Elastic File System):** Managed NFS file storage. Can be mounted by hundreds of compute instances (EC2, ECS, Lambda) simultaneously across different Availability Zones.

---

**Q21. What is Google Cloud Storage (GCS) and how do its storage classes compare to S3?**

**A:** GCS is GCP's highly durable, globally unified object storage service.
*   *GCS Classes vs S3 equivalents:*
    *   **Standard:** Similar to S3 Standard (frequent access).
    *   **Nearline:** Comparable to S3 Standard-IA (accessed less than once a month).
    *   **Coldline:** Comparable to S3 Glacier Instant Retrieval (accessed less than once a quarter).
    *   **Archive:** Comparable to S3 Glacier Deep Archive (long-term archive, but unlike Glacier, GCS Archive supports sub-second access times).

---

**Q22. What is lifecycle management for object storage?**

**A:** It is a set of rules applied to an object storage bucket to automate cost savings and compliance. It defines actions such as transitioning objects to cheaper storage tiers (e.g., Standard to Glacier after 30 days) or permanently deleting objects after a certain period (e.g., cleaning up database backups after 90 days).

---

**Q23. What is S3 versioning and how does it protect against accidental deletion?**

**A:** S3 Versioning keeps multiple variants of an object in the same bucket. When an object is deleted, S3 places a "Delete Marker" on the object instead of permanently deleting it. You can restore previous versions at any time. To permanently delete an object, you must explicitly delete a specific version ID.

---

**Q24. What is a pre-signed URL in S3/GCS?**

**A:** A pre-signed URL grants temporary, secure read or write access to a specific private object without requiring the client to have AWS IAM or GCP credentials. The creator uses their security credentials to sign a URL with a custom expiration time (e.g., valid for 15 minutes to download a generated PDF).

---

## Databases

**Q25. What is Amazon RDS? What databases does it support?**

**A:** Amazon Relational Database Service (RDS) is a managed relational database service. It automates provisioning, patching, backups, point-in-time recovery, and scaling.
*   *Supported Engines:* Amazon Aurora, PostgreSQL, MySQL, MariaDB, Oracle Database, and Microsoft SQL Server.

---

**Q26. What is Aurora and how does it differ from standard RDS?**

**A:** Amazon Aurora is a cloud-native relational database engine (PostgreSQL and MySQL compatible).
*   *Differences from standard RDS:*
    *   **Storage Architecture:** Replicates data 6 ways across 3 AZs. Storage auto-scales up to 128TB and self-heals.
    *   **Failover Performance:** Multi-AZ failover is significantly faster (under 30 seconds) compared to standard RDS (1-2 minutes).
    *   **Replicas:** Supports up to 15 read replicas with sub-10ms replica lag.
    *   **Compute:** Supports Aurora Serverless, which scales compute capacity dynamically.

---

**Q27. What is DynamoDB? What are its key design considerations?**

**A:** DynamoDB is a fully managed, serverless, single-digit millisecond latency NoSQL database.
*   *Key Design Considerations:*
    *   **Keys selection:** Choosing a high-cardinality Partition Key (PK) to avoid hot partition limits (1000 WCU / 3000 RCU per partition).
    *   **Access Patterns:** Must know all query/access patterns upfront to design schemas, local secondary indexes (LSI), or global secondary indexes (GSI) properly (denormalized, single-table design is common).
    *   **Consistency:** Defaults to eventually consistent reads (requires double RCUs for strongly consistent reads).

---

**Q28. What is Cloud Spanner and what makes it unique?**

**A:** Cloud Spanner is GCP's enterprise-grade, globally distributed relational database. It is unique because it provides **horizontal scalability** (like NoSQL databases) combined with **strict ACID transactions and ANSI SQL consistency** (like traditional RDBMS). It uses Google's TrueTime API (synchronized atomic clocks and GPS receivers) to guarantee global external consistency.

---

**Q29. What is Cloud Bigtable and what is it suited for?**

**A:** Cloud Bigtable is GCP's fully managed, high-performance NoSQL wide-column database. It is suited for extremely high-throughput, low-latency workloads (millions of reads/writes per second) with massive datasets (terabytes to petabytes). Common use cases include IoT data streams, financial market telemetry, user history tracking, and ad-tech real-time profiles.

---

**Q30. What is ElastiCache in AWS? What is Memorystore in GCP?**

**A:** They are the respective fully managed in-memory data store and caching services. Both support **Redis** and **Memcached** engines. They are used to improve application performance by caching frequently read database queries, managing user session state, and serving as fast message brokers.

---

**Q31. How do you handle database connection pooling in a cloud-native application?**

**A:** Serverless/container environments scale dynamically, which can rapidly exhaust database connection limits. To handle this:
1.  **Use a Connection Proxy:** Deploy services like **AWS RDS Proxy** or Google's **Cloud SQL Auth Proxy** to pool and share active database connections.
2.  **In-App Pooling:** Configure database client libraries (like HikariCP or PgBouncer) with sensible maximum connection limits matched to container scaling limits.
3.  **Read Replicas:** Route read traffic to read replicas, freeing up primary connection slots for writes.

---

## Networking & CDN

**Q32. What is an Application Load Balancer vs a Network Load Balancer in AWS?**

**A:**
*   **Application Load Balancer (ALB):** Operates at **Layer 7 (HTTP/HTTPS)**. Routes traffic based on host headers, URL paths, query parameters, and request headers. Ideal for microservices, container routing, and web applications.
*   **Network Load Balancer (NLB):** Operates at **Layer 4 (TCP/UDP/TLS)**. Can process millions of requests per second with ultra-low latency. Preserves client IP addresses and handles static/elastic IPs. Ideal for real-time gaming, IoT, and high-frequency streaming.

---

**Q33. What is Cloud Load Balancing in GCP and what are its types?**

**A:** It is GCP's fully managed, software-defined distributed load balancer. It is global by default.
*   *Types:*
    *   **HTTP(S) Load Balancing:** Layer 7 routing for global or regional web traffic.
    *   **TCP/SSL Proxy Load Balancing:** Layer 4 routing for non-HTTP traffic (terminates SSL/TLS).
    *   **Network Service Tiers (Standard vs Premium):** Routes traffic over the public internet (Standard) or Google's private global fiber network (Premium).

---

**Q34. What is AWS CloudFront? What is Google Cloud CDN?**

**A:** They are the respective Content Delivery Network (CDN) services. They cache static and dynamic content at global Edge Locations closest to the user. This decreases latency, reduces origin server load, and protects against DDoS attacks by caching images, scripts, videos, and API payloads close to clients.

---

**Q35. What is AWS Route 53 and what routing policies does it support?**

**A:** Route 53 is a highly available, scalable Domain Name System (DNS) service.
*   *Routing Policies:*
    *   **Simple:** Direct mapping to IP/Alias.
    *   **Weighted:** Distribute traffic by percentage (canary deployments).
    *   **Latency:** Route users to the AWS region offering the lowest latency.
    *   **Failover:** Active-Passive failover based on health checks.
    *   **Geolocation:** Route traffic based on user location.
    *   **Geoproximity:** Route traffic based on physical distance to AWS resources.
    *   **Multivalue Answer:** Return multiple IP records with health check support.

---

**Q36. What is Cloud DNS in GCP?**

**A:** Cloud DNS is GCP's managed, low-latency, resilient Domain Name System running on Google's infrastructure. It supports private DNS zones (resolution within VPCs without exposing endpoints to the internet), public DNS zones, split-horizon DNS, and DNSSEC security.

---

**Q37. What is VPC Peering and how does it differ from Transit Gateway (AWS) or VPC Network Peering (GCP)?**

**A:**
*   **VPC Peering:** Connects two VPCs directly using private IPs. It is non-transitive (VPC A peering with B, and B with C, does *not* connect A to C).
*   **AWS Transit Gateway:** A central hub/router connecting hundreds of VPCs and on-premises networks. It is highly scalable and supports transitive routing.
*   **GCP VPC Network Peering:** Direct, low-latency connection between two GCP VPCs. Unlike AWS, GCP VPC peering routing is transitive under specific import/export custom route configurations.

---

**Q38. What is AWS PrivateLink / GCP Private Service Connect?**

**A:** These services allow private access to SaaS applications, customer services, or cloud APIs hosted in other VPCs/projects *without* exposing the traffic to the public internet or configuring VPC peering. They map services to local, private IP addresses inside your VPC.

---

## Containers & Kubernetes

**Q39. What is Amazon EKS? What is Google Kubernetes Engine (GKE)?**

**A:** They are fully managed, production-ready Kubernetes services. They manage the Kubernetes control plane (API server, etcd, scheduler) for high availability, security patching, and upgrades, allowing you to focus on running workload nodes (worker nodes) and pods. GKE is widely considered the most mature K8s offering due to deep integration with Google's native container systems.

---

**Q40. What is the difference between GKE Standard and GKE Autopilot?**

**A:**
*   **GKE Standard:** You provision, manage, and scale the underlying worker nodes (VMs). You pay for the running VMs and have full control over node OS configuration.
*   **GKE Autopilot:** GCP fully manages node provisioning, scaling, security configurations, and operating systems. You only submit Pod manifests. You are billed only for the CPU, memory, and storage resources requested by your running Pods.

---

**Q41. What is Amazon ECR and what is Google Artifact Registry?**

**A:** They are the respective secure container image registries. They allow teams to store, manage, scan, and deploy Docker images and build artifacts (Helm charts, packages) close to their compute services. Both support vulnerability scanning and IAM-based access control.

---

**Q42. How does cluster autoscaling work in GKE vs EKS?**

**A:**
*   **GKE:** Uses the native GKE Cluster Autoscaler. If a Pod cannot be scheduled due to resource constraints, GKE automatically launches new VM nodes in the node pool. It also downsizes nodes automatically if they are underutilized.
*   **EKS:** Relies on either the **Cluster Autoscaler** (which modifies AWS Auto Scaling Groups) or **Karpenter** (a modern, open-source node provisioner designed for Kubernetes that directly calls EC2 APIs to spin up right-sized instances, bypassing ASGs for much faster scaling).

---

## Messaging & Eventing

**Q43. What is Amazon SQS and what delivery guarantees does it provide?**

**A:** Amazon SQS (Simple Queue Service) is a fully managed message queuing service for decoupling microservices.
*   *Standard Queue:* Guarantees **at-least-once** delivery (duplicates possible due to network timeouts) and **best-effort ordering**.
*   *FIFO Queue:* Guarantees **exactly-once** processing and **strict first-in-first-out ordering** (limited throughput).

---

**Q44. What is the difference between SQS standard queues and FIFO queues?**

**A:**
*   **Standard Queue:** Unbounded message throughput. Best for high-scale, decoupled worker pools where duplicate processing is handled by idempotent consumer logic.
*   **FIFO Queue:** Limited to 300 transactions/sec (or 3,000 with batching). Guarantees messages are processed in the exact order they were sent. Good for order-matching systems or state-dependent workflows.

---

**Q45. What is Amazon SNS and how does fan-out with SQS work?**

**A:** Amazon SNS (Simple Notification Service) is a pub/sub messaging service.
*   **Fan-out Pattern:** A message is published to a single SNS Topic. Multiple SQS queues subscribe to that topic. SNS automatically replicates the message and pushes it to every subscribed SQS queue concurrently, allowing different microservices to process the same event independently.

---

**Q46. What is Google Pub/Sub and how does it compare to SQS/SNS?**

**A:** Google Pub/Sub is a globally unified messaging service. Unlike AWS (which separates queueing [SQS] and pub/sub [SNS]), Google Pub/Sub handles both natively through **Topics** and **Subscriptions**. Subscriptions can be **Pull** (like SQS) or **Push** (like SNS HTTP endpoints). It is globally scalable out-of-the-box and does not require region-specific configurations.

---

**Q47. What is Amazon EventBridge?**

**A:** EventBridge is a serverless event bus service. It receives events from custom applications, AWS services, or SaaS partners, and routes them to targets (like Lambda, Step Functions, or HTTP endpoints) based on configurable matching rules. It supports schema registries, API destinations, and event archiving/replay.

---

## Monitoring & Observability

**Q48. What is AWS CloudWatch? What metrics and logs can it collect?**

**A:** CloudWatch is a monitoring and observability platform.
*   **Metrics:** Collects host CPU, disk write, network out, and custom application metrics (injected via SDK/Agent).
*   **Logs:** Collects application stdout/stderr logs, VPC flow logs, and OS syslog. You can query logs using CloudWatch Logs Insights.

---

**Q49. What is Google Cloud Monitoring and Cloud Logging?**

**A:** They make up the Operations Suite (formerly Stackdriver).
*   **Cloud Monitoring:** Collects performance metrics, dashboards, and triggers alerts (CPU, memory, load balancer latency).
*   **Cloud Logging:** A fully managed, high-scale log repository. It automatically ingests all container logs from GKE and system logs from VM instances, supporting real-time log routing to BigQuery or Pub/Sub.

---

**Q50. What is AWS X-Ray / Google Cloud Trace used for?**

**A:** They are distributed tracing services. They collect data on latency, call chains, and database queries across microservices. They generate service maps visualization to help developers identify application bottlenecks and pinpoint which service is causing failures or long response times.

---

**Q51. What is a CloudWatch Alarm and how do you configure auto-scaling based on it?**

**A:** A CloudWatch Alarm monitors a single metric (e.g., average CPU utilization) over a specified time period.
*   *Auto-scaling configuration:* You link the Alarm to a **Scaling Policy** on an Auto Scaling Group. For example, if CPU utilization exceeds 70% for 3 consecutive minutes, the alarm triggers the policy to scale out (add 2 EC2 instances).

---

## Cost & Optimization

**Q52. How do you optimize cloud costs without sacrificing reliability?**

**A:**
1.  **Use Autoscaling:** Scale compute resources down during off-peak hours.
2.  **Right-Size Infrastructure:** Monitor CPU/Memory metrics to downgrade over-provisioned instances.
3.  **Adopt Spot Instances:** Use Spot instances for stateless, background, or batch workloads.
4.  **Implement Storage Lifecycle Rules:** Transition older object files to Glacier/Archive tiers.
5.  **Utilize Managed Serverless:** Shift components to Lambda/Cloud Run to avoid paying for idle server time.
6.  **Clean up Idle Resources:** Automatically identify and delete unattached storage volumes, elastic IPs, and empty load balancers.

---

**Q53. What is AWS Trusted Advisor / GCP Recommender?**

**A:** They are fully managed advisor services that scan your cloud environment and provide automated recommendations to improve cost efficiency, security posture, system fault tolerance, and performance limits.

---

**Q54. What are reserved instances and committed use discounts?**

**A:** They are financial commitment mechanisms. In exchange for committing to use a minimum level of compute capacity (instances or spending limits) for a 1 or 3-year term, the cloud provider grants substantial discounts (up to 70% off standard hourly rates) compared to standard on-demand pricing.

---

**Q55. How do you use spot/preemptible instances safely for batch workloads?**

**A:**
1.  **Design for Statelessness:** Ensure tasks can be interrupted and resumed from checkpoints without data loss.
2.  **Use Instance Diversification:** Configure your scaling groups to choose from multiple instance types to minimize the chance of a single capacity pool depletion.
3.  **Implement Graceful Shutdowns:** Listen for termination notices (2 minutes in AWS, 30 seconds in GCP) to save work-in-progress to external storage (like S3/GCS) before the VM shuts down.

---
