# CRUD Operations in MongoDB

## 1. Create (Insert)
- `insertOne({ ... })`: Inserts a single document.
- `insertMany([{ ... }, { ... }])`: Inserts multiple documents.
- `ordered: true|false`: Determines if `insertMany` stops on the first error.

## 2. Read (Query)
### Comparison Operators
- `$eq`: Equal to
- `$ne`: Not equal to
- `$gt` / `$gte`: Greater than / Greater than or equal to
- `$lt` / `$lte`: Less than / Less than or equal to
- `$in` / `$nin`: matches any / none of the values in an array.

### Logical Operators
- `$and`, `$or`, `$nor`, `$not`

### Array Operators
- `$elemMatch`: Matches documents that contain an array field with at least one element that matches all the specified query criteria.
- `$size`: Matches arrays with a specific number of elements.
- `$all`: Matches arrays that contain all elements specified in the query.

### Element Operators
- `$exists`: Matches documents that have the specified field.
- `$type`: Matches documents where the field is of a specific BSON type.

## 3. Update
- `updateOne()`, `updateMany()`, `replaceOne()`
- `upsert: true`: Inserts a new document if no document matches the query.

### Update Operators
- `$set`: Sets the value of a field.
- `$unset`: Removes a field.
- `$inc`: Increments a field by a specified value.
- `$push`: Adds an item to an array.
- `$pull`: Removes an item from an array.
- `$addToSet`: Adds an item to an array only if it doesn't already exist (Set behavior).

## 4. Delete
- `deleteOne()`: Deletes the first document that matches the filter.
- `deleteMany()`: Deletes all documents that match the filter.

## 5. Projection
Limits the fields returned in the query result.
```js
db.collection.find({ age: { $gt: 25 } }, { name: 1, email: 1, _id: 0 })
```

## 6. Sorting & Pagination
- `.sort({ field: 1 })`: 1 for ascending, -1 for descending.
- `.skip(n)`: Skips `n` documents.
- `.limit(n)`: Limits the result to `n` documents.

> [!WARNING]
> Using `.skip()` for deep pagination is inefficient (O(N) complexity). For large datasets, use "Cursor-based pagination" by filtering on a unique sorted field (like `_id`).
