# Delivery and Deployment Patterns

Deployment patterns define how you move new code into production while minimizing downtime and risk.

## 1. Blue-Green Deployment
You maintain two identical production environments: "Blue" (current version) and "Green" (new version).
- **How it works**: Deploy the new version to Green. Run tests. If successful, switch the load balancer to point to Green.
- **Pros**: Zero downtime, instant rollback (switch back to Blue).
- **Cons**: Expensive (requires double the resources).

## 2. Canary Deployment
Releasing the new version to a small subset of users before rolling it out to the entire infrastructure.
- **How it works**: Direct 5-10% of traffic to the new version. Monitor metrics. If no errors, gradually increase traffic to 100%.
- **Pros**: Small "blast radius" for bugs, real-world testing.
- **Cons**: Complex routing logic and monitoring requirements.

## 3. Rolling Update (Ramped)
Replacing the old version with the new version one instance at a time.
- **How it works**: Stop one old instance, start one new instance. Repeat until all are updated.
- **Pros**: Efficient use of resources, zero downtime.
- **Cons**: Slow rollout, and for a period, both old and new versions are running simultaneously (can cause data/API schema issues).

## 4. Recreate
Killing all old instances before starting the new ones.
- **How it works**: Shutdown V1 -> Start V2.
- **Pros**: Easy to implement, no compatibility issues between versions.
- **Cons**: **Downtime** is guaranteed.

## 5. Feature Flags (Feature Toggles)
Decoupling **Deployment** (moving code to prod) from **Release** (giving users access to the feature).
- **Strategy**: You deploy the code in an "off" state. You then flip a switch in a management dashboard to enable it for specific users or everyone.

## 6. A/B Testing
Often confused with Canary.
- **Canary** is about **stability** and technical risk.
- **A/B Testing** is about **business impact** (e.g., does the green button result in more sales than the red button?).

> [!IMPORTANT]
> **Database Versioning**: 
> Code is easy to roll back; databases are hard. Always ensure your database migrations are backward-compatible so that the old code can still run even if the database has been updated.

> [!TIP]
> **Summary for Interviews**:
> - Use **Blue-Green** for critical monolithic apps where you need instant rollbacks.
> - Use **Canary** for microservices and high-traffic high-performance apps to limit risk.
> - Use **Rolling** for standard Kubernetes deployments.
