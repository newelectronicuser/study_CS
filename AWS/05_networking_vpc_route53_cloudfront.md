# 05 Networking: VPC, Route53, CloudFront

## Core Concepts

### VPC (Virtual Private Cloud)
A private network in the AWS cloud.
*   **Subnets:** 
    *   **Public:** Has a route to an Internet Gateway (IGW).
    *   **Private:** No direct route to the internet. Uses a **NAT Gateway** (for IPv4) or **Egress-only IGW** (for IPv6) to initiate outbound connections.
*   **Security:**
    *   **Security Groups (SG):** Stateful (response traffic is allowed automatically). Attached to instances/ENIs.
    *   **NACLs (Network Access Control Lists):** Stateless (response traffic must be explicitly allowed). Attached to subnets.

### Route 53 (DNS)
Managed DNS service.
*   **Routing Policies:**
    *   **Simple:** 1 entry.
    *   **Weighted:** Distribute traffic (e.g., 80% to v1, 20% to v2).
    *   **Latency-based:** Route to the region with the lowest latency for the user.
    *   **Failover:** Active-Passive DR.
    *   **Geolocation:** Based on user's location.
    *   **Multi-value Answer:** Returns up to 8 healthy records.

### CloudFront (CDN)
Global content delivery network.
*   **Origins:** S3, EC2, ELB, or custom servers.
*   **Cache Behaviors:** Path-based routing, TTL settings.
*   **Security:** WAF (Web Application Firewall) integration, signed URLs/cookies.

---

## Interview Deep Dives

### 1. "Security Group vs. NACL: Which is better?"
**Answer:**
*   **Statefulness:** SGs are stateful; NACLs are stateless. This makes SGs much easier to manage for standard application traffic.
*   **Layer of Defense:** NACLs are a "first line of defense" at the subnet level. They are good for blocking specific malicious IP ranges or wide bans.
*   **Precedence:** NACLs are evaluated before SGs for inbound traffic.

### 2. "How do you connect two different VPCs?"
**Answer:**
*   **VPC Peering:** Simple, 1-to-1 connection. Low cost (only traffic charges). Doesn't scale well (cannot be transitive).
*   **Transit Gateway:** Hub-and-spoke model. Scales to thousands of VPCs. Supports transitive routing. Centralized management but costs more hourly.
*   **PrivateLink:** Expose a specific service (using NLB) to another VPC without peering the whole network. Traffic stays on the AWS backbone.

### 3. "Explain the difference between IGW and NAT Gateway."
**Answer:**
*   **IGW:** Allows both inbound and outbound traffic. Used for public subnets (Bastion hosts, Load Balancers).
*   **NAT Gateway:** Highly available, managed service that allows instances in private subnets to reach the internet (e.g., for patching) but prevents the internet from initiating a connection to them.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Direct Connect** | Consistent high throughput, dedicated link | Expensive, long lead time for setup. |
| **Site-to-Site VPN** | Quick setup for Hybrid cloud | Internet-dependent, throughput limited to 1.25 Gbps per tunnel. |
| **Route 53 Resolver** | Hybrid DNS resolution | Extra cost; configuration complexity. |

---

## Common Interview Scenarios

### Scenario: "How do you design a VPC for a highly available web application?"
**Troubleshooting/Design:**
1.  **Multi-AZ Subnets:** 2 Public subnets, 2 Private subnets across 2 or more AZs.
2.  **ALB:** Place Application Load Balancer in Public subnets.
3.  **App Tier:** Place EC2/ECS in Private subnets.
4.  **NAT Gateway:** Put a NAT Gateway in each AZ's public subnet for high availability.
5.  **Route Tables:** Configure routes to IGW for public and NAT GW for private.

### Scenario: "Users in Europe are complaining about slow video loads for an app hosted in US-East-1."
**Key Details:**
*   **CloudFront:** Implement CDN with Lambda@Edge for regional logic.
*   **S3 Transfer Acceleration:** To speed up uploads if applicable.
*   **Global Accelerator:** If using non-HTTP traffic (TCP/UDP) to route traffic via the AWS global network.
