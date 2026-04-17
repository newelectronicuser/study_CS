# Management and Operations

Managing resources at scale on GCP requires deep observability and automated infrastructure.

## 1. Cloud Operations (formerly Stackdriver)
A suite of tools for monitoring, logging, and diagnostics.
- **Cloud Monitoring**: Collects metrics, events, and metadata. Used to create dashboards and set up alerts.
- **Cloud Logging**: Fully managed, real-time log management. Stores logs for 30 days (standard) but can be exported to BigQuery or Cloud Storage for long-term retention.
- **Error Reporting**: Aggregates and displays errors for running cloud services.
- **Cloud Trace**: Distributed tracing for latency analysis of your applications.

## 2. Infrastructure as Code (IaC)
- **Terraform**: The industry-standard tool for managing GCP infrastructure. (Highly recommended over the native alternative).
- **Deployment Manager**: GCP-native declarative tool (similar to AWS CloudFormation).

## 3. Compliance and Governance
- **Organization Policy Service**: Gives central control to administrators to restrict how resources can be used across an entire organization (e.g., "Disallow creation of external IPs").
- **Cloud Asset Inventory**: Provides a historical view of all your GCP assets and their configurations.

## 4. Operational Scenarios

### Scenario A: Debug a sudden spike in latency
**Process**: 
1. Check **Cloud Monitoring** dashboards for infrastructure health.
2. Use **Cloud Trace** to find the specific microservice or API call causing the delay.
3. Inspect **Cloud Logging** for any application exceptions during that window.

### Scenario B: Restrict all developers to the `us-east1` region
**Solution**: Apply an **Organization Policy** with the `Resource Location Restriction` constraint at the organization or folder level.

### Scenario C: Long-term archival of application logs for auditing
**Solution**: Create a **Log Sink** in Cloud Logging that exports logs to **Cloud Storage (Archive Class)** or **BigQuery**.

## 5. Billing and Cost Management
- **Labels**: Key-value pairs attached to resources to track costs (e.g., `dept: marketing`).
- **Budgets and Alerts**: Set up email alerts when spending reaches a certain percentage of your budget.
- **Committed Use Discounts (CUDs)**: Discounts for committing to a certain amount of compute usage for 1-3 years.

> [!IMPORTANT]
> **Audit Logs**: 
> - **Admin Activity**: Logged by default, free.
> - **Data Access**: Not logged by default (except for BigQuery), can be very voluminous and expensive to store.

> [!TIP]
> **The GCP Console vs. gcloud CLI**: 
> In a senior interview, emphasize your comfort with the **gcloud CLI** and **Terraform** for repeatable, automated deployments, whereas the Console is for quick discovery and investigation.
