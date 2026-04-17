# GitOps and Pull-Based CD

GitOps is a modern operational framework that takes the principles of DevOps (CI/CD, IaC) and applies them to infrastructure and application configuration.

## 1. What is GitOps?
GitOps uses Git as the **"Single Source of Truth"** for the desired state of your entire system.
- Everything (Code, Config, Infrastructure) is versioned in Git.
- The system's actual state is automatically synchronized with the desired state in Git.

## 2. Push vs. Pull CD

### Push Model (Traditional)
A CI tool (like Jenkins or GitHub Actions) executes a script that "pushes" changes to the target environment (e.g., `kubectl apply`).
- **Pros**: Simple to understand, works with any target.
- **Cons**: Requires the CI tool to have high-privilege access keys to the production cluster.

### Pull Model (Modern/GitOps)
An agent running inside the cluster (like ArgoCD or Flux) "pulls" the state from Git and applies it to the cluster.
- **Pros**: More secure (no external keys needed), automatically fixes "Configuration Drift."
- **Cons**: Requires an agent to be installed in every cluster.

## 3. The Reconciliation Loop
The core engine of GitOps. The agent constantly:
1.  Observes the **Actual State** of the cluster.
2.  Compares it to the **Desired State** in Git.
3.  Takes action to correct any differences (e.g., if a developer manually deleted a pod, the agent will recreate it).

## 4. Why use GitOps?
- **Improved Security**: No need for long-lived credentials in your CI pipelines.
- **Easy Rollbacks**: Rolling back is as simple as running `git revert`.
- **Visibility**: Every change to production is captured in a Git commit history.
- **Reliability**: Prevents "manual hacks" in production because the reconciliation loop will overwrite them.

## 5. Tools of the Trade
- **ArgoCD**: Provides a powerful UI and excellent multi-cluster support.
- **Flux**: A set of modular Kubernetes operators for GitOps.
- **Terraform / Pulumi**: Often used to manage the underlying cloud infrastructure that the K8s cluster runs on.

> [!IMPORTANT]
> **GitOps is not just for Kubernetes**: 
> While it started in the K8s ecosystem, the principles of using a versioned Git repo as the source of truth for desired state can be applied to any infrastructure managed by IaC.

> [!TIP]
> **Environment Repo Pattern**: 
> A common GitOps best practice is to have separate repositories for:
> 1. Your **Application Code**.
> 2. Your **Environment Configuration** (YAML/Helm charts). 
> This prevents a code commit from accidentally triggering a production deployment.
