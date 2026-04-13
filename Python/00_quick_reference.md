# Python Interview Quick-Reference — Master Cheatsheet 🐍

> One-stop revision sheet. Cross-reference the detailed notes for deep dives.

---

## 🔢 Data Types

| Type | Mutable | Notes |
| :--- | :---: | :--- |
| `int` | ❌ | Arbitrary precision; `bool` is subclass of `int` |
| `float` | ❌ | IEEE 754; `0.1 + 0.2 ≠ 0.3` — use `decimal.Decimal` |
| `str` | ❌ | Unicode; use `"".join(list)` for repeated concat |
| `list` | ✅ | O(1) append/pop-end; O(n) insert/pop-front |
| `tuple` | ❌ | Hashable if elements hashable; use for fixed records |
| `dict` | ✅ | O(1) avg; ordered since 3.7; keys must be hashable |
| `set` | ✅ | O(1) membership; no duplicates |
| `frozenset` | ❌ | Hashable set |

**Falsy values**: `False`, `0`, `0.0`, `""`, `[]`, `()`, `{}`, `set()`, `None`

```python
# Key gotchas
True + True     # 2 (bool is int)
0.1 + 0.2       # 0.30000000000000004
[1,2] == [1,2]  # True (value)
[1,2] is [1,2]  # False (identity)
(42,)           # single-element tuple (comma required!)
bool("false")   # True (non-empty string!)
```

---

## 🔄 Comprehensions

```python
[x**2 for x in range(10) if x%2==0]           # list
{k: v for k,v in d.items()}                   # dict
{x for x in lst}                              # set
(x**2 for x in range(10))                     # generator (lazy)
[n for row in matrix for n in row]            # flatten (outer loop first)
```

---

## ⚙️ Functions

```python
# Mutable default argument BUG
def bad(lst=[]):     # lst shared across calls!
    lst.append(1); return lst

def good(lst=None):  # FIX
    if lst is None: lst = []
    lst.append(1); return lst

# Argument types
def func(pos_only, /, normal, *, kw_only): ...
def func(*args, **kwargs): ...   # variadic

# Closures need nonlocal to mutate enclosing scope
def counter():
    n = 0
    def inc():
        nonlocal n
        n += 1
        return n
    return inc
```

---

## 🏗️ OOP

```python
class MyClass(Base):
    class_var = []                    # shared by all instances

    def __init__(self): ...           # instance init
    def instance_method(self): ...   # has self
    @classmethod
    def class_method(cls): ...        # has cls; alternative constructors
    @staticmethod
    def static_method(): ...          # no self/cls; utility

# Key dunder methods
__repr__   # developer repr; repr(obj)
__str__    # user repr; str(obj), print(obj)
__eq__     # ==  (if defined, __hash__ set to None!)
__hash__   # hash(); re-define if __eq__ defined
__len__    # len()
__getitem__ # obj[key]
__iter__   # iter(); for loops
__next__   # next()
__enter__/__exit__  # with statement
__call__   # obj()

# MRO — method resolution order
D.__mro__  # C3 linearization: D → B → C → A → object
```

---

## 🔁 Iterators & Generators

```python
# Iterable:  has __iter__
# Iterator:  has __iter__ + __next__
# Generator: function with yield — lazy, single-pass

def gen():
    yield 1
    yield 2
    yield 3

for x in gen(): print(x)          # 1 2 3
next(iter([1,2,3]))                # 1

# range() is ITERABLE, not iterator (can iterate multiple times)
r = range(5)
list(r)  # [0,1,2,3,4]
list(r)  # [0,1,2,3,4]  — works again!
```

---

## 🚨 Exceptions

```python
try:
    risky()
except (TypeError, ValueError) as e:
    handle(e)
except Exception:
    raise              # bare raise preserves traceback
else:
    success()          # runs only if NO exception
finally:
    cleanup()          # ALWAYS runs

raise ValueError("msg") from original_exc  # chain

# Context manager
class CM:
    def __enter__(self): return self
    def __exit__(self, exc_type, exc_val, exc_tb):
        return True  # suppress exception; False = propagate

from contextlib import contextmanager
@contextmanager
def cm():
    setup()
    try: yield resource
    finally: teardown()
```

---

## 📦 Key Standard Library

| Module | Key Uses |
| :--- | :--- |
| `collections` | `Counter`, `defaultdict`, `deque`, `namedtuple` |
| `itertools` | `chain`, `islice`, `groupby`, `combinations`, `product` |
| `functools` | `reduce`, `partial`, `lru_cache`, `wraps`, `total_ordering` |
| `pathlib` | `Path("/...") / "sub"`, `.read_text()`, `.glob()` |
| `datetime` | `.now()`, `.strftime()`, `timedelta` |
| `json` | `dumps/loads`, `dump/load` |
| `re` | `search`, `findall`, `sub`, `compile` |
| `os` | `getcwd`, `makedirs`, `environ`, `path.join` |
| `sys` | `argv`, `path`, `exit`, `getsizeof` |
| `logging` | Always prefer over `print()` in production |
| `typing` | `Optional`, `Union`, `TypeVar`, `Protocol`, `Callable` |
| `abc` | `ABC`, `@abstractmethod` |
| `copy` | `copy()` shallow, `deepcopy()` deep |

---

## ⚡ Concurrency

| Tool | Best For | GIL? |
| :--- | :--- | :--- |
| `threading` | I/O-bound | Limited by GIL |
| `asyncio` | I/O-bound | No GIL issue (1 thread) |
| `multiprocessing` | CPU-bound | Bypasses GIL |
| `concurrent.futures` | Both | High-level wrapper |

```python
# asyncio essentials
async def main():
    results = await asyncio.gather(coro1(), coro2())  # concurrent
    result  = await coro()                             # sequential

asyncio.run(main())   # entry point
# Never: time.sleep() inside async — use await asyncio.sleep()
# Never: blocking I/O inside async — use asyncio.to_thread()

# Thread-safe queue
q = queue.Queue()
q.put(item)
item = q.get()
```

---

## 🧪 Testing

```python
# pytest
def test_something():
    assert result == expected

@pytest.mark.parametrize("inp,out", [(1,1),(2,4)])
def test_param(inp, out): assert f(inp) == out

@pytest.fixture
def setup(): return {"key": "val"}

# mock
@patch("module.requests.get")
def test_api(mock_get):
    mock_get.return_value.json.return_value = {"ok": True}
```

---

## 🎯 Top 30 Interview Gotchas

| # | Gotcha | Fix |
| :--- | :--- | :--- |
| 1 | Mutable default arg | Use `None`; assign inside |
| 2 | `is` vs `==` | `is` = identity; `==` = value |
| 3 | `bool` is `int` | `True==1`, `False==0` |
| 4 | `0.1+0.2 ≠ 0.3` | Use `decimal.Decimal` |
| 5 | Empty containers are falsy | `bool([]) == False` |
| 6 | `(42)` is int, not tuple | Need `(42,)` |
| 7 | `"false"` is truthy | Non-empty string = truthy |
| 8 | `for...else` | `else` runs if NO `break` |
| 9 | `range()` is not iterator | Can iterate multiple times |
| 10 | Generator is single-pass | Create new generator to re-iter |
| 11 | `nonlocal` required | Without it: new local var created |
| 12 | `__eq__` kills `__hash__` | Re-define `__hash__` explicitly |
| 13 | `super()` follows MRO | Not just direct parent |
| 14 | `@wraps(func)` in decorators | Preserves `__name__`, `__doc__` |
| 15 | `groupby` needs sorted input | Groups only consecutive |
| 16 | `zip` stops at shortest | Use `zip_longest` to pad |
| 17 | `map/filter` return iterators | Wrap in `list()` if needed |
| 18 | GIL limits threading | Use multiprocessing for CPU tasks |
| 19 | `asyncio.sleep` vs `time.sleep` | Never call `time.sleep` in async |
| 20 | `raise` vs `raise e` | `raise` preserves; `raise e` resets traceback |
| 21 | `copy.copy` is shallow | Use `copy.deepcopy` for full independence |
| 22 | `finally` overrides return | `return` in `finally` wins |
| 23 | `__exit__` return value | `True` suppresses; `False` propagates |
| 24 | `@lru_cache` needs hashable args | Mutable args won't work |
| 25 | `list.pop(0)` is O(n) | Use `collections.deque.popleft()` |
| 26 | `__slots__` has no `__dict__` | Can't add dynamic attrs |
| 27 | Type hints not enforced at runtime | Use `isinstance()` for checks |
| 28 | `ProcessPoolExecutor` needs pickling | Lambdas/closures won't work |
| 29 | `__name__` guard | `if __name__ == "__main__"` |
| 30 | `open()` without `with` | Resource leak; always use `with open()` |

---

## 📁 Notes Index

| File | Topics |
| :--- | :--- |
| `01_basics_and_data_types.md` | Variables, int/float/str/list/tuple/dict/set, type conversion, `is` vs `==` |
| `02_control_flow_and_functions.md` | if/for/while, comprehensions, functions, `*args/**kwargs`, lambda, closures, decorators |
| `03_oop.md` | Classes, inheritance, MRO, dunder methods, properties, ABC, dataclasses, `__slots__` |
| `04_iterators_generators_functional.md` | Iterables, iterators, generators, `yield from`, `itertools`, `functools` |
| `05_exceptions_and_context_managers.md` | try/except/else/finally, custom exceptions, chaining, context managers, `@contextmanager` |
| `06_modules_packages_stdlib.md` | Modules, packages, `__name__`, stdlib: `os`, `pathlib`, `collections`, `datetime`, `json`, `re`, `logging` |
| `07_concurrency_and_async.md` | GIL, `threading`, `asyncio`, `multiprocessing`, `concurrent.futures`, `queue` |
| `08_type_hints_testing_patterns.md` | Type hints, `typing` module, `pytest`, `unittest.mock`, design patterns, memory model |
