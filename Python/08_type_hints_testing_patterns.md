# Python Type Hints, Testing & Advanced Patterns — Interview Notes 🐍

## 1. Type Hints (Python 3.5+)

Python is dynamically typed, but type hints enable static analysis (mypy, pyright) and better IDE support.

```python
# Basic annotations
def add(a: int, b: int) -> int:
    return a + b

name: str = "Alice"
count: int = 0

# Optional (can be None)
from typing import Optional
def greet(name: Optional[str] = None) -> str:
    return f"Hello, {name or 'World'}"
# Python 3.10+ shorthand:
def greet(name: str | None = None) -> str: ...

# Union (one of multiple types)
from typing import Union
def parse(val: Union[int, str]) -> str: ...
# Python 3.10+:
def parse(val: int | str) -> str: ...
```

### Collection Types

```python
from typing import List, Dict, Tuple, Set, Sequence, Mapping

# Old style (pre-3.9):
def process(items: List[int]) -> Dict[str, int]: ...

# Modern (Python 3.9+) — use built-ins directly:
def process(items: list[int]) -> dict[str, int]: ...
def coords() -> tuple[float, float]: ...
def unique(items: list[int]) -> set[int]: ...
```

### Advanced Type Hints

```python
from typing import (
    Any, Callable, Generator, Iterator, Iterable,
    TypeVar, Generic, Protocol, Literal, Final,
    TypedDict, overload, cast
)

# Callable
Handler = Callable[[int, str], bool]  # (int, str) -> bool
def apply(fn: Callable[[int], int], x: int) -> int:
    return fn(x)

# TypeVar — generic type variable
T = TypeVar("T")
def first(lst: list[T]) -> T:
    return lst[0]

# Generic class
class Stack(Generic[T]):
    def __init__(self) -> None:
        self._items: list[T] = []
    def push(self, item: T) -> None:
        self._items.append(item)
    def pop(self) -> T:
        return self._items.pop()

# Protocol — structural subtyping (duck typing)
from typing import Protocol
class Drawable(Protocol):
    def draw(self) -> None: ...

def render(obj: Drawable) -> None:   # works with any object that has draw()
    obj.draw()

# TypedDict — typed dictionary
class Movie(TypedDict):
    name: str
    year: int
    rating: float

m: Movie = {"name": "Inception", "year": 2010, "rating": 8.8}

# Literal — specific values only
Mode = Literal["r", "w", "a", "rb", "wb"]
def open_file(path: str, mode: Mode) -> None: ...

# Final — constant (cannot be reassigned)
MAX_SIZE: Final = 100

# overload — multiple signatures
from typing import overload
@overload
def process(x: int) -> int: ...
@overload
def process(x: str) -> str: ...
def process(x):
    if isinstance(x, int): return x * 2
    return x.upper()
```

---

## 2. `dataclasses` + Type Hints

```python
from dataclasses import dataclass, field
from typing import ClassVar

@dataclass
class User:
    name: str
    age: int
    email: str | None = None
    tags: list[str] = field(default_factory=list)
    _count: ClassVar[int] = 0             # class variable, not an instance field

    def __post_init__(self):
        User._count += 1
        if self.age < 0:
            raise ValueError("Age cannot be negative")

u = User("Alice", 30)
```

---

## 3. Testing with `pytest`

```python
# test_math.py
import pytest
from mymodule import add, divide

# Basic test
def test_add():
    assert add(1, 2) == 3
    assert add(-1, 1) == 0

# Test exceptions
def test_divide_by_zero():
    with pytest.raises(ZeroDivisionError):
        divide(1, 0)

def test_divide_with_message():
    with pytest.raises(ValueError, match="cannot be zero"):
        divide(1, 0)

# Parametrize — run test with multiple inputs
@pytest.mark.parametrize("a, b, expected", [
    (1, 2, 3),
    (0, 0, 0),
    (-1, 1, 0),
    (100, 200, 300),
])
def test_add_parametrized(a, b, expected):
    assert add(a, b) == expected

# Fixtures — reusable setup/teardown
@pytest.fixture
def sample_user():
    return {"name": "Alice", "age": 30}

@pytest.fixture
def db_connection():
    conn = connect_to_db()
    yield conn          # teardown after test
    conn.close()

def test_user_name(sample_user):
    assert sample_user["name"] == "Alice"

# Fixture scopes: function (default), class, module, session
@pytest.fixture(scope="module")
def heavy_resource():
    resource = setup_expensive_resource()
    yield resource
    resource.cleanup()

# Marks — skip, xfail, etc.
@pytest.mark.skip(reason="not implemented yet")
def test_future(): ...

@pytest.mark.skipif(sys.platform == "win32", reason="Linux only")
def test_linux_thing(): ...

@pytest.mark.xfail(reason="known bug")
def test_known_failure(): ...
```

### Mocking with `unittest.mock`

```python
from unittest.mock import Mock, MagicMock, patch, call

# Mock object
m = Mock()
m.method(1, 2)
m.method.assert_called_once_with(1, 2)
m.method.return_value = 42

# MagicMock — supports dunder methods
mm = MagicMock()
len(mm)    # works (returns mock)
mm[0]      # works

# patch — replace an object temporarily
@patch("mymodule.requests.get")
def test_api(mock_get):
    mock_get.return_value.json.return_value = {"status": "ok"}
    result = call_api()
    assert result == {"status": "ok"}
    mock_get.assert_called_once_with("https://api.example.com")

# patch as context manager
with patch("mymodule.time.sleep") as mock_sleep:
    do_something_slow()
    mock_sleep.assert_called()

# patch.object — mock a method on a specific object
with patch.object(MyClass, "method", return_value=42) as m:
    obj = MyClass()
    assert obj.method() == 42
```

---

## 4. `__dunder__` Patterns Recap

```python
# Singleton via __new__
class Singleton:
    _instance = None
    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

# Descriptor protocol
class TypeChecked:
    def __set_name__(self, owner, name):
        self.name = name
    def __get__(self, obj, objtype=None):
        if obj is None: return self
        return obj.__dict__.get(self.name)
    def __set__(self, obj, value):
        if not isinstance(value, int):
            raise TypeError(f"{self.name} must be int")
        obj.__dict__[self.name] = value

class Point:
    x = TypeChecked()
    y = TypeChecked()

p = Point()
p.x = 10   # OK
# p.x = "a"  # TypeError
```

---

## 5. Design Patterns in Python

### Factory

```python
class Animal(ABC):
    @abstractmethod
    def speak(self) -> str: ...

class Dog(Animal):
    def speak(self): return "Woof"

class Cat(Animal):
    def speak(self): return "Meow"

def animal_factory(kind: str) -> Animal:
    registry = {"dog": Dog, "cat": Cat}
    cls = registry.get(kind)
    if not cls: raise ValueError(f"Unknown animal: {kind}")
    return cls()
```

### Observer

```python
class EventEmitter:
    def __init__(self):
        self._subscribers: dict[str, list] = defaultdict(list)

    def on(self, event: str, handler):
        self._subscribers[event].append(handler)

    def emit(self, event: str, *args, **kwargs):
        for handler in self._subscribers[event]:
            handler(*args, **kwargs)

emitter = EventEmitter()
emitter.on("data", lambda d: print(f"Got: {d}"))
emitter.emit("data", {"value": 42})
```

### Strategy

```python
from typing import Protocol
class SortStrategy(Protocol):
    def sort(self, data: list) -> list: ...

class BubbleSort:
    def sort(self, data): ...

class QuickSort:
    def sort(self, data): return sorted(data)

class Sorter:
    def __init__(self, strategy: SortStrategy):
        self._strategy = strategy
    def sort(self, data):
        return self._strategy.sort(data)
```

---

## 6. Python Memory Model

```python
import sys

# Reference counting
x = [1, 2, 3]
sys.getrefcount(x)   # at least 1 (extra 1 for the getrefcount call arg)

# id() — memory address (unique identifier)
a = [1, 2, 3]
b = a         # same object — b is a reference to a's object
id(a) == id(b)  # True

b = a[:]      # shallow copy
id(a) == id(b)  # False — different list objects, but same elements

import copy
deep = copy.deepcopy(a)   # completely independent copy

# Interning (CPython-specific)
# Small integers (-5 to 256) and string literals are interned automatically
a = 256; b = 256; a is b   # True (cached)
a = 257; b = 257; a is b   # False (may vary)
sys.intern("my_string")    # force intern a string
```

### `__slots__` Memory Savings

```python
import sys

class WithDict:
    def __init__(self, x, y):
        self.x = x; self.y = y

class WithSlots:
    __slots__ = ("x", "y")
    def __init__(self, x, y):
        self.x = x; self.y = y

sys.getsizeof(WithDict(1,2))    # ~48 bytes + ~240 bytes for __dict__
sys.getsizeof(WithSlots(1,2))   # ~56 bytes, no __dict__
```

---

## 7. Performance Tips

```python
# 1. Use local variables in tight loops (faster lookup)
import math
local_sqrt = math.sqrt
[local_sqrt(x) for x in range(10000)]  # faster than math.sqrt each call

# 2. Join strings — O(n) vs O(n²)
parts = ["a", "b", "c"]
"".join(parts)          # O(n) — correct
result = ""
for p in parts: result += p  # O(n²) — avoid!

# 3. Use sets for membership tests
blacklist = {"spam", "phish", "junk"}  # O(1) lookup
if email in blacklist: ...            # much faster than list

# 4. Use dict.get() to avoid KeyError + second lookup
d = {"a": 1}
val = d.get("b", default)   # vs: if "b" in d: val = d["b"]; else: val = default

# 5. Unpack in loop instead of indexing
for x, y in points:   # vs: for p in points: p[0], p[1]
    dist = (x**2 + y**2) ** 0.5

# 6. Use enumerate, zip instead of range(len())
for i, val in enumerate(lst):   # vs: for i in range(len(lst)): val = lst[i]
    ...

# 7. Generator for large data pipelines
total = sum(x**2 for x in range(1_000_000))   # never builds a list
```

---

## 8. Summary

> [!IMPORTANT]
> **Key Interview Points**:
> 1. Type hints are **not enforced at runtime** — they're for static analysis only. `isinstance()` is still needed for runtime checks.
> 2. `Optional[X]` is equivalent to `Union[X, None]` (or `X | None` in 3.10+).
> 3. `Protocol` enables **structural subtyping** (duck typing with type safety) — no `isinstance` check needed.
> 4. `pytest.fixture(scope="session")` runs setup once for the entire test session.
> 5. `@patch` is applied **bottom-up** when stacking — first decorator in code = last arg to test function.
> 6. `copy.copy()` is **shallow** (copies container, shares elements); `copy.deepcopy()` is fully independent.
> 7. CPython uses **reference counting** + **cyclic garbage collector** for memory management.
> 8. `id(x)` returns the memory address in CPython — only unique while the object is alive.
