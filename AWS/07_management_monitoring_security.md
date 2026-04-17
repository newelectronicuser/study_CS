# 07 Management & Security: CloudWatch, CloudTrail, KMS, Secrets Manager

## Core Concepts

### CloudWatch (Observability)
*   **Metrics:** Time-series data (CPU, RAM, Disk).
*   **Logs:** Centralized log storage. Use **CloudWatch Logs Insights** for querying.
*   **Alarms:** Trigger actions (SNS, Auto Scaling) based on metric thresholds.
*   **Events (EventBridge):** Respond to state changes in AWS resources.

### CloudTrail (Governance/Audit)
*   **Function:** Records every API call made in your account (Who, What, When, from where).
*   **Use Case:** Security auditing and troubleshooting (e.g., "Whom deleted my production database?").

### KMS (Key Management Service)
*   **CMKs (Customer Master Keys):** Used to encrypt data.
*   **Symmetric vs Asymmetric:** Symmetric is standard for data-at-rest; Asymmetric for digital signatures or external systems.
*   **Key Policies:** Resource-based policies that define who can use/manage the key.

### Secrets Manager vs. Parameter Store
*   **Secrets Manager:** Built for secrets (passwords, API keys). Supports auto-rotation of RDS passwords. Costs $0.40/secret.
*   **Parameter Store (SSM):** Built for configuration (URLs, AMIs). Supports secure strings (KMS). Free for standard parameters.

---

## Interview Deep Dives

### 1. "How do you implement centralized logging in a multi-account environment?"
**Answer:**
*   **CloudWatch Destination:** Set up a centralized logging account with an S3 bucket or Kinesis Data Firehose.
*   **IAM Roles:** Grant permission for other accounts to put logs into the centralized destination.
*   **CloudWatch Logs Subscription Filters:** Automatically stream logs from all accounts to the central Kinesis/S3 destination.

### 2. "Encryption at Rest: KMS vs. Client-side?"
**Answer:**
*   **KMS (Server-side):** AWS manages the encryption process. Easier to implement and integrates with almost all AWS services.
*   **Client-side:** You encrypt data *before* sending it to AWS. Provides maximum security (AWS never sees the plaintext), but increases management overhead.

### 3. "What is the difference between CloudWatch and CloudTrail?"
**Answer:**
*   **CloudWatch:** Focuses on **Performance and Health** of applications/resources.
*   **CloudTrail:** Focuses on **Account Activity**. It's an audit log of API calls.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **CloudWatch Logs** | Standard app logs | Querying can be slow for massive datasets; costs can spike if logging is too verbose. |
| **Secrets Manager** | Rotating database credentials | Higher cost compared to SSM. |
| **KMS (CMK)** | Fine-grained security control | Costs $1/month per key + request charges. |
| **AWS Config** | Compliance & History | Can be expensive in high-change environments. |

---

## Common Interview Scenarios

### Scenario: "You need to rotate database passwords every 30 days without downtime."
**Troubleshooting/Design:**
1.  **Secrets Manager:** Enable rotation for the RDS secret.
2.  **Lambda:** Secrets Manager uses a Lambda function to update the password in the RDS database and then update the secret value.
3.  **App Level:** Use the AWS SDK to fetch the secret from Secrets Manager at runtime (or cache it) instead of hardcoding it.

### Scenario: "An engineer accidentally deleted an EBS volume. How do you find out who did it?"
**Key Details:**
*   **CloudTrail:** Go to "Event History" and filter by `Event name = DeleteVolume`.
*   **Identify:** Look at the `userIdentity` field to see the IAM user or role responsible.
*   **Prevention:** Use **AWS Config** rules to alert on volume deletions or **MFA Delete** logic (if supported via SCPs).
