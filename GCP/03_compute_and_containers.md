# Compute and Containers

GCP offers a spectrum of compute options, from Infrastructure as a Service (IaaS) to Serverless.

## 1. Compute Engine (GCE)
Managed virtual machines (VMs).
- **Machine Families**: General-purpose (e.g., E2, N2), Compute-optimized (C2), Memory-optimized (M2).
- **Sole-tenant Nodes**: Physical servers dedicated to your use (useful for compliance/licensing).
- **Preemptible VMs / Spot VMs**: Up to 80% cheaper but Google can shut them down at any time. (Best for batch processing).

## 2. Google Kubernetes Engine (GKE)
Managed Kubernetes service. GCP's strongest compute offering.
- **Autopilot**: Google manages the nodes and scaling of the cluster entirely. You pay per pod usage.
- **Standard**: You manage the node pools and have full control over cluster configuration.
- **Features**: Binary Authorization (Security), GKE Sandbox (Isolation), and Workload Identity.

## 3. Cloud Run
Serverless platform for running containerized applications.
- **How it works**: You provide a container image; Google handles the scaling (down to zero) and routing.
- **Best For**: Microservices, Web APIs, and asynchronous tasks.
- **Billing**: You only pay while the container is processing a request.

## 4. Cloud Functions
Event-driven serverless code.
- **How it works**: Write code (Python, Go, JS) to respond to events from Pub/Sub, Cloud Storage, or HTTP requests.
- **Best For**: Lightweight glue logic, data processing triggers, and simple webhooks.

## 5. Compute Comparison

| Service | Level | Scalability | Complexity |
| :--- | :--- | :--- | :--- |
| **Compute Engine** | IaaS | Manual / Instance Groups | High |
| **GKE** | Managed K8s | High (Cluster Autoscaler) | Medium |
| **Cloud Run** | Serverless Container | Automatic (Fast) | Low |
| **Cloud Functions** | Serverless Code | Automatic | Very Low |

## 6. App Engine (PaaS)
A fully-managed platform for developing and hosting web applications.
- **Standard**: Sandboxed environments, faster scaling to zero.
- **Flexible**: Docker-based, can use any language/library but scales slower.

> [!IMPORTANT]
> **Scaling to Zero**: 
> Cloud Run and Cloud Functions can scale to zero instances when there is no traffic, eliminating idle costs. Compute Engine and GKE (Standard) usually have a minimum capacity cost.

> [!TIP]
> **GKE Autopilot vs Standard**: 
> For most interviews, specify **Autopilot** as the modern best practice because it reduces operational overhead and applies Google's best practices for security by default.
