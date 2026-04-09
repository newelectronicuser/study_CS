# SQL: Stored Procedures & Functions 📦

Stored procedures and functions are sets of SQL statements that are saved and can be reused. They provide better performance, security, and abstraction.

---

## Table of Contents
1. [What are Stored Procedures](#1--what-are-stored-procedures-📦)
2. [Creating a Stored Procedure](#2--creating-a-stored-procedure-🏗️)
3. [Creating Procedures Using MySQLWorkbench](#3--creating-procedures-using-mysqlworkbench-🖱️)
4. [Dropping Stored Procedures](#4--dropping-stored-procedures-🗑️)
5. [Parameters](#5--parameters-📥)
6. [Parameters with Default Value](#6--parameters-with-default-value-⚙️)
7. [Parameter Validation](#7--parameter-validation-🛡️)
8. [Output Parameters](#8--output-parameters-📤)
9. [Variables](#9--variables-🔢)
10. [Functions](#10--functions-⚡)
11. [Other Conventions](#11--other-conventions-📝)

---

## 1- What are Stored Procedures 📦
A **Stored Procedure** is a collection of pre-compiled SQL statements stored in the database.
- **Performance**: Pre-compiled code runs faster.
- **Security**: Can restrict direct table access and only allow procedure calls.
- **Maintenance**: Encapsulates business logic in one place.

---

## 2- Creating a Stored Procedure 🏗️
Use the `CREATE PROCEDURE` statement. It's common practice to use `DELIMITER` to define when the procedure block ends.

```sql
-- Simple procedure to get all clients
DELIMITER $$

CREATE PROCEDURE get_clients()
BEGIN
    SELECT * FROM clients;
END $$

DELIMITER ;

-- Calling the procedure
CALL get_clients();
```

---

## 3- Creating Procedures Using MySQLWorkbench 🖱️
MySQL Workbench provides a user-friendly interface to manage procedures:
1.  Navigate to the **Schemas** tab.
2.  Right-click on **Stored Procedures** and select **Create Stored Procedure**.
3.  The tool automatically manages the `DELIMITER` and boilerplate code for you.
4.  Apply the changes to save it to the database.

---

## 4- Dropping Stored Procedures 🗑️
Always use `IF EXISTS` to prevent errors when dropping a procedure that doesn't exist.

```sql
-- Basic Drop
DROP PROCEDURE get_clients;

-- Recommended: Safe Drop
DROP PROCEDURE IF EXISTS get_clients;
```

---

## 5- Parameters 📥
Parameters allow you to pass data into a procedure.

```sql
DROP PROCEDURE IF EXISTS get_clients_by_state;

DELIMITER $$
CREATE PROCEDURE get_clients_by_state
(
    state CHAR(2) -- Example: 'CA' or 'NY'
)
BEGIN
    SELECT * FROM clients c
    WHERE c.state = state;
END $$
DELIMITER ;

CALL get_clients_by_state('NY');
```

---

## 6- Parameters with Default Value ⚙️
You can use logical operators (`IF`, `IFNULL`) to handle cases where a parameter might be `NULL`.

```sql
DELIMITER $$
CREATE PROCEDURE get_clients_by_state
(
    state CHAR(2)
)
BEGIN
    -- Handling NULL with IFNULL (Most concise)
    SELECT * FROM clients c
    WHERE c.state = IFNULL(state, c.state);
END $$
DELIMITER ;

-- Calling with NULL returns all clients
CALL get_clients_by_state(NULL);
```

---

## 7- Parameter Validation 🛡️
Use `SIGNAL SQLSTATE` to throw errors when input data is invalid.

```sql
DELIMITER $$
CREATE PROCEDURE make_payment(
    invoice_id INT,
    payment_amount DECIMAL(9,2),
    payment_date DATE
)
BEGIN
    -- Basic Validation
    IF payment_amount <= 0 THEN
        SIGNAL SQLSTATE '22003' 
        SET MESSAGE_TEXT = 'Invalid payment amount';
    END IF;

    UPDATE invoices i
    SET i.payment_total = payment_amount,
        i.payment_date = payment_date
    WHERE i.invoice_id = invoice_id;
END $$
DELIMITER ;
```

---

## 8- Output Parameters 📤
Use the `OUT` keyword to return data back to the caller.

```sql
DELIMITER $$
CREATE PROCEDURE get_unpaid_invoices_for_client(
    client_id INT,
    OUT invoices_count INT,
    OUT invoices_total DECIMAL(9,2)
)
BEGIN
    SELECT COUNT(*), SUM(i.invoice_total)
    INTO invoices_count, invoices_total
    FROM invoices i 
    WHERE i.client_id = client_id AND i.payment_total = 0;
END $$
DELIMITER ;

-- Using output parameters
SET @count = 0;
SET @total = 0;
CALL get_unpaid_invoices_for_client(3, @count, @total);
SELECT @count, @total;
```

---

## 9- Variables 🔢
- **User Variables**: Prefix with `@` (last for the entire session).
- **Local Variables**: Defined inside procedures using `DECLARE` (last only during procedure execution).

```sql
DELIMITER $$
CREATE PROCEDURE get_risk_factor()
BEGIN
    -- Declare local variables first
    DECLARE risk_factor DECIMAL(9, 2) DEFAULT 0;
    DECLARE invoices_total DECIMAL(9, 2);
    DECLARE invoices_count INT;
    
    SELECT COUNT(*), SUM(invoice_total)
    INTO invoices_count, invoices_total
    FROM invoices;
    
    SET risk_factor = invoices_total / invoices_count * 5;
    
    SELECT risk_factor;
END $$
DELIMITER ;
```

---

## 10- Functions ⚡
Unlike procedures, functions **must** return a single value and can be used directly in SQL statements.

```sql
CREATE FUNCTION get_risk_factor_for_client(client_id INT)
RETURNS INT
-- Attributes: READS SQL DATA, DETERMINISTIC, or MODIFIES SQL DATA
READS SQL DATA
BEGIN
    DECLARE risk_factor DECIMAL(9, 2) DEFAULT 0;
    DECLARE invoices_total DECIMAL(9, 2);
    DECLARE invoices_count INT;

    SELECT COUNT(*), SUM(invoice_total)
    INTO invoices_count, invoices_total
    FROM invoices i
    WHERE i.client_id = client_id;

    SET risk_factor = invoices_total / invoices_count * 5;
    RETURN risk_factor;
END

-- Using the function in a SELECT statement
SELECT name, get_risk_factor_for_client(client_id) AS risk
FROM clients;
```

---

## 11- Other Conventions 📝
- **Naming**: Use camelCase or snake_case consistently.
- **Prefixing**: Some developers prefix procedures with `sp_` and functions with `fn_`.
- **Indentation**: Keep logic blocks clear to help with debugging complex `IF` statements.

---
*Interview Favorite: "When would you use a Function instead of a Stored Procedure?" (Use Functions for calculations and Procedures for complex logic/batch operations).*
