# SQL: Data Types 🧩

Choosing the correct data type is critical for database performance, storage efficiency, and data integrity. This guide covers all primary MySQL data types.

---

## 1- Introduction 🏁

MySQL supports several categories of data types:

- **String Types**: For storing text and character data.
- **Numeric Types**: For whole numbers and decimals.
- **Date and Time Types**: For tracking temporal data.
- **Blob Types**: For binary data (images, videos).
- **Spatial Types**: For geographic/geometric data.

> [!TIP]
> **Performance Rule**: Always use the smallest data type that can reliably hold your data. Less data in memory leads to faster queries.

---

## 2- String Types 🧵

Strings can be fixed or variable in length.

| Type           | Max Length | Best Use Case                                           |
| :------------- | :--------- | :------------------------------------------------------ |
| **CHAR(x)**    | 255        | Fixed-length strings (State abbreviations: 'VA', 'CA'). |
| **VARCHAR(x)** | 65,535     | Variable-length strings (Usernames, Emails, Addresses). |
| **TINYTEXT**   | 255 bytes  | Very short strings.                                     |
| **TEXT**       | 64 KB      | Descriptions or shorter comments.                       |
| **MEDIUMTEXT** | 16 MB      | JSON objects, CSV data, short books.                    |
| **LONGTEXT**   | 4 GB       | Larger documents or logs.                               |

### Character Encoding & Storage

- **English**: 1 byte per character.
- **European/Middle-Eastern**: ~2 bytes per character.
- **Asian**: ~3 bytes per character.

---

## 3- Integer Types 🔢

Whole numbers with various storage requirements.

| Type          | Storage | Range (Signed) | Range (Unsigned) |
| :------------ | :------ | :------------- | :--------------- |
| **TINYINT**   | 1 byte  | [-128, 127]    | [0, 255]         |
| **SMALLINT**  | 2 bytes | [-32K, 32K]    | [0, 65K]         |
| **MEDIUMINT** | 3 bytes | [-8M, 8M]      | [0, 16M]         |
| **INT**       | 4 bytes | [-2B, 2B]      | [0, 4B]          |
| **BIGINT**    | 8 bytes | [-9Z, 9Z]      | [0, 18Z]         |

> [!NOTE]
> **Zerofill**: `INT(4)` with Zerofill will display `1` as `0001`. This is for display purposes only and does not change how the value is stored.

---

## 4- Fixed-point and Floating-point Types ⚖️

For numbers with decimal points.

- **DECIMAL(p, s)**:
  - **p (Precision)**: Maximum total digits (max 65).
  - **s (Scale)**: Digits after decimal point.
  - **Use Case**: Fixed-point values, typically **currency/monetary values**.
  - **Synonyms**: `DEC`, `NUMERIC`, `FIXED`.
- **FLOAT**: 4 bytes. Used for small numbers without high precision.
- **DOUBLE**: 8 bytes. Used for large numbers where precision isn't paramount.

---

## 5- Boolean Types ✅

Booleans in MySQL are actually internal aliases for numeric types.

- **BOOL / BOOLEAN**: These are synonyms for `TINYINT(1)`.
- **TRUE** = 1
- **FALSE** = 0

---

## 6- Enum and Set Types 🏷️

Used to define a list of allowed values.

- **ENUM**: Allows choosing **one** value from a list.
- **SET**: Allows choosing **multiple** values from a list.

```sql
-- Adding an ENUM column
ALTER TABLE sql_store.products
ADD COLUMN size ENUM('small', 'medium', 'large') NULL AFTER unit_price;
```

> [!WARNING]
> It is generally advised **not to use ENUMs**. Instead, use a **Lookup Table** (a separate table linked via Foreign Key) for better flexibility and easier data updates.

---

## 7- Date and Time Types 📅

Types for storing dates, times, or combinations.

| Type          | Use Case                                                                   |
| :------------ | :------------------------------------------------------------------------- |
| **DATE**      | Stores date only ('YYYY-MM-DD').                                           |
| **TIME**      | Stores time only ('HH:MM:SS').                                             |
| **DATETIME**  | 8 bytes. Wider range, goes far beyond 2038.                                |
| **TIMESTAMP** | 4 bytes. Up to the year 2038. Often used for record creation/update times. |
| **YEAR**      | Stores a 4-digit year.                                                     |

---

## 8- Blob Types 🖼️

Used to store large amounts of binary data (Binary Large Objects).

| Type           | Max Capacity |
| :------------- | :----------- |
| **TINYBLOB**   | 255 Bytes    |
| **BLOB**       | 65 KB        |
| **MEDIUMBLOB** | 16 MB        |
| **LONGBLOB**   | 4 GB         |

> [!CAUTION]
> **The Case Against DB Storage**: Storing files (images/videos) in a database can lead to:
>
> 1. Increased database size.
> 2. Slower backups.
> 3. Performance degradation.
> 4. More complex application code.
>    **Better Approach**: Store files on a file server (S3) and store only the **file path** in the DB.

---

## 9- JSON Type 🏗️

A lightweight format for storing complex, hierarchical data.

```sql
-- Updating a JSON column manually
UPDATE products
SET properties = '
{
    "dimensions" : [1, 2, 3],
    "weight" : 10,
    "manufacturer" : {"name": "sony"}
}'
WHERE product_id = 1;

-- Using JSON functions to create objects
UPDATE products
SET properties = JSON_OBJECT(
    'weight', 10,
    'dimension', JSON_ARRAY(1, 2, 3),
    'manufacturer', JSON_OBJECT('name', 'sony')
)
WHERE product_id = 1;

-- Extracting data using JSON_EXTRACT
SELECT product_id, JSON_EXTRACT(properties, '$.weight')
FROM products
WHERE product_id = 1;

-- Using the column path operator (->)
SELECT product_id, properties -> '$.weight' FROM products WHERE product_id = 1;
SELECT product_id, properties -> '$.dimensions' FROM products WHERE product_id = 1;
SELECT product_id, properties -> '$.dimensions[0]' FROM products WHERE product_id = 1;
SELECT product_id, properties -> '$.manufacturer.name' FROM products WHERE product_id = 1;

-- Using the inline path operator (->>) to remove quotes from strings
SELECT product_id, properties ->> '$.manufacturer.name'
FROM products
WHERE product_id = 1;

-- Filtering by JSON properties
SELECT product_id, properties ->> '$.manufacturer.name'
FROM products
WHERE properties ->> '$.manufacturer.name' = 'sony';

-- Modifying specific JSON fields
UPDATE products
SET properties = JSON_SET(
    properties,
    '$.weight', 20,
    '$.age', 10
)
WHERE product_id = 1;

-- Removing fields
UPDATE products
SET properties = JSON_REMOVE(
    properties,
    '$.age'
)
WHERE product_id = 1;
```
