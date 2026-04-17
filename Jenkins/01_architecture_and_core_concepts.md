# Architecture and Core Concepts

Jenkins is an open-source automation server that enables developers to reliably build, test, and deploy their software.

## 1. Master-Agent Architecture
Jenkins uses a distributed architecture to handle large numbers of jobs and complex builds.

### Controller (Formerly Master)
- The central brain of the Jenkins installation.
- Handles the UI, configuration, and scheduling of jobs.
- Dispatched actual build work to the Agents.
- **Note**: Best practice is to **NEVER** run build jobs on the Controller itself to prevent performance bottlenecks or stability issues.

### Agents (Formerly Slaves / Nodes)
- Separate machines (physical, virtual, or containers) that perform the actual build work.
- They connect to the Controller via SSH or JNLP.
- A cluster can have hundreds of agents running across different OS environments (Linux, Windows, macOS).

## 2. Core Terms
- **Executor**: A "slot" on an agent where a single build task can run. An agent can have multiple executors (usually matching the number of CPU cores).
- **Workspace**: A dedicated directory on the agent where the source code is checked out and the build is performed.
- **Build**: One specific execution of a job. Each build is assigned a unique number.
- **Item (Job)**: A task that Jenkins performs (e.g., pulling code, running a script).

## 3. Item Types
- **Freestyle Project**: The legacy way. You configure the job via the UI by filling out forms. (Not recommended for professional use).
- **Pipeline**: The modern way. Defined as code (Groovy) and stored in a `Jenkinsfile`.
- **Multibranch Pipeline**: Automatically creates a set of Pipeline projects for every branch in your repository.

> [!IMPORTANT]
> **Why Distributed Builds?**
> If you have 50 teams running builds at the same time, a single machine will crash. The Master-Agent model allows you to scale horizontally by adding more executors and agents as needed.

> [!TIP]
> **Dynamic Agents**: 
> In modern DevOps, it is common to use **Docker** or **Kubernetes** to spin up "ephemeral agents" that only exist for the duration of a build and are destroyed afterward.
