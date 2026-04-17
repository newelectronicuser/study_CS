# Advanced Jenkins Concepts

To manage Jenkins efficiently at scale, you need to move beyond single-repo pipelines.

## 1. Jenkins Shared Libraries
As you add more projects, you'll find that many pipelines use the same logic (e.g., standard way to run SonarQube or deploy to Kubernetes).
- **Shared Libraries** allow you to write reusable Groovy code in a separate repository.
- You can then import this library into any `Jenkinsfile` using `@Library('my-shared-library') _`.
- This promotes the **DRY (Don't Repeat Yourself)** principle and significantly reduces the size and complexity of individual Jenkinsfiles.

## 2. JCasC (Jenkins Configuration as Code)
Managing Jenkins via the UI is slow and prone to human error.
- **JCasC** allows you to define your entire Jenkins configuration (Plugins, Security, Credentials, Nodes) in a single YAML file.
- When Jenkins starts, it reads this file and configures itself.
- This allows you to treat your Jenkins server like any other infrastructure component: versioned in Git and easily reproducible.

## 3. Multibranch Pipelines
In a modern Git workflow, developers create many branches.
- Instead of creating a job for every branch manually, a **Multibranch Pipeline** automatically scans your repository.
- It creates a build job for every branch that contains a `Jenkinsfile`.
- When a branch is deleted in Git, Jenkins automatically deletes the corresponding job.

## 4. Master-less / Ephemeral Jenkins
The modern trend is to avoid maintaining a "Pet" Jenkins server.
- Using **Jenkins Operator** on Kubernetes, you can spin up a Jenkins instance on-demand.
- With **JCasC** and **Job DSL**, the instance is pre-configured and ready to run jobs immediately.

## 5. Job DSL (Domain Specific Language)
A plugin that allows you to define Jenkins jobs (even legacy Freestyle ones) using a programmatic API. This was the precursor to the modern Pipeline.

> [!IMPORTANT]
> **Shared Library Structure**: 
> A shared library must follow a specific directory structure:
> - `src/`: Standard Java/Groovy classes.
> - `vars/`: Global variables (these become "steps" in your pipeline).
> - `resources/`: External resources like JSON or scripts.

> [!TIP]
> **Unit Testing Shared Libraries**: 
> Use the **JenkinsPipelineUnit** framework to test your shared library code locally without needing a running Jenkins server.
