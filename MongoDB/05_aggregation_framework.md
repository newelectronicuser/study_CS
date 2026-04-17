# Aggregation Framework in MongoDB

The aggregation framework is a powerful pipeline-based system for processing and transforming data.

## 1. The Pipeline Concept
Data flows through a series of stages. Each stage transforms the document stream.
`db.collection.aggregate([ { stage1 }, { stage2 }, ... ])`

## 2. Common Pipeline Stages
- **`$match`**: Filters documents (like `find()`). Place this as early as possible to reduce data size.
- **`$project`**: Reshapes documents (select fields, rename fields, add compute fields).
- **`$group`**: Groups documents by a specific key.
    - Uses **Accumulators**: `$sum`, `$avg`, `$min`, `$max`, `$push`, `$addToSet`.
- **`$sort`**: Sorts documents.
- **`$limit` / `$skip`**: For pagination.
- **`$unwind`**: Deconstructs an array field into individual documents for each element.
- **`$lookup`**: Performs a Left Outer Join with another collection.
- **`$addFields`**: Adds new fields to documents.
- **`$out` / `$merge`**: Writes the resulting documents to a new or existing collection.

## 3. Example Pipeline
```js
db.orders.aggregate([
  { $match: { status: "A" } },
  { $group: { _id: "$customer_id", total: { $sum: "$amount" } } },
  { $sort: { total: -1 } }
])
```

## 4. Performance Optimization
- **Order Matters**: `$match` and `$sort` should come first to take advantage of indexes.
- **Memory Limit**: Individual stages are limited to **100 MB** of RAM. To handle larger datasets, use `{ allowDiskUse: true }`.
- **Index Usage**: Only `$match` and `$sort` (at the beginning of the pipeline) can use indexes.

## 5. Aggregation vs. Map-Reduce
- **Aggregation Framework**: Written in C++, usually faster and more efficient. Preferred for most use cases.
- **Map-Reduce**: Uses JavaScript, more flexible but slower. Deprecated in newer versions in favor of Aggregation.

> [!TIP]
> Use `$facet` when you need to perform multiple aggregation pipelines on the same set of input documents (e.g., for creating dashboards with sidebar counts + main results).
