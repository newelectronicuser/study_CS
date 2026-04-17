# IAM and Security

Google Cloud IAM lets you manage access control by defining who (identity) has what access (role) for which resource.

## 1. IAM Identities (Who)
- **Google Account**: A standard personal account.
- **Service Account**: An identity for an application or a VM rather than a person. (Used for server-to-server communication).
- **Google Group**: A collection of Google Accounts and Service Accounts. (Recommended for managing permissions).
- **Google Workspace / Cloud Identity**: A domain-level identity provider.

## 2. IAM Roles (What)
A role is a collection of permissions.
- **Primitive Roles**: Owner, Editor, Viewer. (Too broad, avoid in production).
- **Predefined Roles**: Fine-grained roles managed by Google (e.g., `Compute Admin`, `Storage Object Viewer`). (Recommended).
- **Custom Roles**: Allows you to group specific permissions if predefined roles don't meet your needs.

## 3. Policy Inheritance
Permissions are inherited from the top down:
`Organization -> Folder -> Project -> Resource`
- If you have "Viewer" access at the Folder level, you have "Viewer" access to every Project and Resource inside that folder.
- **Note**: You cannot take away an inherited permission. You can only add more.

## 4. Service Account Best Practices
- **Least Privilege**: Only give the service account the minimum permissions it needs.
- **Key Rotation**: If using service account keys (JSON), rotate them regularly.
- **Workload Identity**: In GKE, use Workload Identity to link K8s service accounts to GCP service accounts without needing to manage JSON keys.

## 5. Security Services
- **Cloud KMS**: Key Management Service for managing encryption keys (CMK).
- **Identity-Aware Proxy (IAP)**: Uses identity to guard access to your apps and VMs. It eliminates the need for a VPN for internal applications.
- **Secret Manager**: Securely store and manage API keys, passwords, and certificates.
- **Cloud Armor**: Protects your applications from DDoS attacks and provides a WAF for Global Load Balancer.

> [!IMPORTANT]
> **Policy Evaluation**: 
> GCP IAM only supports **Allow** policies. There is no concept of a "Deny" policy in standard IAM (though VPC Service Controls and Organization Policy Service provide similar restrictive functionality at a higher level).

> [!TIP]
> **Service Account Impersonation**: 
> Instead of downloading JSON keys for your developers, let them "impersonate" a service account. This is much more secure and provides an audit trail in Cloud Logging.
