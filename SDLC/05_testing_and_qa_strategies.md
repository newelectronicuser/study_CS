# Testing and QA Strategies

Quality Assurance (QA) is more than just finding bugs; it is about building a process that ensures high-quality deliverables.

## 1. Levels of Testing
- **Unit Testing**: Testing individual components or functions in isolation. Usually done by developers.
- **Integration Testing**: Testing the interaction between two or more components.
- **System Testing**: Testing the entire application as a whole against the requirements.
- **Acceptance Testing (UAT)**: Final testing performed by the client/business to ensure the product meets their needs.

## 2. Common Testing Types
- **Smoke Testing**: A quick test to see if the main features are working before committing to deeper testing.
- **Regression Testing**: Re-testing existing features to ensure new changes haven't broken them.
- **Exploratory Testing**: Manual, unscripted testing where the tester "explores" the app to find edge cases.
- **Performance Testing**: Checking the speed, scalability, and stability under load (Load, Stress, Endurance).
- **Security Testing**: Identifying vulnerabilities (SQL Injection, XSS, etc.).

## 3. The Testing Pyramid
A concept that suggests you should have a large base of Unit Tests, a smaller middle layer of Integration Tests, and a tiny top layer of End-to-End (E2E) UI Tests.
- **Why?**: Unit tests are fast, cheap, and precise. UI tests are slow, flaky, and hard to maintain.

## 4. QA Methodologies
- **TDD (Test Driven Development)**: Writing the test *before* the code. (Red-Green-Refactor).
- **BDD (Behavior Driven Development)**: Writing tests in human-readable language (Given-When-Then) based on user stories.
- **Shift-Left Testing**: The practice of moving testing to as early as possible in the SDLC (e.g., during requirements or design).

## 5. Black Box vs. White Box
- **Black Box**: Tester doesn't know the internal code. Focuses on inputs and outputs.
- **White Box**: Tester knows the internal code. Focuses on logic, branches, and statements.

> [!IMPORTANT]
> **Bug life cycle**: New -> Assigned -> Open -> Fixed -> Pending Retest -> Verified -> Closed.
