# Functional Programming in JavaScript

JavaScript supports functional programming (FP) patterns, which emphasize purity, immutability, and function composition.

## 1. Pure Functions
A function is pure if:
- It always returns the same output for the same input.
- It has no **Side Effects** (e.g., modifying global variables, logging to console, network requests).

## 2. Higher-Order Functions (HOF)
A function that either:
- Takes one or more functions as arguments.
- Returns a function as its result.

Common examples: `.map()`, `.filter()`, `.reduce()`, `.find()`.

## 3. Currying
The process of transforming a function that takes multiple arguments into a sequence of functions that each take a single argument.

```javascript
// Normal
const add = (a, b) => a + b;

// Curried
const add = a => b => a + b;
const addFive = add(5);
console.log(addFive(3)); // 8
```

## 4. Function Composition
The process of combining two or more functions to produce a new function.
`f(g(x))`

## 5. Immutability
Data that cannot be changed after it is created. In FP, instead of changing an object, you create a new one with the changes.

- **`Object.assign()`** or **Spread Operator** for objects.
- **`.map()`, `.filter()`, `.concat()`** for arrays (these return new arrays).

## 6. Declarative vs. Imperative
- **Imperative**: Focuses on *how* to do something (using loops, state changes).
- **Declarative**: Focuses on *what* to do (using HOFs, recursion).

```javascript
// Imperative
const doubled = [];
for (let i = 0; i < arr.length; i++) {
  doubled.push(arr[i] * 2);
}

// Declarative
const doubled = arr.map(x => x * 2);
```

## 7. Functors & Monads (Briefly)
- **Functor**: An object that implements a `map` method which preserves its structure.
- **Monad**: An extension of Functors that also implements a `flatMap` (or `chain`) method. Promises are often cited as a common example of monad-like behavior in JS.

> [!TIP]
> Master **`.reduce()`**. It is the most powerful array method and can be used to implement almost all other array methods (map, filter, find).
