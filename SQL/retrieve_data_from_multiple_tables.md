# SQL: Retrieving Data From Multiple Tables 🔗

## 1- Inner Joins 🤝
The most common type of join. It returns records that have matching values in both tables.

```sql
-- Basic Inner Join
SELECT
    c.customer_id
FROM orders o
JOIN customers c 
  ON o.customer_id = c.customer_id;

-- Joining products and order items
SELECT
    oi.product_id,
    p.name,
    oi.quantity,
    oi.unit_price
FROM order_items oi
JOIN products p 
  ON oi.product_id = p.product_id;
```

---

## 2- Joining Across Databases 🌐
You can join tables from different databases by prefixing the table names with the database name.

```sql
SELECT
    oi.product_id,
    p.name,
    oi.quantity,
    oi.unit_price
FROM sql_store.order_items oi
JOIN sql_inventory.products p 
  ON oi.product_id = p.product_id;
```

---

## 3- Self Joins 🪞
A self join is used to join a table with itself. This is useful for hierarchical data like employees and their managers.

```sql
-- Joining employees to find their managers
SELECT
    e.employee_id, 
    e.first_name, 
    m.first_name AS manager
FROM sql_hr.employees e
JOIN sql_hr.employees m 
  ON e.reports_to = m.employee_id;
```

> [!TIP]
> Always use different aliases for the same table to distinguish between roles (e.g., `e` for employee, `m` for manager).

---

## 4- Joining Multiple Tables 🧱
You can join more than two tables by chaining multiple `JOIN` clauses.

```sql
-- Joining orders, statuses, and customers
SELECT
    o.order_id,
    o.order_date,
    c.first_name,
    c.last_name,
    os.name AS status
FROM orders o
JOIN order_statuses os 
  ON o.status = os.order_status_id
JOIN customers c 
  ON o.customer_id = c.customer_id;

-- Invoicing example: payments, clients, and payment methods
SELECT
    c.name AS client_name,
    p.invoice_id,
    p.`date`,
    p.amount,
    pm.name AS payment_method
FROM sql_invoicing.payments p
JOIN sql_invoicing.clients c 
  ON p.client_id = c.client_id
JOIN sql_invoicing.payment_methods pm 
  ON p.payment_method = pm.payment_method_id;
```

---

## 5- Compound Join Conditions 🔐
Used when a table has a **Composite Primary Key** (multiple columns).

```sql
SELECT *
FROM order_items oi
JOIN order_item_notes oin
  ON oi.order_id = oin.order_Id
  AND oi.product_id = oin.product_id;
```

---

## 6- Implicit Join Syntax 📜
An older way of joining tables without using the `JOIN` keyword.

```sql
-- Modern (Explicit) Syntax - RECOMMENDED
SELECT *
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id;

-- Implicit Syntax - AVOID
SELECT *
FROM orders o, customers c
WHERE o.customer_id = c.customer_id;
```

> [!CAUTION]
> If you forget the `WHERE` clause in implicit syntax, you will get a **Cross Join** (Cartesian product). Use explicit `JOIN` for safety.

---

## 7- Outer Joins 🚪
Outer joins return all records from one table, and the matching records from the other.

- **LEFT JOIN**: All records from the left table.
- **RIGHT JOIN**: All records from the right table.

```sql
-- Right Join: All orders, even if the customer is missing (not typical)
SELECT
    c.customer_id,
    c.first_name,
    o.order_id
FROM customers c
RIGHT JOIN orders o ON c.customer_id = o.customer_id
ORDER BY c.customer_id;

-- Left Join: All products, even if never ordered
SELECT
    p.product_id,
    p.name,
    oi.quantity
FROM products p
LEFT JOIN order_items oi ON p.product_id = oi.product_id;
```

---

## 8- Outer Join Between Multiple Tables 🏗️
Chaining multiple outer joins.

```sql
SELECT
    c.customer_id,
    c.first_name,
    o.order_id,
    s.name AS shipper
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
LEFT JOIN shippers s ON o.shipper_id = s.shipper_id
ORDER BY c.customer_id;
```

---

## 9- Self Outer Joins 🪞🚪
Using `LEFT JOIN` on a self join to include the top-level entity (e.g., the CEO who has no manager).

```sql
SELECT
    e.employee_id,
    e.first_name,
    m.first_name AS manager
FROM sql_hr.employees e
LEFT JOIN sql_hr.employees m 
  ON e.reports_to = m.employee_id;
```

---

## 10- The USING Clause 🛠️
A shorthand for joining when the column names are identical in both tables.

```sql
-- Using ON
-- JOIN customers c ON o.customer_id = c.customer_id

-- Using USING (Shorter)
SELECT
    o.order_id,
    c.first_name,
    s.name AS shipper
FROM orders o
JOIN customers c USING (customer_id)
LEFT JOIN shippers s USING (shipper_id);

-- Compound USING
SELECT *
FROM order_items oi
JOIN order_item_notes oin USING (order_id, product_id);
```

---

## 11- Natural Joins 🌿
The database automatically joins on any common column names.

```sql
-- Dangerous: Use with caution!
SELECT *
FROM orders o
NATURAL JOIN customers c;
```

> [!WARNING]
> Natural joins are unpredictable because they depend on column names being unique and matching across tables. Avoid in production.

---

## 12- Cross Joins ✖️
Combines every row from the first table with every row from the second table (Cartesian Product).

```sql
-- Explicit Cross Join
SELECT
    c.first_name AS customer,
    p.name AS product
FROM customers c
CROSS JOIN products p
ORDER BY c.first_name;

-- Implicit Cross Join
SELECT * FROM shippers s, products p;
```

---

## 13- Unions ➕
Unions allow you to combine result sets vertically from multiple queries.

```sql
-- Combining names of customers and shippers
SELECT c.first_name FROM customers c
UNION
SELECT s.name FROM shippers s;

-- Categorizing customers by points
SELECT c.customer_id, c.first_name, c.points, 'Bronze' AS type
FROM customers c WHERE c.points < 2000
UNION
SELECT c.customer_id, c.first_name, c.points, 'Silver' AS type
FROM customers c WHERE c.points BETWEEN 2000 AND 3000
UNION
SELECT c.customer_id, c.first_name, c.points, 'Gold' AS type
FROM customers c WHERE c.points > 3000
ORDER BY first_name;
```

> [!IMPORTANT]
> To use `UNION`, queries must return the **same number of columns**.

---

## A Quick Note 💡
- **Inner vs Outer**: Inner Join drops rows with no matches; Outer Join (Left/Right) preserves rows from one side.
- **Self Joins**: Always use aliases.
- **Explicit vs Implicit**: Always prefer explicit `JOIN` for readability and error prevention.
