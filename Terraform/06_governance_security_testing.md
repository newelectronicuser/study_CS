# 06 Governance, Security & Testing

## Core Concepts

### Policy as Code
Enforcing rules on infrastructure before it is created.
*   **Sentinel:** HashiCorp's proprietary policy engine (requires Terraform Enterprise/Cloud).
*   **OPA (Open Policy Agent):** Open-source general-purpose policy engine. Uses **Rego** language.
    *   *Example Policy:* "No S3 buckets can be public," or "All EC2 instances must have a 'CostCenter' tag."

### Static Analysis & Scanning
Tools that analyze your code *without* running it.
*   **TFLint:** Checks for provider-specific errors (e.g., using an invalid instance type) and enforces best practices.
*   **TFSec / Checkov:** Scans for security vulnerabilities (e.g., open security groups, unencrypted databases).

### Terraform Testing
*   **Terraform Test (v1.6+):** Native framework to write tests in HCL. Best for unit testing modules.
*   **Terratest:** Go-based library for integration testing. It physically creates infrastructure, validates it (e.g., hits an HTTP endpoint), and tears it down.

---

## Interview Deep Dives

### 1. "How do you enforce security guardrails in a large organization?"
**Answer:**
*   **Shift Left:** Integrate `tfsec` or `checkov` into the local IDE and the CI/CD pipeline.
*   **Mandatory Policies:** Use OPA/Sentinel during the `plan` phase of CI. If a policy fails (e.g., "Critical" security violation), the pipeline automatically fails and blocks the merge.
*   **Service Control Policies (SCPs):** Combine Terraform-level policies with cloud-native policies (AWS SCPs) for a defense-in-depth approach.

### 2. "Explain the 'Testing Pyramid' for Infrastructure."
**Answer:**
1.  **Static Analysis (Base):** Faster & Cheapest. (TFLint, TFSec). Catch 80% of typos/misconfigs.
2.  **Contract Testing:** Validate that module inputs/outputs match expectations without creating resources (Terraform 1.6+ `check` blocks).
3.  **Integration Testing (Top):** Slower & Expensive. (Terratest). Spin up real resources to verify functionality.

### 3. "How do you manage provider versions in a large project?"
**Answer:**
*   **Version Constraints:** Use the `required_providers` block with optimistic versioning (e.g., `~> 4.0`).
*   **Dependency Lock File:** Commit the `.terraform.lock.hcl` to version control. This ensures every team member and the CI runner uses the exact same provider binary version.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Sentinel** | Enterprises using HashiStack | Costly; proprietary language. |
| **OPA (Rego)** | Multi-tool policy consistency | Steeper learning curve for Rego syntax. |
| **Terratest** | Critical complex infrastructure | Very slow; incurs cloud costs during test runs. |

---

## Common Interview Scenarios

### Scenario: "How do you prevent developers from committing AWS credentials to Git?"
**Troubleshooting/Design:**
1.  **Git Hooks:** Use `pre-commit` hooks to run `detect-secrets` or `trufflehog`.
2.  **IAM Roles:** Configure CI/CD runners (like GitHub Actions) to use OIDC (OpenID Connect) to assume a role rather than using static access keys.
3.  **Governance:** If a secret is leaked, rotate it immediately and invalidate the old ones using automation.

### Scenario: "Design a policy to ensure all S3 buckets are encrypted."
**Key Details:**
*   Using OPA/Rego:
    ```rego
    deny[msg] {
      resource := input.resource_changes[_]
      resource.type == "aws_s3_bucket"
      not resource.change.after.server_side_encryption_configuration
      msg := "S3 buckets must have encryption enabled"
    }
    ```
    Senior engineers should understand how to parse the `tfplan.json` output in a policy engine.
