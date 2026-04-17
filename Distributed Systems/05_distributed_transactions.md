# Distributed Transactions

Ensuring that a transaction succeeds on multiple nodes or fails everywhere is extremely difficult due to network and node failures.

## 1. Two-Phase Commit (2PC)
A classic algorithm for achieving atomic commitment.
- **Phase 1 (Prepare)**: The coordinator asks all participants if they are ready to commit.
- **Phase 2 (Commit)**: If all respond "Yes," the coordinator sends a "Commit" message. If any respond "No" or time out, it sends an "Abort" message.
- **Constraint**: It is **Blocking**. If the coordinator fails after Phase 1, the participants are stuck waiting.

## 2. Three-Phase Commit (3PC)
An extension of 2PC to make it non-blocking.
- Adds an intermediate **Pre-Commit** phase.
- If the coordinator fails, the participants can collectively decide to commit or abort based on whether any node reached the pre-commit state.
- **Limitation**: Still vulnerable to network partitions.

## 3. Saga Pattern
The modern way to handle long-running, distributed transactions without using 2PC (which scales poorly).
- A transaction is split into a series of **local transactions**.
- Each local transaction has a **Compensating Transaction** that reverses its effects.
- If a step fails, the Saga runs the compensating transactions for all previous steps.

## 4. TCC (Try-Confirm-Cancel)
Similar to Saga but more explicit.
- **Try**: Reserve resources.
- **Confirm**: Finalize the action.
- **Cancel**: Release the reserved resources if failure occurs.

## 5. Distributed Transaction Isolation
Achieving ACID properties in a distributed setting.
- **Snapshot Isolation**: Each transaction reads from a consistent snapshot of the data.
- **Strict Serializable**: The most expensive but most consistent level.

> [!IMPORTANT]
> **Data Integrity over Scalability**: 
> 2PC/3PC provide strong consistency but reduce system availability and performance. Sagas provide high availability and scalability but lead to **Eventual Consistency**.
