# Data Modeling in MongoDB

In MongoDB, "Data that is accessed together should be stored together."

## 1. Embedding vs. Referencing
| Feature | Embedding (Denormalization) | Referencing (Normalization) |
| :--- | :--- | :--- |
| **Concept** | Sub-documents nested within a field. | Storing the `_id` of another document. |
| **Pros** | High performance (One read), Atomic updates. | Prevents duplication, avoids 16MB document limit. |
| **Cons** | Redundancy, 16MB limit, potentially large documents. | Multiple reads required (though `$lookup` helps). |
| **Use Case** | 1-to-1 relationships, 1-to-Low-N. | 1-to-Many (High N), Many-to-Many. |

## 2. Relationships
- **One-to-One**: Usually Prefer Embedding.
- **One-to-Few**: Embedding (e.g., User addresses).
- **One-to-Many**: Referencing (e.g., Comments on a post if comments can grow indefinitely).
- **Many-to-Many**: Referencing on both sides (e.g., Books and Authors).

## 3. Schema Design Patterns
- **Bucket Pattern**: Group data in "buckets" (e.g., sensor data for 1 hour in one document) to reduce the number of documents.
- **Outlier Pattern**: If most documents follow a pattern but some have massive arrays, move the extras to another collection.
- **Extended Reference**: Store only the frequently accessed fields from the referenced document (e.g., store `user_name` inside `order` even if the full `user` is referenced) to avoid `$lookup`.
- **Schema Versioning**: Keep a `schema_version` field to handle migrations gracefully.

## 4. Document Size Limit
A single BSON document cannot exceed **16 MB**. This is a hard limit. If your data exceeds this, you must use Referencing or GridFS (for binary files).

## 5. Write Side Effects
- **Atomic updates**: MongoDB only guarantees atomicity at the **document level** (unless using multi-document transactions).
- **Document Growth**: If a document grows significantly (e.g., pushing to a nested array), WiredTiger may need to rewrite the document in a new location, which can impact performance.

> [!TIP]
> **Cardinality** is key. 
> - 1 to Few (Embed)
> - 1 to Many (Reference)
> - 1 to Squillions (Reference)
