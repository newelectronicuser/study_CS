# Consensus and Replication

Consensus is the process of getting a group of nodes to agree on a single value or a series of values (a log).

## 1. Consensus Properties
A consensus algorithm must satisfy:
- **Agreement**: All non-faulty nodes decide on the same value.
- **Validity**: The decided value must have been proposed by at least one node.
- **Termination**: All non-faulty nodes eventually decide.

## 2. Practical Consensus Algorithms

### Paxos
The "classic" consensus algorithm. It is notoriously difficult to understand and implement.
- **Roles**: Proposer, Acceptor, Learner.
- **Phases**: Prepare (get a promise), Accept (get a vote).

### Raft
Designed specifically to be easier to understand than Paxos.
- **Strong Leader**: Only the leader can accept writes and propagate logs.
- **Leader Election**: If a leader fails, a new one is elected after a timeout.
- **Log Replication**: The leader sends log entries to followers. Once a majority ($N/2 + 1$) have acknowledged, the entry is "committed."

## 3. Byzantine Fault Tolerance (BFT)
Traditional consensus (like Raft) assumes nodes only fail by stopping (crash-fail). **Byzantine** faults occur when nodes behave maliciously or unpredictably (send conflicting information).
- Requires a much larger majority to reach agreement (usually $3f + 1$ nodes to handle $f$ failures).

## 4. Replication Strategies
- **Passive Replication (Primary-Backup)**: One node handles requests and sends state changes to others.
- **Active Replication (State Machine Replication)**: Every node receives and processes the same sequence of requests (requires total ordering via consensus).

## 5. Viewstamped Replication (VR)
Another consensus algorithm similar to Paxos and Raft, used in early distributed systems.

> [!IMPORTANT]
> **Consensus and Partitioning**: 
> During a network partition, a consensus-based system will only remain operational if a **majority** of nodes can communicate with each other. If no majority exists, the system stops accepting writes to prevent inconsistency (CP).
