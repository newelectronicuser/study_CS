# Time and Event Ordering

In a distributed system, there is no global clock. Synchronizing time and ordering events across multiple nodes is one of the hardest challenges.

## 1. Physical Clocks
Nodes have their own internal clocks (oscillators) which inevitably drift relative to each other.
- **Clock Drift**: The difference in time between two clocks.
- **NTP (Network Time Protocol)**: A protocol used to synchronize clocks over a network, usually accurate within tens of milliseconds.

## 2. Logical Clocks
When physical time isn't accurate enough to determine the order of events, we use logical clocks.

### Lamport Timestamps
Based on the **"Happened-Before"** relation ($a \to b$).
- Each process maintains a counter $C$.
- When an event occurs locally: $C = C + 1$.
- When sending a message: Include $C$.
- When receiving a message: $C = \max(C_{local}, C_{received}) + 1$.
- **Limitation**: If $L(a) < L(b)$, it doesn't necessarily mean $a \to b$. It only guarantees that if $a \to b$, then $L(a) < L(L)$.

### Vector Clocks
A more complex logical clock that can detect **Concurrency** and **Causality**.
- Each node maintains a vector of counters, one for every node in the system $[V_1, V_2, ..., V_n]$.
- If $V(a) < V(b)$, then $a \to b$ (Causally related).
- If neither is smaller, the events are **Concurrent** (Conflict).

## 3. Total vs. Causal Ordering
- **Causal Ordering**: If one event caused another, all nodes see them in that order. Concurrent events can be seen in any order.
- **Total Ordering**: All nodes see all events in the exact same order (requires consensus).

## 4. External Consistency
Events are seen in the order they actually occurred in the "real world." This is achieved by Google Spanner using **TrueTime** (GPS and atomic clocks).

> [!TIP]
> **Why do we need this?**
> Without ordering, a social media app might show a comment *before* the post it's replying to, or a bank might allow a withdrawal before the corresponding deposit has been processed.
