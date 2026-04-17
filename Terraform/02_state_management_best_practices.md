# 02 State Management & Backends

## Core Concepts

### Terraform State
The state file (`terraform.tfstate`) is a JSON file that maps your configuration to real-world resources and tracks metadata.
*   **Purpose:** Performance (mapping), Dependency tracking, and determining the "diff" during plans.
*   **Warning:** State files often contain **sensitive data** (metadata, initial passwords) in plain text.

### Remote Backends
Storing state outside your local machine (e.g., S3, Google Cloud Storage, Terraform Cloud).
*   **Benefits:** Collaboration (team access), State Locking, and Security.
*   **State Locking:** Prevents two users from running `apply` simultaneously. For S3, this requires a **DynamoDB table** with a `LockID` primary key.

---

## Interview Deep Dives

### 1. "What is a 'Blast Radius' and how do you reduce it in Terraform?"
**Answer:**
*   **Definition:** The potential impact of a single Terraform failure or accidental deletion.
*   **Reduction Strategy:**
    1.  **Split State Files:** Instead of one giant `main.tf`, split by environment (prod, stage) and by component (networking, app, data).
    2.  **Remote State Data Sources:** Use `terraform_remote_state` to read outputs from other state files without having write access to them.
    3.  **Standardized Modules:** Use versioned modules to ensure consistency.

### 2. "Explain the process of migrating state from local to a remote S3 backend."
**Answer:**
1.  Add the `backend "s3"` block to your configuration.
2.  Run `terraform init`.
3.  Terraform will detect the change and ask if you want to copy the existing state to the new backend.
4.  Answer **Yes**. Terraform handles the migration and locking setup.

### 3. "How do you handle a locked state when the process that held the lock crashed?"
**Answer:**
*   Use the `terraform force-unlock <LOCK_ID>` command.
*   **Caution:** Only do this after verifying that no other process is actually running. You can find the Lock ID in the error message or by checking the DynamoDB table.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **S3 + DynamoDB** | AWS-native teams | Manual setup of the bucket and table (bootstrapping problem). |
| **Terraform Cloud** | Managed experience | Potential vendor lock-in; cost scales with resources/users. |
| **Local Backend** | Quick experimentation | Zero collaboration; high risk of data loss. |

---

## Common Interview Scenarios

### Scenario: "You need to import an existing EC2 instance into your Terraform state."
**Troubleshooting/Design:**
1.  **Write Code:** Create a resource block in your `.tf` file that matches the instance's configuration.
2.  **Import Command:** Run `terraform import aws_instance.example i-1234567890`.
3.  **Verify:** Run `terraform plan`. If the plan shows zero changes, the import was successful. (Note: Terraform 1.5+ supports the `import` block for a more declarative approach).

### Scenario: "How do you handle sensitive data in the state file?"
**Key Details:**
*   **Remote Storage:** Use backends that support encryption-at-rest (S3 with KMS).
*   **Strict RBAC:** Limit who can read the state bucket. (Admins only, CI/CD runners).
*   **Post-processing:** Treat the state file as a "secret" and never output sensitive values to the console unnecessarily.
