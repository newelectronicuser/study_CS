# 05 Advanced Automation & CI/CD

## Core Concepts

### Terraform Workspaces
Allows you to have multiple states for a single configuration.
*   **Default Workspace:** The initial workspace created by `init`.
*   **Isolation:** Each workspace has its own state file.
*   **Access in Code:** `${terraform.workspace}` returns the current workspace name.

### GitOps & PR Workflows
Using Git as the "Source of Truth" for infrastructure.
*   **PR/MR Workflow:** A user creates a branch, runs `terraform plan` in CI, a reviewer approves the plan output in the comment, and merging triggers `terraform apply`.
*   **Tools:** Atlantis (PR-based automation), GitHub Actions, GitLab CI/CD, Terraform Cloud/Enterprise.

### Terragrunt
A thin wrapper for Terraform that provides extra tools for keeping your configurations DRY, working with multiple Terraform modules, and managing remote state.
*   **Key Feature:** Prevents repeating the same `backend` block in every module folder.

---

## Interview Deep Dives

### 1. "Workspaces vs. Directory-based separation: Which is better?"
**Answer:**
*   **Workspaces:** Good for managing multiple environments of the *exact same* configuration (e.g., testing different feature branches in an ephemeral account).
*   **Directories:** Better for long-lived, differing environments (e.g., Prod vs. Dev). Why?
    1.  **Visibility:** You can see different variables/configs in separate files/folders.
    2.  **Safety:** Harder to accidentally run an apply in the wrong workspace.
    3.  **Backends:** Directories can have different backend configurations (different buckets/regions).

### 2. "How do you handle 'Infrastructure Drift' in a CI/CD pipeline?"
**Answer:**
*   **Detection:** Run a scheduled `terraform plan -detailed-exitcode` in CI. If the exit code is 2 (changes detected), alert the team.
*   **Remediation:** 
    1.  **Automated:** Automatically run `apply` (only recommended for non-critical resources).
    2.  **Manual:** Review the drift, fix the code to match the manual change, or run `apply` to revert the manual change.

### 3. "Explain the 'Atlantis' workflow."
**Answer:**
*   It's a self-hosted application that listens for GitHub/GitLab webhooks. 
*   When a PR is opened, Atlantis runs `plan` and comments on the PR. 
*   Developers can run `atlantis apply` via a comment. 
*   It handles **state locking at the PR level**, stopping other PRs from affecting the same state until the first one is merged/closed.

---

## Senior Level Trade-offs

| Feature | Best For | Trade-off |
| :--- | :--- | :--- |
| **Monorepo** | Consistency, single source | Massive CI/CD jobs; blast radius concerns if not carefully partitioned. |
| **Multi-repo** | Isolation, team autonomy | Dependency management becomes harder; code duplication. |
| **Terragrunt** | DR-ying up backends/configs | Added layer of abstraction; non-native Terraform syntax. |

---

## Common Interview Scenarios

### Scenario: "Your Terraform plan takes 20 minutes to run. How do you speed it up?"
**Troubleshooting/Design:**
1.  **Parallelism:** Use the `-parallelism=$N$` flag (default is 10). Increase it if the cloud provider API limits allow.
2.  **Targeting:** Use `-target` for surgical updates (last resort as it bypasses the full graph).
3.  **Refactor:** Split the monolithic state into smaller components (e.g., separate networking from application).
4.  **Refresh-only:** Use `-refresh=false` if you are certain the state is mostly accurate (use with caution).

### Scenario: "How do you ensure that an 'apply' only happens after a human approval in GitHub Actions?"
**Key Details:**
*   Use **GitHub Environments** with "Required Reviewers."
*   The `plan` job runs first and saves the plan to an artifact (`-out=tfplan`).
*   The `apply` job is linked to the "Production" environment and only runs after approval, using the saved `tfplan` to ensure consistency.
