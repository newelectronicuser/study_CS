# SQL: Essential MySQL Functions 🛠️

A comprehensive guide to the most important built-in functions in MySQL for data manipulation, formatting, and logical operations.

---

## Table of Contents

1. [Numeric Functions](#1--numeric-functions-input_numbers)
2. [String Functions](#2--string-functions-abc)
3. [Date Functions in MySQL](#3--date-functions-in-mysql-calendar)
4. [Formatting Dates and Times](#4--formatting-dates-and-times-clock3)
5. [Calculating Dates and Times](#5--calculating-dates-and-times-heavy_plus_sign)
6. [The IFNULL and COALESCE Functions](#6--the-ifnull-and-coalesce-functions-question)
7. [The IF Function](#7--the-if-function-decision_認)
8. [The CASE Operator](#8--the-case-operator-alt)

---

## 1- Numeric Functions 🔢

Used for mathematical calculations and rounding.

```sql
-- Rounding to specified decimals
SELECT ROUND(5.73451, 3);    -- Output: 5.735

-- Removing decimals without rounding
SELECT TRUNCATE(5.73451, 3); -- Output: 5.734

-- Smallest integer >= x
SELECT CEILING(5.1);        -- Output: 6

-- Largest integer <= x
SELECT FLOOR(5.9);          -- Output: 5

-- Absolute value
SELECT ABS(-5.2);           -- Output: 5.2

-- Random floating-point (0 to 1)
SELECT RAND();
```

---

## 2- String Functions 🔤

Essential for cleaning and transforming text data.

```sql
-- Length of string
SELECT LENGTH('SKY');               -- Output: 3

-- Changing Case
SELECT UPPER('sky');                -- Output: SKY
SELECT LOWER('SKY');                -- Output: sky

-- Trimming Whitespace
SELECT LTRIM('  sky');              -- Left trim
SELECT RTRIM('sky  ');              -- Right trim
SELECT TRIM('  sky  ');             -- Both sides

-- Extraction
SELECT LEFT('Kindergarten', 4);     -- Output: Kind
SELECT RIGHT('Kindergarten', 4);    -- Output: rten
SELECT SUBSTRING('Kindergarten', 3, 5); -- Output: nderg (Starts at 3, length 5)
SELECT SUBSTRING('Kindergarten', 3);    -- Output: ndergarten (Starts at 3 to end)

-- Finding & Replacing
SELECT LOCATE('N', 'Kindergarten'); -- Output: 3 (1-indexed)
SELECT REPLACE('Kindergarten', 'garten', 'garden');

-- Concatenation
SELECT CONCAT('First', ' ', 'Last');
```

> [!TIP]
> `SUBSTRING` in MySQL is **1-indexed**. In most programming languages, it's 0-indexed.

---

## 3- Date Functions in MySQL 📅

Managing and extracting parts of temporal data.

```sql
-- Current date and time
SELECT NOW(), CURDATE(), CURTIME();

-- Extracting Parts
SELECT YEAR(NOW()), MONTH(NOW()), DAY(NOW());
SELECT HOUR(NOW()), MINUTE(NOW()), SECOND(NOW());

-- Name-based extraction
SELECT DAYNAME(NOW()), MONTHNAME(NOW());

-- Using EXTRACT for better readability
SELECT EXTRACT(YEAR FROM NOW());
```

---

## 4- Formatting Dates and Times 🕰️

Customizing how dates appear in reports.

```sql
-- Date Formatting
-- %M: Month Name, %d: Day, %Y: Year (4 digits), %y: Year (2 digits)
SELECT DATE_FORMAT(NOW(), '%M %d %Y');

-- Time Formatting
-- %H: Hour (24h), %h: Hour (12h), %i: Minutes, %p: AM/PM
SELECT TIME_FORMAT(NOW(), '%h:%i %p');
```

---

## 5- Calculating Dates and Times ➕

Performing arithmetic on dates.

```sql
-- Adding/Subtracting Intervals
SELECT DATE_ADD(NOW(), INTERVAL 1 DAY);   -- Tomorrow
SELECT DATE_ADD(NOW(), INTERVAL -1 YEAR);-- Last Year

-- Difference between two dates (returns days)
SELECT DATEDIFF('2024-01-01', '2023-01-01');

-- Time Conversion
SELECT TIME_TO_SEC('09:00') - TIME_TO_SEC('08:30');
```

---

## 6- The IFNULL and COALESCE Functions ❓

Handling missing values (NULL).

```sql
-- IFNULL: Provide a fallback if a value is NULL
SELECT
    order_id,
    IFNULL(shipper_id, 'Not assigned') AS shipper
FROM orders;

-- COALESCE: Returns the first non-NULL value in a list
SELECT
    order_id,
    COALESCE(shipper_id, comments, 'Not assigned') AS fallback_check
FROM orders;
```

---

## 7- The IF Function 🛤️

Simple conditional logic in SQL.

```sql
-- Syntax: IF(condition, value_if_true, value_if_false)
SELECT
    order_id,
    IF(EXTRACT(YEAR FROM order_date) = YEAR(NOW()),
       'Active',
       'Archived') AS status
FROM orders;
```

---

## 8- The CASE Operator 🔀

Complex conditional logic (Like Switch/If-Else).

```sql
SELECT
    customer_id,
    points,
    CASE
        WHEN points > 3000 THEN 'Gold'
        WHEN points >= 2000 THEN 'Silver'
        ELSE 'Bronze'
    END AS membership_tier
FROM customers
ORDER BY points DESC;
```

> [!IMPORTANT]
> Use `CASE` when you have more than two branches of logic. For simple binary choices, `IF()` is more concise.

