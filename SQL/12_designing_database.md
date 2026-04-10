Designing Databases

Data Modelling

Analyze and understand the business requirements
Build a Conceptual Model
Identify the entities and its relationship with each other
Build a Logical Model
Data structures to store data
Build a Physical Model
Implementation of a logical model for a specific database management system.
Exact datatype support by chosen DBMS
Nullable values or not
Views, stored procedures, triggers etc.

Conceptual Models

Represents the entities and their relationships.

Entity Relationship (ER)
Unified Modeling Language (UML)

Data modelling is an iterative process. You can’t come up with a perfect design in your first attempt. So, you need to constantly go back and forth between requirements and your models and keep refining them.

Use draw.io to create ER or UML diagrams

online course platform[gdrive link][draw.io]

![Rebase List](files/Screenshot%20from%202026-03-27%2007-18-43.png)

Logical Models

Refine the online course platform to come up with a data structure for storing data.

Relationships
One-to-one
One-to-many
Many-to-many
Other relationships are variations of this.

![Rebase List](files/Screenshot%20from%202026-03-27%2007-17-54.png)

Physical Models

Implementation of a logical model for a specific database technology.

Convention of Mosh:
Use singular names for Logical entities
Use plural names for Physical entities

![Rebase List](files/Screenshot%20from%202026-03-27%2008-37-19.png)

Foreign Key Constraints

On Update or Delete
RESTRICT
CADCADE
SET NULL
NO ACTION

NO ACTION is same as RESTRICT

Normalization

In almost 99% of the applications, we use the first three out of seven
1 NF
2 NF
3 NF

1NF - FIrst Normal Form

Each cell should have a single value and we cannot have repeated columns.

Create a new table called tags to solve the tags in courses to comply with 1NF

![Rebase List](files/Screenshot%20from%202026-03-27%2009-17-37.png)

Link Tables

![Rebase List](files/Screenshot%20from%202026-03-27%2009-35-15.png)

2NF - Second Normal Form

Every table should describe one entity, and every column in that table should describe that entity.

![Rebase List](files/Screenshot%20from%202026-03-27%2009-45-02.png)

3NF - Third Normal Form

A column in a table should not be derived from other columns.

Balance should not be divided from invoice_total and payment_total. Because it needs to be updated every time the other columns change.

![Rebase List](files/Screenshot%20from%202026-03-27%2009-48-15.png)

Full name should not be derived from first_name and last_name.

![Rebase List](files/Screenshot%20from%202026-03-27%2009-48-33.png)

My Pragmatic Advice

When working on real projects with other people. Just focus on removing redundancy, not applying normalization rules.

Don’t Model the Universe

![Rebase List](files/Screenshot%20from%202026-03-27%2009-59-42.png)

Solve today’s problems, not future problems that may never happen.

Build a model for your problem domain, not the real world.

![Rebase List](files/Screenshot%20from%202026-03-27%2010-01-47.png)

Simplicity is the Ultimate Sophistication

![Rebase List](files/Screenshot%20from%202026-03-27%2010-07-17.png)

Forward Engineering a Model

Create a database and its tables from the model we have been working on mysql workbench.
Mysql Workbench > Database > Forward Engineer

Synchronize a Model with a Database

Make changes in the model and sync it with the database.

Reverse Engineering a Database

Create a model from the database.
