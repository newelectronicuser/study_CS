# Jenkins Pipelines

Pipelines are the most important feature of modern Jenkins, allowing you to define your CI/CD process as code.

## 1. Pipeline as Code
Instead of clicking buttons in the UI, you write a script (usually named `Jenkinsfile`) that resides in your source code repository. This makes the build process versionable, reviewable, and repeatable.

## 2. Declarative vs. Scripted
Jenkins supports two types of pipeline syntax.

### Declarative Pipeline (Recommended)
- Introduced to provide a more simplified and opinionated syntax.
- Easier to read and write for most users.
- Strict structure (starts with `pipeline { ... }`).
```groovy
pipeline {
    agent any
    stages {
        stage('Build') {
            steps { echo 'Building...' }
        }
    }
}
```

### Scripted Pipeline
- The legacy syntax based on Groovy.
- Much more flexible and powerful but has a steeper learning curve.
- No strict structure; just a Groovy script.

## 3. Core Directives
- **`agent`**: Specifies where the pipeline or a specific stage will execute.
- **`stages`**: Corresponds to a specific part of the process (e.g., Build, Test, Deploy).
- **`steps`**: The actual commands to run (e.g., `sh 'npm install'`).
- **`post`**: Defines actions to run based on the outcome of the build (`always`, `success`, `failure`).
- **`environment`**: Defines variables accessible to all steps.

## 4. Post Actions
Handles notifications or cleanup after the build.
- **`success`**: Runs only if the build finishes with a successful status.
- **`failure`**: Runs if the build fails (perfect for sending Slack/Email alerts).
- **`unstable`**: Runs if the tests failed but the build itself completed.
- **`always`**: Runs regardless of the outcome.

## 5. Parameterized Pipelines
Allows users to provide input values when a build is triggered (e.g., selecting a target environment).

> [!IMPORTANT]
> **Why use Declarative?**
> Declarative pipelines provide better syntax validation, built-in "restart from stage" functionality, and a cleaner UI in Blue Ocean. Choose Declarative unless you have extremely complex logic that requires the full power of the Groovy programming language.

> [!TIP]
> **Blue Ocean**: 
> A modern UI for Jenkins that provides a visual representation of your pipelines, making it much easier to identify exactly where a build failed.
