# Python — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Language Fundamentals

**Q1. What is Python's GIL and how does it affect multi-threaded programs?**

**A:** The Global Interpreter Lock (GIL) is a mutex in CPython that allows only one thread to execute Python bytecode at a time. This means Python threads cannot run in true parallel on multiple CPU cores for CPU-bound tasks. The GIL is released during I/O operations, so threading works well for I/O-bound tasks (HTTP calls, file reads). For CPU-bound parallelism, use `multiprocessing` (separate processes, each with its own GIL).

---

**Q2. What is the difference between mutable and immutable types?**

**A:**
- **Immutable**: `int`, `float`, `bool`, `str`, `tuple`, `frozenset`, `bytes` — cannot be changed after creation. Reassignment creates a new object.
- **Mutable**: `list`, `dict`, `set`, `bytearray` — can be modified in place.

Immutability matters for: dictionary keys (must be hashable/immutable), function default arguments (mutable defaults are shared across calls — a classic gotcha), and thread safety.

---

**Q3. What are `*args` and `**kwargs`?**

**A:** `*args` collects positional arguments into a tuple. `**kwargs` collects keyword arguments into a dictionary.

```python
def f(*args, **kwargs):
    print(args)   # (1, 2, 3)
    print(kwargs) # {'name': 'Alice', 'age': 30}

f(1, 2, 3, name='Alice', age=30)
```

Also used for unpacking: `func(*my_list)`, `func(**my_dict)`.

---

**Q4. What are Python decorators?**

**A:** A decorator is a function that wraps another function, modifying its behavior without changing its source. Uses `@syntax` sugar.

```python
def timer(func):
    import time
    def wrapper(*args, **kwargs):
        start = time.time()
        result = func(*args, **kwargs)
        print(f"{func.__name__} took {time.time()-start:.2f}s")
        return result
    return wrapper

@timer
def slow_function(): time.sleep(1)
```

Real-world uses: logging, authentication checks, caching (`@functools.lru_cache`), retry logic.

---

**Q5. What is a generator and how does `yield` differ from `return`?**

**A:** A generator is a function that uses `yield` to produce a sequence of values lazily — one at a time, on demand. Unlike `return`, which exits the function, `yield` suspends it and resumes from where it left off on the next `next()` call. Generators are memory-efficient for large sequences.

```python
def fibonacci():
    a, b = 0, 1
    while True:
        yield a
        a, b = b, a + b

gen = fibonacci()
print(next(gen), next(gen), next(gen))  # 0 1 1
```

---

## OOP in Python

**Q6. What is Python's MRO (Method Resolution Order)?**

**A:** MRO determines the order Python searches for a method in a class hierarchy, especially with multiple inheritance. Python uses the **C3 Linearization** algorithm. View it with `ClassName.__mro__`.

```python
class A: pass
class B(A): pass
class C(A): pass
class D(B, C): pass
print(D.__mro__)  # D → B → C → A → object
```

---

**Q7. What is the difference between `__str__` and `__repr__`?**

**A:** `__repr__` is the "official" string representation for developers — should be unambiguous and ideally `eval()`-able back to the object. `__str__` is the "informal" human-readable representation for end users. `print()` and `str()` call `__str__`; the interactive REPL and `repr()` call `__repr__`. If only one is defined, `__repr__` is used as a fallback.

---

**Q8. What is `__slots__`?**

**A:** By default, Python stores instance attributes in a `__dict__` (a dictionary per instance). Defining `__slots__ = ['x', 'y']` replaces this with a fixed-size array of attribute slots. Benefits: reduced memory usage (no per-instance dict), slightly faster attribute access. Trade-off: cannot add arbitrary attributes dynamically.

---

**Q9. What is a property decorator?**

**A:** `@property` allows you to define getter (and optionally setter/deleter) logic for an attribute access pattern while keeping the interface like a plain attribute — no parentheses needed.

```python
class Circle:
    def __init__(self, radius): self._radius = radius
    
    @property
    def area(self): return 3.14 * self._radius ** 2
    
    @property
    def radius(self): return self._radius
    
    @radius.setter
    def radius(self, value):
        if value < 0: raise ValueError("Radius must be positive")
        self._radius = value
```

---

## Functional Programming

**Q10. What is a closure in Python?**

**A:** A closure is a function that captures variables from its enclosing scope, even after the outer function has returned.

```python
def multiplier(factor):
    def multiply(x):
        return x * factor  # 'factor' is captured from enclosing scope
    return multiply

double = multiplier(2)
print(double(5))  # 10
```

Closures are the basis for decorators and are used for data encapsulation and factory functions.

---

**Q11. What is `functools.lru_cache()`?**

**A:** A decorator that caches the results of a function call based on its arguments (memoization). Subsequent calls with the same arguments return the cached result without re-executing the function. The `maxsize` parameter controls cache size (LRU eviction). Use `@functools.cache` (Python 3.9+) for an unbounded cache.

```python
@functools.lru_cache(maxsize=128)
def fib(n):
    if n < 2: return n
    return fib(n-1) + fib(n-2)
```

---

## Concurrency & Async

**Q12. What is the difference between threading, multiprocessing, and asyncio?**

**A:**
- **threading**: OS threads sharing memory. I/O-bound tasks. Limited by GIL for CPU-bound work.
- **multiprocessing**: Separate processes with separate memory. True CPU parallelism. Higher memory overhead. Use for CPU-bound tasks.
- **asyncio**: Single-threaded cooperative concurrency. Coroutines yield control at `await` points. Excellent for high-concurrency I/O (web servers, API clients) with minimal thread overhead.

---

**Q13. What is `asyncio` and how does it achieve concurrency?**

**A:** `asyncio` implements an event loop that manages coroutines. A coroutine is a function defined with `async def`. When it `await`s an I/O operation (network call, file read), it yields control back to the event loop, which can run other ready coroutines. No OS thread switching overhead — all happens on one thread.

```python
async def fetch(url):
    async with aiohttp.ClientSession() as session:
        async with session.get(url) as response:
            return await response.json()

async def main():
    results = await asyncio.gather(fetch(url1), fetch(url2), fetch(url3))
```

---

**Q14. What is `asyncio.gather()` vs `asyncio.wait()`?**

**A:**
- `asyncio.gather(*coros)`: Runs coroutines concurrently, returns list of results in order. Cancels all if one fails by default.
- `asyncio.wait(tasks, return_when=...)`: More control — can wait for FIRST_COMPLETED, FIRST_EXCEPTION, or ALL_COMPLETED. Returns sets of done and pending tasks. Good for "race" patterns.

---

## Error Handling & Context Managers

**Q15. What is a context manager and how does `with` work?**

**A:** A context manager manages resources with setup (`__enter__`) and teardown (`__exit__`) logic. `with` calls `__enter__` on entry and `__exit__` on exit (even if an exception occurs).

```python
class ManagedFile:
    def __init__(self, name): self.name = name
    def __enter__(self): self.file = open(self.name); return self.file
    def __exit__(self, exc_type, exc_val, exc_tb): self.file.close(); return False
```

Or use `@contextlib.contextmanager` with a generator:
```python
@contextlib.contextmanager
def managed_file(name):
    f = open(name)
    try: yield f
    finally: f.close()
```

---

## Performance & Best Practices

**Q16. What is the `collections` module? Name four useful classes.**

**A:**
- `defaultdict`: Dict with a default factory for missing keys.
- `Counter`: Dict subclass for counting hashable objects.
- `deque`: Double-ended queue with O(1) appends/pops on both ends.
- `OrderedDict`: Dict that remembers insertion order (less needed in Python 3.7+ where all dicts maintain order).
- `namedtuple`: Lightweight immutable data class with named fields.

---

**Q17. What is the difference between a deep copy and a shallow copy?**

**A:**
- **Shallow copy** (`copy.copy()`): Creates a new object but references the same nested objects. Changes to nested mutable objects affect both.
- **Deep copy** (`copy.deepcopy()`): Recursively copies all nested objects. Fully independent copy.

```python
import copy
original = [[1, 2], [3, 4]]
shallow = copy.copy(original)
shallow[0].append(99)  # modifies original[0] too!
deep = copy.deepcopy(original)
deep[0].append(99)     # original is unaffected
```

---

**Q18. What are type hints in Python?**

**A:** Type hints (PEP 484) annotate variables and function signatures with expected types. They are not enforced at runtime but enable static type checking with `mypy` and improve IDE support and documentation.

```python
from typing import List, Optional

def get_user(user_id: int) -> Optional[dict]:
    ...

def process(items: List[str]) -> None:
    ...
```

Python 3.10+ supports `X | Y` union syntax: `def f(x: int | None) -> str | None`.

---

**Q19. What is `pytest` and how does it differ from `unittest`?**

**A:** Both are testing frameworks. `unittest` is the standard library framework, requiring classes inheriting `TestCase` and verbose `assertEqual` methods. `pytest` is more Pythonic: plain functions with `assert`, auto-discovers tests, rich fixture system with dependency injection, powerful plugins, and better output. `pytest` is the de facto standard for modern Python projects.

---

**Q20. How do you use `unittest.mock.patch()`?**

**A:** `patch()` replaces an object in the module under test with a `MagicMock` during the test, then restores the original after.

```python
from unittest.mock import patch

@patch('mymodule.requests.get')
def test_api_call(mock_get):
    mock_get.return_value.json.return_value = {'data': 'test'}
    result = mymodule.fetch_data()
    mock_get.assert_called_once_with('https://api.example.com/data')
    assert result == {'data': 'test'}
```

---
