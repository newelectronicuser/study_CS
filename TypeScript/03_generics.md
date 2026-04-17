# Generics in TypeScript

Generics allow you to create reusable components that can work with a variety of types rather than a single one.

## 1. Generic Functions
A generic function captures the type of the argument provided and uses it for the return type or internal logic.
```typescript
function identity<T>(arg: T): T {
  return arg;
}

let output = identity<string>("myString"); // Explicit
let output2 = identity(123);              // Inferred as number
```

## 2. Generic Interfaces and Classes
You can also pass types to interfaces and classes.
```typescript
interface Box<T> {
  contents: T;
}

class GenericNumber<T> {
    zeroValue: T;
    add: (x: T, y: T) => T;
}
```

## 3. Generic Constraints
Sometimes you want to restrict the types that can be passed to a generic. Use the `extends` keyword.
```typescript
interface Lengthwise {
  length: number;
}

function loggingIdentity<T extends Lengthwise>(arg: T): T {
  console.log(arg.length); // Now safe to access .length
  return arg;
}
```

## 4. Using Type Parameters in Generic Constraints
You can declare a type parameter that is constrained by another type parameter.
```typescript
function getProperty<T, K extends keyof T>(obj: T, key: K) {
  return obj[key];
}
```

## 5. Default Generic Values
You can provide default types for generics.
```typescript
interface Container<T = string> {
  value: T;
}
```

> [!NOTE]
> Generics are vital for maintaining type safety in functional programming patterns (like higher-order functions) and when building libraries that interact with user-defined data structures.
