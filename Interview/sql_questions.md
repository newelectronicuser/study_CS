# SQL — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Fundamentals

1. What is the difference between `WHERE` and `HAVING`?
2. Explain the different types of SQL JOINs with examples.
3. What is the difference between `UNION` and `UNION ALL`?
4. What are aggregate functions? Name five common ones.
5. What is a subquery? What is a correlated subquery?
6. What is the difference between `DELETE`, `TRUNCATE`, and `DROP`?
7. What is the difference between `IN`, `EXISTS`, and `ANY`/`ALL`?
8. Explain the execution order of a SQL query (FROM → WHERE → GROUP BY → HAVING → SELECT → ORDER BY).
9. What are NULL values and how does SQL handle them in comparisons?
10. What is the difference between `CHAR`, `VARCHAR`, and `TEXT`?

---

## Indexes & Performance

11. What is a database index? How does it improve query performance?
12. What is the difference between a clustered and a non-clustered index?
13. What is a composite index? How does column order matter?
14. When would an index hurt performance instead of helping?
15. What is an index covering query? What is a covering index?
16. What is the `EXPLAIN` / `EXPLAIN ANALYZE` command used for?
17. What are statistics in a database and how do they affect query planning?
18. What is a query execution plan and how do you read one?

---

## Transactions & Concurrency

19. What are the ACID properties of a database transaction?
20. What are the four transaction isolation levels? Explain each.
21. What is a dirty read, non-repeatable read, and phantom read?
22. What is a deadlock and how do you detect and prevent it?
23. What is optimistic vs pessimistic locking?
24. What is a savepoint in a transaction?

---

## Schema Design & Normalization

25. What are the normal forms (1NF, 2NF, 3NF, BCNF)?
26. When would you intentionally denormalize a schema?
27. What is a surrogate key vs a natural key?
28. What is a foreign key constraint and what are the `ON DELETE` options?
29. What is the difference between a primary key and a unique key?
30. How would you design a schema for a many-to-many relationship?

---

## Window Functions & Advanced SQL

31. What are window functions? How do they differ from aggregate functions?
32. Explain `ROW_NUMBER()`, `RANK()`, and `DENSE_RANK()` with examples.
33. What is the `PARTITION BY` clause in a window function?
34. What is a Common Table Expression (CTE)? How is it different from a subquery?
35. What is a recursive CTE? Give an example use case.
36. What is `LEAD()` and `LAG()` used for?
37. How would you calculate a running total using a window function?

---

## Stored Procedures, Views & Triggers

38. What is a stored procedure? What are its advantages and disadvantages?
39. What is the difference between a view and a materialized view?
40. What is a trigger? Name use cases where a trigger is appropriate vs inappropriate.
41. What is the difference between a function and a stored procedure in SQL?

---

## Performance Tuning & Production

42. How do you find and kill long-running queries in a database?
43. What is connection pooling and why is it important?
44. What is database sharding? What are its trade-offs?
45. What is vertical vs horizontal partitioning?
46. How do you handle pagination efficiently for large datasets?
47. What is the N+1 query problem? How do you solve it?
48. How would you optimize a slow JOIN between two large tables?

---
