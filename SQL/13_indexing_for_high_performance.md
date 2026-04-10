Indexing for High Performance

Indexes speed up our queries

SQL - 13- Performance Best Practices.pdf

Indexes

Cost of Indexes
Increase the database
Slow down the writes

Reserve indexes for performance critical queries.

Design indexes based on your queries, not your tables.

Creating Indexes

EXPLAIN SELECT c.customer_id
FROM sql_store.customers c
WHERE c.state = 'VA';

The following column values you would see:
type => ALL
rows => 10

CREATE INDEX idx_state ON sql_store.customers (state);

But, after indexing you would see this:
type => ref
rows => 1

EXPLAIN SELECT \*
FROM sql_store.customers c
WHERE c.points > 1000;
-- type ALL, rows 10

CREATE INDEX idx_points ON sql_store.customers (points);
-- type range, rows 5

Viewing Indexes

ANALYZE TABLE sql_store.customers;

SHOW INDEXES IN sql_store.customers;

BTREE = Binary Tree

Mysql creates indexes on foreign keys to quickly join tables.

Prefix Indexes

![Prefix Indexes](files/Screenshot%20from%202026-03-27%2013-58-48.png)

SELECT \*, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p
WHERE MATCH(p.title, p.body ) AGAINST('react -redux' IN BOOLEAN MODE);

Full-text Indexes

CREATE FULLTEXT INDEX idx_title_body ON sql_blog.posts (title, body);

SELECT \*
FROM sql_blog.posts p
WHERE MATCH(p.title, p.body) AGAINST('react redux');

SELECT \*, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p
WHERE MATCH(p.title, p.body ) AGAINST('react -redux' IN BOOLEAN MODE);

Looking for rows that have react but not redux.

SELECT \*, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p
WHERE MATCH(p.title, p.body ) AGAINST('react -redux +form' IN BOOLEAN MODE);

SELECT \*, MATCH(p.title, p.body ) AGAINST('react redux' IN BOOLEAN MODE)
FROM sql_blog.posts p
WHERE MATCH(p.title, p.body ) AGAINST('"handling a form"' IN BOOLEAN MODE);

    Search for an exact phrase in double quotes.

Composite Indexes

SHOW INDEXES IN sql_store.customers;

EXPLAIN SELECT c.customer_id
FROM sql_store.customers c
WHERE c.state = 'CA' AND c.points > 1000;

CREATE INDEX idx_state_points ON customers (state, points);

Order of Columns in Composite Index
Put the most frequently used columns first.
Put the columns with higher cardinality first.
Take your queries into account.

![Order of Columns in Composite Index](files/Screenshot%20from%202026-03-27%2016-16-23.png)

One can enforce a different INDEX in query

![Order of Columns in Composite Index](files/Screenshot%20from%202026-03-27%2016-17-41.png)

When Indexes are Ignored

![When Indexes are Ignored](files/Screenshot%20from%202026-03-27%2016-30-23.png)

The bottom could be better than the above

![When Indexes are Ignored](files/Screenshot%20from%202026-03-27%2016-33-17.png)

![When Indexes are Ignored](files/Screenshot%20from%202026-03-27%2016-34-29.png)

The bottom is better in performance than the above.

![When Indexes are Ignored](files/Screenshot%20from%202026-03-27%2016-34-42.png)

Using Indexes for Sorting

![Using Indexes for Sorting](files/Screenshot%20from%202026-03-27%2016-41-26.png)

Order by a column which is already indexed.

Covering Indexes

![Covering Indexes](files/Screenshot%20from%202026-03-27%2016-46-24.png)

Covering Indexes

Index Maintenance

Before creating new indexes, check the existing ones.

![Covering Indexes](files/Screenshot%20from%202026-03-27%2016-52-36.png)
