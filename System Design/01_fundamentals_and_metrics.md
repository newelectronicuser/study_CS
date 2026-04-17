# Fundamentals and Metrics

System design is the process of defining the architecture, components, and data for a system to satisfy specified requirements.

## 1. Key Metrics
- **Latency**: The time it takes for a request to be fulfilled (measured in ms).
- **Throughput**: The number of requests a system can handle in a given period (measured in QPS - Queries Per Second).
- **Availability**: The percentage of time a system is operational. Measured in "nines" (e.g., 99.9% is "three nines").
- **Reliability**: The probability that a system will perform its function without failure over a time interval.

## 2. Scalability
The ability of a system to handle a growing amount of work by adding resources.
- **Vertical Scaling**: Adding more power (CPU, RAM) to an existing server. (Limited by hardware).
- **Horizontal Scaling**: Adding more servers to the pool. (Practically unlimited).

## 3. Reliability vs. Availability
A system can be available but not reliable (returning errors).
- **Reliability**: A system is reliable if it does what it's supposed to do.
- **Availability**: A system is available if it's "up" and can be reached.

## 4. The CAP Theorem
In a distributed system, you can only provide two of the following three guarantees:
- **Consistency**: Every read receives the most recent write or an error.
- **Availability**: Every request receives a (non-error) response, without the guarantee that it contains the most recent write.
- **Partition Tolerance**: The system continues to operate despite an arbitrary number of messages being dropped or delayed by the network between nodes.

> [!IMPORTANT]
> **Partition Tolerance is a MUST**: 
> In the real world, network partitions *will* happen. Therefore, you must choose between **CP** (Consistency and Partition Tolerance) or **AP** (Availability and Partition Tolerance).

## 5. PACELC Theorem
An extension of CAP.
- **P (Partition)**: If there is a partition, choose between **Availability** or **Consistency**.
- **E (Else)**: Else (no partition), choose between **Latency** or **Consistency**.

> [!TIP]
> **SLA vs SLO vs SLI**:
> - **SLI (Indicator)**: What we measure (e.g., Latency).
> - **SLO (Objective)**: What we want to achieve (e.g., Latency < 100ms).
> - **SLA (Agreement)**: The legal contract with the user (e.g., SLO must be met 99.9% of the time combined with a penalty for failure).
