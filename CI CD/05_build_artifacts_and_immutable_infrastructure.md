# Build Artifacts and Immutable Infrastructure

How we package and manage our applications determines the consistency and reliability of our deployments.

## 1. Build Artifacts
An artifact is the output of your build process (e.g., a `.jar` file, a `.zip` file, or a Docker image).
- **Rule**: Build Once, Deploy Many. You should never rebuild the same commit for different environments. The exact same artifact should move from Dev -> Staging -> Prod.

## 2. Artifact Repositories
Secure storage for your artifacts.
- **Docker Registry**: (Docker Hub, AWS ECR, GCR) for container images.
- **Binary Repository**: (Nexus, Artifactory) for Java libraries, NPM packages, etc.

## 3. Immutable Infrastructure
A philosophy where servers are never modified after they are deployed.
- **Mutable (Traditional)**: You SSH into a server to update a config file or install a patch. Over time, servers "drift" and become unique ("Snowflake Servers").
- **Immutable**: If you need to change a config or update the app, you build a new machine image (or container) and replace the old one.
- **Benefits**: Consistent environments, easy rollbacks, and predictable scaling.

## 4. Environment Promotion
The process of moving a verified artifact through a series of increasingly production-like environments.
- **Dev**: Rapid testing, frequent failures.
- **Staging / UAT (User Acceptance Testing)**: An exact replica of production where final verification happens.
- **Production**: Where the live users reside.

## 5. Configuration Management
Since the artifact is immutable, how do we handle environment-specific settings (like DB hostnames)?
- **Environment Variables**: The standard way (following Twelve-Factor App principles).
- **Config as Code**: Storing configuration in Git and injecting it at runtime.

## 6. Infrastructure as Code (IaC)
Managing and provisioning infrastructure through machine-readable definition files (e.g., Terraform, CloudFormation, Ansible). 
- IaC allows your infrastructure to be part of the CI/CD pipeline, ensuring that the environment is versioned alongside the code.

> [!IMPORTANT]
> **Build Once, Deploy Many**: 
> If you rebuild your application for each environment, you risk introducing subtle differences (e.g., a different version of a library being pulled) that were never tested in the previous stages.

> [!TIP]
> **Semantic Versioning (SemVer)**: 
> Use SemVer (`MAJOR.MINOR.PATCH`) to tag your artifacts, making it easy to understand the impact of a change.
