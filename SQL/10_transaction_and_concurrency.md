Transaction and Concurrency

Transaction : A group of SQL statements that represent a single unit of work.

Atomicity : transactions are like atoms. Each transaction is a single unit of work no matter how many statements it contains.

Consistency : Database will always remain in a consistent state. We won’t end up with an order without an item.

Isolation : Transactions are isolated or protected from each other if they try to modify the same data. So, they cannot interfere with each other.

Durability : At the time of power failure or system failure, data won’t be lost.

Creating Transactions

SET @@autocommit = 0;

SELECT @@autocommit;

START TRANSACTION;

INSERT INTO sql_store.orders
(customer_id, order_date, status)
VALUES
(1, '2019-01-01', 1);
INSERT INTO sql_store.order_items
VALUES (LAST_INSERT_ID(), 1,1,1);

COMMIT;

ROLLBACK;

SHOW VARIABLES LIKE '%autocommit';

Concurrency and Locking

USE sql_store;
START TRANSACTION;
UPDATE customers
SET points = points +10
WHERE customer_id = 1;
COMMIT;

In mysql workbench, open two tabs with new connections to db. In one of the tabs, start a transaction and execute the update statement. But, don’t commit yet. Now, in another tab, try to run this entire script. You will see the spinner because it didn’t get the lock due to the previous transaction which didn’t complete yet. This operation may get timed out.

Concurrency Problems

Lost Updates : during race conditions, the last commit will overwrite the data.

![Lost Updates](files/Screenshot%20from%202026-03-26%2008-58-32.png)

Dirty Reads : Happens when a transaction reads data that hasn’t been committed yet.

![Dirty Reads](files/Screenshot%20from%202026-03-26%2009-01-49.png)

Non-repeating Reads : During a course of a transaction, you read something twice and get different results.

![Non-repeating Reads](files/Screenshot%20from%202026-03-26%2009-04-49.png)

Phantom Reads : We have data that suddenly appears like ghosts or we miss them in our query, because they get added, updated or removed after we execute our query.

![Phantom Reads](files/Screenshot%20from%202026-03-26%2009-08-18.png)

Try to prevent phantom reads only when it is absolutely necessary with an isolation level called serializable.

Transaction Isolation Levels

![Transaction Isolation Levels](files/Screenshot%20from%202026-03-26%2009-14-33.png)

By default mysql is configured with repeatable-read as isolation level.

READ UNCOMMITTED Isolation Level

USE sql_store;

START TRANSACTION;

UPDATE customers
SET points = 20
WHERE customer_id = 1;

COMMIT;

ROLLBACK;

Run the above transaction without commit.

USE sql_store;

SET TRANSACTION ISOLATION LEVEL READ UNCOMMITTED;

SELECT points
FROM customers
WHERE customer_id = 1;

Then run this statement and see points as 20.

READ COMMITTED Isolation Level

USE sql_store;

START TRANSACTION;

UPDATE customers
SET points = 20
WHERE customer_id = 1;

COMMIT;

ROLLBACK;

Run the above transaction without commit.

USE sql_store;

SET TRANSACTION ISOLATION LEVEL READ COMMITTED;

SELECT points
FROM customers
WHERE customer_id = 1;

Then run this statement and see the old uncommitted data.
But, run this statement once again after committing the data. You will see 20.

REPEATABLE READ Isolation Level

Code 1
USE sql_store;

START TRANSACTION;

UPDATE customers
SET points = 40
WHERE customer_id = 1;

COMMIT;

ROLLBACK;

Code 2
USE sql_store;

SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;

START TRANSACTION;

SELECT points
FROM customers
WHERE customer_id = 1;

-- Run Code 3

SELECT points
FROM customers
WHERE customer_id = 1;

COMMIT;

Code 3
USE sql_store;

START TRANSACTION;

UPDATE customers
SET points = 40
WHERE customer_id = 1;

COMMIT;

ROLLBACK;

Transaction at Code 2 won't be able to see the changes done by Code 3 during race conditions.

SERIALIZABLE Isolation Level

Code 1
USE sql_store;

SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

START TRANSACTION;

-- Run Code 2

SELECT \*
FROM customers
WHERE state = 'VA';

COMMIT;

Code 2
USE sql_store;

START TRANSACTION;

UPDATE customers
SET state = 'VA'
WHERE customer_id = 1;

COMMIT;

ROLLBACK;

Code 1 will wait for Code 2 to finish because Code 2 got the lock first and won't show old data.

Deadlock

USE sql_store;

START TRANSACTION;

UPDATE customers
SET state = 'VA'
WHERE customer_id = 1;

UPDATE orders
SET status = 1
WHERE order_id = 1;

COMMIT;

ROLLBACK;

USE sql_store;

START TRANSACTION;

UPDATE orders
SET status = 1
WHERE order_id = 1;

UPDATE customers
SET state = 'VA'
WHERE customer_id = 1;

COMMIT;

ROLLBACK;

During race condition, there is a very high probability that the codes above will get stuck due to deadlock.
