# SQL: Retrieving Data From a Single Table 🗄️

## 1- The SELECT Statement 📝
The basic syntax of a SQL query involves at least two clauses: `SELECT` and `FROM`.

```sql
USE sql_store;

SELECT customer_id, first_name
FROM customers c
WHERE c.customer_id = 1
ORDER BY c.first_name ASC;
```

> [!TIP]
> Use the `USE` statement to select the database you want to work with.

---

## 2- The SELECT Clause ✨
The `SELECT` clause determines which columns are returned in the result set. You can use math expressions and assign **aliases** using the `AS` keyword.

```sql
-- Selecting literals
SELECT 9, 6;

-- Math expressions and Aliases
SELECT 
    c.last_name, 
    c.first_name, 
    c.points, 
    (c.points + 10) * 100 AS 'discount factor'
FROM customers c;
```

---

## 3- The WHERE Clause 🔍
The `WHERE` clause is used to filter the row count based on specific conditions.

```sql
SELECT *
FROM customers c
WHERE c.birth_date > '1990-01-01';
```

> [!IMPORTANT]
> In most SQL engines, you **cannot** use an alias defined in the `SELECT` clause in the `WHERE` clause directly. You must repeat the expression:
> `WHERE (points + 10) * 100 > 1000`

---

## 4- The AND, OR and NOT Operators ⚖️
These logical operators allow you to combine multiple filtering conditions.

```sql
-- AND operator (Both conditions must be true)
SELECT *
FROM orders o
WHERE order_date >= '2018-01-01' 
  AND o.order_date < '2018-12-31';

-- NOT and OR operators
SELECT *
FROM customers c
WHERE NOT (c.birth_date > '1990-01-01' OR c.points > 1000);

-- Complex logic with parentheses
SELECT *
FROM order_items oi
WHERE oi.product_id = 6 AND (oi.unit_price * oi.quantity > 30);
```

---

## 5- The IN Operator 📍
The `IN` operator is a shorthand for multiple `OR` conditions.

```sql
-- Matching multiple values
SELECT *
FROM customers c
WHERE c.state IN ('VA', 'FL', 'GA');

-- NOT IN operator
SELECT *
FROM customers c
WHERE c.state NOT IN ('VA', 'FL', 'GA');

SELECT *
FROM products p
WHERE p.quantity_in_stock IN (49, 38, 72);
```

---

## 6- The BETWEEN Operator 📏
Used to find values within a specific range (inclusive).

```sql
-- Using comparison operators (Less readable)
SELECT *
FROM customers c
WHERE c.points >= 1000 AND c.points <= 3000;

-- Using BETWEEN (More concise)
SELECT *
FROM customers c
WHERE c.points BETWEEN 1000 AND 3000;

SELECT *
FROM customers c
WHERE c.birth_date BETWEEN '1990-01-01' AND '2000-01-01';
```

---

## 7- The LIKE Operator 🎭
Used for pattern matching within a string.
- `%` : Represents any number of characters.
- `_` : Represents a single character.

```sql
-- Starts with 'bo'
SELECT *
FROM customers c
WHERE c.last_name LIKE 'bo%';

-- Starts with 'b', exactly 4 chars in between, ends with 'y'
SELECT *
FROM customers c
WHERE c.last_name LIKE 'b____y';

-- Contains 'TRAIL' or 'AVENUE'
SELECT *
FROM customers c
WHERE c.address LIKE '%TRAIL%' OR c.address LIKE '%AVENUE%';

-- Ends with '9'
SELECT *
FROM customers c
WHERE c.phone LIKE '%9';
```

---

## 8- The REGEXP Operator 🤖
Regular Expressions are more powerful than `LIKE` for complex pattern matching.

```sql
-- Contains 'field'
SELECT *
FROM customers c
WHERE c.last_name REGEXP 'field';

-- Anchors: ^ (beginning), $ (end)
SELECT * FROM customers c WHERE c.last_name REGEXP '^b'; -- Starts with b
SELECT * FROM customers c WHERE c.last_name REGEXP 'y$'; -- Ends with y

-- Logical OR (|)
SELECT *
FROM customers c
WHERE c.last_name REGEXP '^field|mac|rose';

-- Character Sets []
SELECT * FROM customers c WHERE c.last_name REGEXP '[gim]e'; -- ge, ie, or me
SELECT * FROM customers c WHERE c.last_name REGEXP '[a-h]e'; -- any char a-h then e

-- More Examples
SELECT * FROM customers c WHERE c.first_name REGEXP 'elka|ambur';
SELECT * FROM customers c WHERE c.last_name REGEXP 'ey$|on$';
SELECT * FROM customers c WHERE c.last_name REGEXP '^my|se';
SELECT * FROM customers c WHERE c.last_name REGEXP 'b[ru]';
```

---

## 9- The IS NULL Operator ⚪
Used to find records where a column value is missing.

```sql
-- Missing phone number
SELECT *
FROM customers c
WHERE c.phone IS NULL;

-- Orders not yet shipped
SELECT *
FROM orders o
WHERE o.shipped_date IS NULL;
```

---

## 10- The ORDER BY Clause ↕️
Used to sort the result set in ascending (`ASC`, default) or descending (`DESC`) order.

```sql
-- Sorting by multiple columns
SELECT *
FROM customers c
ORDER BY c.state DESC, c.first_name DESC;

-- Sorting by expressions
SELECT *
FROM order_items oi
WHERE oi.order_id = 2
ORDER BY oi.quantity * oi.unit_price DESC;
```

---

## 11- The LIMIT Clause 🧱
Used to limit the number of records returned. Great for **Pagination**.

```sql
-- Skip first 6 records, pick the next 3
SELECT *
FROM customers c
LIMIT 6, 3;

-- Example: 9th record is skipped, then 3 records are returned (9, 10, 11)
SELECT *
FROM customers c
LIMIT 9, 3;
```
