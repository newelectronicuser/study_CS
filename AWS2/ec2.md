# 🚀 EC2 (Elastic Compute Cloud) – Interview Notes

## 🔹 What is EC2?

- AWS service that provides resizable virtual servers (instances)
- Infrastructure as a Service (IaaS)
- Rent virtual machines instead of physical servers

---

## 🔹 Key Concepts

### 1. Instance

- Virtual server
- Defined by:
  - CPU
  - RAM
  - Storage
  - Network

---

### 2. AMI (Amazon Machine Image)

- Pre-configured template
- Includes:
  - OS (Linux/Windows)
  - Application stack
  - Configurations

---

### 3. Instance Types

| Type                      | Purpose            |
| ------------------------- | ------------------ |
| General Purpose (t3, t4g) | Balanced workloads |
| Compute Optimized (c5)    | CPU-heavy apps     |
| Memory Optimized (r5)     | RAM-heavy apps     |
| Storage Optimized (i3)    | High IOPS          |
| GPU Instances             | ML, AI workloads   |

---

### 4. Pricing Models

| Type          | Description                 |
| ------------- | --------------------------- |
| On-Demand     | Pay per second/hour         |
| Reserved      | Long-term discount          |
| Spot          | Cheapest, but interruptible |
| Savings Plans | Flexible pricing            |

---

### 5. Storage Options

#### EBS (Elastic Block Store)

- Persistent storage
- Like a hard disk
- Attached to EC2

#### Instance Store

- Temporary storage
- Lost on stop/terminate

#### S3

- Object storage

---

### 6. Security

#### Security Groups

- Acts like firewall
- Controls inbound and outbound traffic

#### Key Pair

- Used for SSH login
- Public + Private key

---

### 7. Networking

#### VPC (Virtual Private Cloud)

- Isolated network

#### Subnet

- Public / Private segmentation

#### Elastic IP

- Static public IP

---

## 🔹 EC2 Lifecycle

1. Launch
2. Running
3. Stop (restart later)
4. Terminate (permanent delete)

---

## 🔹 Auto Scaling

- Automatically adjusts number of instances

Example:

- Traffic ↑ → Add instances
- Traffic ↓ → Remove instances

---

## 🔹 Load Balancing

- Distributes traffic across instances
- Improves availability and fault tolerance

Types:

- Application Load Balancer (HTTP/HTTPS)
- Network Load Balancer (TCP/UDP)

---

## 🔹 High Availability

Achieved using:

- Multiple Availability Zones (AZs)
- Load Balancer
- Auto Scaling

---

## 🔹 Scaling Types

| Type               | Description            |
| ------------------ | ---------------------- |
| Vertical Scaling   | Increase instance size |
| Horizontal Scaling | Add more instances     |

---

## 🔹 Placement Groups

| Type      | Use                       |
| --------- | ------------------------- |
| Cluster   | Low latency               |
| Spread    | Fault isolation           |
| Partition | Large distributed systems |

---

## 🔹 Common Interview Questions

### Q1: Difference between EBS and Instance Store?

- EBS → Persistent
- Instance Store → Temporary

---

### Q2: What happens when EC2 stops?

- RAM cleared
- Instance store lost
- EBS persists

---

### Q3: How to make EC2 highly available?

- Multi-AZ deployment
- Load balancer
- Auto scaling

---

### Q4: How to secure EC2?

- Security Groups
- IAM roles
- Key pairs

---

### Q5: How do you handle traffic spikes?

- Auto Scaling + Load Balancer

---

## 🔹 Real-World Architecture Example

Client → Load Balancer → EC2 → Database

---

## 🔹 Best Practices

- Do not expose DB to internet
- Use IAM roles instead of credentials
- Use Auto Scaling in production
- Monitor with CloudWatch

---

## 🔹 Monitoring

- Track:
  - CPU
  - Memory (custom)
  - Disk
  - Network

---

## 🔹 Advanced Concepts

- User Data scripts (bootstrapping)
- Immutable infrastructure
- Blue-Green deployment
- Rolling updates
- Spot fleet

---

## 🔥 One-Line Summary

EC2 is a scalable virtual server service in AWS that allows you to run applications with flexible compute capacity along with integrated storage, networking, and security.
