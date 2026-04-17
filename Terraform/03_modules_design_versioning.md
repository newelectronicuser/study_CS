# 03 Module Design & Versioning

## Core Concepts

### Terraform Modules
A module is a container for multiple resources that are used together.
*   **Root Module:** The current working directory containing the main `.tf` files.
*   **Child Modules:** Modules called from another module.
*   **Best Practice:** Keep modules "focused" (e.g., a module for an S3 bucket with policy, not a module for an entire VPC + Apps).

### Logic: Count vs. For_Each
*   **count:** Best for creating $N$ identical resources. Access via index (e.g., `aws_instance.web[0]`).
    *   *Issue:* If you remove `web[1]`, Terraform might re-index all subsequent resources, causing unnecessary destruction/recreation.
*   **for_each:** Best for creating resources based on a map or set of strings. Access via key (e.g., `aws_instance.web["api"]`).
    *   *Benefit:* Stability. Adding/removing items doesn't affect other resources in the map.

---

## Interview Deep Dives

### 1. "Composition vs. Configuration: How do you design flexible modules?"
**Answer:**
*   **Configuration:** Passing 50+ variables to a single "do-it-all" module. This becomes hard to maintain and fragile.
*   **Composition:** Passing the *output* of one focused module into another. 
    *   *Example:* A `network` module outputs a `subnet_id`, which is then passed into an `app` module. This keeps modules small, testable, and reusable.

### 2. "How do you handle module versioning?"
**Answer:**
1.  **Semantic Versioning:** Tag your module git repository with versions (e.g., `v1.2.3`).
2.  **Source Pinning:** In the calling code, always specify a version:
    ```hcl
    module "vpc" {
      source  = "github.com/org/terraform-aws-vpc?ref=v1.2.3"
      # ...
    }
    ```
3.  **Registry:** Use a Private Registry (Terraform Cloud, Artifactory) for easier discovery and better version management.

### 3. "Explain Dynamic Blocks."
**Answer:**
*   **Function:** They allow you to generate repeated nested blocks (like `ingress` rules in a security group) based on a list or map.
*   **Why use it:** Avoids hardcoding multiple blocks and makes the module adaptable to different requirements without manual code changes.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Count** | Simple replication (e.g., 3 identical app nodes) | Index-shifting issues when removing items. |
| **For_each** | Heterogeneous resources | Map keys must be known at plan time (cannot be derived from non-static resource outputs easily). |
| **Local Modules** | Quick iteration | Harder to share across teams/repos. |

---

## Common Interview Scenarios

### Scenario: "You have a module that works for AWS but now need to support Azure. How do you handle this?"
**Troubleshooting/Design:**
1.  **Don't Multi-Cloud one module:** Trying to make a single module support two different providers leads to complex "if/else" logic and bloat.
2.  **Separate Modules:** Create a `network-aws` and a `network-azure` module.
3.  **Abstractions:** Use a top-level "Environment" composition that calls the correct provider-specific module based on a variable.

### Scenario: "How do you validate that someone passed a valid environment name to your module?"
**Key Details:**
*   Use **Variable Validation** (v0.13+):
    ```hcl
    variable "env_name" {
      type = string
      validation {
        condition     = contains(["prod", "stage", "dev"], var.env_name)
        error_message = "env_name must be prod, stage, or dev."
      }
    }
    ```
    This fails early during `terraform plan` rather than failing mid-apply.
