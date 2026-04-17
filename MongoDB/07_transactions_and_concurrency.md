# Transactions and Concurrency in MongoDB

## 1. ACID Transactions
- **Atomicity**: Either all operations in the transaction succeed or none do.
- **Consistency**: Data is in a valid state before and after the transaction.
- **Isolation**: Transactions are isolated from one another.
- **Durability**: Once committed, changes are permanent even in case of a crash.

### Multi-Document Transactions
MongoDB supports multi-document ACID transactions for:
- **Replica Sets** (since version 4.0)
- **Sharded Clusters** (since version 4.2)

> [!NOTE]
> In most cases, you should design your schema so that updates are concentrated on a single document, which is **atomic by default**. Use transactions only when absolutely necessary (e.g., bank transfers between different users/accounts).

## 2. Locking and Concurrency
MongoDB uses **Multi-Version Concurrency Control (MVCC)** for WiredTiger.

- **Storage Level**: WiredTiger uses **Document-Level Concurrency Control**. Unlike older versions (which had collection or database locks), multiple clients can modify different documents in a collection simultaneously.
- **Lock Management**: MongoDB uses a ticketing system to manage concurrent operations.
- **Intent Locks**: MongoDB uses intent locks (IS, IX) to indicate that a thread intends to read or write at a more granular level.

## 3. Read Isolation levels
- **Read Concern `snapshot`**: Provides the strongest guarantee. The query reads from a point-in-time snapshot across the whole cluster.
- **Read Concern `majority`**: Reads only data that has been acknowledged by a majority of nodes (prevents reading "dirty" data that might be rolled back).

## 4. Write Concern and Durability
- **`j: true`**: Write is acknowledged only after being written to the **journal** on disk. This ensures durability in case of server failure.

## 5. Causal Consistency
Ensures that the order of operations is preserved from the perspective of a single client. For example, if you write a value, you are guaranteed to read that same value (Read-Your-Writes consistency).

> [!TIP]
> **Performance Impact of Transactions**: Transactions have a performance cost and can lead to execution time limits (default 60 seconds). Keep transaction operations small and fast.
