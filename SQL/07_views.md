# SQL: Views 📽️

Views are virtual tables that provide a simplified or restricted view of the underlying data. They don't store data themselves but represent a saved SQL query.

---

## Table of Contents

1. [Creating Views](#1-creating-views-📽️)
2. [Altering or Dropping Views](#2-altering-or-dropping-views-✂️)
3. [Updatable Views](#3-updatable-views-🔄)
4. [THE WITH CHECK OPTION Clause](#4-the-with-check-option-clause-🛡️)
5. [Other Benefits of Views](#5-other-benefits-of-views-💎)

---

## 1- Creating Views 📽️

Views are created using the `CREATE VIEW` statement. They are excellent for simplifying complex queries that you run frequently.

```sql
-- Create a view for Sales by Client
CREATE VIEW sql_invoicing.sales_by_client AS
SELECT
    c.client_id,
    c.name,
    SUM(i.invoice_total) AS total_sales
FROM sql_invoicing.clients c
JOIN sql_invoicing.invoices i USING(client_id)
GROUP BY c.client_id, c.name;

-- You can now query the view like a regular table
SELECT *
FROM sql_invoicing.sales_by_client sbc
ORDER BY sbc.total_sales DESC;

-- You can even join views with other tables
SELECT *
FROM sql_invoicing.sales_by_client sbc
JOIN sql_invoicing.clients c USING(client_id);
```

---

## 2- Altering or Dropping Views ✂️

If you need to change the definition of a view or remove it entirely.

```sql
-- Removing a view
DROP VIEW sql_invoicing.sales_by_client;

-- Modifying a view (Recommended: use CREATE OR REPLACE)
CREATE OR REPLACE VIEW sql_invoicing.sales_by_client AS
SELECT
    c.client_id,
    c.name,
    SUM(i.invoice_total) AS total_sales
FROM sql_invoicing.clients c
JOIN sql_invoicing.invoices i USING(client_id)
GROUP BY c.client_id, c.name;
```

> [!TIP]
> Using `CREATE OR REPLACE` is often more convenient than dropping and recreating, especially if you have scripts or tools managing your schema.

---

## 3- Updatable Views 🔄

You can update, delete, or insert data through a view if it meets certain criteria (like not having `DISTINCT`, `GROUP BY`, or aggregate functions).

```sql
CREATE OR REPLACE VIEW sql_invoicing.invoices_with_balance AS
SELECT
    i.invoice_id,
    i.number,
    i.client_id,
    i.invoice_total,
    i.payment_total,
    i.invoice_total - i.payment_total AS balance,
    i.invoice_date,
    i.due_date,
    i.payment_date
FROM sql_invoicing.invoices i
WHERE (i.invoice_total - i.payment_total) > 0;

-- Deleting via a view
DELETE FROM sql_invoicing.invoices_with_balance
WHERE invoice_id = 1;

-- Updating via a view
UPDATE sql_invoicing.invoices_with_balance
SET due_date = DATE_ADD(due_date, INTERVAL 2 DAY)
WHERE invoice_id = 2;
```

---

## 4- THE WITH CHECK OPTION Clause 🛡️

Used to prevent updates that would make a row "disappear" from the view.

```sql
CREATE OR REPLACE VIEW sql_invoicing.invoices_with_balance AS
SELECT
    i.invoice_id,
    i.number,
    i.client_id,
    i.invoice_total,
    i.payment_total,
    i.invoice_total - i.payment_total AS balance,
    i.invoice_date,
    i.due_date,
    i.payment_date
FROM sql_invoicing.invoices i
WHERE (i.invoice_total - i.payment_total) > 0
WITH CHECK OPTION;

-- This update would fail because it makes the balance 0,
-- causing the row to disappear from the view criteria.
UPDATE sql_invoicing.invoices_with_balance
SET payment_total = invoice_total
WHERE invoice_id = 2;
```

> [!IMPORTANT]
> `WITH CHECK OPTION` is a best practice for updatable views to ensure data integrity.

---

## 5- Other Benefits of Views 💎

Views provide several secondary advantages beyond just query simplification:

1.  **Reduce Impact of Change**: If your table structure changes, you can simply update the view definition without changing every single application query.
2.  **Security/Table-level Permissions**: You can give users access to a view (e.g., restricted columns) instead of giving them access to the full underlying table.
3.  **Encapsulation**: Views encapsulate complex join and aggregation logic, providing a cleaner API for data consumers.

---

_Interview Favorite: "What is the difference between a View and a Table?" (Views don't store data; tables do)._
