# SQL: Triggers and Events ⚡️

Triggers and Events allow you to automate tasks within the database. Triggers respond to data changes (INSERT, UPDATE, DELETE), while Events are scheduled tasks that run at specific times.

---

## 1- Triggers 🔫
A trigger is a block of SQL code that automatically executes in response to a specific event on a particular table.

> [!NOTE]
> Use `NEW` to access the values being inserted or updated, and `OLD` to access values being updated or deleted.

```sql
-- Trigger to update invoice totals after a payment is inserted
DELIMITER $$

DROP TRIGGER IF EXISTS sql_invoicing.payments_after_insert;

CREATE TRIGGER sql_invoicing.payments_after_insert
	AFTER INSERT ON sql_invoicing.payments
	FOR EACH ROW
BEGIN
	UPDATE sql_invoicing.invoices
	SET payment_total = payment_total + NEW.amount
	WHERE invoice_id = NEW.invoice_id;
END $$

DELIMITER ;

-- Test the trigger
INSERT INTO sql_invoicing.payments
VALUES (DEFAULT, 5, 3, '2019-01-01', 10, 1);

-- Trigger to update invoice totals after a payment is deleted
DELIMITER $$

DROP TRIGGER IF EXISTS sql_invoicing.payments_after_delete;

CREATE TRIGGER sql_invoicing.payments_after_delete
    AFTER DELETE ON sql_invoicing.payments
    FOR EACH ROW
BEGIN
    UPDATE sql_invoicing.invoices
    SET payment_total = payment_total - OLD.amount
    WHERE invoice_id = OLD.invoice_id;
END $$

DELIMITER ;

-- Test the delete trigger
DELETE FROM sql_invoicing.payments
WHERE client_id = 5 AND invoice_id = 3;
```

---

## 2- Viewing Triggers 👁️
You can inspect the triggers defined in your database using the `SHOW TRIGGERS` command.

```sql
-- View all triggers in a database
SHOW TRIGGERS FROM sql_invoicing;

-- Filter triggers by a pattern
SHOW TRIGGERS FROM sql_invoicing LIKE 'payments%';
```

---

## 3- Dropping Triggers 🗑️
Triggers can be removed when they are no longer needed.

> [!TIP]
> Always use `IF EXISTS` to prevent errors if the trigger has already been deleted.

```sql
DROP TRIGGER IF EXISTS sql_invoicing.payments_after_insert;
```

---

## 4- Using Triggers for Auditing 📋
Triggers are excellent for maintaining an audit trail (a log of all changes made to a table).

```sql
-- Create an audit table
CREATE TABLE sql_invoicing.payments_audit
(
    client_id    INT NOT NULL,
    date         DATE NOT NULL,
    amount       DECIMAL(9, 2) NOT NULL,
    action_type  VARCHAR(50) NOT NULL,
    action_date  DATETIME NOT NULL
);

-- Trigger to log insertions into the audit table
DELIMITER $$

DROP TRIGGER IF EXISTS sql_invoicing.payments_after_insert;

CREATE TRIGGER sql_invoicing.payments_after_insert
AFTER INSERT ON sql_invoicing.payments
FOR EACH ROW
BEGIN
    UPDATE sql_invoicing.invoices
    SET payment_total = payment_total + NEW.amount
    WHERE invoice_id = NEW.invoice_id;

    INSERT INTO sql_invoicing.payments_audit
    VALUES (NEW.client_id, NEW.date, NEW.amount, 'Insert', NOW());
END $$

DELIMITER ;

-- Trigger to log deletions into the audit table
DELIMITER $$

DROP TRIGGER IF EXISTS sql_invoicing.payments_after_delete;

CREATE TRIGGER sql_invoicing.payments_after_delete
AFTER DELETE ON sql_invoicing.payments
FOR EACH ROW
BEGIN
    UPDATE sql_invoicing.invoices
    SET payment_total = payment_total - OLD.amount
    WHERE invoice_id = OLD.invoice_id;

    INSERT INTO sql_invoicing.payments_audit
    VALUES (OLD.client_id, OLD.date, OLD.amount, 'Delete', NOW());
END $$

DELIMITER ;
```

---

## 5- Events ⏰
An event is a task (or block of SQL code) that runs according to a schedule.

> [!IMPORTANT]
> Ensure the `event_scheduler` is turned on in your MySQL configuration for events to execute.

```sql
-- Check if the event scheduler is enabled
SHOW VARIABLES LIKE 'event%';

-- Enable the event scheduler globally
SET GLOBAL event_scheduler = ON;

-- Create an event to delete stale audit records annually
DELIMITER $$

CREATE EVENT sql_invoicing.yearly_delete_stale_audit_rows
ON SCHEDULE
    EVERY 1 YEAR STARTS '2019-01-01' ENDS '2029-01-01'
DO BEGIN
    DELETE FROM sql_invoicing.payments_audit
    WHERE action_date < NOW() - INTERVAL 1 YEAR;
END $$

DELIMITER ;
```

---

## 6- Viewing, Dropping and Altering Events 🛠️
Manage your scheduled events using these administrative commands.

```sql
-- List all events
SHOW EVENTS FROM sql_invoicing;

-- List events matching a pattern
SHOW EVENTS FROM sql_invoicing LIKE 'yearly%';

-- Modify an existing event
DELIMITER $$

ALTER EVENT sql_invoicing.yearly_delete_stale_audit_rows
ON SCHEDULE
    EVERY 1 YEAR STARTS '2019-01-01' ENDS '2029-01-01'
DO BEGIN
    DELETE FROM sql_invoicing.payments_audit
    WHERE action_date < NOW() - INTERVAL 1 YEAR;
END $$

DELIMITER ;

-- Disable or Enable an event without deleting it
ALTER EVENT sql_invoicing.yearly_delete_stale_audit_rows DISABLE;
ALTER EVENT sql_invoicing.yearly_delete_stale_audit_rows ENABLE;
```
