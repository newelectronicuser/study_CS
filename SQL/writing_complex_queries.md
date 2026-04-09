# 📊 Writing Complex Queries: SQL Study Guide

Mastering subqueries and complex join logic is essential for advanced data manipulation and interview preparation. This guide breaks down the core concepts into 10 key sections with practical examples.

---

## 1. Introduction
Subqueries (or inner queries) are queries nested within another SQL statement. They provide a powerful way to perform multi-step data retrieval in a single command.

> [!NOTE]
> A subquery can return a single value (scalar), a list of values, or a full table depending on where it's used.

---

## 2. Subqueries
The most common use of a subquery is to filter data in the `WHERE` clause based on a dynamic calculation.

```sql
-- Find products with a unit price higher than 'lettuce'
SELECT *
FROM products p
WHERE p.unit_price > (
    SELECT p.unit_price
    FROM products p
    WHERE p.name LIKE '%lettuce%'
);

-- Find employees who earn more than the average salary
SELECT *
FROM sql_hr.employees e
WHERE e.salary > (
    SELECT AVG(e.salary)
    FROM sql_hr.employees e
);
```

---

## 3. The IN Operator
Used when a subquery returns a list of values. It's often used to exclude or include specific IDs.

```sql
-- Find products that have NEVER been ordered (using NOT IN)
SELECT *
FROM products p
WHERE p.product_id NOT IN (
    SELECT DISTINCT oi.product_id
    FROM sql_store.order_items oi
);

-- Alternative using GROUP BY
SELECT *
FROM products p
WHERE p.product_id NOT IN (
    SELECT oi.product_id
    FROM sql_store.order_items oi
    GROUP BY oi.product_id
);

-- Find clients without invoices
SELECT *
FROM sql_invoicing.clients c
WHERE c.client_id NOT IN (
    SELECT DISTINCT i.client_id
    FROM sql_invoicing.invoices i
);
```

---

## 4. Subqueries vs Joins
Many queries can be written using either subqueries or joins. Choosing the right one depends on readability and performance.

> [!TIP]
> **Joins** are generally more readable and performant for larger datasets, but **Subqueries** can be easier for logic that involves "selecting based on a set."

```sql
-- Using LEFT JOIN to find clients without invoices
SELECT *
FROM sql_invoicing.clients c
LEFT JOIN sql_invoicing.invoices i USING (client_id)
WHERE i.invoice_id IS NULL;

-- Nested Subqueries: Find customers who bought 'lettuce'
SELECT *
FROM sql_store.customers c
WHERE c.customer_id IN (
    SELECT o.customer_id
    FROM sql_store.orders o
    WHERE o.order_id IN (
        SELECT oi.order_id
        FROM sql_store.order_items oi
        WHERE oi.product_id IN (
            SELECT p.product_id
            FROM products p
            WHERE p.name LIKE '%lettuce%'
        )
    )
);

-- Re-written as JOIN (Cleaner & more readable)
SELECT c.customer_id, c.first_name, c.last_name
FROM sql_store.customers c
JOIN sql_store.orders o USING(customer_id)
JOIN sql_store.order_items oi ON o.order_id = oi.order_id
JOIN sql_store.products p ON oi.product_id = p.product_id
WHERE p.name LIKE '%lettuce%'
GROUP BY c.customer_id
ORDER BY c.customer_id;
```

---

## 5. The ALL Keyword
The `ALL` keyword allows you to compare a single value against every value returned by a subquery.

```sql
-- Find invoices larger than EVERY invoice for client 3 (Same as MAX)
SELECT *
FROM sql_invoicing.invoices i
WHERE i.invoice_total > (
    SELECT MAX(i.invoice_total)
    FROM sql_invoicing.invoices i
    WHERE i.client_id = 3
);

-- Using the ALL keyword
SELECT *
FROM sql_invoicing.invoices i
WHERE i.invoice_total > ALL(
    SELECT i2.invoice_total
    FROM sql_invoicing.invoices i2
    WHERE i2.client_id = 3
);
```

---

## 6. The ANY Keyword
`ANY` (or `SOME`) checks if a condition is true for at least one value in the set.

```sql
-- Find clients with at least two invoices (Using IN)
SELECT *
FROM sql_invoicing.clients
WHERE client_id IN (
    SELECT client_id
    FROM sql_invoicing.invoices i
    GROUP BY i.client_id
    HAVING COUNT(*) > 2
);

-- Find clients with at least two invoices (Using ANY)
SELECT *
FROM sql_invoicing.clients
WHERE client_id = ANY (
    SELECT client_id
    FROM sql_invoicing.invoices i
    GROUP BY i.client_id
    HAVING COUNT(*) > 2
);
```

---

## 7. Correlated Subqueries
A subquery that refers to a column from the outer query for each row processed.

> [!CAUTION]
> Correlated subqueries are executed once for every row in the outer query, which can be slow on large datasets.

```sql
-- Get employees earning more than the average in THEIR SPECIFIC office
SELECT *
FROM sql_hr.employees e
WHERE e.salary > (
    SELECT AVG(e2.salary)
    FROM sql_hr.employees e2
    WHERE e2.office_id = e.office_id
);

-- Get invoices larger than the client's average invoice amount
SELECT *
FROM sql_invoicing.invoices i
WHERE i.invoice_total > (
    SELECT AVG(i2.invoice_total)
    FROM sql_invoicing.invoices i2
    WHERE i2.client_id = i.client_id
);
```

---

## 8. The EXISTS Operator
`EXISTS` checks for the presence of rows. It is often faster than `IN` because it stops searching as soon as it finds a match.

```sql
-- Find clients that have invoices (Using EXISTS vs IN)
SELECT *
FROM sql_invoicing.clients c
WHERE EXISTS (
    SELECT i.client_id
    FROM sql_invoicing.invoices i
    WHERE i.client_id = c.client_id
);

-- Find products never ordered
SELECT *
FROM sql_store.products p
WHERE NOT EXISTS (
    SELECT oi.product_id
    FROM sql_store.order_items oi
    WHERE oi.product_id = p.product_id
);
```

---

## 9. Subqueries in the SELECT Clause
You can use subqueries to calculate dynamic values that appear as columns.

```sql
-- Show every invoice with the global average and the difference
SELECT 
    i.invoice_id,
    i.invoice_total,
    (SELECT AVG(i2.invoice_total) FROM sql_invoicing.invoices i2) AS invoice_average,
    i.invoice_total - (SELECT invoice_average) AS difference
FROM sql_invoicing.invoices i;

-- Summary of sales per client vs global average
SELECT 
    c.client_id,
    c.name,
    (SELECT SUM(invoice_total) FROM sql_invoicing.invoices WHERE client_id = c.client_id) AS total_sales,
    (SELECT AVG(i2.invoice_total) FROM sql_invoicing.invoices i2) AS average,
    (SELECT total_sales - average) AS difference
FROM sql_invoicing.clients c;
```

---

## 10. Subqueries in the FROM Clause
You can treat the result of a query as a virtual table (often called a Derived Table).

> [!IMPORTANT]
> When using a subquery in a `FROM` clause, you **must** provide an alias.

```sql
-- Select from a summary result to filter for non-null sales
SELECT *
FROM (
    SELECT 
        c.client_id,
        c.name,
        (SELECT SUM(invoice_total) FROM sql_invoicing.invoices WHERE client_id = c.client_id) AS total_sales,
        (SELECT AVG(i2.invoice_total) FROM sql_invoicing.invoices i2) AS average,
        (SELECT total_sales - average) AS difference
    FROM sql_invoicing.clients c
) AS sales_summary
WHERE total_sales IS NOT NULL;
```
