# 🚀 SQL: Indexing for High Performance
 
Indexes are powerful tools used by database engines to quickly locate and access data without scanning every row in a table. Think of an index like the index at the back of a textbook—it tells you exactly where to find a topic without reading the whole book.
 
---
 
## 💰 The Cost of Indexes
 
While indexes speed up reads, they aren't free. You must balance the benefits against the costs:
 
- **Increased Storage**: Indexes take up additional disk space.
- **Slower Writes**: Every time you `INSERT`, `UPDATE`, or `DELETE`, the database must also update the corresponding indexes.
 
> [!TIP]
> **Reserve indexes for performance-critical queries.** Design your indexes based on the specific queries your application runs, not just the tables you have.
 
---
 
## 🛠️ Creating and Analyzing Indexes
 
Use the `EXPLAIN` keyword to see how MySQL executes a query and whether it uses an index.
 
```sql
-- Before Indexing
EXPLAIN SELECT c.customer_id
FROM sql_store.customers c
WHERE c.state = 'VA';
```
**Execution Plan (Before):**
- `type`: `ALL` (Table scan - bad for performance)
- `rows`: `10` (Scanned every row in the table)
 
```sql
-- Create the index
CREATE INDEX idx_state ON sql_store.customers (state);
```
 
**Execution Plan (After):**
- `type`: `ref` (Using an index reference - much faster)
- `rows`: `1` (Found the row instantly)
 
### Range Indexes
Indexes also speed up range queries (e.g., `>`, `<`, `BETWEEN`).
```sql
CREATE INDEX idx_points ON sql_store.customers (points);
-- Changes 'type' from ALL to 'range' and reduces scanned rows.
```
 
---
 
## 👁️ Viewing and Maintaining Indexes
 
To see all indexes on a table and their metadata:
 
```sql
-- Refresh table statistics for accurate row counts
ANALYZE TABLE sql_store.customers;
 
-- View indexes
SHOW INDEXES IN sql_store.customers;
```
*Note: MySQL typically uses **BTREE** (Binary Tree) structures for its indexes.*
 
---
 
## 📝 Prefix Indexes
 
When indexing long string columns (like `CHAR`, `VARCHAR`, `TEXT`), you can index just the first few characters to save space while maintaining performance.
 
![Prefix Indexes Illustration](files/Screenshot%20from%202026-03-27%2013-58-48.png)
 
---
 
## 🔍 Full-text Indexes
 
Used for searching large blocks of text (natural language search). Standard indexes are limited to exact matches or prefix matches, while Full-text indexes allow for sophisticated keyword searching.
 
```sql
CREATE FULLTEXT INDEX idx_title_body ON sql_blog.posts (title, body);
```
 
### Natural Language Search
The default mode for full-text searches. It searches for rows that contain any of the given keywords and ranks them by relevance.

```sql
SELECT *
FROM sql_blog.posts p 
WHERE MATCH(p.title, p.body) AGAINST('react redux');
```

### Boolean Mode
Boolean mode gives you more control over the search using operators (+, -, etc.).

```sql
-- 1. Find rows with 'react' but NOT 'redux'
-- We can also project the relevance score
SELECT *, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p 
WHERE MATCH(p.title, p.body ) AGAINST('react -redux' IN BOOLEAN MODE);

-- 2. Find rows with 'react', NOT 'redux', and MUST have 'form'
SELECT *, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p 
WHERE MATCH(p.title, p.body ) AGAINST('react -redux +form' IN BOOLEAN MODE);

-- 3. Search for an exact phrase using double quotes
SELECT *, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p 
WHERE MATCH(p.title, p.body ) AGAINST('"handling a form"' IN BOOLEAN MODE);
```
 
> [!NOTE]
> Use double quotes for exact phrase searches: `AGAINST('"handling a form"' IN BOOLEAN MODE)`.
 
---
 
## 🧬 Composite Indexes
 
An index that contains multiple columns. These are extremely powerful for queries that filter by multiple criteria.
 
```sql
CREATE INDEX idx_state_points ON customers (state, points);
```
 
### The Order of Columns Matters
When designing a composite index, follow these general rules:
1. Put the **most frequently used** columns first.
2. Put columns with **higher cardinality** (more unique values) first.
3. Tailor the order to your specific, most frequent queries.
 
![Column Ordering Rules](files/Screenshot%20from%202026-03-27%2016-16-23.png)
![Index Enforcement](files/Screenshot%20from%202026-03-27%2016-17-41.png)
 
---
 
## 🚫 When Indexes are Ignored
 
Avoid these common mistakes that prevent MySQL from using your carefully crafted indexes:
 
### 1. Using Functions on Indexed Columns
If you wrap an indexed column in a function (like `MONTH()`), MySQL cannot use the index.
 
![Index Ignored by Function](files/Screenshot%20from%202026-03-27%2016-30-23.png)
 
### 2. Performing Math on Indexed Columns
Keep the column "isolated" on one side of the operator.
 
![Math Operation Pitfall](files/Screenshot%20from%202026-03-27%2016-33-17.png)
![Performance Comparison](files/Screenshot%20from%202026-03-27%2016-34-29.png)
![Better Performance Pattern](files/Screenshot%20from%202026-03-27%2016-34-42.png)
 
---
 
## 🏎️ Advanced Indexing Techniques
 
### Using Indexes for Sorting
If your `ORDER BY` clause matches the order of your index, the database can skip the expensive "filesort" operation.
 
![Sorting with Indexes](files/Screenshot%20from%202026-03-27%2016-41-26.png)
 
### Covering Indexes
An index that contains "covers" all the data needed for a query. If the index has all the values required by the `SELECT` and `WHERE` clauses, the database never has to touch the actual table rows.
 
![Covering Index Efficiency](files/Screenshot%20from%202026-03-27%2016-46-24.png)
 
---
 
## 🧹 Index Maintenance
 
- **Check before you create**: Before adding a new index, run `SHOW INDEXES` to see if a similar or redundant index already exists.
- **Remove Redundant Indexes**: An index on `(A, B)` usually makes an index on `(A)` redundant because the engine can use the first column of the composite index.
- **Monitor Size**: Bloated indexes can eventually degrade performance.
 
![Maintenance Checklist](files/Screenshot%20from%202026-03-27%2016-52-36.png)
