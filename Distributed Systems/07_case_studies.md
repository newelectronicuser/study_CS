# Case Studies

Real-world implementations of the concepts discussed in previous modules.

## 1. Google Spanner (NewSQL)
Distributed database designed for strong consistency at a global scale.
- **TrueTime API**: Uses GPS and atomic clocks to provide highly accurate timestamps with dynamic bounds.
- **External Consistency**: Ensures that if transition $A$ finishes before transaction $B$ starts, $A$ is assigned a smaller timestamp than $B$.
- **Paxos**: Used for replication within each shard.
- **2PC**: Used for cross-shard transactions.

## 2. Apache ZooKeeper (Coordination)
A centralized service for maintaining configuration information, naming, and providing distributed synchronization.
- **ZAB Protocol (Zookeeper Atomic Broadcast)**: A consensus-based protocol specifically designed for Zookeeper.
- **Ephemeral Nodes**: Automatically deleted if the client that created them disconnects (perfect for distributed locks and leader election).
- **Watches**: Clients can subscribe to changes on a node and be notified immediately.

## 3. Amazon Dynamo (Key-Value)
The foundation of Amazon's shopping cart system. Focuses on high availability.
- **Eventual Consistency**: Uses background reconciliation.
- **Consistent Hashing**: Dynamically distributes data across nodes.
- **Vector Clocks**: Used to detect and reconcile version conflicts during writes.
- **Sloppy Quorum and Hinted Handoff**: If the intended replica is down, the system writes to a temporary neighbor instead of failing.

## 4. Apache Kafka (Messaging)
High-throughput, distributed event streaming platform.
- **Distributed Commit Log**: Data is stored as an ordered sequence of records on disk.
- **Consumer Groups**: Multiple consumers can read from the same topic in parallel.
- **Partitioning**: Topics are split into partitions for horizontal scaling.

## 5. Raft Implementation (e.g., etcd)
Used by Kubernetes to store all its cluster state. It provides a highly available and consistent key-value store.

> [!IMPORTANT]
> **Learning from Failure**: 
> Most of these systems were born out of specific failures in earlier architectures. Spanner was built to provide better consistency than BigTable. Dynamo was built because strict consistency made Amazon's checkout process too fragile during high-traffic events.
