# Branching Strategies

How you organize your code repository (SCM) directly impacts the speed and stability of your CI/CD pipeline.

## 1. Trunk-Based Development (TBD)
The industry standard for high-performance teams.
- **How it works**: Developers push small, frequent updates to a single branch (the "trunk," usually `main`).
- **Pros**: Eliminates "merge hell," supports continuous integration, and simplifies the pipeline.
- **Cons**: Requires high discipline and automated tests to prevent breaking the trunk.

## 2. GitFlow
A more traditional, complex model used by teams with structured release cycles.
- **How it works**: Uses multiple long-lived branches: `master` (prod), `develop` (dev), `feature/`, `release/`, and `hotfix/`.
- **Pros**: Great for planned releases and maintaining multiple versions of the software.
- **Cons**: Complex merging, long-lived branches delay integration (violates CI principles).

## 3. GitHub Flow
A lightweight, branch-based workflow.
- **How it works**: create a feature branch from `main`, push commits, open a Pull Request (PR), merge back to `main` after review/test.
- **Pros**: Simple, excellent for open source and many web projects.
- **Cons**: Can lead to long-lived feature branches if PRs aren't reviewed quickly.

## 4. Branching Strategy Comparison

| Strategy | Integration Frequency | Complexity | Best For |
| :--- | :--- | :--- | :--- |
| **Trunk-Based** | Very High | Low | High-velocity SaaS, DevOps teams |
| **GitFlow** | Low | High | Mobile apps, Legacy software, Banks |
| **GitHub Flow** | Medium | Low | Simple Web Apps, Open Source |

## 5. Feature Flags (Feature Toggles)
A technique to turn functionality on or off at runtime without deploying new code.
- **Why?**: It allows you to merge incomplete features into the trunk (Trunk-Based Development) without exposing them to users until they are ready.

> [!IMPORTANT]
> **Branching and CI**: 
> The core goal of CI is to integrate code **at least once a day**. If your branching strategy results in feature branches that stay open for weeks, you are doing "Integration," but you are definitely not doing "Continuous Integration."

> [!TIP]
> **Keep Branches Short-Lived**: 
> Regardless of the strategy, the longer a branch exists, the more difficult it will be to merge. Aim for branches that last no more than 2-3 days.
