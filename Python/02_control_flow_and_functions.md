# Python Control Flow & Functions — Interview Notes 🐍

## 1. Conditionals

```python
x = 10

if x > 0:
    print("positive")
elif x == 0:
    print("zero")
else:
    print("negative")

# Ternary expression (conditional expression)
result = "even" if x % 2 == 0 else "odd"

# match-case (Python 3.10+) — structural pattern matching
match command:
    case "quit":
        quit()
    case "go" | "move":
        go()
    case {"action": action, "speed": speed}:   # dict destructuring
        handle(action, speed)
    case Point(x=0, y=0):                       # class pattern
        print("origin")
    case _:                                     # wildcard (default)
        print("unknown")
```

---

## 2. Loops

```python
# for loop — iterate over any iterable
for i in range(5):           # 0 1 2 3 4
    print(i)

for i in range(2, 10, 3):   # 2 5 8
    print(i)

for char in "hello":
    print(char)

for i, val in enumerate(["a", "b", "c"], start=1):
    print(i, val)   # 1 a, 2 b, 3 c

for a, b in zip([1,2,3], ["x","y","z"]):
    print(a, b)     # 1 x, 2 y, 3 z

# while loop
n = 10
while n > 0:
    n -= 1

# Loop control
for i in range(10):
    if i == 3: continue   # skip rest of body, go to next iteration
    if i == 7: break      # exit loop immediately
else:
    print("loop completed without break")  # else runs if no break hit!
```

> [!IMPORTANT]
> **`for...else` / `while...else`**: The `else` block runs **only if the loop completed without hitting a `break`**. Very useful for search patterns.

```python
# Classic search pattern with for-else
def find_prime(lst):
    for n in lst:
        for d in range(2, int(n**0.5)+1):
            if n % d == 0:
                break
        else:
            return n   # no break hit: n is prime
    return None
```

---

## 3. Comprehensions

```python
# List comprehension
squares   = [x**2 for x in range(10)]
even_sq   = [x**2 for x in range(10) if x % 2 == 0]
flat      = [n for row in matrix for n in row]    # nested — outer loop first

# Dict comprehension
inv       = {v: k for k, v in d.items()}

# Set comprehension
unique_sq = {x**2 for x in nums}

# Generator expression (lazy — no [] needed inside calls)
total     = sum(x**2 for x in range(1000))        # memory efficient
```

> [!TIP]
> Use a **generator expression** instead of a list comprehension inside `sum()`, `max()`, `min()`, `any()`, `all()` — the values are computed lazily without building a list in memory.

---

## 4. Functions

```python
def greet(name, greeting="Hello"):   # default parameter
    """Return a greeting string."""  # docstring
    return f"{greeting}, {name}!"

greet("Alice")            # 'Hello, Alice!'
greet("Bob", "Hi")        # 'Hi, Bob!'
greet(greeting="Yo", name="Eve")  # keyword args (any order)
```

### `*args` and `**kwargs`

```python
def variadic(*args, **kwargs):
    print(args)    # tuple of positional args
    print(kwargs)  # dict of keyword args

variadic(1, 2, 3, name="Alice", age=30)
# (1, 2, 3)
# {'name': 'Alice', 'age': 30}

# Keyword-only arguments (after *)
def kw_only(a, b, *, sep=","):
    return f"{a}{sep}{b}"

kw_only(1, 2, sep="-")   # '1-2'
# kw_only(1, 2, "-")     # TypeError — sep is keyword-only

# Positional-only arguments (before /) — Python 3.8+
def pos_only(a, b, /, c):
    return a + b + c

pos_only(1, 2, c=3)   # 6
# pos_only(a=1, b=2)  # TypeError
```

### Unpacking into function calls

```python
args   = [1, 2, 3]
kwargs = {"sep": "-"}
kw_only(*args[:2], **kwargs)   # unpacking
```

---

## 5. Return Values

```python
def nothing(): pass          # returns None implicitly

def multi():
    return 1, "a", True     # returns a TUPLE — (1, 'a', True)

x, y, z = multi()           # unpacking
```

---

## 6. Lambda Functions

Anonymous, single-expression functions.

```python
double   = lambda x: x * 2
add      = lambda x, y: x + y

# Used with higher-order functions
nums = [3, 1, 4, 1, 5]
sorted(nums, key=lambda x: -x)            # sort descending
sorted(people, key=lambda p: p["age"])    # sort by field

# Avoid complex lambdas — use def for readability
```

> [!WARNING]
> Lambda functions cannot contain statements (`if/else` control flow is OK as an expression, but `for`, `while`, `try` blocks are not). Use `def` for anything non-trivial.

---

## 7. Higher-Order Functions

Functions that accept or return other functions.

```python
# map() — apply function to every element (returns iterator)
list(map(str, [1, 2, 3]))           # ['1', '2', '3']

# filter() — keep elements where predicate is True
list(filter(lambda x: x > 0, [-1, 0, 1, 2]))  # [1, 2]

# reduce() — fold sequence to single value
from functools import reduce
reduce(lambda acc, x: acc + x, [1,2,3,4])  # 10

# zip() — combine iterables
list(zip([1,2,3], ["a","b","c"]))   # [(1,'a'), (2,'b'), (3,'c')]

# sorted() with key
sorted(["banana","apple","fig"], key=len)  # by length

# Prefer comprehensions over map/filter for readability in most cases
```

---

## 8. Closures

A function that **captures variables from its enclosing scope**.

```python
def make_counter():
    count = 0
    def increment():
        nonlocal count    # modify enclosing variable
        count += 1
        return count
    return increment

c = make_counter()
c()  # 1
c()  # 2
c()  # 3

# Classic factory pattern
def multiplier(factor):
    return lambda x: x * factor

double = multiplier(2)
triple = multiplier(3)
double(5)   # 10
triple(5)   # 15
```

> [!IMPORTANT]
> Use `nonlocal` to modify an enclosing (non-global) variable. Without it, assignment creates a **new local variable** and you get `UnboundLocalError` on read-before-assign.

---

## 9. Decorators

A decorator is a function that wraps another function.

```python
import functools

def timer(func):
    @functools.wraps(func)   # preserves __name__, __doc__
    def wrapper(*args, **kwargs):
        import time
        start = time.perf_counter()
        result = func(*args, **kwargs)
        elapsed = time.perf_counter() - start
        print(f"{func.__name__} took {elapsed:.4f}s")
        return result
    return wrapper

@timer
def slow_add(a, b):
    import time; time.sleep(0.1)
    return a + b

slow_add(1, 2)   # prints timing, returns 3

# Decorator with arguments
def repeat(n):
    def decorator(func):
        @functools.wraps(func)
        def wrapper(*args, **kwargs):
            for _ in range(n):
                result = func(*args, **kwargs)
            return result
        return wrapper
    return decorator

@repeat(3)
def say_hi():
    print("Hi!")

say_hi()   # prints "Hi!" 3 times
```

> [!TIP]
> Always use `@functools.wraps(func)` in decorator wrappers — it copies `__name__`, `__doc__`, `__module__` from the wrapped function. Without it, all decorated functions report the same name (`wrapper`), breaking debugging and introspection.

### Stacking Decorators

```python
@decorator_a
@decorator_b
def func(): pass
# Equivalent to: func = decorator_a(decorator_b(func))
# decorator_b runs first (innermost), decorator_a runs outermost
```

---

## 10. `*args` vs `**kwargs` Full Reference

```python
# / = positional-only boundary
# * = keyword-only boundary
def full_sig(pos_only, /, normal, *, kw_only, **rest):
    pass

full_sig(1, 2, kw_only=3, extra=4)
full_sig(1, normal=2, kw_only=3)
```

---

## 11. Summary Cheatsheet

| Feature | Syntax | Notes |
| :--- | :--- | :--- |
| Default args | `def f(x=0)` | Evaluated once at definition time! |
| `*args` | Positional varargs → tuple | |
| `**kwargs` | Keyword varargs → dict | |
| Keyword-only | `def f(*, kwarg)` | Must use keyword at call site |
| Positional-only | `def f(a, b, /)` | Python 3.8+ |
| Lambda | `lambda x: expr` | Single expression only |
| Closure | Captures enclosing scope | Use `nonlocal` to mutate |
| Decorator | `@func` syntactic sugar | Use `@wraps` always |

> [!IMPORTANT]
> **Key Interview Points**:
> 1. **Mutable default arguments are a common bug**: `def f(lst=[])` — the list is shared across all calls! Use `None` and assign inside.
> 2. `for...else` / `while...else`: `else` runs only if no `break` was hit.
> 3. `nonlocal` modifies enclosing scope; `global` modifies module scope.
> 4. Decorators are just functions returning functions — `@dec` is sugar.
> 5. Generator expressions are lazy; list comprehensions are eager.

```python
# Mutable default argument bug
def bad(lst=[]):
    lst.append(1)
    return lst

bad()   # [1]
bad()   # [1, 1]  ← NOT [1]!

# Fix
def good(lst=None):
    if lst is None:
        lst = []
    lst.append(1)
    return lst
```
