# Testing and Security Scans

Automation is useless if you are just automating the deployment of broken or insecure code. Testing and scanning are the "gatekeepers" of the pipeline.

## 1. The Test Pyramid
An ideal distribution of tests to ensure speed and stability.
- **Unit Tests (Base)**: Test individual methods/classes. Fast, numerous, and cheap. (Run on every commit).
- **Integration Tests (Middle)**: Test how different components work together (e.g., API calling a DB). Slower and more complex.
- **E2E / UI Tests (Top)**: Test the entire user flow. Slowest, most fragile, and expensive. (Run at late stages).

## 2. Shift Left Testing
The practice of moving testing earlier in the software development lifecycle. By running tests in the CI stage, you catch bugs long before they reach production.

## 3. Security Scans (DevSecOps)
Integrating security into the pipeline is known as DevSecOps.
- **SAST (Static Application Security Testing)**: Scanning the source code for vulnerabilities (e.g., SonarQube, Snyk). (Fast, run during Build).
- **DAST (Dynamic Application Security Testing)**: Scanning the running application for vulnerabilities (e.g., OWASP ZAP). (Slow, run after Deployment to Staging).
- **SCA (Software Composition Analysis)**: Scanning third-party libraries and dependencies for known vulnerabilities (e.g., NPM audit, Snyk).

## 4. Linting and Code Quality
Checking code for style consistency and potential "code smells" (e.g., ESLint, Pylint). It ensures that the codebase remains maintainable.

## 5. Quality Gates
Conditions that must be met for the pipeline to proceed.
- Example: "Fail the build if code coverage is below 80%" or "Fail the build if any Critical vulnerabilities are found."

## 6. Smoke Testing
A small set of tests run immediately after deployment to production (or staging) to ensure that the core functionality is still working. If it fails, an automated rollback is triggered.

> [!IMPORTANT]
> **Total Coverage is a Myth**: 
> Don't aim for 100% test coverage. It's impossible and often results in "useless" tests. Aim for high coverage in the most critical business logic (the "Happy Path" and known edge cases).

> [!TIP]
> **Component vs. Contract Testing**: 
> In a microservice world, use **Contract Testing** (e.g., Pact) to ensure that changes in Service A don't break Service B that depends on it, without needing to run a full E2E environment.
