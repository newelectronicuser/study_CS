# Spring Data JPA

Spring Data JPA aims to significantly reduce the amount of boilerplate code required to implement data access layers.

## 1. Core Concepts
- **JPA (Java Persistence API)**: A specification for ORM (Object-Relational Mapping).
- **Hibernate**: The most popular implementation of the JPA specification.
- **Spring Data JPA**: Adds a layer on top of JPA to provide repository support.

## 2. Repositories
You only need to define an interface that extends `JpaRepository<Entity, IdType>`. Spring will automatically provide the implementation during runtime.
```java
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByLastName(String lastName); // Derived queries
    
    @Query("SELECT u FROM User u WHERE u.email = :email") // JPQL query
    User findByEmailCustom(@Param("email") String email);
}
```

## 3. Transaction Management
Using `@Transactional` allows you to define the boundaries of database transactions.
- **Propagation**: Defines how a transaction relates to existing ones (e.g., `REQUIRED`, `REQUIRES_NEW`).
- **Isolation**: Defines the data visibility level between transactions (e.g., `READ_COMMITTED`, `SERIALIZABLE`).
- **Rollback**: By default, Spring only rolls back for **RuntimeExceptions**. Use `rollbackFor = Exception.class` to include checked exceptions.

## 4. Entity Lifecycle States
1.  **Transient**: Object is created but not associated with a session.
2.  **Persistent**: Object is associated with a session and a database record.
3.  **Detached**: Object was persistent but the session is closed.
4.  **Removed**: Object is scheduled for deletion.

## 5. Pagination and Sorting
Instead of returning a `List`, you can return a `Page<T>` or `Slice<T>`.
```java
Page<User> findByAge(int age, Pageable pageable);
```

## 6. Hibernate "Dirty Checking"
Hibernate keeps track of all persistent entities in its session. If you change a property on a persistent entity, Hibernate will automatically detect the change and update the database when the transaction commits—no need to call `repository.save()`.

> [!WARNING]
> **N+1 Problem**: 
> Occurs when you fetch a list of entities and then access a lazy-loaded relationship for each one. 
> **Solution**: Use `FETCH JOIN` in your JPQL or use Entity Graphs to fetch the relationship in a single query.
