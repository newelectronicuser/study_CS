# Big Data and Analytics

GCP's data analytics stack is often cited as its greatest strength, built on decades of Google's internal innovation (e.g., Dremel, Flume, Colossus).

## 1. BigQuery
Serverless, highly scalable, and cost-effective multi-cloud data warehouse.
- **How it works**: Separation of compute and storage. You only pay for the data you store and the queries you run.
- **Slots**: The unit of compute capacity used by BigQuery to execute SQL queries.
- **Features**: BigQuery ML (Machine Learning inside BQ), BigQuery Omni (Multi-cloud), and BI Engine.

## 2. Cloud Pub/Sub
A global, horizontally scalable messaging service for event-driven systems and streaming analytics.
- **Publisher-Subscriber Model**: Decouples services that produce events from those that process them.
- **Retention**: Messages are retained for up to 7 days by default.
- **Architecture**: Global by design. Producers and consumers can be anywhere in the world.

## 3. Dataflow
A fully managed service for executing Apache Beam pipelines for stream and batch data processing.
- **Unified Programming Model**: One set of code for both streaming and batch data.
- **Features**: Horizontal autoscaling, dynamic work rebalancing (to handle "straggler" nodes).

## 4. Other Analytics Services
- **Dataproc**: Managed Spark and Hadoop. Best for migrating existing on-premises clusters.
- **Looker**: Modern enterprise BI and data modeling platform.
- **Cloud Composer**: Managed Apache Airflow for orchestrating complex data workflows.

## 5. Analytics Pipeline Example
`Ingest (Pub/Sub) -> Process (Dataflow) -> Store (BigQuery) -> Visualize (Looker)`

## 6. BigQuery Cost Optimization
- **Partitioning**: Dividing a table into segments (e.g., by date) to reduce the amount of data scanned.
- **Clustering**: Sorting data within partitions to improve query performance.
- **Reservations**: Moving from on-demand pricing to flat-rate pricing (buying dedicated slots).

> [!IMPORTANT]
> **Serverless Analytics**: 
> In an interview, emphasize that services like BigQuery and Pub/Sub require **zero infrastructure management**. You don't provision servers or worry about disk space; you just use the API.

> [!TIP]
> **Partitioning vs Clustering**: 
> Always **Partition** a table first (usually by date). Use **Clustering** on high-cardinality columns (like `customer_id`) that are frequently used in filters or aggregations.
