# Estimation and Metrics

Measuring progress and estimating work accurately is essential for predictable software delivery.

## 1. Estimation Techniques
Agile teams prefer **Relative Estimation** over Absolute Estimation (hours/days).

- **Story Points**: A unit of measure for expressing an estimate of the overall effort required to fully implement a product backlog item. Points are based on:
    - **Complexity**
    - **Uncertainty**
    - **Effort**
- **Planning Poker**: A consensus-based estimation technique where team members use cards with Fibonacci-like numbers (1, 2, 3, 5, 8, 13...) to estimate story points.
- **T-Shirt Sizing**: Using sizes (XS, S, M, L, XL) for high-level, long-term estimation.
- **Dot Voting**: Team members use "dots" to vote on the importance or size of tasks.

## 2. Key Agile Metrics
- **Velocity**: The average number of story points a team completes in a sprint. Used for long-term capacity planning. (Stable velocity is better than high velocity).
- **Burn-down Chart**: Shows how much work remains in a sprint. (Ideally, the trend is downward to zero).
- **Burn-up Chart**: Shows the total scope of the project and how much has been completed. Useful for tracking scope creep.
- **Lead Time**: Total time from when a requirement is first raised to when it's delivered to the customer.
- **Cycle Time**: Total time from when work starts on a task to when it's completed.

## 3. Quality Metrics
- **Code Coverage**: Percentage of code covered by automated tests.
- **Defect Density**: Number of bugs per 1,000 lines of code.
- **Escaped Defects**: Number of bugs found by customers after release.

## 4. DORA Metrics (DevOps Excellence)
1.  **Deployment Frequency**: How often code is successfully deployed to production.
2.  **Lead Time for Changes**: Time from code commit to production.
3.  **Change Failure Rate**: Percentage of deployments causing a failure in production.
4.  **Failed Service Recovery Time**: Time to restore service after a production failure.

> [!CAUTION]
> **Story points are not hours.** Mapping 1 point to 8 hours is an anti-pattern that leads to pressure and inaccurate estimations. Points should represent relative effort compared to a "baseline" story.
