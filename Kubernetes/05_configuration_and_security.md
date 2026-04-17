# Configuration and Security

Decoupling configuration from containers and securing the cluster are essential for production systems.

## 1. ConfigMaps
Used to store non-confidential data in key-value pairs. High-level pods can use ConfigMaps as:
- Environment variables.
- Command-line arguments.
- Configuration files in a volume.

## 2. Secrets
Similar to ConfigMaps but intended for small amounts of sensitive data (passwords, tokens, keys).
- Secrets are stored as base64-encoded strings.
- In modern clusters, Secrets can be encrypted at rest in etcd.
- It's common to integrate with external secrets managers (AWS Secrets Manager, HashiCorp Vault).

## 3. RBAC (Role-Based Access Control)
Manages WHO can do WHAT to WHICH resources.
- **Role**: Defines permissions within a specific namespace.
- **ClusterRole**: Defines permissions cluster-wide.
- **RoleBinding / ClusterRoleBinding**: Links a User, Group, or ServiceAccount to a Role or ClusterRole.

## 4. Service Accounts
Identities for processes running in your Pods. When a process communicates with the API Server, it authenticates as a specific Service Account.

## 5. Security Contexts
Definitions of privilege and access control settings for a Pod or Container.
- **`runAsUser`**: Which Linux user ID to use.
- **`allowPrivilegeEscalation`**: Whether a process can gain more privileges than its parent.
- **`readOnlyRootFilesystem`**: Whether the container has a read-only root filesystem.

## 6. Network Policies
Already discussed in Networking, but critical for security. It provides Layer 3/4 segmentation between Pods.

## 7. Namespace
A virtual cluster within a physical cluster. They are used to partition resources into logically named groups (e.g., `dev`, `staging`, `prod`).

> [!CAUTION]
> **Secrets are not Encrypted by Default**: 
> By default, Kubernetes Secrets are sadece base64 encoded, not encrypted. Anyone with access to the API or etcd can decode them easily. You must enable "Encryption at Rest" or use an external provider for true security.

> [!TIP]
> **Principle of Least Privilege**: 
> Always assign the minimum necessary permissions to your Service Accounts and Users using RBAC. Avoid using the default `cluster-admin` role unless absolutely necessary.
