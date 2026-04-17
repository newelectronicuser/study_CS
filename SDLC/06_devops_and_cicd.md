# DevOps and CI/CD

DevOps is a set of practices that combines software development (Dev) and IT operations (Ops) to shorten the SDLC and provide continuous delivery.

## 1. The DevOps Lifecycle
A continuous loop involving:
**Plan -> Code -> Build -> Test -> Release -> Deploy -> Operate -> Monitor.**

## 2. CI/CD Pipeline
Continuous Integration and Continuous Delivery are the heart of DevOps.

### Continuous Integration (CI)
The practice of merging all developer working copies to a shared mainline several times a day.
- **Goal**: Identify and fix integration errors quickly.
- **Process**: Code is pushed -> Building -> Automated Unit/Integration tests.

### Continuous Delivery (CD)
Automatically preparing code changes for a release to production.
- **Goal**: Ensure that Every build is "releasable" at any time.
- **Process**: CI -> Deployment to Staging/Pre-prod -> Manual approval for Production.

### Continuous Deployment (CD)
Every change that passes all stages of your production pipeline is released to your customers automatically.
- **Goal**: Eliminate manual intervention in the release process.

## 3. Infrastructure as Code (IaC)
Managing and provisioning infrastructure through machine-readable definition files (e.g., Terraform, Ansible) rather than manual configuration.
- **Pros**: Consistency, speed, version control for infrastructure.

## 4. Shift-Left and Shift-Right
- **Shift-Left**: Testing, security, and performance analysis are moved to the early stages of development.
- **Shift-Right**: Testing in production (A/B testing, Canary releases) and heavy focus on monitoring/observability after deployment.

## 5. Deployment Strategies
- **Rolling Deployment**: Update one server at a time.
- **Blue-Green**: Fast switch between two identical environments.
- **Canary**: Deploy to 5-10% of users first.

> [!TIP]
> **DevOps is a culture**, not just a set of tools. It requires breaking down silos between departments and fostering a shared responsibility for the product's success.
