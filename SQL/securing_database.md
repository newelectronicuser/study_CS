# SQL: Securing Databases 🔐

Database security involves managing user accounts and controlling their access to sensitive data. Following the **Principle of Least Privilege** ensures that users only have the minimum permissions necessary to perform their jobs.

---

## 1- Introduction 🏁
MySQL security is built around the concept of "Users" and "Privileges". A user is identified not just by their username, but also by the **host** (location) from which they are connecting.

---

## 2- Creating a User 👤
When creating a user, you specify where they are allowed to connect from using the `'user'@'host'` pattern.

```sql
-- Specific IP address
CREATE USER john@127.0.0.1;

-- Local machine only
CREATE USER john@'localhost';

-- Specific subdomain using wildcards (%)
CREATE USER john@'%.codewithmosh.com';

-- Wildcard host (Anywhere)
CREATE USER john; -- Equivalent to john@'%'

-- Creating with a password
CREATE USER john IDENTIFIED BY 'MyPassword!';
```

> [!TIP]
> Use `localhost` for application servers running on the same machine as the database to prevent external connection attempts for those accounts.

---

## 3- Viewing Users 👁️
User information is stored in the internal `mysql` database.

```sql
-- List all users and their host permissions
SELECT * FROM mysql.user;
```

---

## 4- Dropping Users 🗑️
Remove user accounts and all their associated privileges.

```sql
-- Example: Creating and then deleting a user
CREATE USER bob@codewithmosh.com IDENTIFIED BY '1234';

DROP USER bob@codewithmosh.com;
```

---

## 5- Changing Passwords 🔑
Passwords can be updated for specific users or for the session you are currently logged into.

```sql
-- Change password for a specific user
SET PASSWORD FOR john = '1234';

-- Change password for the currently logged-in user
SET PASSWORD = '1234';
```

---

## 6- Granting Privileges 🏗️
Privileges can be granted at multiple levels: **Global** (system-wide), **Database**, or **Table**.

```sql
-- Pattern for specific permissions (Recommended for Apps)
CREATE USER moon_app@'localhost' IDENTIFIED BY '1234';

GRANT SELECT, INSERT, UPDATE, DELETE, EXECUTE
ON sql_store.* -- All tables within 'sql_store'
TO moon_app@'localhost';

-- Pattern for Admin access (Global)
GRANT ALL
ON *.* -- Every table in every database
TO john;
```

> [!IMPORTANT]
> Always prefer granting specific permissions on specific databases (`db.*`) rather than using administrative access (`*.*`) for web or desktop applications.

---

## 7- Viewing Privileges 📜
Check exactly what rights a specific user has been granted.

```sql
-- Check privileges for a specific user
SHOW GRANTS FOR moon_app@'localhost';

-- Check privileges for a global user
SHOW GRANTS FOR john;

-- Check privileges for the current session
SHOW GRANTS;
```

---

## 8- Revoking Privileges 🛑
If a user no longer needs certain permissions, they can be revoked using a syntax similar to the `GRANT` command.

```sql
-- Remove specific permissions
REVOKE UPDATE, DELETE
ON sql_store.*
FROM moon_app@'localhost';

-- Remove all permissions (but keep the user account)
REVOKE ALL PRIVILEGES, GRANT OPTION 
FROM john;
```
