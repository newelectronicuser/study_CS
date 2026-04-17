# Patterns and Ecosystem

Standard solutions to recurring problems and the wider tools used to manage Kubernetes at scale.

## 1. Multi-Container Pod Patterns
- **Sidecar Pattern**: A container that enhances or extends the main container (e.g., a logging agent like `Fluentd`).
- **Adapter Pattern**: A container that standardizes output from the main container so it can be consumed by external systems (e.g., exposing custom metrics in Prometheus format).
- **Ambassador Pattern**: A proxy that handles network communication for the main container (e.g., connecting a local app to a database with SSL/authentication).

## 2. Helm
The **package manager** for Kubernetes. 
- It uses **Charts** (packages of pre-configured K8s resources) to deploy complex applications with a single command (`helm install`).
- Allows for versioning, rollbacks, and templating (using `values.yaml`).

## 3. Operators and CRDs
- **Custom Resource Definitions (CRDs)**: Allows you to define your own Kubernetes objects (e.g., a `Databases` or `FrontendApp` object).
- **Operator**: A method of packaging, deploying, and managing a Kubernetes application. It uses a custom controller to watch CRDs and take domain-specific actions (e.g., an "SQL Operator" that handles backups and failovers automatically).

## 4. Deployment Strategies
- **Rolling Update (Default)**: Replaces pods one by one.
- **Canary Deployment**: Routes a small percentage of traffic to the new version before rolling it out to everyone.
- **Blue-Green Deployment**: Switches 100% of traffic from the old version (Blue) to the new version (Green).
- **Recreate**: Kills all old pods before starting new ones (results in downtime).

## 5. Affinity and Anti-Affinity
Control which nodes your Pods are scheduled on.
- **Node Affinity**: Pulls Pods toward nodes with specific labels.
- **Pod Anti-Affinity**: Ensures that specific Pods are NOT scheduled on the same node (e.g., keeping two replicas of a database on different nodes for high availability).

## 6. Taints and Tolerations
- **Taints**: Applied to a **Node**. It allows a node to "repel" a set of pods.
- **Tolerations**: Applied to a **Pod**. It allows the pod to be scheduled on a node with matching taints.
- Use case: Reserving a node with high-end GPUs for only specific worker pods.

> [!TIP]
> **Kustomize**: 
> A template-free way to customize Kubernetes configurations. Since K8s 1.14, it is built directly into `kubectl` (`kubectl apply -k`).

> [!IMPORTANT]
> **GitOps**: 
> A modern operational framework that uses Git as the "single source of truth" for infrastructure and applications. Tools like **ArgoCD** or **Flux** automatically sync the state of your K8s cluster with your Git repository.
