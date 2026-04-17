# Interfaces vs. Type Aliases

In TypeScript, both **Interfaces** and **Type Aliases** can be used to describe the shape of an object or a function signature, but they have key differences.

## 1. Syntax Comparison
### Interface
```typescript
interface User {
  name: string;
  age: number;
}
```

### Type Alias
```typescript
type User = {
  name: string;
  age: number;
};
```

## 2. Key Differences

| Feature | Interface | Type Alias |
| :--- | :--- | :--- |
| **Expansion** | Can be augmented (Declaration Merging). | Cannot be changed once defined. |
| **Flexibility** | Limited to objects/classes. | Can represent primitives, unions, tuples. |
| **Inheritance** | Uses `extends`. | Uses Intersections (`&`). |
| **Implementation** | Class can `implements` interface. | Class can `implements` type (if it's an object). |

## 3. Declaration Merging
Interfaces can be defined multiple times, and TypeScript will merge them into a single definition. Type aliases cannot.
```typescript
interface Window {
  title: string;
}
interface Window {
  ts: TypeScript;
}
// Merged Result: Window { title: string; ts: TypeScript; }
```

## 4. Union and Intersection
Type aliases are more capable when it comes to complex type compositions.
```typescript
type ID = string | number; // Union (Not possible with interface)
type Pet = Dog & Cat;      // Intersection
```

## 5. When to use Which?
- **Use `interface`** for:
    - Public API definitions (so others can merge).
    - Describing object shapes/contracts for classes.
- **Use `type`** for:
    - Unions or Tuples.
    - Complex type logic (conditional, mapped types).
    - Aliasing primitive types.

> [!TIP]
> The TypeScript team recommends using `interface` by default for performance (they are cached better by the compiler) and for extensibility via declaration merging. Use `type` when you need features unique to it.
