# Introduction and Architecture

## 1. Monolith vs. Microservices
### Monolithic Architecture
A single, unified unit where all components (UI, Business Logic, Data Access) are tightly coupled and deployed together.
- **Pros**: Simpler to develop/test/deploy initially, low latency between components.
- **Cons**: Difficult to scale individual parts, single point of failure, long build times, "Dependency Hell".

### Microservices Architecture
A collection of small, autonomous services that communicate over a network. Each service is self-contained and implements a single business capability.
- **Pros**: Independent scaling, technology diversity (polyglot), faster deployments, fault isolation.
- **Cons**: Operational complexity, distributed system challenges (latency, consistency), difficult to test end-to-end.

## 2. The 12-Factor App Methodology
A set of best practices for building modern, cloud-native applications.
1.  **Codebase**: One codebase tracked in revision control, many deploys.
2.  **Dependencies**: Explicitly declare and isolate dependencies.
3.  **Config**: Store config in the environment.
4.  **Backing services**: Treat backing services as attached resources.
5.  **Build, release, run**: Strictly separate build and run stages.
6.  **Processes**: Execute the app as one or more stateless processes.
7.  **Port binding**: Export services via port binding.
8.  **Concurrency**: Scale out via the process model.
9.  **Disposability**: Fast startup and graceful shutdown.
10. **Dev/prod parity**: Keep development, staging, and production as similar as possible.
11. **Logs**: Treat logs as event streams.
12. **Admin processes**: Run admin/management tasks as one-off processes.

## 3. CAP Theorem
In a distributed system, you can only guarantee two of the following three:
- **Consistency (C)**: Every read receives the most recent write or an error.
- **Availability (A)**: Every request receives a (non-error) response, without the guarantee that it contains the most recent write.
- **Partition Tolerance (P)**: The system continues to operate despite an arbitrary number of messages being dropped or delayed by the network between nodes.

> [!IMPORTANT]
> Since network partitions are inevitable in distributed systems, we must choose between **CP** (Consistency and Partition Tolerance) or **AP** (Availability and Partition Tolerance).

## 4. Scalability Types
- **Vertical Scaling (Scale Up)**: Adding more power (CPU, RAM) to an existing machine. (Has an upper limit).
- **Horizontal Scaling (Scale Out)**: Adding more machines to the pool. (Microservices excel here).

> [!TIP]
> In an interview, don't say Microservices are always better. A "Modular Monolith" is often a great starting point for startups to avoid the "Distributed Systems Overhead" until they truly need the scale.
