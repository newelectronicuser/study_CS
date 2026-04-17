# Indexing in MongoDB

Indexes support the efficient execution of queries in MongoDB. Without indexes, MongoDB must perform a *collection scan* (read every document).

## 1. Types of Indexes
- **Single Field Index**: User-defined index on a single field.
- **Compound Index**: Index on multiple fields.
    - **ESR Rule**: Equality, Sort, Range. Order your fields in this sequence for optimal performance.
- **Multikey Index**: Index on an array field.
- **Text Index**: Supports searching for string content.
- **Hashed Index**: Used for shard keys in hashed sharding.
- **TTL (Time To Live) Index**: Automatically removes documents after a certain time (e.g., for sessions).
- **Partial Index**: Only indexes documents that meet a filter expression (saves storage).
- **Sparse Index**: Only indexes documents that contain the indexed field.

## 2. Query Optimization - `explain()`
Use `.explain("executionStats")` to analyze query performance.
Key fields to watch:
- `stage`:
    - `COLLSCAN`: Collection scan (Bad).
    - `IXSCAN`: Index scan (Good).
    - `FETCH`: Retrieving documents (Follows IXSCAN).
- `nReturned`: Number of documents returned.
- `totalKeysExamined`: Number of index entries scanned.
- `totalDocsExamined`: Number of documents scanned from disk.
- **Ideal Ratio**: `nReturned` : `totalKeysExamined` : `totalDocsExamined` = 1 : 1 : Document/Index dependent (aim for minimal scans).

## 3. Covered Queries
A query is "covered" if the index contains all the fields returned by the query. MongoDB does not need to fetch documents from memory/disk (Stage: `IXSCAN` without `FETCH`).

## 4. Index Write Performance
Indexes improve read performance but **slow down writes** (each index must be updated). Avoid over-indexing.

## 5. Index Intersection
MongoDB can use the intersection of multiple indexes to fulfill a query, but it is usually more efficient to use a single **Compound Index**.

> [!IMPORTANT]
> **Compound Index Order Matters**: An index on `{ a: 1, b: 1 }` can support queries on `a` or `a and b`, but NOT queries on only `b`.
