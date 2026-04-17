# ES6+ Features

Modern JavaScript (ES6 and beyond) introduced significant syntax improvements and new features that changed how we write JS.

## 1. Let and Const vs. Var
- **`var`**: Function-scoped, hoisted (with `undefined`), can be re-declared.
- **`let` / `const`**: Block-scoped, not accessible before declaration (TDZ), cannot be re-declared in the same scope.
- **`const`**: Same as `let`, but the *reference* cannot be changed. (Object properties *can* still be mutated).

## 2. Arrow Functions
Concise syntax for writing functions. Key differences from regular functions:
- **No `this` binding**: They use `this` from the outer lexical scope.
- **No `arguments` object**: Use rest parameters instead.
- **Cannot be used as constructors**: You cannot use `new` with arrow functions.

## 3. Destructuring
Easily extract values from arrays or objects.
```javascript
const user = { name: "John", age: 30 };
const { name, age } = user;

const list = [1, 2, 3];
const [first, second] = list;
```

## 4. Spread and Rest Operators (`...`)
- **Rest**: Gathers multiple elements into an array (used in function parameters or destructuring).
- **Spread**: Unpacks elements from an array or object into a new one.

## 5. Template Literals
Standardized way to create multiline strings and perform string interpolation (`${expression}`).

## 6. Symbols
A primitive data type used to create unique identifiers for object properties.
```javascript
const mySymbol = Symbol("desc");
const obj = { [mySymbol]: "value" };
```

## 7. Generators and Iterators
- **Iterators**: Objects that define a sequence and potentially a return value upon its termination.
- **Generators**: Special functions (`function*`) that can be paused (`yield`) and resumed.

```javascript
function* sequence() {
  yield 1;
  yield 2;
}
const gen = sequence();
console.log(gen.next().value); // 1
```

## 8. Modules (ESM)
Support for `import` and `export` statements natively in JavaScript.

> [!TIP]
> Use **Optional Chaining (`?.`)** and **Nullish Coalescing (`??`)** to handle deeply nested objects and default values safely.
> - `const city = user?.address?.city ?? "Unknown";`
