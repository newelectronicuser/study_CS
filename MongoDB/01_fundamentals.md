# MongoDB Fundamentals

## 1. SQL vs. NoSQL
| Feature | SQL (RDBMS) | NoSQL (MongoDB) |
| :--- | :--- | :--- |
| **Data Model** | Relational (Tables/Rows) | Document-oriented (BSON/JSON) |
| **Schema** | Fixed/Rigid | Dynamic/Flexible |
| **Scaling** | Vertical (Scale-up) | Horizontal (Scale-out via Sharding) |
| **Joins** | Native Support | Aggregation ($lookup) / Denormalization |
| **Transactions** | ACID (at table level) | ACID (now multi-document supported) |

## 2. Key Terminology
- **Document**: The basic unit of data in MongoDB (equivalent to a row in SQL).
- **Collection**: A group of documents (equivalent to a table in SQL).
- **Database**: A container for collections.
- **Field**: A key-value pair in a document (equivalent to a column).
- **_id**: Every document must have a unique `_id` field (Primary Key).

## 3. BSON (Binary JSON)
MongoDB stores data in **BSON** format internally.
- **Why BSON over JSON?**
    - **Speed**: Optimized for fast parsing and searching.
    - **Data Types**: Supports more types like `Date`, `BinData`, `Decimal128`, and `ObjectId`.
    - **Storage**: More efficient binary representation.

## 4. Architecture Components
- **mongod**: The primary daemon process that handles data requests and manages background operations.
- **mongos**: The routing service for sharded clusters; it determines which shard holds the requested data.
- **Storage Engine**: The component responsible for managing how data is stored on disk.
    - **WiredTiger**: The default storage engine (document-level concurrency control, compression, checkpoints).

## 5. Why MongoDB?
- **Flexibility**: No need to define schema upfront; handles polymorphic data easily.
- **Scalability**: Native horizontal scaling via sharding.
- **High Availability**: Built-in replication with automatic failover.
- **Developer API**: Queries are expressed in JSON-like syntax (Query DSL), which is intuitive for JavaScript/Python developers.

> [!TIP]
> In an interview, emphasize that MongoDB is **Schema-flexible**, not "Schema-less". You should still have a schema design in mind to optimize performance.
