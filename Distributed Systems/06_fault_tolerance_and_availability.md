# Fault Tolerance and Availability

Distributed systems are designed with the assumption that things **will** fail.

## 1. Type of Failures
- **Crash-fail**: A node stops working and doesn't respond.
- **Crash-recovery**: A node stops but later restarts with its state preserved on disk.
- **Omission fault**: A node fails to send or receive a message.
- **Byzantine fault**: A node behaves arbitrarily or maliciously.

## 2. Failure Detection
- **Heartbeats**: Every node periodically sends a "I am alive" message to a monitor or other nodes.
- **Phi-Accrual Failure Detector**: Instead of a simple binary (alive/dead) check, it outputs a probability that a node has failed based on history. This allows the system to adjust to network conditions.

## 3. Gossip Protocols (Epidemic Algorithms)
A decentralized way to share information across a large group of nodes.
- **Infection**: A node picks $N$ random neighbors and shares its state.
- **Scalability**: Information spreads exponentially fast ($O(\log N)$ steps).
- **Resilience**: Extremely hard to break because there's no single point of failure.
- **Usage**: Used in Cassandra (cluster state) and Amazon Dynamo.

## 4. Availability Patterns
- **Redundancy**: Having multiple copies of everything.
- **Failover**: Automatically switching to a standby node when the leader fails.
- **Replication**:
    - **Active Replication**: Every node processes the same requests. (High overhead, low failover time).
    - **Passive Replication**: Only one node processes, others just store. (Lower overhead, higher failover time).

## 5. Checkpointing and Logging
Saving a snapshot of the system state to stable storage so that non-faulty nodes can resume work after a failure.

> [!TIP]
> **Availability vs. Reliability**: 
> - **Availability** = $\frac{\text{up-time}}{\text{total-time}}$.
> - **Reliability** = $\text{Probability that service is correct for interval } T$.
