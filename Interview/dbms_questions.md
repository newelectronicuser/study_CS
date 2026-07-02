# DBMS — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Fundamentals

1. What is a DBMS? How does it differ from a file system?
2. What are the different types of database models (relational, hierarchical, network, object-oriented)?
3. What is a data model? What is the difference between the conceptual, logical, and physical data model?
4. What is the three-schema architecture (external, conceptual, internal)?
5. What is data independence? Distinguish between logical and physical data independence.
6. What is a DDL vs DML vs DCL vs TCL in SQL?
7. What is a database schema?

---

## ER Modeling

8. What is an Entity-Relationship (ER) diagram? What are the components?
9. What is the difference between a strong entity and a weak entity?
10. What is cardinality in an ER diagram (1:1, 1:N, M:N)?
11. What is a participation constraint (total vs partial)?
12. How do you convert an ER diagram to relational tables?
13. What is a generalization vs specialization in ER modeling?

---

## Normalization

14. What is normalization and why is it important?
15. Explain 1NF, 2NF, 3NF, and BCNF with examples.
16. What is a functional dependency?
17. What is a partial dependency and how does it violate 2NF?
18. What is a transitive dependency and how does it violate 3NF?
19. What is the difference between 3NF and BCNF?
20. What are 4NF and 5NF? When do they apply?
21. When and why would you denormalize a database?

---

## Relational Algebra & Calculus

22. What is relational algebra? Name the fundamental operators.
23. What is the difference between selection (σ) and projection (π)?
24. What is the difference between natural join, equi-join, and theta join?
25. What is the Cartesian product in relational algebra?
26. What is relational calculus and how does it differ from relational algebra?

---

## Transactions & Concurrency Control

27. What are the ACID properties? Define each.
28. What is a transaction? What is a transaction log?
29. What are transaction isolation levels? List them in order of strictness.
30. What is a dirty read, non-repeatable read, and phantom read?
31. What is a schedule in DBMS? What is a serializable schedule?
32. What is conflict serializability? What is view serializability?
33. What is a precedence graph and how is it used to test conflict serializability?
34. What is two-phase locking (2PL)? What is strict 2PL?
35. What is a deadlock in DBMS? How is it detected and resolved?
36. What is timestamp-based concurrency control?
37. What is optimistic concurrency control (OCC)?
38. What is MVCC (Multi-Version Concurrency Control)? Which databases use it?

---

## Indexing & Query Processing

39. What is an index in DBMS? What is the difference between dense and sparse indexes?
40. What is a B-Tree and a B+ Tree? Why is B+ Tree preferred for database indexes?
41. What is a hash index? When is it better than a B+ Tree index?
42. What is a bitmap index and when is it used?
43. What is query optimization? What is a query execution plan?
44. What is a cost-based optimizer vs a rule-based optimizer?
45. What is join ordering and why does it matter for query performance?
46. What are the different join algorithms (nested loop, hash join, sort-merge join)?

---

## Recovery & Reliability

47. What is database recovery? What types of failures exist?
48. What is a checkpoint in DBMS and how does it speed up recovery?
49. What is write-ahead logging (WAL)?
50. What is the ARIES recovery algorithm?
51. What is the difference between undo logging and redo logging?
52. What is shadow paging?

---

## Distributed Databases

53. What is a distributed database? What are its advantages and challenges?
54. What is data fragmentation (horizontal, vertical, mixed)?
55. What is data replication and what consistency models exist?
56. What is the CAP theorem and how does it apply to distributed DBs?
57. What is a two-phase commit (2PC)? What are its limitations?
58. What is a distributed query?

---
