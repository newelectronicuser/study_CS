# Networking (VPC, Route 53, CloudFront)

Networking is the backbone of AWS security and performance.

## 1. Amazon VPC (Virtual Private Cloud)
A logically isolated section of the AWS cloud where you can launch AWS resources.

### Subnets
- **Public Subnet**: Has a route to an **Internet Gateway (IGW)**.
- **Private Subnet**: No direct access to the internet. Uses a **NAT Gateway** (in the public subnet) for outbound-only internet access.

### Connectivity & Security
- **Security Groups**: Stateful firewall at the **Instance** level. (Allowed rules only).
- **Network ACLs (NACLs)**: Stateless firewall at the **Subnet** level. (Support Allow/Deny rules).
- **VPC Peering**: Connecting two VPCs using AWS private network. (Not transitive!).
- **Transit Gateway**: A hub to connect thousands of VPCs and on-premises networks.

> [!IMPORTANT]
> **VPC Endpoints (PrivateLink)**: 
> Allows you to connect your VPC to supported AWS services without using an Internet Gateway or NAT Gateway.
> - **Gateway Endpoints**: Free. Supports **S3** and **DynamoDB**.
> - **Interface Endpoints**: Paid. Provides a private IP address for the service.

## 2. Amazon Route 53 (DNS)
Highly available and scalable cloud Domain Name System.

### Routing Policies
- **Simple**: Standard domain-to-IP mapping.
- **Latency**: Routes to the region with the lowest latency for the user.
- **Failover**: Used for Active-Passive disaster recovery.
- **Geolocation**: Routes based on user's physical location.
- **Weighted**: Distributes traffic based on percentages (Good for Blue-Green or Canary).

## 3. Amazon CloudFront (CDN)
Global content delivery network that uses Edge Locations.
- Reduces latency by caching content closer to users.
- Provides DDoS protection (Integrates with WAF and Shield).
- Supports **Lambda@Edge** for running logic at the edge (e.g., A/B testing, header manipulation).

## 4. Architectural Scenarios

### Scenario A: Securely connect a Private Subnet to S3
**Wrong**: Use a NAT Gateway (costs money).
**Right**: Use a **VPC Gateway Endpoint** for S3 (free, more secure, traffic stays on AWS backbone).

### Scenario B: Multi-Region Disaster Recovery
**Solution**: Use **Route 53 Failover Routing**. Set up Health Checks on the primary region; if it fails, DNS will automatically point to the secondary region.

### Scenario C: Prevent SQL Injection in a Web App
**Solution**: Deploy an **Application Load Balancer (ALB)** and attach **AWS WAF** with managed rules for SQLi.

> [!TIP]
> **Security Groups are Stateful**: 
> If you allow an inbound request on port 80, the outbound response is automatically allowed. **NACLs are Stateless**, so you must explicitly allow both inbound and outbound traffic.

> [!WARNING]
> **NAT Gateway Cost**: 
> NAT Gateways are expensive and charged per hour + per GB of data. For high-volume traffic to S3 or DynamoDB, always use **VPC Endpoints** instead.
