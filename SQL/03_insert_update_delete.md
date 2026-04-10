# SQL: Summarizing Data (Insert, Update, Delete) 🛠️

## 1- Column Attributes 📊

Before performing DML (Data Manipulation Language) operations, it's essential to understand column constraints:

- **PK (Primary Key)**: Uniquely identifies each record.
- **NN (Not Null)**: The column cannot contain a `NULL` value.
- **AI (Auto Increment)**: The database automatically generates a unique number for new rows.
- **Default**: The value assigned to a column if no value is explicitly provided.

---

## 2- Inserting a Row 📥

There are two ways to insert data into a table.

```sql
-- Pattern 1: Supplying all values (Position-based)
-- Uses DEFAULT for AI columns and NULL for optional ones.
INSERT INTO customers
VALUES (DEFAULT, 'John', 'Smith', '1990-01-01', NULL, 'address', 'city', 'CA', DEFAULT);

-- Pattern 2: Specifying column names (RECOMMENDED)
-- Allows skipping AI columns or columns with default values.
INSERT INTO customers (first_name, last_name, birth_date, address, city, state)
VALUES ('John', 'Smith', '1990-01-01', 'address', 'city', 'CA');
```

---

## 3- Inserting Multiple Rows 📚

You can insert several records in a single query by separating value sets with commas.

```sql
INSERT INTO shippers (name)
VALUES ('Shipper1'), ('Shipper2'), ('Shipper3');

INSERT INTO products (name, quantity_in_stock, unit_price)
VALUES
    ('iPhone 16', 25, 29.36),
    ('Galaxy S24', 30, 25.68),
    ('Oneplus Nord', 12, 25.31);
```

---

## 4- Inserting Hierarchical Rows 🏗️

Used when you need to insert a parent record (Order) and linked child records (Order Items).

```sql
-- Step 1: Insert the Parent row
INSERT INTO orders (customer_id, order_date, status)
VALUES (1, '2019-01-02', 1);

-- Step 2: Use LAST_INSERT_ID() to link children
-- This function returns the ID of the last row inserted in the CURRENT session.
INSERT INTO order_items
VALUES
    (LAST_INSERT_ID(), 1, 1, 2.95),
    (LAST_INSERT_ID(), 2, 1, 3.95);
```

---

## 5- Creating a Copy of a Table 📂

You can clone a table's data or populate an existing table from a query result.

```sql
-- Clone structure and data (NOTE: Loses PK/AI attributes)
CREATE TABLE orders_archived AS
SELECT * FROM orders;

-- Append data to an existing table
INSERT INTO orders_archived
SELECT * FROM orders o
WHERE o.order_date < '2019-01-01';
```

> [!WARNING]
> `CREATE TABLE AS` does **not** copy column attributes like Primary Keys or Auto-increment. You may need to add them manually afterward.

---

## 6- Updating a Single Row ✏️

Use the `UPDATE` statement to modify existing records.

```sql
UPDATE sql_invoicing.invoices
SET payment_total = 10,
    payment_date = '2019-03-01'
WHERE invoice_id = 1;

-- Resetting to NULL or DEFAULT
UPDATE sql_invoicing.invoices
SET payment_total = DEFAULT,
    payment_date = NULL
WHERE invoice_id = 1;
```

---

## 7- Updating Multiple Rows 💹

You can update multiple rows by using broader filters in the `WHERE` clause.

```sql
-- Update all invoices for a specific client
UPDATE sql_invoicing.invoices
SET payment_total = invoice_total * 0.7,
    payment_date = due_date
WHERE client_id IN (3, 4);

-- Incrementing values
UPDATE customers
SET points = points + 50
WHERE birth_date < '1990-01-01';
```

---

## 8- Using Subqueries in Updates 🔄

Subqueries allow you to update rows based on criteria from other tables.

```sql
-- Update invoices for a client identified by NAME
UPDATE sql_invoicing.invoices
SET payment_total = invoice_total * 0.5,
    payment_date = due_date
WHERE client_id = (
    SELECT client_id
    FROM sql_invoicing.clients
    WHERE name = 'Myworks'
);

-- Advanced: Update orders based on customer points
UPDATE orders
SET comments = 'Gold'
WHERE customer_id IN (
    SELECT c.customer_id
    FROM customers c
    WHERE c.points > 3000
);
```

---

## 9- Deleting Rows 🗑️

Remove unwanted records.

```sql
DELETE FROM invoices
WHERE invoice_id = 1;

-- To delete all rows (Dangerous!)
-- DELETE FROM invoices;
```

> [!TIP]
> Use `TRUNCATE TABLE name` to clear an entire table more efficiently than `DELETE`.

---

## 10- Restoring the Databases 🛠️

Always keep a backup script to reset your database after performing destructive DML operations.

- **Workflow**: Run the original creation script (`create-db.sql`) to wipe your changes and return to a clean state.

---

## Summary

- **Insert**: Use `LAST_INSERT_ID()` for relations.
- **Update**: Use subqueries for dynamic logic.
- **Delete**: Always double-check your `WHERE` condition!
