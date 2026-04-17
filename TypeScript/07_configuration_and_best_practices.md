# Configuration and Best Practices

Setting up TypeScript correctly is just as important as writing the code itself.

## 1. Essential `tsconfig.json` Options
The `tsconfig.json` file is where you configure how the TypeScript compiler behaves.

- **`strict: true`**: The most important setting. It enables a suite of type-checking behaviors (like `noImplicitAny`, `strictNullChecks`, etc.) that make your code much safer.
- **`target`**: The version of JavaScript you want the compiler to output (e.g., `ESNext`, `ES2022`).
- **`module`**: The module system to use (`CommonJS`, `ESNext`, `NodeNext`).
- **`lib`**: Specifies the library files to be included in the compilation (e.g., `["DOM", "ESNext"]`).
- **`paths`**: Allows you to define alias paths (e.g., `@/components/*` -> `src/components/*`).

## 2. Best Practices
### Avoid `any`
Using `any` stops TypeScript from performing its job. If you truly don't know the type, use `unknown`.

### Use `strictNullChecks`
This ensures that variables cannot be `null` or `undefined` unless explicitly stated.
```typescript
let name: string = null; // Error with strictNullChecks: true
let name: string | null = null; // OK
```

### Preference for Interfaces
As mentioned in the "Interfaces vs Types" section, use interfaces for object shapes unless you need the specific features of a type alias.

### Use `as const` for Readonly Literals
`as const` informs TypeScript that a specific object or array is a permanent literal and should not be widened.
```typescript
const colors = ["red", "green", "blue"] as const;
// colors is now readonly ["red", "green", "blue"]
```

### Avoid Non-Null Assertions (`!`)
The `!` operator tells TypeScript that a value is definitely not null. This can lead to runtime errors if you're wrong. Use type guards or optional chaining (`?.`) instead.

### Exhaustive Check in Unions
When working with discriminated unions, ensure your switch statements handle all cases.
```typescript
function assertNever(x: never): never {
  throw new Error("Unexpected object: " + x);
}

function handle(shape: Shape) {
  switch (shape.kind) {
    case "square": ...
    case "circle": ...
    default: return assertNever(shape); // Compilation error if a case is missing
  }
}
```

> [!WARNING]
> Don't over-engineer your types. TypeScript is a tool to help you find bugs, not a puzzle to be solved. If a type becomes too complex to read, consider simplifying or adding comments.
