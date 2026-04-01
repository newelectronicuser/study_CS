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
```

> [!TIP]
> When using `ROLLUP`, you cannot use an `ORDER BY` clause in the same query (in some versions/syntaxes) as the rollup implicitly sorts the data for its calculation.
