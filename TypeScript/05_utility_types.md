# TypeScript Utility Types

TypeScript provides several utility types to facilitate common type transformations. These are available globally.

## 1. Property Modifiers
- **`Partial<T>`**: Makes all properties in `T` optional.
- **`Required<T>`**: Makes all properties in `T` required.
- **`Readonly<T>`**: Makes all properties in `T` readonly (cannot be reassigned).

## 2. Selection / Exclusion
- **`Pick<T, K>`**: Constructs a type by picking a set of properties `K` from `T`.
- **`Omit<T, K>`**: Constructs a type by picking all properties from `T` and then removing `K`.
- **`Record<K, T>`**: Constructs an object type whose property keys are `K` and whose property values are `T`.
    - Example: `Record<string, number>` is equivalent to `{ [key: string]: number }`.

## 3. Union Manipulation
- **`Exclude<T, U>`**: Excludes from `T` those types that are assignable to `U`.
- **`Extract<T, U>`**: Extracts from `T` those types that are assignable to `U`.
- **`NonNullable<T>`**: Excludes `null` and `undefined` from `T`.

## 4. Function Types
- **`ReturnType<T>`**: Obtains the return type of a function type.
- **`Parameters<T>`**: Obtains the parameters of a function type as a tuple.

## 5. Other Tools
- **`Awaited<T>`**: Unwraps a Promise type (Recursive).
- **`InstanceType<T>`**: Obtains the instance type of a constructor function type.

## Practical Examples
```typescript
interface Todo {
  title: string;
  description: string;
}

// Partial: Update only some fields
function updateTodo(id: string, fieldsToUpdate: Partial<Todo>) { ... }

// Pick: Preview only selection
type TodoPreview = Pick<Todo, "title">;

// Omit: Remove internal ID
interface TodoWithId extends Todo { id: string; }
type TodoWithoutId = Omit<TodoWithId, "id">;
```

> [!IMPORTANT]
> **Pick vs Omit**: 
> - Use `Pick` when you want a small subset of a large interface (it's "whitelisting").
> - Use `Omit` when you want almost everything except a few fields (it's "blacklisting").
