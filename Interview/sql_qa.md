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

**Q2. Explain the different types of SQL JOINs with examples.**

**A:**
- **INNER JOIN:** Returns rows when there is a match in both tables.
- **LEFT JOIN (OUTER):** Returns all rows from the left table, and matched rows from the right table. Fields on the right are NULL if no match exists.
- **RIGHT JOIN (OUTER):** Returns all rows from the right table, and matched rows from the left.
- **FULL OUTER JOIN:** Returns all rows when there is a match in either left or right table.
- **CROSS JOIN:** Returns the Cartesian product of both tables (each row of left joined with all rows of right).
- **SELF JOIN:** A table joined with itself (useful for querying hierarchical relations, e.g. finding employee and their manager).

---

**Q3. What is the difference between `UNION` and `UNION ALL`?**

**A:** Both combine result sets of two or more SELECT queries with the same column structures. `UNION` removes duplicate rows (which requires sorting/scanning and is slower). `UNION ALL` includes all rows including duplicates and is faster because no sorting is done.

---

**Q4. What are aggregate functions? Name five common ones.**

**A:** Aggregate functions perform a calculation on a set of values and return a single value. They ignore NULL values (except `COUNT(*)`). Five common ones:
1. `COUNT()` - Returns the number of rows.
2. `SUM()` - Returns the total sum of a numeric column.
3. `AVG()` - Returns the average value of a numeric column.
4. `MIN()` - Returns the minimum value in a set.
5. `MAX()` - Returns the maximum value in a set.

---

**Q5. What is a subquery? What is a correlated subquery?**

**A:**
- **Subquery:** A query nested inside another query (e.g., in a `WHERE`, `FROM`, or `SELECT` clause) that executes independently once and returns results to the outer query.
- **Correlated Subquery:** A nested query that references columns of the outer query. It cannot run independently and is evaluated once for each row processed by the outer query, which can make it slower.

```sql
-- Correlated Subquery Example
SELECT e.name, e.salary FROM employees e
WHERE e.salary > (
  SELECT AVG(salary) FROM employees WHERE department = e.department
);
```

---

**Q6. What is the difference between `DELETE`, `TRUNCATE`, and `DROP`?**

**A:**
- `DELETE`: DML command. Removes specific rows based on a `WHERE` condition. Row-by-row deletions are logged, triggers are fired, and it can be rolled back inside a transaction.
- `TRUNCATE`: DDL command. Removes all rows from a table. It bypasses transaction logging for individual rows, resets auto-increment keys, is much faster, does not fire triggers, and cannot be rolled back in some databases.
- `DROP`: DDL command. Completely removes the table structure, indices, permissions, and all data permanently.

---

**Q7. What is the difference between `IN`, `EXISTS`, and `ANY`/`ALL`?**

**A:**
- `IN`: Checks if a value matches any value in a list or subquery. Best for small lists/static values.
- `EXISTS`: Evaluates to TRUE/FALSE as soon as the first matching row is found in a subquery (stops scanning). Often faster than `IN` for large tables because of this early exit.
- `ANY` / `ALL`: Used with comparison operators. `> ANY` means greater than at least one value in the set. `> ALL` means greater than every value in the set.

---

**Q8. Explain the execution order of a SQL query.**

**A:** Although written starting with `SELECT`, the database engine executes clauses in the following logical order:
1. `FROM` & `JOIN` (identify tables and join them)
2. `WHERE` (filter raw rows)
3. `GROUP BY` (group rows)
4. `HAVING` (filter grouped rows)
5. `SELECT` (select and calculate columns)
6. `DISTINCT` (remove duplicates)
7. `ORDER BY` (sort results)
8. `LIMIT` / `OFFSET` (restrict result size)

---

**Q9. What are NULL values and how does SQL handle them in comparisons?**

**A:** `NULL` represents the absence of a value or an unknown value. In SQL, any direct comparison with `NULL` using operators like `=` or `!=` evaluates to `UNKNOWN` (neither TRUE nor FALSE). To test for NULL, you must use `IS NULL` or `IS NOT NULL`. Functions like `COALESCE(val, default)` or `IFNULL()` are used to substitute a default value for NULL.

---

**Q10. What is the difference between `CHAR`, `VARCHAR`, and `TEXT`?**

**A:**
- `CHAR(N)`: Fixed-length character string. If the stored string is shorter than N, it is padded with spaces. Best for fixed-size fields (e.g. status codes, country codes like "USA").
- `VARCHAR(N)`: Variable-length character string up to N. Only stores the actual characters plus 1-2 bytes of length metadata. Better for arbitrary text like names or emails.
- `TEXT`: Stores long-form text. Usually stored off-table with a pointer, which makes sorting and indexing text fields more resource-intensive. Cannot have default values in many databases.

---

## Indexes & Performance

**Q11. What is a database index? How does it improve query performance?**

**A:** An index is a data structure (typically a B+ Tree) that the database engine maintains to quickly locate rows without doing a full-table sequential scan. It maps indexed column values to pointers/row locators. It speeds up `SELECT` queries with `WHERE`, `JOIN`, or `ORDER BY` clauses but slows down write operations (`INSERT`, `UPDATE`, `DELETE`) because the index must be updated.

---

**Q12. What is the difference between a clustered and a non-clustered index?**

**A:**
- **Clustered Index:** Defines the physical order in which rows are stored on disk. There can only be **one** per table (usually default on the Primary Key). Leaf nodes contain the actual data rows.
- **Non-Clustered Index:** A separate structure from the data rows. Contains a sorted list of the indexed columns and a pointer (row locator) to the actual data row. A table can have multiple non-clustered indexes.

---

**Q13. What is a composite index? How does column order matter?**

**A:** A composite index is an index built on multiple columns, e.g., `(col_a, col_b)`. Column order matters because the index is sorted by the first column first, then by the second. The database can only use this index if the query filters on the **leftmost prefix** of the columns. An index on `(last_name, first_name)` is useful for queries searching `last_name` or `last_name AND first_name`, but is useless for queries searching only `first_name`.

---

**Q14. When would an index hurt performance instead of helping?**

**A:**
- **Write-Heavy Tables:** Every insert, update, and delete requires updating the index, introducing overhead.
- **Low Cardinality Columns:** Indexing columns with few unique values (e.g. a boolean `is_active` or a `gender` column) doesn't help because the engine will likely prefer a full-table scan.
- **Small Tables:** For tables with only a few hundred rows, reading the index and then looking up the row takes more time than scanning the entire table directly.

---

**Q15. What is an index covering query? What is a covering index?**

**A:** A **Covering Index** is a non-clustered index that includes all the columns referenced by a query (in the `SELECT`, `WHERE`, `JOIN`, etc. clauses). An **index covering query** is a query that can be resolved entirely by scanning the index structure without fetching the actual row from the table (avoiding "bookmark lookup" or "key lookup" overhead).

---

**Q16. What is the `EXPLAIN` / `EXPLAIN ANALYZE` command used for?**

**A:**
- `EXPLAIN`: Shows the execution plan chosen by the query optimizer (e.g., whether it will use an index scan, full table scan, nested loops, or hash joins).
- `EXPLAIN ANALYZE`: Actually runs the query and displays the execution plan along with real runtime stats, showing how much time and resources each step took. Essential for query optimization.

---

**Q17. What are statistics in a database and how do they affect query planning?**

**A:** Statistics are metadata collected by the DBMS about the distribution of data in tables and indexes (e.g., table size, number of unique values, histograms). The query optimizer uses these statistics to calculate the cost of different execution plans. Outdated statistics lead the optimizer to make poor choices, such as using a full table scan instead of an index.

---

**Q18. What is a query execution plan and how do you read one?**

**A:** A query execution plan is the step-by-step sequence of physical operations chosen by the DBMS to execute a query. When reading one, trace it from the inside-out (or bottom-up). Look for:
1. **Scans vs. Looks:** Search for sequential/table scans (bad on large tables) vs. index scans or index seeks (good).
2. **Cost Percentages:** Find which step takes the highest percentage of the query cost.
3. **Join operations:** Note if it is using a nested loop, hash join, or merge join.

---

## Transactions & Concurrency

**Q19. What are the ACID properties of a database transaction?**

**A:**
- **Atomicity:** All operations in a transaction succeed, or the entire transaction is rolled back (all-or-nothing).
- **Consistency:** Transactions must transition the database from one valid state to another, maintaining all schema constraints and rules.
- **Isolation:** Concurrent transactions execute independently without visible intermediate states.
- **Durability:** Once committed, changes are written to persistent storage and survive crashes.

---

**Q20. What are the four transaction isolation levels? Explain each.**

**A:**
1. **Read Uncommitted:** Allows reading uncommitted changes (dirty reads). Highest concurrency, lowest safety.
2. **Read Committed:** Prevents dirty reads. Can only read committed data, but a query run twice in the same transaction might see different values if another transaction committed changes in between (non-repeatable reads).
3. **Repeatable Read:** Guarantees that multiple reads of the same rows return the same values. Prevents dirty and non-repeatable reads, but might see new rows inserted by other transactions (phantom reads).
4. **Serializable:** Strict isolation. Prevents all anomalies by locking ranges or using locks. Slowest, lowest concurrency.

---

**Q21. What is a dirty read, non-repeatable read, and phantom read?**

**A:**
- **Dirty Read:** Transaction A reads data modified by Transaction B that has not been committed yet. If B rolls back, A's data is invalid.
- **Non-Repeatable Read:** Transaction A reads a row, Transaction B modifies/commits that row, Transaction A reads it again and finds it has changed.
- **Phantom Read:** Transaction A queries a range of rows matching a criteria, Transaction B inserts new rows matching that criteria, Transaction A queries again and finds "phantom" new rows.

---

**Q22. What is a deadlock and how do you detect and prevent it?**

**A:** A deadlock occurs when two or more transactions are blocked waiting for locks held by each other in a circular dependency.
- **Detection:** The DBMS builds a "waits-for" graph and runs cycle-detection. If a cycle is found, it aborts one of the transactions (the "victim").
- **Prevention:** Acquire locks in a consistent order; use short transactions; use optimistic locking; set lock timeout limits.

---

**Q23. What is optimistic vs pessimistic locking?**

**A:**
- **Pessimistic Locking:** Locks the rows immediately when reading (`SELECT ... FOR UPDATE`), preventing other transactions from modifying them until the lock is released. Use for high concurrency contention.
- **Optimistic Locking:** No locks are taken during read. Instead, a version number or timestamp is verified during the write. If the version changed, the transaction is rejected and retried. Use for low contention.

---

**Q24. What is a savepoint in a transaction?**

**A:** A savepoint is a marker within a transaction that allows rolling back only the operations performed after that point, rather than rolling back the entire transaction. This is useful for complex workflows with conditional logic.

```sql
SAVEPOINT my_savepoint;
-- perform operations
ROLLBACK TO my_savepoint; -- undoes only operations after the savepoint
COMMIT; -- commits the rest of the transaction
```

---

## Schema Design & Normalization

**Q25. What are the normal forms (1NF, 2NF, 3NF, BCNF)?**

**A:** Normalization rules prevent data redundancy and update anomalies:
- **1NF (First Normal Form):** Table cells must contain atomic values (no lists); columns must have unique names; each row must be unique.
- **2NF (Second Normal Form):** 1NF + no partial dependency. If the primary key is composite, all non-key attributes must depend on the *entire* key, not a part of it.
- **3NF (Third Normal Form):** 2NF + no transitive dependency. Non-key attributes must depend only on the primary key, not on other non-key attributes.
- **BCNF (Boyce-Codd Normal Form):** A stronger version of 3NF. For any dependency `X -> Y`, X must be a superkey.

---

**Q26. When would you intentionally denormalize a schema?**

**A:** Denormalization is done for read-heavy workloads where the cost of multiple SQL `JOIN` operations impairs performance. Common scenarios include building data warehouses (star schema), reporting dashboards, or caching precomputed sums directly on a parent table.

---

**Q27. What is a surrogate key vs a natural key?**

**A:**
- **Natural Key:** A unique attribute that already exists in the real-world data (e.g., SSN, email address, VIN).
- **Surrogate Key:** A unique identifier generated by the database with no real-world meaning (e.g., auto-incrementing integer or UUID). Better because they don't change if business rules change.

---

**Q28. What is a foreign key constraint and what are the `ON DELETE` options?**

**A:** A Foreign Key constraint maintains referential integrity by ensuring a column value matches a primary key in another table.
*   *ON DELETE options:*
    *   `CASCADE`: If the parent row is deleted, all matching child rows are automatically deleted.
    *   `SET NULL`: Sets the foreign key column in the child rows to NULL.
    *   `RESTRICT` / `NO ACTION`: Prevents deleting the parent row if child rows exist.

---

**Q29. What is the difference between a primary key and a unique key?**

**A:**
- **Primary Key:** Uniquely identifies each row. Cannot contain NULL values. Only one primary key per table. Typically used for database clustering.
- **Unique Key:** Guarantees uniqueness for a column. Can contain NULL values (though some databases only allow one NULL value). Multiple unique keys allowed per table.

---

**Q30. How would you design a schema for a many-to-many relationship?**

**A:** A many-to-many relationship requires a junction (bridge/associative) table containing foreign keys pointing to the primary keys of both entity tables. The primary key of this junction table is typically a composite key of the two foreign keys.

```sql
CREATE TABLE student_courses (
  student_id INT REFERENCES students(id),
  course_id INT REFERENCES courses(id),
  PRIMARY KEY (student_id, course_id)
);
```

---

## Window Functions & Advanced SQL

**Q31. What are window functions? How do they differ from aggregate functions?**

**A:** Window functions compute values over a group of rows (a "window") without collapsing the rows into a single output. Unlike `GROUP BY` aggregates, the query retains the individual row identities.

```sql
SELECT name, salary,
  AVG(salary) OVER(PARTITION BY department) as dept_avg
FROM employees;
```

---

**Q32. Explain `ROW_NUMBER()`, `RANK()`, and `DENSE_RANK()` with examples.**

**A:**
- `ROW_NUMBER()`: Assigns a unique sequential integer starting from 1.
- `RANK()`: Assigns ranks, but identical values get the same rank, and the next rank skips numbers (e.g., 1, 2, 2, 4).
- `DENSE_RANK()`: Identical values get the same rank, but no numbers are skipped (e.g., 1, 2, 2, 3).

---

**Q33. What is the `PARTITION BY` clause in a window function?**

**A:** `PARTITION BY` divides the result set into partitions to which the window function is independently applied. If omitted, the function treats the entire query result as a single partition.

---

**Q34. What is a Common Table Expression (CTE)? How is it different from a subquery?**

**A:** A CTE is a temporary, named result set defined using the `WITH` clause that exists only for the duration of the query.
*   *Differences:*
    *   CTEs can be referenced multiple times within the same query.
    *   CTEs are generally more readable and maintainable than deeply nested subqueries.
    *   Supports recursive operations.

---

**Q35. What is a recursive CTE? Give an example use case.**

**A:** A recursive CTE is a CTE that references itself. It is used to traverse hierarchical data, such as organizational charts, file systems, or category sub-trees.

```sql
WITH RECURSIVE org_chart AS (
  SELECT emp_id, name, manager_id, 1 as level FROM employees WHERE manager_id IS NULL
  UNION ALL
  SELECT e.emp_id, e.name, e.manager_id, o.level + 1 FROM employees e
  JOIN org_chart o ON e.manager_id = o.emp_id
)
SELECT * FROM org_chart;
```

---

**Q36. What is `LEAD()` and `LAG()` used for?**

**A:**
- `LAG(col, offset)`: Accesses data from a previous row in the same result set without using a self-join.
- `LEAD(col, offset)`: Accesses data from a subsequent row in the same result set.
- Useful for calculating year-over-year growth or finding time differences between events.

---

**Q37. How would you calculate a running total using a window function?**

**A:**
```sql
SELECT transaction_date, amount,
  SUM(amount) OVER(ORDER BY transaction_date ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) as running_total
FROM transactions;
```

---

## Stored Procedures, Views & Triggers

**Q38. What is a stored procedure? What are its advantages and disadvantages?**

**A:** A stored procedure is a group of SQL statements compiled and saved in the database.
*   *Advantages:* Reduces network traffic (sends command, not text); pre-compiled (faster execution); centralizes business logic; increases security by hiding table structures.
*   *Disadvantages:* Hard to version control and debug; increases database CPU utilization; vendor lock-in (PL/SQL vs. T-SQL).

---

**Q39. What is the difference between a view and a materialized view?**

**A:**
- **View:** A virtual table representing a saved query. It does not store data on disk; the query runs every time the view is accessed.
- **Materialized View:** Stores the query results physically on disk. Speeds up read queries on complex joins, but requires periodic refreshing to sync with the base tables.

---

**Q40. What is a trigger? Name use cases where a trigger is appropriate vs inappropriate.**

**A:** A trigger is a database block that runs automatically in response to specific events (like `INSERT`, `UPDATE`, or `DELETE`).
*   *Appropriate:* Maintaining audit logs; automatically syncing denormalized columns; executing database-level data validation.
*   *Inappropriate:* Implementing complex business logic (makes code hard to trace and debug); running slow operations (triggers run inside transactions and block client response).

---

**Q41. What is the difference between a function and a stored procedure in SQL?**

**A:**
- **Function:** Must return a single value. Can be called directly within a `SELECT` statement (e.g. `SELECT my_func(col) ...`). Cannot execute transactions (commit/rollback) or DDL.
- **Stored Procedure:** Does not need to return a value (can return multiple result sets via OUT parameters). Cannot be called inside a `SELECT` query. Can manage transactions.

---

## Performance Tuning & Production

**Q42. How do you find and kill long-running queries in a database?**

**A:**
- **PostgreSQL:** Query `pg_stat_activity` to find the process ID (`pid`), then run `SELECT pg_cancel_backend(pid)` (graceful cancel) or `SELECT pg_terminate_backend(pid)` (force kill).
- **MySQL:** Run `SHOW PROCESSLIST;` to identify the Thread `Id`, then execute `KILL <Id>;`.

---

**Q43. What is connection pooling and why is it important?**

**A:** Connection pooling maintains a cache of active database connections. When an application needs to run a query, it leases a connection from the pool and returns it immediately after. This avoids the high CPU and latency overhead of opening and closing physical TCP connections to the database on every request.

---

**Q44. What is database sharding? What are its trade-offs?**

**A:** Sharding is a horizontal partitioning technique where a database is split into smaller, independent databases (shards) across different servers.
*   *Trade-offs:* Allows scaling writes linearly and handling massive datasets, but makes cross-shard joins impossible, distributed transactions complex (2PC), and resharding difficult.

---

**Q45. What is vertical vs horizontal partitioning?**

**A:**
- **Vertical Partitioning:** Splitting a table by columns. For example, moving rarely accessed large BLOB columns or text fields into a separate table linked by a foreign key.
- **Horizontal Partitioning (Sharding):** Splitting a table by rows. For example, storing customers with IDs 1-1,000,000 in Table A, and IDs 1,000,001+ in Table B.

---

**Q46. How do you handle pagination efficiently for large datasets?**

**A:** Avoid `OFFSET` because the database engine must scan all preceding rows before returning the page. Instead, use **Keyset Pagination (Cursor-based)**:

```sql
-- Keyset pagination (Fast: uses index on primary key/column)
SELECT * FROM orders
WHERE id > 100000
ORDER BY id ASC
LIMIT 50;
```

---

**Q47. What is the N+1 query problem? How do you solve it?**

**A:** The N+1 query problem occurs when an application fetches a list of N parent records, and then makes N subsequent queries to fetch child records for each parent.
*   *Solution:* Use SQL `JOIN`s to fetch all data in a single query, or use eager loading / batch fetching configurations in your ORM framework.

---

**Q48. How would you optimize a slow JOIN between two large tables?**

**A:**
1.  Ensure the columns used in the `ON` join condition are indexed on both tables.
2.  Analyze the execution plan (`EXPLAIN ANALYZE`) to verify if the optimizer is using a Hash Join or nested loops.
3.  Filter the rows in a `WHERE` clause before joining to reduce dataset sizes.
4.  Increase the database work memory configuration so sorting or hashing can happen in RAM instead of disk.

---
