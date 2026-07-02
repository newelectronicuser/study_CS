# SQL — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Fundamentals

**Q1. What is the difference between `WHERE` and `HAVING`?**

**A:** `WHERE` filters rows **before** grouping and aggregation — it cannot reference aggregate functions. `HAVING` filters **after** grouping and can use aggregate functions.

```sql
SELECT department, COUNT(*) AS total
FROM employees
WHERE salary > 30000          -- filters individual rows first
GROUP BY department
HAVING COUNT(*) > 5;          -- filters aggregated groups
```

---

**Q2. Explain the different types of SQL JOINs.**

**A:**
- **INNER JOIN**: Returns rows with matching values in both tables.
- **LEFT JOIN**: All rows from the left table + matching rows from the right (NULLs where no match).
- **RIGHT JOIN**: All rows from the right table + matching from the left.
- **FULL OUTER JOIN**: All rows from both tables; NULLs where no match on either side.
- **CROSS JOIN**: Cartesian product of both tables.
- **SELF JOIN**: A table joined with itself (e.g., employee-manager hierarchy).

---

**Q3. What is the difference between `UNION` and `UNION ALL`?**

**A:** Both combine result sets of two SELECT queries with the same column structure. `UNION` removes duplicate rows (requires sorting). `UNION ALL` includes all rows including duplicates and is faster. Use `UNION ALL` when duplicates are acceptable or impossible.

---

**Q4. What is a correlated subquery?**

**A:** A correlated subquery references a column from the outer query, making it re-evaluated for each row of the outer query. It's slower than a non-correlated subquery but more powerful.

```sql
SELECT e.name FROM employees e
WHERE e.salary > (
  SELECT AVG(salary) FROM employees WHERE department = e.department
);
```

---

**Q5. What is the difference between `DELETE`, `TRUNCATE`, and `DROP`?**

**A:**
- `DELETE`: Removes specific rows; can use `WHERE`; logged per row; triggers fire; can be rolled back.
- `TRUNCATE`: Removes all rows; minimal logging; faster; resets auto-increment; cannot be rolled back in some DBs; no triggers.
- `DROP`: Removes the entire table structure and data permanently.

---

**Q6. What are NULL values and how does SQL handle them?**

**A:** NULL represents an unknown or missing value. Any comparison with NULL using `=` or `!=` returns NULL (not TRUE or FALSE). Use `IS NULL` or `IS NOT NULL`. Aggregate functions like `SUM()` ignore NULLs. Use `COALESCE(col, default)` to substitute a default.

---

## Indexes & Performance

**Q7. What is the difference between a clustered and a non-clustered index?**

**A:**
- **Clustered index**: Determines the physical storage order of rows. Only one per table (usually the primary key). Leaf nodes contain actual row data.
- **Non-clustered index**: A separate structure with pointers back to the actual row. A table can have many. Leaf nodes contain the indexed column(s) + a row locator.

---

**Q8. What is a composite index and how does column order matter?**

**A:** A composite index covers multiple columns. The index is most useful when the query filters on the **leftmost prefix** of the indexed columns. For an index on `(last_name, first_name)`, a filter on `last_name` alone uses the index, but a filter on only `first_name` does not.

---

**Q9. What is `EXPLAIN`/`EXPLAIN ANALYZE` used for?**

**A:** `EXPLAIN` shows the query execution plan — how the database engine will execute a query (index scans, sequential scans, join algorithms, sort operations). `EXPLAIN ANALYZE` actually executes the query and shows actual row counts and timing for each step. Used for query performance tuning.

---

## Transactions & Concurrency

**Q10. What are the ACID properties?**

**A:**
- **Atomicity**: A transaction is all-or-nothing. On failure, all changes are rolled back.
- **Consistency**: A transaction brings the database from one valid state to another.
- **Isolation**: Concurrent transactions don't interfere with each other.
- **Durability**: Committed transactions survive system failures (written to disk).

---

**Q11. What are the four transaction isolation levels?**

**A:** In order of increasing strictness:
1. **Read Uncommitted**: Can read uncommitted data (dirty reads allowed).
2. **Read Committed** (default in most DBs): Can only read committed data. Prevents dirty reads.
3. **Repeatable Read**: Same row reads return the same data within a transaction. Prevents dirty and non-repeatable reads.
4. **Serializable**: Fully isolated. Prevents dirty reads, non-repeatable reads, and phantom reads. Slowest.

---

**Q12. What is a dirty read, non-repeatable read, and phantom read?**

**A:**
- **Dirty read**: Reading uncommitted data from another transaction that may be rolled back.
- **Non-repeatable read**: Reading the same row twice in a transaction gives different results (another transaction modified it).
- **Phantom read**: A query returns different rows on re-execution because another transaction inserted/deleted rows matching the filter.

---

**Q13. What is optimistic vs pessimistic locking?**

**A:**
- **Pessimistic locking**: Locks the resource before accessing it (`SELECT FOR UPDATE`). Prevents conflicts but reduces concurrency.
- **Optimistic locking**: No lock is taken. Before updating, check a version number or timestamp. If it changed, abort and retry. Better for low-contention scenarios.

---

## Schema Design & Normalization

**Q14. Explain 1NF, 2NF, 3NF, and BCNF.**

**A:**
- **1NF**: No repeating groups; each cell holds one atomic value; each row is unique.
- **2NF**: 1NF + no partial dependency (non-key columns depend on the *whole* primary key, not just part of it). Applies to composite keys.
- **3NF**: 2NF + no transitive dependency (non-key columns depend only on the primary key, not on other non-key columns).
- **BCNF**: Stricter 3NF — every determinant must be a candidate key.

---

**Q15. When would you intentionally denormalize a schema?**

**A:** Denormalize for read-heavy workloads where JOIN cost is unacceptable. Examples: data warehouses, analytics tables, reporting dashboards. Trade-off: data redundancy and write complexity for faster reads.

---

## Window Functions & Advanced SQL

**Q16. What are window functions and how do they differ from aggregate functions?**

**A:** Window functions compute a value for each row based on a "window" (related rows) without collapsing rows into a single output. Unlike `GROUP BY` aggregates, the original rows are preserved.

```sql
SELECT name, salary,
  AVG(salary) OVER (PARTITION BY department) AS dept_avg
FROM employees;
```

---

**Q17. Explain `ROW_NUMBER()`, `RANK()`, and `DENSE_RANK()`.**

**A:**
- `ROW_NUMBER()`: Unique sequential number per row, no ties.
- `RANK()`: Tied rows get the same rank; the next rank skips numbers (1,1,3).
- `DENSE_RANK()`: Tied rows get the same rank; no gaps in ranking (1,1,2).

---

**Q18. What is a Common Table Expression (CTE)?**

**A:** A CTE is a named temporary result set defined with `WITH`, valid for the duration of the query. It improves readability, allows reuse, and enables recursive queries. Unlike a subquery, a CTE is defined once and can be referenced multiple times.

```sql
WITH high_earners AS (
  SELECT * FROM employees WHERE salary > 100000
)
SELECT department, COUNT(*) FROM high_earners GROUP BY department;
```

---

**Q19. What is a recursive CTE?**

**A:** A recursive CTE references itself to traverse hierarchical data (org charts, category trees). It has an anchor member (base case) and a recursive member that references the CTE itself.

```sql
WITH RECURSIVE org AS (
  SELECT id, name, manager_id FROM employees WHERE manager_id IS NULL
  UNION ALL
  SELECT e.id, e.name, e.manager_id FROM employees e
  JOIN org ON e.manager_id = org.id
)
SELECT * FROM org;
```

---

## Performance Tuning

**Q20. How do you handle pagination efficiently for large datasets?**

**A:** Avoid `OFFSET` for large pages (it scans all preceding rows). Use **keyset (cursor) pagination**: filter by the last seen primary key.

```sql
-- Page 1
SELECT * FROM orders WHERE id > 0 ORDER BY id LIMIT 20;
-- Next page (last_id = 20)
SELECT * FROM orders WHERE id > 20 ORDER BY id LIMIT 20;
```

---

**Q21. What is the N+1 query problem and how do you solve it?**

**A:** N+1 occurs when you fetch N records, then execute an additional query for each record's related data — N+1 total queries. Solve with:
- SQL JOINs or subqueries to fetch all data in one query.
- ORM eager loading (`JOIN FETCH` in Hibernate, `.include()` in ActiveRecord).
- Batch loading.

---

**Q22. How would you optimize a slow JOIN between two large tables?**

**A:**
1. Ensure JOIN columns are indexed on both tables.
2. `EXPLAIN ANALYZE` to identify the actual join algorithm (hash join vs nested loop).
3. Filter data before joining using WHERE to reduce row counts.
4. Consider materialized views or pre-aggregated tables for repeated analytical joins.
5. Ensure statistics are up to date (`ANALYZE` in PostgreSQL).

---
