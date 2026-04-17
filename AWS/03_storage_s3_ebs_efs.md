# 03 Storage: S3, EBS, EFS

## Core Concepts

### S3 (Simple Storage Service) - Object Storage
*   **Key Features:** 99.999999999% (11 9's) durability, virtually unlimited scaling, event notifications.
*   **Storage Tiers:**
    *   **Standard:** Frequent access.
    *   **Intelligent Tiering:** Automatically moves data based on access patterns (best for unknown patterns).
    *   **Standard-IA:** Infrequent access, cheaper storage, retrieval fee.
    *   **One Zone-IA:** Same as IA but in 1 AZ (less durable).
    *   **Glacier Instant/Flexible/Deep Archive:** Long-term archival with varying retrieval times (ms to hours).

### EBS (Elastic Block Store) - Block Storage
*   **Key Features:** Attached to a single EC2 instance (usually), persistent across instance termination.
*   **Types:**
    *   **gp3/gp2:** General purpose SSD. gp3 allows independent IOPS/Throughput.
    *   **io2/io1:** Provisioned IOPS. Best for high-perf databases.
    *   **st1/sc1:** Magnetic storage for throughput-heavy or cold data.

### EFS (Elastic File System) - File Storage
*   **Key Features:** Managed NFS. Can be mounted by hundreds of EC2 instances simultaneously across AZs.
*   **Properties:** Scalable, expensive compared to EBS, but allows shared access.

---

## Interview Deep Dives

### 1. "How do you optimize S3 performance for high request rates?"
**Answer:**
*   **Prefixing:** S3 scales based on prefixes. If you have >3500 PUT or >5500 GET requests per second, spread your data across different prefixes (folders).
*   **Multipart Upload:** Use for files >100MB; required for files >5GB.
*   **S3 Transfer Acceleration:** Uses CloudFront edge locations for faster uploads over long distances.
*   **Byte-Range Fetches:** Parallelize GET requests by fetching specific byte ranges of a large file.

### 2. "EBS vs. EFS vs. Instance Store: Which to choose and when?"
**Answer:**
*   **Instance Store:** Ephmeral (data lost on stop/terminate). Use for **High IOPS / Low Latency** buffers or caches where data permanence isn't required.
*   **EBS:** Use for OS volumes, critical database files, or workloads needing consistent low-latency block storage.
*   **EFS:** Use for content management systems (WordPress), shared configuration files, or big data processing where multiple nodes need the same file system.

### 3. "Explain S3 Consistency Model."
**Answer:**
*   S3 provides **Strong Read-after-Write consistency** for all objects (both new PUTs and overwrites). There is no longer a "partial visibility" window after an update.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **S3** | Static assets, Data lakes | Not for frequent random writes (not a block device). |
| **EBS** | DB local storage | Locked to one AZ (mostly); expensive for huge data. |
| **EFS** | Shared data across AZs | High latency compared to EBS; expensive per GB. |

---

## Common Interview Scenarios

### Scenario: "You need to reduce S3 costs for a bucket with 100TB of data accessed rarely."
**Troubleshooting/Design:**
1.  **Analyze Access Patterns:** Use S3 Storage Class Analysis.
2.  **Lifecycle Policies:** Move data to **S3 Standard-IA** after 30 days and **Glacier Deep Archive** after 90 days.
3.  **Delete Versions:** If versioning is on, clean up old versions or expired delete markers.

### Scenario: "How do you protect S3 data from accidental deletion?"
**Key Details:**
*   **Versioning:** Keep multiple versions of an object.
*   **MFA Delete:** Requires an MFA token to permanently delete a version.
*   **Object Lock:** Implements WORM (Write Once Read Many) for compliance.
*   **Bucket Policies:** Restrict `s3:DeleteObject` permissions to specific roles.
