# 'this' and Prototypes

## 1. The `this` Keyword
The value of `this` is determined by **how a function is called**, not where it is defined (except for arrow functions).

### Rules of Binding (Order of Precedence)
1.  **New Binding**: `this` refers to the newly created object when using `new`.
2.  **Explicit Binding**: `this` is explicitly set via `.call()`, `.apply()`, or `.bind()`.
3.  **Implicit Binding**: `this` refers to the object on which the method was called (e.g., `obj.method()`).
4.  **Default Binding**: In non-strict mode, `this` is the global object (`window` in browsers). In strict mode, it is `undefined`.

### Arrow Functions
Arrow functions **do not** have their own `this`. They capture the `this` value of the enclosing lexical scope at the time they are created.

## 2. Prototypal Inheritance
Every object in JavaScript has an internal link to another object called its **Prototype**.

- **Prototype Chain**: When you access a property on an object, JS first checks the object itself. If not found, it checks its prototype, then the prototype's prototype, and so on, until it reaches `null` (the end of the chain).
- **`__proto__` vs `prototype`**:
    - `__proto__`: The property on an *instance* that points to its actual prototype.
    - `prototype`: A property on *constructor functions* that used to set the `__proto__` for instances created by that constructor.

## 3. Creating Objects with Prototypes
```javascript
const animal = {
  eat: true
};
const rabbit = Object.create(animal);
console.log(rabbit.eat); // true (inherited)
```

## 4. Classes (ES6)
Classes are primarily **syntactic sugar** over JavaScript's existing prototypal inheritance.

```javascript
class Animal {
  constructor(name) {
    this.name = name;
  }
  speak() {
    console.log(`${this.name} makes a noise.`);
  }
}
```

## 5. `Object.create(null)`
Creating an object with `null` as the prototype creates an object that has *no* inherited properties (not even `toString` or `hasOwnProperty`). This is useful for building "pure" dictionaries.

> [!IMPORTANT]
> **call vs apply vs bind**:
> - `call`: Invokes the function immediately. Arguments are passed separately: `fn.call(this, arg1, arg2)`.
> - `apply`: Invokes the function immediately. Arguments are passed as an array: `fn.apply(this, [args])`.
> - `bind`: Does **not** invoke the function. It returns a new function with `this` permanently bound.
