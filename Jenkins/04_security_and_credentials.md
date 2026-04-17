# Security and Credentials Management

As Jenkins handles your source code, deployment keys, and production access, securing it is paramount.

## 1. Authentication (Who are you?)
Jenkins can verify users against several sources:
- **Jenkins' own user database**: Good for small teams.
- **LDAP / Active Directory**: The corporate standard.
- **SAML / OAuth**: Single Sign-On (SSO) with providers like Google, GitHub, or Okta.

## 2. Authorization (What can you do?)
- **Matrix-based security**: Fine-grained permissions (Read, Build, Delete) per user/group.
- **Role-Based Strategy (RBAC)**: Create roles (e.g., "Developer," "Admin," "Viewer") and assign users to them. This is much easier to manage at scale than matrix-based security.

## 3. Credentials Management
Jenkins provides a built-in "Credentials" library to store sensitive data.
- **Supported types**: SSH usernames with private keys, Secret text (API tokens), Secret file, Username with password.
- **Scopes**:
    - **Global**: Available everywhere in Jenkins.
    - **System**: Available only to the Jenkins master (used for SSH agents).
    - **User**: Available only to a specific user.

## 4. Injecting Credentials into Pipelines
Use the `credentials()` helper to securely inject secrets into your build script.
```groovy
pipeline {
    agent any
    environment {
        DB_PASSWORD = credentials('database-password-id')
    }
    stages {
        stage('Deploy') {
            steps {
                sh "deploy_script.sh --password $DB_PASSWORD"
            }
        }
    }
}
```

## 5. Script Security and Sandboxing
By default, Groovy scripts in Jenkins run in a "Sandbox" to prevent them from executing malicious code that could compromise the master node. Some advanced methods require "Script Approval" by an administrator.

## 6. CSRF Protection
Jenkins has built-in protection against Cross-Site Request Forgery. Ensure it is enabled in the "Global Security" settings.

> [!CAUTION]
> **Credential Masking**: 
> Jenkins automatically masks known secrets in the console output (showing `****` instead). However, avoid printing secrets intentionally or using them in ways that could expose them (e.g., in URL parameters).

> [!TIP]
> **Project-based Matrix Authorization**: 
> If you have sensitive projects (e.g., "HR-Data"), use project-based matrix authorization to ensure that only the specific team can see or build that job, even if other developers have global "Read" access.
