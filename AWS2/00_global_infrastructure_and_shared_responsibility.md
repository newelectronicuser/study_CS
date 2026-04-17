# Global Infrastructure and Shared Responsibility

The foundation of AWS: How it's built and the critical division of security duties between AWS and the Customer.

## 1. Global Infrastructure Architecture

| Component | Definition | Strategic Significance |
| :--- | :--- | :--- |
| **Regions** | Isolated physical locations containing multiple AZs. | Data Sovereignty, Latency, and Disaster Recovery. |
| **Availability Zones (AZs)** | Discrete data centers with redundant power and networking. | High Availability (HA) and Fault Tolerance. |
| **Edge Locations** | CDN endpoints for CloudFront. | Low latency content delivery and DDoS mitigation (Shield). |
| **Local Zones** | Extension of Regions closer to end users. | Sub-millisecond latency for real-time apps. |

## 2. Shared Responsibility Model
A "must-know" for any senior interview. It defines who is responsible for which security layer.

> [!IMPORTANT]
> **The Golden Rule**: 
> - AWS is responsible for security **OF** the cloud (Physical, Hardware, Global Infra).
> - The Customer is responsible for security **IN** the cloud (Data, IAM, App Code, Config).

### Managed vs. Unmanaged Services Trade-off
- **Unmanaged (EC2)**: Customer is responsible for OS patching, firewall config (Security Groups), and scaling logic.
- **Managed (RDS/Lambda)**: AWS handles OS patching and physical scaling. The Customer only manages the application configuration and data access.

## 3. The 6 Pillars of Well-Architected Framework
Senior interviews often frame questions around these pillars:
1.  **Operational Excellence**: Automating changes, responding to events.
2.  **Security**: Protecting data, systems, and assets.
3.  **Reliability**: Ability of a system to recover from infrastructure or service disruptions.
4.  **Performance Efficiency**: Using computing resources efficiently.
5.  **Cost Optimization**: Avoiding unnecessary costs.
6.  **Sustainability**: Minimizing the environmental impact of running cloud workloads.

## 4. AWS Organizations and Control
- **Consolidated Billing**: Volume discounts and single payment point.
- **Service Control Policies (SCPs)**: JSON policies that specify the maximum permissions for an organization or OU. 
- **Tagging Strategy**: Critical for cost allocation and automated resource cleanup.

> [!TIP]
> **Disaster Recovery (DR) vs High Availability (HA)**: 
> - **HA** is about surviving the failure of a single data center (AZ). 
> - **DR** is about surviving the failure of an entire geographical location (Region).

> [!WARNING]
> **Root User Security**: 
> Never use the root user for daily tasks. Create IAM users/roles, enable MFA immediately, and lock away the root credentials.
