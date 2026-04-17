# IAM and Security

Security is "Job Zero" at AWS. IAM is the core service that controls access to everything.

## 1. IAM Evaluation Logic
One of the most common senior-level interview questions is how IAM evaluates multiple policies.

> [!IMPORTANT]
> **The Decision Chain**:
> 1.  **Explicit Deny**: If any policy (IAM, SCP, Resource-based) contains a `Deny`, the request is denied immediately.
> 2.  **Explicit Allow**: If there is no Deny, and there is an `Allow`, the request is allowed.
> 3.  **Default Deny**: If there is no explicit Deny and no explicit Allow, the request is denied by default.

## 2. Identities and Entities
- **IAM User**: Long-term credentials (User/Pass or Access Keys). Best for people or applications outside AWS.
- **IAM Group**: Collection of users. Assign permissions to groups, not individual users.
- **IAM Role**: Temporary credentials. Used by AWS services (EC2, Lambda) or for **Cross-Account Access**.
- **Service Account (EKS)**: Linking K8s service accounts to IAM roles (IRSA).

## 3. Policy Types
- **Identity-based**: Attached to a User, Group, or Role.
- **Resource-based**: Attached to a resource (e.g., S3 Bucket Policy, KMS Key Policy).
- **Service Control Policy (SCP)**: Attached to an AWS Organization unit to set "Permissive Bounds."

## 4. Key Security Services

| Service | Advanced Insight |
| :--- | :--- |
| **KMS** | FIPS 140-2 Level 3 compliant. Regional service. Supports Symmetric and Asymmetric keys. |
| **Secrets Manager** | Supports automatic rotation for RDS/Redshift/DocumentDB natively via Lambda. |
| **CloudTrail** | Logs every API call. **Data Events** (like S3 object access) are NOT logged by default and cost extra. |
| **Shield** | **Advanced** provides DDoS cost protection (credits back for scaling charges during attack). |
| **GuardDuty** | Analyzing VPC Flow Logs and DNS query logs without performance impact on the resources. |

## 5. Security Scenarios

### Scenario A: EC2 Needs to access S3
**Wrong**: Store Access Keys in a `.env` file or code.
**Right**: Create an IAM Role with a policy allowing S3 access and attach it to an **EC2 Instance Profile**.

### Scenario B: Cross-Account Access
**Process**: 
1. Define a Role in Account B that allows Account A to **Assume** it (`sts:AssumeRole`).
2. Give Account A user permission to call `AssumeRole`.
3. Account A user calls STS to get temporary credentials for the Role in Account B.

> [!TIP]
> **Inline vs. Managed Policies**: 
> Always prefer **AWS Managed** or **Customer Managed** policies over Inline policies. Managed policies can be reused, versioned, and audited more effectively.

> [!WARNING]
> **Access Key Proliferation**: 
> Avoid creating IAM Users with Access Keys whenever possible. Use IAM Roles for any code running inside AWS.
