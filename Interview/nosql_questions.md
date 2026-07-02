# NoSQL — Interview Questions

> Mid to Senior Level | Software Developer Role

---

## Core Concepts

1. What is NoSQL? How does it differ from relational databases?
2. What are the four main categories of NoSQL databases? Give an example for each.
3. What is the CAP theorem? Explain each property.
4. What is the BASE model and how does it compare to ACID?
5. What is eventual consistency? When is it acceptable?
6. When would you choose NoSQL over SQL for a project?
7. What are the trade-offs between consistency and availability in distributed NoSQL systems?
8. What is a schema-less database? What are the challenges of operating one at scale?

---

## Document Stores (MongoDB)

9. What is a document in MongoDB? How is it stored internally?
10. What is a collection in MongoDB vs a table in SQL?
11. What are embedded documents vs referenced documents? When do you use each?
12. How does indexing work in MongoDB? What types of indexes are supported?
13. What is the aggregation pipeline in MongoDB? Give an example.
14. What is the `$lookup` stage and how does it perform JOINs?
15. What is sharding in MongoDB? How does it distribute data?
16. What is a replica set in MongoDB and how does it provide high availability?
17. What is the oplog? How does it support replication?
18. What is write concern and read preference in MongoDB?

---

## Key-Value Stores (Redis)

19. What is Redis and what are its primary use cases?
20. What data structures does Redis support natively?
21. How does Redis handle persistence (RDB vs AOF)?
22. What is Redis pub/sub? When would you use it vs a message queue?
23. How does Redis clustering work?
24. What is a Redis pipeline and why does it improve performance?
25. What is TTL in Redis and how do you implement cache eviction policies?
26. What is the difference between `SET` and `SETNX` in Redis?

---

## Wide-Column Stores (Cassandra)

27. What is Apache Cassandra and what is its primary strength?
28. What is the partition key and clustering key in Cassandra?
29. How does Cassandra achieve high write throughput?
30. What is a gossip protocol and how does Cassandra use it?
31. What is a tombstone in Cassandra and why can it cause performance issues?
32. Why should you model tables in Cassandra by query patterns rather than data relationships?
33. What is the replication factor and consistency level in Cassandra?

---

## Graph Databases

34. What is a graph database? Name two popular ones.
35. What problems are best solved with a graph database?
36. What is a Cypher query? Give a basic example.

---

## Design & Production

37. How do you handle schema migrations in a NoSQL database?
38. What is a hot partition and how do you avoid it?
39. How do you implement pagination in a document store?
40. How do you ensure data consistency across multiple NoSQL collections?
41. How would you model a time-series dataset in a NoSQL database?
42. What is multi-document transaction support in MongoDB? When should you use it?
43. How do you back up and restore a NoSQL database in production?

---
