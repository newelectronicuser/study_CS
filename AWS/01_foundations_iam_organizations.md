# 01 Foundations: IAM & Organizations

## Core Concepts

### IAM (Identity and Access Management)
AWS IAM is the backbone of security in AWS. It allows you to manage access to AWS services and resources securely.

*   **Users:** Permanent credentials for people or applications. (Best practice: Avoid long-term credentials for apps; use Roles).
*   **Groups:** Collections of users. Attach policies to groups rather than individuals for easier management.
*   **Roles:** Temporary credentials. Used by AWS services (EC2, Lambda) or federated users to perform actions.
*   **Policies:** JSON documents that define permissions (Allow/Deny). 
    *   **Identity-based:** Attached to User/Group/Role.
    *   **Resource-based:** Attached to a resource (e.g., S3 Bucket Policy).

### AWS Organizations & SCPs
Used to manage multiple AWS accounts centrally.
*   **Organizational Units (OUs):** Logical grouping of accounts.
*   **Service Control Policies (SCPs):** JSON policies that specify the *maximum* permissions for an organization or OU. 
    *   **Crucial Note:** SCPs do not *grant* permissions; they act as a filter. If an SCP denies `s3:DeleteBucket`, no one in that account (even Root) can delete a bucket.

---

## Interview Deep Dives

### 1. "How do you enforce the Principle of Least Privilege in a large organization?"
**Answer:**
*   Start with **IAM Access Analyzer** to identify unused permissions.
*   Use **Managed Policies** for common tasks, but transition to **Customer Managed Policies** for fine-grained control.
*   Implement **Permissions Boundaries** to limit the maximum permissions an administrator can grant to a role.
*   Use **SCPs** at the Organization level to restrict regions or dangerous services (e.g., disabling `iam:DeleteLogGroup` in production).

### 2. "Roles vs. Users: When to use which?"
**Answer:**
*   **Users:** Use only for human administrators who need console access (ideally behind MFA).
*   **Roles:** Use for everything else. EC2 instances should have Instance Profiles (roles), and Lambda functions should have Execution Roles. Roles provide temporary credentials via STS (Security Token Service), which rotate automatically, reducing the risk of credential leakage.

### 3. "What happens when an identity-based policy allows 'S3:PutObject' but a resource-based bucket policy denies it?"
**Answer:**
*   **Explicit Deny ALWAYS wins.** In AWS evaluation logic:
    1.  Default is Deny.
    2.  Check for Explicit Deny. If found, Final Decision = Deny.
    3.  Check for Explicit Allow. If found, Final Decision = Allow.
    4.  If none found, Final Decision = Deny.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Inline Policies** | Strict 1-to-1 mapping | Hard to manage at scale; cannot be reused. |
| **Managed Polices** | Reusability & Versioning | Limited to 10-20 per identity (quota limits). |
| **Resource Policies** | Cross-account access | Harder to audit (you have to check each resource). |

---

## Common Interview Scenarios

### Scenario: "A developer says they have full S3 access but get 'Access Denied' when trying to upload."
**Troubleshooting Steps:**
1.  Check for **SCPs** at the account level.
2.  Check for **Permissions Boundaries** on the user/role.
3.  Check the **S3 Bucket Policy** for an explicit deny.
4.  Check for **VPC Endpoint Policies** if they are connecting from a VPC.
5.  Check if the object is **encrypted with KMS**; do they have `kms:GenerateDataKey` permissions?
