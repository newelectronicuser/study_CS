# Python — Interview Questions & Answers

> Mid to Senior Level | Software Developer Role

---

## Language Fundamentals

**Q1. What is Python's GIL (Global Interpreter Lock)? How does it affect multi-threaded programs?**

**A:** The GIL is a mutex (lock) used by the CPython interpreter to ensure that only one thread executes Python bytecode at a time.
*   *Effect:* In CPU-bound multi-threaded programs, the GIL prevents threads from running in parallel on multiple CPU cores. Instead, they share a single core via time-slicing, making threading ineffective for CPU-heavy tasks. However, the GIL is released during blocking I/O operations (like disk read/write, database calls, network requests), making threading useful for I/O-bound tasks. To bypass the GIL for CPU-bound tasks, developers use the `multiprocessing` module instead of `threading`.

---

**Q2. What are Python's built-in data types? Which are mutable and which are immutable?**

**A:**
*   **Mutable (Can be modified in place):** `list`, `dict`, `set`, `bytearray`.
*   **Immutable (Value cannot change after creation; modifications return a new object):** `int`, `float`, `complex`, `str`, `tuple`, `bool`, `bytes`, `frozenset`.

---

**Q3. What is the difference between a list, a tuple, a set, and a dictionary?**

**A:**
*   **List (`[1, 2]`):** Ordered, indexable sequence. Allows duplicate elements. Mutable.
*   **Tuple (`(1, 2)`):** Ordered, indexable sequence. Allows duplicate elements. Immutable. Often used for structured records or data integrity.
*   **Set (`{1, 2}`):** Unordered collection of unique, hashable elements. Mutable. Optimized for membership tests ($O(1)$ lookup).
*   **Dictionary (`{"key": "value"}`):** Unordered collection of key-value pairs. Keys must be unique and hashable. Mutable. Optimized for key-based lookups.

---

**Q4. What is list comprehension? How does it compare to `map()` and `filter()`?**

**A:** List comprehension is a concise syntax for creating lists from iterable objects.
```python
# List comprehension
evens = [x * 2 for x in range(10) if x % 2 == 0]
```
*   *Comparison:* It is generally preferred over `map()` and `filter()` because it is cleaner, more readable, and allows performing mapping and filtering inside a single statement. Under the hood, list comprehensions run at C-speed in CPython, making them highly efficient.

---

**Q5. What are `*args` and `**kwargs`? How are they used?**

**A:** They allow a function to accept a variable number of arguments:
*   `*args`: Collects positional arguments into a **tuple**.
*   `**kwargs`: Collects keyword arguments into a **dictionary**.

```python
def my_func(*args, **kwargs):
    print(args)   # e.g., (1, 2)
    print(kwargs) # e.g., {'name': 'Alice'}
```

---

**Q6. What is the difference between `is` and `==` in Python?**

**A:**
*   `==`: Compares **values** (checks if the contents of two objects are equal). Equivalent to calling the `__eq__` dunder method.
*   `is`: Compares **identity** (checks if two variables reference the exact same object in memory, comparing their memory addresses `id()`).

---

**Q7. What is Python's memory management model? How does reference counting work?**

**A:** Python manages memory using two main mechanisms:
1.  **Reference Counting:** Every Python object has a counter tracking how many references point to it. When the count drops to 0, Python immediately deallocates the object's memory.
2.  **Cyclic Garbage Collector:** Reference counting cannot detect reference cycles (e.g. Object A points to B, and B points to A, but both are unreachable by the program). Python runs a generational garbage collector in the background to detect and break these circular reference cycles.

---

**Q8. What are Python decorators? Write a simple decorator example.**

**A:** A decorator is a design pattern that wraps a function, allowing you to execute code before and after the wrapped function runs without permanently modifying its source.

```python
def my_decorator(func):
    def wrapper(*args, **kwargs):
        print("Before execution")
        result = func(*args, **kwargs)
        print("After execution")
        return result
    return wrapper

@my_decorator
def say_hello():
    print("Hello!")
```

---

**Q9. What is a generator in Python? How does `yield` differ from `return`?**

**A:** A generator is a special type of function that returns a lazy iterator.
*   *Yield vs Return:* A `return` statement completely terminates the function, returning a value and destroying local state. A `yield` statement pauses the function, returns a value to the caller, and **saves the execution state** (local variables and instruction pointer), allowing the function to resume exactly where it left off when next called. This makes generators memory-efficient for processing large datasets since they yield one item at a time instead of loading entire lists in memory.

---

**Q10. What is the difference between `__iter__` and `__next__`?**

**A:**
*   `__iter__`: A method that must return an iterator object (typically `self`). Implementing this makes an object **iterable** (compatible with `for` loops).
*   `__next__`: A method on the iterator object that returns the next item in the sequence. If there are no more elements, it must raise `StopIteration` to signal the loop to terminate.

---

## OOP in Python

**Q11. What is the difference between `__str__` and `__repr__`?**

**A:**
*   `__str__`: Returns a user-friendly, readable string representation of the object. Called by `print()` and `str()`.
*   `__repr__`: Returns an unambiguous, developer-focused string representation of the object, ideally showing how to reconstruct the object (e.g. `User(id=1, name='Alice')`). Called by interactive shell/debuggers and `repr()`.

---

**Q12. What is multiple inheritance in Python and how does Python resolve MRO?**

**A:** Multiple inheritance allows a child class to inherit from more than one parent class. Python resolves method resolution order (MRO) using the **C3 Linearization** algorithm. MRO defines the search order for attributes/methods. You can check the MRO of a class by accessing `Class.__mro__` or calling `Class.mro()`. It prevents the diamond problem by preserving local precedence and monotonicity.

---

**Q13. What is the difference between a class method, a static method, and an instance method?**

**A:**
*   **Instance Method:** Default. Takes `self` as the first argument. Can access and modify instance-level state and class-level state.
*   **Class Method (`@classmethod`):** Takes `cls` (the class itself) as the first argument. Can modify class-level state but cannot access instance-level state. Used as factory methods.
*   **Static Method (`@staticmethod`):** Takes no implicit first argument (neither `self` nor `cls`). Behaves like a plain function utility nested inside the class namespace.

---

**Q14. What are dunder (magic) methods? Name ten common ones.**

**A:** Dunder (Double Under score) methods are built-in methods with double underscores that allow classes to hook into Python's syntax features (operator overloading, lifecycle).
*   *Ten Common:*
    1. `__init__` (initializer)
    2. `__str__` (string format)
    3. `__repr__` (eval format)
    4. `__len__` (length)
    5. `__getitem__` (bracket index)
    6. `__setitem__` (bracket assign)
    7. `__eq__` (equality `==`)
    8. `__lt__` (less than `<`)
    9. `__enter__` (context start)
    10. `__exit__` (context end)

---

**Q15. What is a property decorator in Python? How is it different from a plain attribute?**

**A:** The `@property` decorator exposes a method as a read-only getter attribute. You can also define corresponding setters and deleters.
*   *Difference:* A plain attribute allows unrestricted reads and writes. A property allows executing validation logic, lazy computation, or authorization checks transparently whenever a client accesses the attribute.

```python
class Circle:
    def __init__(self, radius):
        self._radius = radius
    @property
    def radius(self):
        return self._radius
    @radius.setter
    def radius(self, val):
        if val < 0: raise ValueError("Negative radius")
        self._radius = val
```

---

**Q16. What is the difference between composition and inheritance? When do you prefer each?**

**A:**
*   **Inheritance:** Models an "IS-A" relationship (e.g., `Dog` is an `Animal`). It couples the child class tightly to the parent class structure.
*   **Composition:** Models a "HAS-A" relationship (e.g., `Car` has an `Engine`). A class holds references to other helper classes rather than extending them.
*   *Preference:* Favor composition over inheritance because it keeps classes decoupled, allows changing behavior at runtime, and prevents rigid inheritance hierarchies.

---

**Q17. What is an abstract base class (ABC) in Python?**

**A:** An ABC is a class that cannot be instantiated. It defines a blueprint / interface for subclasses by declaring abstract methods using the `@abstractmethod` decorator from the `abc` module. Subclasses must implement all abstract methods before they can be instantiated.

---

**Q18. What is `__slots__` and how does it improve memory efficiency?**

**A:** By default, Python instances store attributes in a dynamic dictionary (`self.__dict__`), which has high memory overhead. Declaring `__slots__ = ('name', 'age')` inside a class prevents dictionary creation and instead allocates a fixed-size array for instance attributes, significantly reducing memory usage when spawning millions of objects.

---

## Functional Programming

**Q19. What is a lambda function in Python? When would you avoid using one?**

**A:** A lambda function is a small, anonymous, single-expression function defined with the `lambda` keyword.
*   *Avoid:* Avoid using lambdas for complex operations. Because they are limited to a single expression, using them for multi-line statements hurts readability. In such cases, use a named function (`def`).

---

**Q20. What is `functools.partial()`? Give a use case.**

**A:** `partial()` returns a new callable where some of the arguments of the original function are pre-filled (frozen).
*   *Use Case:* Adapting generic function interfaces to specific callback APIs. For example, freezing the port in a generic connection function: `connect_db = partial(connect, host='localhost', port=5432)`.

---

**Q21. What is `functools.lru_cache()` and when is it useful?**

**A:** A decorator that wraps a function with a Least Recently Used (LRU) cache. It caches function arguments and their returned values.
*   *When useful:* For expensive, pure functions that are called repeatedly with the same arguments (e.g., calculating Fibonacci sequence recursively, fetching static DB configs).

---

**Q22. How do `map()`, `filter()`, and `reduce()` work in Python?**

**A:**
*   `map(func, iterable)`: Applies a function to all items and returns an iterator of results.
*   `filter(pred, iterable)`: Tests each element with a predicate function, returning an iterator of items evaluating to True.
*   `reduce(func, iterable)`: Located in `functools`. Applies a function of two arguments cumulatively to sequence items to reduce the sequence to a single value.

---

**Q23. What is a closure in Python? Give an example.**

**A:** A closure is an inner function that retains access to variables from its outer (enclosing) scope, even after the outer function has finished executing.

```python
def make_multiplier(x):
    def multiplier(y):
        return x * y # accesses x from outer scope
    return multiplier

double = make_multiplier(2)
print(double(5)) # Output: 10
```

---

## Error Handling

**Q24. What is the difference between `Exception` and `BaseException`?**

**A:**
*   `BaseException`: The root class for all exceptions in Python.
*   `Exception`: Inherits from `BaseException`. Almost all user-defined and standard library exceptions inherit from `Exception`. System-exiting exceptions like `SystemExit`, `KeyboardInterrupt` (Ctrl+C), and `GeneratorExit` inherit directly from `BaseException`. You should write `except Exception:` to catch errors, avoiding catching system exits.

---

**Q25. How do you create a custom exception class in Python?**

**A:** Inherit from the built-in `Exception` class (or a specific subclass like `ValueError`).
```python
class InsufficientFundsError(Exception):
    def __init__(self, balance, amount):
        super().__init__(f"Balance {balance} is less than requested amount {amount}")
        self.balance = balance
        self.amount = amount
```

---

**Q26. What is the `else` clause in a try-except block?**

**A:** The `else` block executes only if the code in the `try` block did **not** raise any exceptions. It is useful for separating target code from exception handling, preventing catching unexpected exceptions inside success routines.

---

**Q27. What is a context manager? How do `__enter__` and `__exit__` work?**

**A:** A context manager is an object that manages resource setup and teardown inside a `with` statement (e.g. closing files).
*   `__enter__`: Runs first and returns the resource (assigned using `as`).
*   `__exit__(exc_type, exc_val, exc_tb)`: Runs at the end of the block. If an exception occurs, its details are passed as arguments. Returning `True` from `__exit__` suppresses the exception.

---

**Q28. How do you raise an exception while preserving the original traceback?**

**A:** Use the `raise ... from` syntax (exception chaining):
```python
try:
    connect_database()
except ConnectionError as e:
    raise AppDatabaseError("DB call failed") from e
```

---

## Concurrency & Async

**Q29. What is the difference between threading, multiprocessing, and asyncio in Python?**

**A:**
*   `threading`: Best for I/O-bound tasks. Shared memory, lightweight, but blocked by the GIL for CPU tasks.
*   `multiprocessing`: Best for CPU-bound tasks. Spawns independent Python interpreter processes with separate memory spaces. Bypasses the GIL but has high serialization and memory overhead.
*   `asyncio`: Single-threaded, single-process cooperative multitasking using an event loop. Extremely fast and lightweight for handling thousands of concurrent network connections (I/O) without thread context switching.

---

**Q30. How does `asyncio` achieve concurrency without multiple threads?**

**A:** `asyncio` uses **Cooperative Multitasking** on a single thread. It runs an **Event Loop** that keeps track of active tasks (coroutines). When a task executes an I/O operation (e.g. `await database_query()`), it yields control back to the event loop. The loop switches to run other ready tasks. Once the I/O completes, the loop schedules the original task to resume.

---

**Q31. What is an event loop? What is a coroutine?**

**A:**
*   **Event Loop:** The engine in `asyncio` that coordinates the execution of asynchronous tasks, handles network I/O events, and schedules operations.
*   **Coroutine:** A specialized generator function declared with `async def`. It can suspend execution using `await` and resume later.

---

**Q32. What is `async def` and `await`? Give an example.**

**A:**
*   `async def`: Declares a function to be a coroutine.
*   `await`: Suspends the execution of the coroutine until the awaited task completes, yielding control back to the event loop.

```python
import asyncio

async def fetch_data():
    await asyncio.sleep(1) # yields control
    return "data"

async def main():
    result = await fetch_data()
    print(result)

asyncio.run(main())
```

---

**Q33. What is `asyncio.gather()` vs `asyncio.wait()`?**

**A:**
*   `asyncio.gather(*tasks)`: Runs tasks concurrently and returns a list of results in the order they were submitted. If one task raises an exception, the others continue running unless configured otherwise.
*   `asyncio.wait(tasks)`: Runs tasks concurrently but returns two sets: `(done, pending)` futures. Allows finer control, like returning as soon as the first task completes (`FIRST_COMPLETED`).

---

**Q34. When would you use `multiprocessing` over `threading` in Python?**

**A:** Use `multiprocessing` when the application is bottlenecked by CPU-intensive computations (e.g., image/video processing, machine learning models, running matrix math loops). Threading will not improve performance due to the GIL locking execution to a single core.

---

**Q35. What is a `ThreadPoolExecutor` and `ProcessPoolExecutor` in `concurrent.futures`?**

**A:**
*   `ThreadPoolExecutor`: Manages a pool of threads. Best for executing multiple I/O calls concurrently.
*   `ProcessPoolExecutor`: Manages a pool of worker processes. Best for executing CPU-bound calculations across multiple processor cores.

---

## Modules, Packages & Environment

**Q36. What is the difference between a module and a package in Python?**

**A:**
*   **Module:** A single `.py` file containing Python code (functions, classes, variables) that can be imported.
*   **Package:** A folder directory containing multiple modules (and sub-packages), structured to allow namespace importing.

---

**Q37. What is `__init__.py` and when is it required?**

**A:** `__init__.py` is placed in a folder directory to make Python treat that folder as an importable package. Since Python 3.3, it is not strictly required for basic namespace packages, but is still used to execute initialization code or expose explicit public APIs.

---

**Q38. What is `__all__` in a module?**

**A:** `__all__` is a list of strings defined at the module level. It controls what attributes are imported when a client runs `from module import *`.
```python
__all__ = ['public_func', 'PublicClass'] # private_func is excluded from wildcard imports
```

---

**Q39. What is the difference between `pip`, `pipenv`, and `poetry`?**

**A:**
*   `pip`: The default, basic package installer for Python packages from PyPI. Does not handle virtual environments natively.
*   `pipenv`: A tool that combines virtualenv management with dependency locking using a `Pipfile.lock`.
*   `poetry`: A modern dependency management and packaging tool. It uses `pyproject.toml` (standard package specification), resolves dependencies efficiently, and handles building/publishing packages.

---

**Q40. What are virtual environments and why are they important?**

**A:** A virtual environment is an isolated directory containing a private Python interpreter copy and its dependencies. It is important because it prevents dependency conflicts between different projects running on the same machine (e.g., Project A requiring Django 3, and Project B requiring Django 5).

---

**Q41. What is `__name__ == "__main__"` and why is it used?**

**A:** `__name__` is a built-in variable. If a script is run directly from the terminal, `__name__` is set to `"__main__"`. If the script is imported as a module, `__name__` is set to the filename. Checking this prevents execution of main block logic when importing utilities into other files.

---

## Performance & Best Practices

**Q42. How do you profile Python code to find performance bottlenecks?**

**A:**
*   Use the built-in `cProfile` module: `python -m cProfile script.py` to analyze execution times per function.
*   Use `line_profiler` for line-by-line runtime analysis.
*   Use memory profilers like `memory_profiler` to inspect memory allocation.

---

**Q43. What is the difference between `append()` and `extend()` for lists?**

**A:**
*   `append(item)`: Adds the single item argument to the end of the list.
*   `extend(iterable)`: Unrolls the iterable argument and appends each element to the end of the list.

---

**Q44. Why are string concatenations in a loop slow in Python? What is the alternative?**

**A:** Because strings are immutable, adding strings (`s1 += s2`) creates a new string object and copies the old contents, resulting in $O(n^2)$ time complexity.
*   *Alternative:* Append substrings to a list and call `''.join(list_of_strings)` at the end, which performs in $O(n)$ time.

---

**Q45. What is the `collections` module? Name four useful classes from it.**

**A:** A built-in module offering high-performance container alternatives to list, dict, and tuple.
*   *Four Useful Classes:*
    1. `defaultdict` (automatically initializes missing keys with defaults).
    2. `counter` (counts hashable elements).
    3. `deque` (double-ended queue optimized for fast pops/appends on both ends).
    4. `namedtuple` (creates tuple subclasses with named fields).

---

**Q46. What is `itertools` and when is it useful?**

**A:** A built-in module providing efficient, memory-friendly iterator tools (like `chain`, `cycle`, `permutations`, `groupby`). It is useful for processing infinite sequences or combinatorics cleanly without allocating memory lists.

---

**Q47. What are type hints in Python? How do you use `mypy` for static type checking?**

**A:** Type hints annotate variable and function parameter types (e.g. `def add(x: int) -> int:`). They are ignored by the Python runtime but analyzed by static type checkers like **mypy** to catch type mismatches before running code.

---

**Q48. What is the difference between deep copy and shallow copy?**

**A:**
*   **Shallow Copy (`copy.copy()`):** Creates a new container object but copies references to the nested objects inside. If you modify a nested object, the change is reflected in both copies.
*   **Deep Copy (`copy.deepcopy()`):** Recursively creates copies of all nested objects inside the container, producing an entirely independent copy.

---

## Testing

**Q49. What is `pytest`? How does it differ from `unittest`?**

**A:**
*   `unittest`: The built-in framework requiring class-based structures and assertion methods (`self.assertEqual()`).
*   `pytest`: A modern framework supporting plain function-based tests and python assertions (`assert x == y`). It has a rich ecosystem of plugins, dynamic fixtures, and parameterization, making tests simpler to write.

---

**Q50. What is mocking in Python? How do you use `unittest.mock.patch()`?**

**A:** Mocking replaces real code components (like third-party APIs or database connections) with mock objects during tests to assert behaviors without side effects.
*   `patch()`: Temporarily replaces a module attribute or class with a mock object inside a context or test method.

```python
from unittest.mock import patch

@patch('mymodule.requests.get')
def test_api(mock_get):
    mock_get.return_value.status_code = 200
    # execute test
```

---
