# 04 Variables, Outputs & Data Sources

## Core Concepts

### Variables (Inputs)
Parameterize your configuration.
*   **Types:** `string`, `number`, `bool`, `list`, `map`, `object`, `set`.
*   **Default values:** Make a variable optional.
*   **Sensitive flag:** Prevents the variable value from being logged to the console (but it remains in the state file).

### Outputs
Expose information about your infrastructure.
*   **Use Cases:** Showing resource IPs/DNS at the end of `apply`, or passing data between modules/state files.

### Data Sources
Allow Terraform to use information defined outside of Terraform, or defined by another separate Terraform configuration.
*   **Read-only:** They don't create resources; they just fetch data (e.g., finding the latest Amazon Linux AMI ID).

---

## Interview Deep Dives

### 1. "Variable vs. Local: When to use which?"
**Answer:**
*   **Variables:** Use for values that change between environments or are provided by the user (like "instance_type" or "region").
*   **Locals:** Use for internal logic, computed values, or to avoid repeating the same complex expression. They are not visible to the user of the module.
    *   *Analogy:* Variables are like "Function Arguments," and Locals are like "Let/Const variables" inside the function.

### 2. "How do you handle secrets (DB passwords) in variables?"
**Answer:**
1.  **Mark as Sensitive:** Set `sensitive = true` in the variable definition.
2.  **External Injection:** Don't put them in `.tfvars` files committed to Git. Inject them via environment variables (`TF_VAR_password`) or fetch them from a secure store using a **Data Source** (e.g., `aws_secretsmanager_secret_version`).
3.  **Encrypted Backend:** Ensure the backend is encrypted because the secret will still be in the state file.

### 3. "Explain the 'Refresh' stage and its impact on Data Sources."
**Answer:**
*   By default, `terraform plan` and `apply` refresh the state. This means Terraform re-reads all data sources. 
*   If a data source depends on a resource being created in the same apply (e.g., querying for an ID of a resource created in the same commit), it may have to wait until the resource is finished before the query can run.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Variable Validation** | Enforcing naming conventions | Adds boilerplate code; can be complex to write for objects. |
| **Remote State Data** | Decoupling state files | Tight coupling to the structure of the other state file. |
| **Hardcoded Values** | Quick proof-of-concept | Zero reusability; brittle code. |

---

## Common Interview Scenarios

### Scenario: "How do you find the latest Ubuntu AMI in a specific region automatically?"
**Troubleshooting/Design:**
*   Use the `aws_ami` data source with filters:
    ```hcl
    data "aws_ami" "ubuntu" {
      most_recent = true
      filter {
        name   = "name"
        values = ["ubuntu/images/hvm-ssd/ubuntu-focal-20.04-amd64-server-*"]
      }
      owners = ["099720109477"] # Canonical
    }
    ```
    This ensures your code always picks the latest patched image without manual updates.

### Scenario: "You have a list of IPs and need to filter out only those from a specific range."
**Key Details:**
*   Use a **Local** with a **for loop** and **if condition**:
    ```hcl
    locals {
      trusted_ips = [for ip in var.all_ips : ip if can(regex("^10\\.0\\.", ip))]
    }
    ```
    Senior engineers should be comfortable with HCL collection transformation expressions.
