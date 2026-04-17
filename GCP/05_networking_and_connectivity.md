# Networking and Connectivity

GCP's networking architecture is unique because its Virtual Private Clouds (VPCs) are **Global**, not regional.

## 1. Global VPC Architecture
In most clouds (like AWS), a VPC is regional. In GCP, a VPC is global.
- **Regions and Subnets**: A VPC can have subnets in multiple regions around the world.
- **Internal Communication**: Resources in different regions but the same VPC can communicate using internal IP addresses as if they were in the same data center.

## 2. Shared VPC vs. VPC Peering
- **Shared VPC**: Allows an organization to connect resources from multiple projects to a common VPC network. (Best for centralized network management).
- **VPC Peering**: Connects two VPC networks (can be in different projects or organizations) so that resources can communicate over internal IPs.

## 3. Cloud Load Balancing
GCP offers a fully-managed, high-performance global load balancing service.
- **Global Load Balancing**:
    - **HTTP(S) LB**: Layer 7. Can route traffic to the nearest region with available capacity.
    - **SSL Proxy LB / TCP Proxy LB**: Layer 4. Terminate SSL close to the user.
- **Regional Load Balancing**:
    - **Network LB**: Layer 4. Passthrough load balancing for non-HTTP traffic.
    - **Internal LB**: Cross-region HTTP(S) or Network LB for private traffic within a VPC.

## 4. Connectivity to On-Premises
- **Cloud VPN**: Secure connections over the public internet (IPsec).
- **Dedicated Interconnect**: Direct 10/100 Gbps physical link between Google's network and your data center.
- **Partner Interconnect**: Connectivity through a supported service provider.

## 5. Network Security
- **Firewall Rules**: Distributed firewall that allows or denies traffic to/from VM instances based on **Tags** or Service Accounts (not just IP ranges).
- **Cloud Armor**: Protects against DDoS and WAF attacks at the global load balancer level.
- **Identity-Aware Proxy (IAP)**: Guards access to applications without requiring a VPN.

## 6. Private Service Access
Allows you to connect to managed services (like Cloud SQL or Redis) over a private IP range, ensuring traffic never leaves Google's network.

> [!IMPORTANT]
> **Subnets are Regional**: 
> While the VPC is Global, individual Subnets are assigned to a specific **Region**. You define IP ranges at the subnet level.

> [!TIP]
> **Premium Tier vs Standard Tier**: 
> - **Premium Tier**: Google's global fiber network (Lower latency, higher cost).
> - **Standard Tier**: Public internet (Higher latency, lower cost).
> Premium Tier is the default and a significant competitive advantage for GCP.
