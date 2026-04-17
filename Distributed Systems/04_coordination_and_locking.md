# Coordination and Locking

Coordination ensures that distributed components work together to perform complex tasks without interfering with each other.

## 1. Leader Election
Selecting a single node to act as the "coordinator" or "source of truth."
- **Bully Algorithm**: When a node detects the leader is down, it challenges all nodes with higher IDs. If no one responds, it becomes the leader.
- **Ring Algorithm**: Nodes are arranged in a logical ring. An election message circles the ring to find the highest ID.

## 2. Quorum-based Protocols
To ensure consistency in a replicated system, we use a Quorum.
- If we have $N$ replicas:
    - **Read Quorum ($R$)**: Number of nodes to read from.
    - **Write Quorum ($W$)**: Number of nodes to write to.
- **Strict Quorum constraint**: $R + W > N$. This ensures at least one node in the Read Quorum has the latest write.

## 3. Distributed Locking
Preventing multiple nodes from accessing a shared resource simultaneously.
- **Zookeeper**: Uses **Ephemeral Nodes**. If a node crashes, its connection is lost and the node vanishes, automatically releasing the lock for others.
- **Redis (Redlock)**: A distributed lock manager that works by acquiring locks on multiple independent Redis instances.

## 4. Barrier Synchronization
Ensuring that all processes in a group stop at a certain point and wait until all other processes reach the same point before proceeding.

## 5. Group Communication
A way for a set of processes to communicate reliably.
- **Atomic Multicast**: Either all non-faulty processes receive the message, or none of them do.

> [!WARNING]
> **Fencing Tokens**: 
> A common issue in distributed locking is a "GC Pause." If Node A holds a lock but pauses for a long time, the lock might expire and be given to Node B. When Node A wakes up, it still thinks it has the lock. 
> **Solution**: Use a monotonically increasing **Fencing Token**. The storage system only accepts writes with a token value higher than the one previously seen.
