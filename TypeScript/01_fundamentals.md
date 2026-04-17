# TypeScript Fundamentals

## 1. Basic Types
TypeScript provides several basic types to define the shape and nature of your data:
- **`string`**, **`number`**, **`boolean`**: The primitive types.
- **`any`**: Opts out of type checking. Use sparingly.
- **`unknown`**: A type-safe counterpart to `any`. Anything is assignable to `unknown`, but `unknown` isn't assignable to anything else without a type check (narrowing).
- **`never`**: Represents values that *never* occur (e.g., a function that always throws an error or has an infinite loop).
- **`void`**: Used for functions that do not return a value.
- **`Array<T>`** or **`T[]`**: Represents a list of elements of type `T`.
- **`Tuple`**: Represents an array with a fixed number of elements whose types are known but need not be the same.
    - Example: `let x: [string, number] = ["hello", 10];`
- **`enum`**: A way of giving more friendly names to sets of numeric or string values.

## 2. Type Inference vs. Type Annotations
- **Type Annotations**: Explicitly telling TypeScript what the type is.
    - `let count: number = 0;`
- **Type Inference**: TypeScript automatically determining the type based on the value.
    - `let count = 0;` // inferred as number
- **Best Practice**: Let TS infer types where possible; use annotations for function parameters, return types, and complex objects.

## 3. Object Types
Objects can be defined using explicit type annotations or interfaces.
```typescript
const user: { name: string; id: number } = {
  name: "Alice",
  id: 1,
};
```

## 4. Literal Types
You can specify the exact value a string, number, or boolean must have.
- `let direction: "left" | "right" | "up" | "down";`
- `let probability: 0 | 0.5 | 1;`

## 5. Type Assertions (Casting)
Telling TypeScript "trust me, I know what I'm doing."
- `const myCanvas = document.getElementById("main_canvas") as HTMLCanvasElement;`
- `const value = <string>someValue;` (Older syntax, avoid in JSX files).

> [!IMPORTANT]
> **Any vs Unknown**: 
> - Use `unknown` when you don't know the type of a value (e.g., from an API). It forces you to perform type checking before using it, making your code safer than using `any`.
