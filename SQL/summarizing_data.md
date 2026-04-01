# SQL: Summarizing Data - Interview Notes 📊

## 1- Aggregate Functions 🧮
Aggregate functions take a series of values and return a single value. They are the backbone of data reporting.

```sql
SELECT
    MAX(i.invoice_total) AS highest,
    MIN(i.invoice_total) AS lowest,
    AVG(i.invoice_total) AS average,
    SUM(i.invoice_total) + 10000 AS total,
    COUNT(i.invoice_total) AS number_of_invoices,
    COUNT(i.payment_date) AS count_of_payments, -- Counts only NOT NULL values
    COUNT(*) AS total_records,                 -- Counts everything including NULLs
    COUNT(DISTINCT i.client_id) AS total_clients
FROM sql_invoicing.invoices i
WHERE i.invoice_date > '2019-07-01';

-- Comprehensive Report Example (Using UNION)
SELECT
    'First half of 2019' AS date_range,
    SUM(i.invoice_total) AS total_sales,
    SUM(i.payment_total) AS total_payments,
    SUM(i.invoice_total - i.payment_total) AS what_we_expect
FROM sql_invoicing.invoices i
WHERE i.invoice_date BETWEEN '2019-01-01' AND '2019-06-30'
UNION
SELECT
    'Second half of 2019' AS date_range,
    SUM(i.invoice_total) AS total_sales,
    SUM(i.payment_total) AS total_payments,
    SUM(i.invoice_total - i.payment_total) AS what_we_expect
FROM sql_invoicing.invoices i
WHERE i.invoice_date BETWEEN '2019-07-01' AND '2019-12-31'
UNION
SELECT
    'Total' AS date_range,
    SUM(i.invoice_total) AS total_sales,
    SUM(i.payment_total) AS total_payments,
    SUM(i.invoice_total - i.payment_total) AS what_we_expect
FROM sql_invoicing.invoices i
WHERE i.invoice_date BETWEEN '2019-01-01' AND '2019-12-31';
```

> [!IMPORTANT]
> Aggregate functions **ignore NULL values** (except for `COUNT(*)`). If you want to count unique entries, use `COUNT(DISTINCT column)`.

---

## 2- The GROUP BY Clause 📂
The `GROUP BY` clause divides the result set into groups based on one or more columns.

```sql
-- Grouping by a single column
SELECT
    i.client_id,
    SUM(i.invoice_total) AS total_sales
FROM sql_invoicing.invoices i
WHERE i.invoice_date >= '2019-07-01'
GROUP BY i.client_id
ORDER BY total_sales DESC;

-- Grouping by multiple columns (Hierarchical)
SELECT
    c.state,
    c.city,
    SUM(i.invoice_total) AS total_sales
FROM sql_invoicing.invoices i
JOIN sql_invoicing.clients c USING(client_id)
GROUP BY c.state, c.city;

-- Grouping by Date and Payment Method
SELECT 
    p.`date` AS date,
    pm.name AS payment_method,
    SUM(p.amount) AS total_payments
FROM sql_invoicing.payments p 
JOIN sql_invoicing.payment_methods pm ON
    p.payment_method = pm.payment_method_id 
GROUP BY date, payment_method
ORDER BY date ASC;
```

---

## 3- The HAVING Clause 🔍
The `HAVING` clause is used to filter data **after** grouping has occurred.

```sql
-- Filtering groups based on aggregated results
SELECT
    i.client_id,
    SUM(i.invoice_total) AS total_sales,
    COUNT(*) AS number_of_invoices
FROM sql_invoicing.invoices i
GROUP BY client_id  
HAVING total_sales > 500 AND number_of_invoices > 5;

-- Complex Example: Joins + Grouping + Having
-- Finds customers in 'VA' who spent more than $100
SELECT 
    c.customer_id,
    c.first_name AS customer_name,
    c.state,
    SUM(oi.quantity * oi.unit_price) AS money_spent
FROM orders o 
JOIN customers c USING(customer_id)
JOIN order_items oi USING(order_id)
WHERE c.state = 'VA'
GROUP BY customer_id
HAVING money_spent > 100;
```

> [!IMPORTANT]
> **WHERE vs. HAVING**:
> - `WHERE`: Filters rows **before** they are grouped.
> - `HAVING`: Filters rows **after** they have been grouped (used specifically for aggregated columns).

---

## 4- The ROLLUP Operator [MySQL Only] 🌀
The `WITH ROLLUP` operator calculates subtotals and grand totals for the grouped columns.

```sql
-- Single level total
SELECT
    client_id,
    SUM(invoice_total) AS total_sales
FROM sql_invoicing.invoices i
GROUP BY client_id WITH ROLLUP;

-- Multi-level totals (State subtotal + Grand total)
SELECT
    state,
    city,
    SUM(invoice_total) AS total_sales
FROM sql_invoicing.invoices i
JOIN sql_invoicing.clients c USING(client_id)
GROUP BY state, city WITH ROLLUP;

-- Totaling by Payment Method
SELECT 
    pm.name AS payment_method,
    SUM(p.amount) AS total
FROM sql_invoicing.payments p 
JOIN sql_invoicing.payment_methods pm ON
    p.payment_method = pm.payment_method_id
GROUP BY pm.name WITH ROLLUP;
```

> [!TIP]
> When using `ROLLUP`, you cannot use an `ORDER BY` clause in the same query (in some versions/syntaxes) as the rollup implicitly sorts the data for its calculation.
