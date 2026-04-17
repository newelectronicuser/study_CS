# Infrastructure and Resource Hierarchy

Google Cloud Platform (GCP) is organized into a strict hierarchy that makes it easy to manage permissions and billing at scale.

## 1. The Resource Hierarchy
Understanding the hierarchy is the foundation of GCP governance.

- **Organization**: The top-level node (usually linked to a Google Workspace or Cloud Identity domain).
- **Folders**: Optional. Used to group projects (e.g., by department `Finance` or environment `Production`).
- **Projects**: The primary containers for resources. Every resource MUST belong to exactly one project. Projects are the level where billing is managed and APIs are enabled.
- **Resources**: The individual GCP services (VMs, Buckets, DBs).

## 2. Global Infrastructure
GCP is built on Google's world-class global network.

| Scope | Description | Examples |
| :--- | :--- | :--- |
| **Global** | Resources accessible across all regions. | VPC, IAM, Cloud DNS, Cloud Storage (Multi-region). |
| **Regional** | Resources tied to a specific geographic area. | App Engine, Regional VPC Subnets, Cloud SQL. |
| **Zonal** | Resources tied to a specific data center in a region. | Compute Engine VMs, Persistent Disks. |

## 3. Regions and Zones
- **Region**: A specific geographical location (e.g., `us-central1`).
- **Zone**: An isolated data center within a region (e.g., `us-central1-a`).
- **Deployment Strategy**: To achieve high availability, deploy resources across multiple zones. To achieve disaster recovery, deploy across multiple regions.

## 4. Points of Presence (PoPs)
Google maintains hundreds of PoPs around the world. These are used for:
- **Cloud CDN**: Caching content.
- **Cloud Load Balancing**: Terminating traffic close to the user.
- **Dedicated Interconnect**: Connecting on-premises data centers to Google's backbone.

> [!IMPORTANT]
> **Project IDs vs Project Names**: 
> - **Project ID**: A globally unique, immutable string. Used in APIs and CLI.
> - **Project Name**: A user-friendly name, can be changed, and doesn't need to be unique.
> - **Project Number**: A unique, automatically assigned number.

> [!TIP]
> **Everything is an API**: 
> In GCP, you must manually enable the API for each service (e.g., Compute Engine API) within a project before you can use it.
