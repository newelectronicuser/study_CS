# Replication and Sharding in MongoDB

## 1. Replication (High Availability)
Replication provides redundancy and increases data availability with multiple copies of data on different servers.

### Replica Set
- **Primary**: Receives all write operations.
- **Secondary**: Replicates the primary's **oplog** (operations log) and applies operations to their data sets.
- **Arbiters**: Do not have a copy of data; they only participate in elections.
- **Failover**: If the Primary becomes unavailable, an election is held to select a new Primary from the Secondaries.

### Heartbeats and Elections
- Nodes send heartbeats to each other every 2 seconds.
- If a primary is unreachable for 10 seconds, an election is triggered.

## 2. Sharding (Horizontal Scaling)
Sharding is the process of storing data records across multiple machines. It is MongoDB's approach to meeting the demands of data growth.

### Sharded Cluster Components
- **Shard**: Each shard contains a subset of the sharded data.
- **Mongos**: The query router, providing an interface between client applications and the sharded cluster.
- **Config Servers**: Store metadata and configuration settings for the cluster.

### Shard Key
The field that determines the distribution of documents across shards.
- **Hashed Sharding**: Uses a hashed index of the shard key's value to assign documents to shards. (Even distribution).
- **Ranged Sharding**: Documents are partitioned into ranges based on shard key values. (Good for range-based queries).

## 3. High Availability vs. Scalability
- **Replication** = High Availability (Reading from secondaries, keeping data safe).
- **Sharding** = Scalability (Handling massive datasets and high throughput).

## 4. Read and Write Concerns
- **Read Concern**: Controls the consistency and isolation properties of data read from replica sets.
    - `local`, `majority`, `snapshot`.
- **Write Concern**: Controls the level of acknowledgment requested from MongoDB for write operations.
    - `w: 1` (Acknowledge from primary), `w: majority` (Acknowledge from majority of nodes).

> [!CAUTION]
> **Choosing a Shard Key is critical**. Once sharded, it is difficult to change the shard key (though MongoDB 5.0+ allows resharding, it's resource-intensive). Avoid "Monotonic keys" (like auto-incrementing IDs or Timestamps) for ranged sharding as it creates a "hot shard".
