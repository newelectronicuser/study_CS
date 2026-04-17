# 01 HCL & Lifecycle Basics

## Core Concepts

### HCL (HashiCorp Configuration Language)
Terraform uses HCL, a declarative language designed to be human-readable and machine-friendly.
*   **Declarative:** You define the *desired state*, and Terraform determines the actions to reach that state.
*   **Implicit Dependencies:** Terraform automatically builds a dependency graph based on resource references.
*   **Explicit Dependencies:** Use the `depends_on` meta-argument when a dependency isn't visible via references (e.g., a role must exist before an instance uses it, but the instance doesn't reference the role directly).

### The Terraform Lifecycle
1.  ** terraform init:** Initializes the working directory, downloads providers, and configures the backend.
2.  ** terraform plan:** Creates an execution plan. It compares the current state with the configuration and identifies changes.
3.  ** terraform apply:** Executes the actions proposed in the plan.
4.  ** terraform destroy:** Terminate all managed infrastructure.
5.  ** terraform refresh:** Updates the local state file with the real-world infrastructure (deprecated in favor of `plan -refresh-only`).

---

## Interview Deep Dives

### 1. "Explain the difference between Declarative and Imperative IaC."
**Answer:**
*   **Declarative (Terraform):** You say "I want 3 servers." If 1 exists, Terraform creates 2. It manages the "how."
*   **Imperative (Ansible/Scripts):** You say "Create a server, then create another, then another." You must handle the logic for existing resources, making scripts harder to maintain at scale.

### 2. "Why are Provisioners considered a 'last resort' in Terraform?"
**Answer:**
*   **Idempotency:** Provisioners (e.g., `remote-exec`) often don't handle state well. If a script fails halfway, Terraform might "taint" the resource, but the partial changes remain.
*   **Complexity:** They require SSH/WinRM access, which complicates networking and security.
*   **Better Alternatives:** Use **Cloud-Init (User Data)**, **Image Baking (Packer)**, or **Configuration Management (Ansible/Chef)** after the infrastructure is provisioned.

### 3. "What is Terraform 'Tainting' and how has it changed?"
**Answer:**
*   **Old Way:** `terraform taint` marked a resource for recreation during the next apply because it was in an unstable state.
*   **New Way (v0.15+):** Use `terraform apply -replace="resource_name"`. This achieves the same result without manually modifying the state file first.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Depends_on** | Ensuring execution order | Can lead to overly rigid graphs; use sparingly. |
| **Provisioners** | Quick one-off scripts | Hard to debug; breaks the "pure" declarative model. |
| **Alias (Providers)** | Multi-region deployments | Increases code complexity; must pass aliases to modules. |

---

## Common Interview Scenarios

### Scenario: "A resource was manually deleted in the AWS Console. How does Terraform handle this?"
**Troubleshooting/Design:**
1.  **Plan:** When you run `terraform plan`, Terraform refreshes the state. It will see the resource is missing.
2.  **Correction:** The plan will show a `+ create` action for the missing resource to bring the real world back into alignment with the code.

### Scenario: "You need to change a resource name in your code without destroying and recreating the actual infrastructure."
**Key Details:**
*   Use the `terraform state mv` command to rename the resource in the state file to match the new name in your `.tf` code.
*   Alternatively, in Terraform 1.1+, use a **`moved` block** in your code:
    ```hcl
    moved {
      from = aws_instance.old_name
      to   = aws_instance.new_name
    }
    ```
    This is the preferred "Senior" answer as it is version-controlled and doesn't require manual CLI commands.
