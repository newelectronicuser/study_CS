# Fundamentals and Architecture

A distributed system is a collection of independent computers that appears to its users as a single coherent system.

## 1. Transparency Goals
The main goal of a distributed system is to hide its distributed nature.
- **Access Transparency**: Hide differences in data representation and how a resource is accessed.
- **Location Transparency**: Hide where a resource is located.
- **Migration Transparency**: Hide that a resource may move to another location.
- **Replication Transparency**: Hide that a resource may be replicated.
- **Failure Transparency**: Hide the failure and recovery of a resource.

## 2. CAP Theorem
In a distributed data store, you can only provide two of the following three guarantees:
- **Consistency**: Every read receives the most recent write.
- **Availability**: Every request receives a (non-error) response.
- **Partition Tolerance**: The system continues to operate despite network partitions.

> [!IMPORTANT]
> Since network partitions are unavoidable in distributed systems, we must choose between **CP** (Consistency and Partition Tolerance) or **AP** (Availability and Partition Tolerance).

## 3. PACELC Theorem
An extension of CAP that describes the trade-offs during normal operation (no partition).
- If there is a **Partition (P)**, choose between **Availability (A)** or **Consistency (C)**.
- **Else (E)** (no partition), choose between **Latency (L)** or **Consistency (C)**.

## 4. Communication Strategies
- **RPC (Remote Procedure Call)**: Making a call to a remote service look like a local function call.
- **Rest/HTTP**: Stateless, resource-based communication.
- **Message-Oriented Middleware (MOM)**: Asynchronous communication via queues/topics (e.g., Kafka).

## 5. Architectural Models
- **Client-Server**: Centralized authority.
- **Peer-to-Peer (P2P)**: Decentralized, every node is both client and server.
- **Microservices**: Functional decomposition into small, independent services.

> [!TIP]
> **Eight Fallacies of Distributed Computing**:
> 1. The network is reliable.
> 2. Latency is zero.
> 3. Bandwidth is infinite.
> 4. The network is secure.
> 5. Topology doesn't change.
> 6. There is one administrator.
> 7. Transport cost is zero.
> 8. The network is homogeneous.
> *Always assume these are false when designing.*
