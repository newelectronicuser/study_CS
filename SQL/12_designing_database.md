# 📐 SQL: Designing Databases
 
Database design is the process of producing a detailed data model of a database. It involves moving from business requirements to a physical implementation through multiple layers of abstraction.
 
---
 
## 🏗️ The Data Modeling Process
 
Data modeling is an **iterative process**. You won’t get a perfect design on your first attempt; you must constantly refine your models as you understand the requirements better.
 
### 1. Conceptual Model
The highest level of abstraction. It represents entities and their relationships without worrying about technical implementation.
- **Goal**: Identify business entities and how they relate.
- **Tools**: Entity Relationship (ER) diagrams or Unified Modeling Language (UML).
- **Implementation**: Use tools like [draw.io](https://draw.io) to visualize these relationships.
 
![Conceptual Model Example](files/Screenshot%20from%202026-03-27%2007-18-43.png)
 
### 2. Logical Model
Refines the conceptual model by defining specific data structures to store the data.
- **Goal**: Define attributes (columns) and relationships (One-to-One, One-to-Many, Many-to-Many).
- **Example**: Creating the structure for an online course platform.
 
![Logical Model Example](files/Screenshot%20from%202026-03-27%2007-17-54.png)
 
### 3. Physical Model
The actual implementation of the logical model for a specific DBMS (e.g., MySQL, PostgreSQL).
- **Goal**: Define exact data types, nullability, primary keys, and foreign keys.
- **Naming Conventions**: A common convention (e.g., by Mosh) is to use **singular** names for logical entities (e.g., User) and **plural** names for physical tables (e.g., Users).
 
![Physical Model Example](files/Screenshot%20from%202026-03-27%2008-37-19.png)
 
---
 
## 🔑 Foreign Key Constraints
 
Define what happens to child records when a parent record is updated or deleted:
 
- **RESTRICT / NO ACTION**: Prevents the update/delete of the parent if child records exist.
- **CASCADE**: Automatically updates/deletes child records when the parent is modified.
- **SET NULL**: Sets the foreign key in child records to NULL when the parent is deleted.
 
---
 
## 📏 Normalization
 
Normalization is the process of organizing data to reduce redundancy and improve data integrity. While there are seven normal forms, 99% of applications only need the first three.
 
### 1NF: First Normal Form
- **Rule**: Each cell must contain a single (atomic) value.
- **Rule**: No repeating columns.
- **Solution**: Create new tables (like a `tags` table) to handle multi-valued attributes.
 
![1NF Implementation](files/Screenshot%20from%202026-03-27%2009-17-37.png)
 
### 2NF: Second Normal Form
- **Rule**: Every table should describe exactly one entity.
- **Rule**: Every column in the table must describe that specific entity.
- **Link Tables**: Use many-to-many link tables to connect entities properly.
 
![Link Tables Visual](files/Screenshot%20from%202026-03-27%2009-35-15.png)
![2NF Implementation](files/Screenshot%20from%202026-03-27%2009-45-02.png)
 
### 3NF: Third Normal Form
- **Rule**: Columns should not be derived from other columns in the same table.
- **Example**: Do not store a `balance` column if it can be calculated from `total` and `payment`.
- **Example**: Do not store `full_name` if you already have `first_name` and `last_name`.
 
![3NF Calculation Example](files/Screenshot%20from%202026-03-27%2009-48-15.png)
![3NF Name Example](files/Screenshot%20from%202026-03-27%2009-48-33.png)
 
---
 
## 💡 Pragmatic Advice
 
> [!TIP]
> **Focus on Redundancy**: When working in real-world teams, the primary goal is to **remove redundancy**. Don't get bogged down in academic definitions of normal forms; if data is repeated, it probably needs a new table.
 
### Don’t Model the Universe
- Solve **today’s problems**, not hypothetical future problems.
- Build a model for your specific **problem domain**, not a perfect simulation of the real world.
 
![Problem Domain Focus](files/Screenshot%20from%202026-03-27%2009-59-42.png)
![Efficiency over Reality](files/Screenshot%20from%202026-03-27%2010-01-47.png)
 
> [!IMPORTANT]
> **Simplicity is the Ultimate Sophistication.** A simple, working design is always superior to a complex, over-engineered one.
 
![Simplicity in Design](files/Screenshot%20from%202026-03-27%2010-07-17.png)
 
---
 
## 🛠️ Workbench Engineering Tools
 
MySQL Workbench provides powerful tools to manage the transition between models and live databases:
 
1. **Forward Engineering**: Create a physical database and its tables directly from your visual model (`Database > Forward Engineer`).
2. **Synchronize Model**: Automatically update the live database to match changes made in your model.
3. **Reverse Engineering**: Create a visual model from an existing live database.
