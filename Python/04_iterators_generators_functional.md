# Python Iterators, Generators & Functional — Interview Notes 🐍

## 1. Iterables vs Iterators

```mermaid
graph LR
    Iterable["Iterable\n__iter__() → returns iterator"] -->|iter()| Iterator["Iterator\n__iter__() + __next__()"]
    Iterator -->|next()| Value["next value"]
    Iterator -->|exhausted| Stop["StopIteration"]

    style Iterable fill:#4a90d9,color:#fff
    style Iterator fill:#7b68ee,color:#fff
    style Stop fill:#e74c3c,color:#fff
```

| Concept | Has `__iter__` | Has `__next__` | Notes |
| :--- | :---: | :---: | :--- |
| **Iterable** | ✅ | ❌ | `list`, `str`, `dict`, `set`, `range` |
| **Iterator** | ✅ | ✅ | `map`, `filter`, `zip`, `enumerate`, generators |

```python
lst = [1, 2, 3]          # iterable
it = iter(lst)           # get iterator
next(it)                 # 1
next(it)                 # 2
next(it)                 # 3
next(it)                 # StopIteration

# for loop is just syntactic sugar for:
it = iter(iterable)
while True:
    try:
        x = next(it)
        # loop body
    except StopIteration:
        break
```

---

## 2. Custom Iterator

```python
class Countdown:
    def __init__(self, start):
        self.current = start

    def __iter__(self):    # makes it an iterable
        return self        # returns itself (it's also the iterator)

    def __next__(self):    # makes it an iterator
        if self.current <= 0:
            raise StopIteration
        val = self.current
        self.current -= 1
        return val

for n in Countdown(5):
    print(n)    # 5 4 3 2 1
```

> [!NOTE]
> An object can be both an iterable and an iterator (returns `self` from `__iter__`). But then it can only be iterated **once** — once exhausted, it stays exhausted. Prefer generators for this pattern.

---

## 3. Generators

A function with `yield` — produces values **lazily** on demand.

```python
def countdown(start):
    while start > 0:
        yield start         # pauses here, returns value to caller
        start -= 1          # resumes from here on next()

gen = countdown(3)
next(gen)    # 3
next(gen)    # 2
next(gen)    # 1
next(gen)    # StopIteration

# for loop consumes it
for n in countdown(5):
    print(n)
```

### Why generators?

```python
# Eager list — loads ALL in memory
def first_n_squares_list(n):
    return [x**2 for x in range(n)]

# Lazy generator — computes on demand
def first_n_squares_gen(n):
    for x in range(n):
        yield x**2

# Generator expression (inline)
gen = (x**2 for x in range(1_000_000))    # tiny memory footprint
sum(gen)   # evaluates lazily
```

> [!IMPORTANT]
> Generators are **single-use iterators**. Once a generator is exhausted, calling `next()` raises `StopIteration` — you can't reset it. Create a new generator object to re-iterate.

---

## 4. `yield from` (Python 3.3+)

Delegate to a sub-generator.

```python
def chain(*iterables):
    for it in iterables:
        yield from it          # flattens one level

list(chain([1,2], [3,4], [5]))  # [1, 2, 3, 4, 5]

# Recursive example: flatten nested lists
def flatten(nested):
    for item in nested:
        if isinstance(item, list):
            yield from flatten(item)
        else:
            yield item

list(flatten([1, [2, [3, 4]], 5]))  # [1, 2, 3, 4, 5]
```

---

## 5. Generator `.send()` — Two-Way Communication

Generators can receive values back via `.send()`.

```python
def accumulator():
    total = 0
    while True:
        value = yield total    # yield sends total OUT, receives value IN
        if value is None:
            break
        total += value

gen = accumulator()
next(gen)          # prime the generator (must call next first or send(None))
gen.send(10)       # 10
gen.send(20)       # 30
gen.send(5)        # 35
```

---

## 6. `itertools` — Essential Toolkit

```python
import itertools as it

# Infinite iterators
it.count(10, 2)           # 10, 12, 14, ... (start, step)
it.cycle([1, 2, 3])       # 1, 2, 3, 1, 2, 3, ...
it.repeat(x, n)           # x, x, x, ... n times

# Slicing / Chaining
it.islice(gen, 5)          # take first 5 items from iterator
it.chain([1,2], [3,4])     # 1, 2, 3, 4
it.chain.from_iterable([[1,2],[3,4]])  # same

# Combinatorics
list(it.permutations("AB", 2))    # [('A','B'), ('B','A')]
list(it.combinations("ABC", 2))   # [('A','B'), ('A','C'), ('B','C')]
list(it.combinations_with_replacement("AB", 2))  # with repeats
list(it.product("AB", repeat=2))  # cartesian product

# Grouping
data = [("A",1),("A",2),("B",3),("B",4)]
for key, group in it.groupby(data, key=lambda x: x[0]):
    print(key, list(group))   # must be sorted by key first!

# Accumulate
list(it.accumulate([1,2,3,4], lambda acc,x: acc+x))  # [1,3,6,10]

# Filtering
list(it.takewhile(lambda x: x<5, [1,3,7,2]))   # [1, 3]
list(it.dropwhile(lambda x: x<5, [1,3,7,2]))   # [7, 2]
list(it.filterfalse(lambda x: x%2, range(6)))  # [0, 2, 4]

# Zip variants
list(it.zip_longest([1,2,3], [4,5], fillvalue=0))  # [(1,4),(2,5),(3,0)]
```

---

## 7. `functools` — Functional Utilities

```python
from functools import reduce, partial, lru_cache, wraps, total_ordering

# reduce — fold
reduce(lambda a, b: a + b, [1,2,3,4])   # 10

# partial — freeze some arguments
from functools import partial
def power(base, exp): return base ** exp
square = partial(power, exp=2)
cube   = partial(power, exp=3)
square(5)   # 25
cube(3)     # 27

# lru_cache — memoize results
@lru_cache(maxsize=None)   # None = unbounded cache
def fib(n):
    if n < 2: return n
    return fib(n-1) + fib(n-2)

fib(100)   # instant; without cache this would be astronomical
fib.cache_info()        # CacheInfo(hits=..., misses=..., ...)
fib.cache_clear()       # clear the cache

# cache (Python 3.9+) — simpler, equivalent to lru_cache(maxsize=None)
from functools import cache
@cache
def fib(n): ...

# total_ordering — define __eq__ and one of __lt__/__le__/__gt__/__ge__
@total_ordering
class Version:
    def __init__(self, major, minor):
        self.major = major
        self.minor = minor
    def __eq__(self, other):
        return (self.major, self.minor) == (other.major, other.minor)
    def __lt__(self, other):
        return (self.major, self.minor) < (other.major, other.minor)
    # __le__, __gt__, __ge__ auto-generated!
```

---

## 8. `map`, `filter`, `zip` Deep Dive

```python
# All return lazy iterators (not lists)
nums = [1, 2, 3, 4]

m = map(lambda x: x*2, nums)     # lazy
f = filter(lambda x: x>2, nums)  # lazy
z = zip(nums, "abcd")            # lazy — stops at shorter

# Unzipping
pairs = [(1,"a"), (2,"b"), (3,"c")]
nums, letters = zip(*pairs)       # unzip: nums=(1,2,3), letters=('a','b','c')

# any() / all() — short-circuit
any(x > 3 for x in nums)   # True  (stops at first True)
all(x > 0 for x in nums)   # True  (stops at first False)
```

---

## 9. Summary Cheatsheet

| Tool | Lazy? | Notes |
| :--- | :---: | :--- |
| List comprehension `[...]` | ❌ | Eager, stores all in memory |
| Generator expression `(...)` | ✅ | Lazy, one-time use |
| `map()` / `filter()` | ✅ | Lazy iterators |
| `zip()` | ✅ | Stops at shortest; use `zip_longest` for padding |
| `range()` | ✅ | Lazy sequence, NOT a generator |
| `itertools.islice()` | ✅ | Slice any iterator |
| `@lru_cache` | — | Memoizes function results |

> [!IMPORTANT]
> **Key Interview Points**:
> 1. **Iterables** have `__iter__`; **Iterators** have both `__iter__` and `__next__`.
> 2. Generator functions **pause at `yield`** and resume on `next()`.
> 3. Generators are **single-pass** — cannot be re-iterated.
> 4. `range()` is an iterable but NOT an iterator — it can be iterated multiple times.
> 5. `@lru_cache` requires all arguments to be **hashable**.
> 6. `itertools.groupby()` only groups **consecutive** elements — sort first!
> 7. `zip()` stops at the **shortest** iterable; use `zip_longest` to pad.
