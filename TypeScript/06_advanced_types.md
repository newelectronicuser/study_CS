# Advanced Typing Techniques

These features allow you to write highly dynamic and type-safe code that can adapt to different data structures.

## 1. `keyof` and `typeof` (Type Level)
- **`keyof`**: Takes an object type and produces a string or numeric literal union of its keys.
    ```typescript
    interface Point { x: number; y: number; }
    type P = keyof Point; // "x" | "y"
    ```
- **`typeof`**: Used in a type context to refer to the type of a variable or property.
    ```typescript
    let s = "hello";
    let n: typeof s; // type n is string
    ```

## 2. Indexed Access Types
Use an indexed access type to look up a specific property on another type.
```typescript
type Person = { age: number; name: string; alive: boolean };
type Age = Person["age"]; // number
```

## 3. Mapped Types
Allow you to create a new type based on an old one by "mapping" over the properties.
```typescript
type OptionsFlags<Type> = {
  [Property in keyof Type]: boolean;
};

interface Features {
  drive: () => void;
  break: () => void;
}
type FeatureFlags = OptionsFlags<Features>;
// Result: { drive: boolean; break: boolean; }
```

## 4. Conditional Types
Allow you to choose a type based on a condition, similar to ternary operators in JavaScript.
`SomeType extends OtherType ? TrueType : FalseType`
```typescript
type IsString<T> = T extends string ? "yes" : "no";

type A = IsString<string>; // "yes"
type B = IsString<number>; // "no"
```

## 5. Template Literal Types
Built on string literal types, and have the ability to expand into many strings via unions.
```typescript
type World = "world";
type Greeting = `hello ${World}`; // "hello world"

type EmailLocaleIDs = "welcome_email" | "email_heading";
type FooterLocaleIDs = "footer_title" | "footer_sendoff";
type AllLocaleIDs = `${EmailLocaleIDs | FooterLocaleIDs}_id`;
```

## 6. The `infer` Keyword
Used within conditional types to infer a type from within the matching branch.
```typescript
type GetReturnType<T> = T extends (...args: any[]) => infer R ? R : never;

type T1 = GetReturnType<() => string>; // string
```

> [!TIP]
> **Advanced Types** are what separate junior TS developers from seniors. Mastering **Mapped Types** and **Conditional Types** allows you to build generic, reusable, and perfectly typed abstractions.
