# Union, Intersection, and Type Guards

## 1. Union Types (`|`)
Allows a value to be one of several types.
```typescript
function printId(id: number | string) {
  console.log("Your ID is: " + id);
}
```

## 2. Intersection Types (`&`)
Combines multiple types into one. The new type has all the features of the combined types.
```typescript
interface ErrorHandling {
  success: boolean;
  error?: { message: string };
}
interface ArtworksData {
  artworks: { title: string }[];
}
type ArtworksResponse = ArtworksData & ErrorHandling;
```

## 3. Type Narrowing (Type Guards)
Narrowing is the process of moving from a less specific type to a more specific type.

### Built-in Type Guards
- **`typeof`**: Used for primitives (`string`, `number`, `boolean`, `symbol`).
- **`instanceof`**: Used for classes and constructor functions.
- **`in`**: Used to check if a property exists on an object.

```typescript
function process(input: string | number | Date) {
  if (typeof input === "string") {
    return input.toUpperCase();
  }
  if (input instanceof Date) {
    return input.getTime();
  }
}
```

## 4. User-Defined Type Guards
A function that performs a runtime check and informs the compiler via a **Type Predicate** (`arg is Type`).
```typescript
interface Bird {
  fly(): void;
  layEggs(): void;
}
interface Fish {
  swim(): void;
  layEggs(): void;
}

function isFish(pet: Fish | Bird): pet is Fish {
  return (pet as Fish).swim !== undefined;
}

// Usage
if (isFish(pet)) {
  pet.swim();
} else {
  pet.fly();
}
```

## 5. Discriminated Unions
A common pattern in functional programming where types have a common property with literal types (the "discriminant").
```typescript
interface Circle {
  kind: "circle";
  radius: number;
}
interface Square {
  kind: "square";
  sideLength: number;
}
type Shape = Circle | Square;

function getArea(shape: Shape) {
  switch (shape.kind) {
    case "circle": return Math.PI * shape.radius ** 2;
    case "square": return shape.sideLength ** 2;
  }
}
```

> [!TIP]
> Use **Discriminated Unions** for complex state management (like Redux actions or API responses). It's the most robust way to ensure exhaustive check and type safety.
