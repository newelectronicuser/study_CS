# Fundamentals and Definitions

CI/CD is a method to frequently deliver apps to customers by introducing automation into the stages of app development.

## 1. Continuous Integration (CI)
The practice of merging all developer working copies to a shared mainline several times a day.
- **Goal**: Detect integration bugs early.
- **Key Actions**: Automated building, unit testing, and code quality scanning on every commit.
- **Philosophy**: Fail Fast.

## 2. Continuous Delivery (CDE)
An extension of CI that ensures you can release new changes to your customers quickly and in a sustainable way.
- **Goal**: Every build that passes the CI suite is a potential release candidate.
- **Key Actions**: The release to production requires a **manual trigger** or approval.
- **Outcome**: Code is always in a deployable state.

## 3. Continuous Deployment (CD)
Takes Continuous Delivery one step further.
- **Goal**: Every change that passes all stages of your production pipeline is released to your customers automatically.
- **Key Actions**: There is **no manual intervention**.
- **Outcome**: Only failed tests prevent a new change from being deployed to production.

## 4. The Pipeline Lifecycle
A standard CI/CD pipeline consists of the following phases:
1.  **Commit**: Code is pushed to the repository.
2.  **Build**: Compiling the code and building the artifact (e.g., Docker image).
3.  **Test**: Running unit tests and integration tests.
4.  **Scan**: Checking for security vulnerabilities and code smells.
5.  **Deploy**: Pushing the artifact to the target environment.
6.  **Verify**: Post-deployment testing (Smoke tests).

## 5. Benefits of CI/CD
- **Smaller code changes**: Easier to integrate and debug.
- **Faster Mean Time To Resolution (MTTR)**: Due to smaller changes and automated testing.
- **Increased transparency**: Everyone knows the status of the build.
- **Reduced manual errors**: Automation replaces manual deployment scripts.

> [!IMPORTANT]
> **Delivery vs. Deployment**: 
> This is a common interview question.
> - **Continuous Delivery**: Automated build and test, but **manual** stage for production release.
> - **Continuous Deployment**: Automated build, test, and **automatic** release to production.

> [!TIP]
> **Idempotent Deployments**: 
> A deployment should be idempotent, meaning running the same deployment multiple times should have the same effect as running it once, and shouldn't cause errors if the system is already in the desired state.
