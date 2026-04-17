# Compute (EC2, Lambda, ECS)

AWS offers compute options ranging from complete virtual machine control (EC2) to fully managed serverless (Lambda).

## 1. AWS Lambda (Serverless)
The default choice for event-driven, scalable logic.

> [!IMPORTANT]
> **Cold Starts**: 
> When a Lambda function is called after being idle, AWS must spin up a new environment. This causes latency.
> - **Mitigation**: Increase memory (more CPU is allocated), use smaller packages (minimize imports), or use **Provisioned Concurrency** (keeps environments warm).

### Lambda Configuration
- **Timeout**: Max 15 minutes.
- **Memory**: 128MB to 10GB. CPU scales proportionally with memory.
- **Environment Variables**: Max 4KB. For larger configs, use AppConfig or Parameter Store.
- **Ephemeral Storage**: `/tmp` up to 10GB.

## 2. Amazon EC2 (Virtual Machines)
- **Instance Types**: **C** (Compute), **R** (Memory), **I** (Storage), **G** (GPU).
- **Purchasing Options**:
    - **On-Demand**: Pay per second. No commitment.
    - **Reserved (RI)**: 1-3 year commitment. Up to 75% discount.
    - **Spot Instances**: Up to 90% discount. AWS can reclaim with a 2-minute warning. (Best for batch jobs).

## 3. Auto Scaling and Load Balancing
- **Application Load Balancer (ALB)**: Layer 7 (HTTP/HTTPS). Supports path-based and host-based routing.
- **Network Load Balancer (NLB)**: Layer 4 (TCP/UDP). Handles millions of requests per second with ultra-low latency.
- **Target Groups**: Groups of resources (EC2, Lambda, IP) that the LB forwards traffic to.

## 4. Containers: ECS vs. EKS vs. Fargate
- **ECS (Elastic Container Service)**: AWS-native container orchestrator. Simpler than Kubernetes.
- **EKS (Elastic Kubernetes Service)**: Managed Kubernetes. Best for portability and complex microservices.
- **Fargate**: Serverless compute engine for containers. You don't manage the underlying EC2 instances.

## 5. Architectural Scenarios

### Scenario A: Handle unpredictable traffic spikes
**Solution**: Use **Lambda** (scales automatically) or **EC2 with Auto Scaling** based on a "Target Tracking" policy (e.g., maintain 50% CPU).

### Scenario B: Long-running batch process (> 15 mins)
**Solution**: Cannot use Lambda. Use **EC2 Spot Instances** or **ECS on Fargate** to avoid the 15-minute timeout.

### Scenario C: Microservices with complex interactions
**Solution**: **EKS** for orchestration combined with **App Mesh** (Service Mesh) for observability and traffic control.

> [!TIP]
> **Scaling: Vertical vs Horizontal**: 
> - **Vertical (Scale Up)**: Increasing instance size (e.g., m5.large -> m5.xlarge). Limited by max instance size.
> - **Horizontal (Scale Out)**: Increasing the number of instances. The cloud-native way to achieve infinite scale.

> [!WARNING]
> **Lambda in VPC**: 
> If a Lambda needs to access a resource in a VPC (like an RDS), it must be assigned a Subnet and Security Group. This used to cause long cold starts due to ENI creation, but AWS improved this with Hyperplane ENIs.
