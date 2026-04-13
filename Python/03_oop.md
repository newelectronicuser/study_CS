# Python OOP — Interview Notes 🐍

## 1. Classes & Objects

```python
class Animal:
    # Class variable — shared by ALL instances
    kingdom = "Animalia"

    def __init__(self, name, sound):
        # Instance variables — unique to each instance
        self.name = name
        self._sound = sound      # convention: protected
        self.__id = id(self)     # name-mangled: _Animal__id

    def speak(self):
        return f"{self.name} says {self._sound}"

    def __repr__(self):
        return f"Animal(name={self.name!r})"

    def __str__(self):
        return self.name

dog = Animal("Rex", "woof")
dog.speak()      # 'Rex says woof'
str(dog)         # 'Rex'
repr(dog)        # "Animal(name='Rex')"
```

---

## 2. `__init__` vs `__new__`

| Method | Called | Purpose |
| :--- | :--- | :--- |
| `__new__(cls, ...)` | Before `__init__` | Creates and returns the new instance |
| `__init__(self, ...)` | After `__new__` | Initializes the instance |

> [!NOTE]
> `__new__` is rarely overridden — mainly for immutable types (`str`, `int`, `tuple`) and implementing patterns like Singleton.

---

## 3. Instance, Class, and Static Methods

```python
class MyClass:
    count = 0

    def __init__(self):
        MyClass.count += 1

    def instance_method(self):
        """Has access to self (instance and class)."""
        return self

    @classmethod
    def class_method(cls):
        """Has access to cls (class only). No instance needed."""
        return cls.count

    @staticmethod
    def static_method(x, y):
        """No access to self or cls. Pure utility function."""
        return x + y

obj = MyClass()
obj.instance_method()     # via instance
MyClass.class_method()    # via class  → returns count
MyClass.static_method(3, 4)  # 7
```

> [!TIP]
> Use `@classmethod` for alternative constructors:
> ```python
> @classmethod
> def from_string(cls, s):
>     name, age = s.split(",")
>     return cls(name.strip(), int(age))
>
> p = Person.from_string("Alice, 30")
> ```

---

## 4. Inheritance

```python
class Animal:
    def __init__(self, name):
        self.name = name

    def speak(self):
        raise NotImplementedError("Subclass must implement speak()")

class Dog(Animal):
    def speak(self):
        return f"{self.name}: Woof!"

class Cat(Animal):
    def speak(self):
        return f"{self.name}: Meow!"

# super() — call parent method
class GoldenRetriever(Dog):
    def __init__(self, name, color):
        super().__init__(name)    # call Dog → Animal.__init__
        self.color = color

    def speak(self):
        base = super().speak()   # Dog.speak
        return f"{base} (happily)"
```

### Multiple Inheritance & MRO

```python
class A:
    def method(self): print("A")

class B(A):
    def method(self): print("B")

class C(A):
    def method(self): print("C")

class D(B, C):
    pass

D().method()          # B  — follows MRO
D.__mro__             # (D, B, C, A, object) — C3 linearization
```

> [!IMPORTANT]
> Python uses **C3 Linearization (MRO)** for method resolution in multiple inheritance. Check `ClassName.__mro__` to see the lookup order. `super()` follows this order, not just the immediate parent.

---

## 5. Encapsulation & Name Mangling

```python
class BankAccount:
    def __init__(self, balance):
        self._balance = balance       # protected: convention, accessible
        self.__pin = "1234"           # private: name-mangled to _BankAccount__pin

    def get_balance(self):
        return self._balance

    def _validate(self, pin):         # protected method (convention)
        return pin == self.__pin

# Name mangling
acc = BankAccount(100)
acc._balance              # accessible (just convention)
# acc.__pin               # AttributeError
acc._BankAccount__pin     # '1234' — accessible if you know the mangled name
```

---

## 6. Properties

Control attribute access with getter/setter/deleter.

```python
class Temperature:
    def __init__(self, celsius):
        self._celsius = celsius

    @property
    def celsius(self):
        return self._celsius

    @celsius.setter
    def celsius(self, value):
        if value < -273.15:
            raise ValueError("Temperature below absolute zero!")
        self._celsius = value

    @celsius.deleter
    def celsius(self):
        del self._celsius

    @property
    def fahrenheit(self):
        return self._celsius * 9/5 + 32

t = Temperature(25)
t.celsius          # 25  — calls getter
t.celsius = 100   # calls setter
t.fahrenheit      # 212.0  — computed property
del t.celsius      # calls deleter
```

---

## 7. Abstract Classes

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @abstractmethod
    def area(self) -> float:
        """Must be implemented by subclasses."""
        ...

    @abstractmethod
    def perimeter(self) -> float: ...

    def describe(self):         # concrete method — inherited as-is
        return f"Area: {self.area():.2f}, Perimeter: {self.perimeter():.2f}"

class Circle(Shape):
    def __init__(self, radius):
        self.radius = radius

    def area(self):
        import math
        return math.pi * self.radius ** 2

    def perimeter(self):
        import math
        return 2 * math.pi * self.radius

# Shape()     # TypeError: Can't instantiate abstract class
Circle(5).area()   # 78.54
```

---

## 8. Dunder (Magic) Methods

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    # String representations
    def __repr__(self): return f"Vector({self.x}, {self.y})"
    def __str__(self):  return f"({self.x}, {self.y})"

    # Arithmetic
    def __add__(self, other):  return Vector(self.x+other.x, self.y+other.y)
    def __sub__(self, other):  return Vector(self.x-other.x, self.y-other.y)
    def __mul__(self, scalar): return Vector(self.x*scalar, self.y*scalar)
    def __rmul__(self, scalar): return self.__mul__(scalar)  # 3 * v

    # Comparison
    def __eq__(self, other):   return self.x == other.x and self.y == other.y
    def __lt__(self, other):   return abs(self) < abs(other)

    # Length/magnitude
    def __abs__(self):
        import math
        return math.sqrt(self.x**2 + self.y**2)

    def __len__(self): return 2  # number of dimensions

    # Boolean
    def __bool__(self): return self.x != 0 or self.y != 0

    # Iteration
    def __iter__(self): return iter((self.x, self.y))

    # Container protocol
    def __len__(self):    return 2
    def __getitem__(self, i): return (self.x, self.y)[i]

    # Callable
    def __call__(self, factor):
        return Vector(self.x*factor, self.y*factor)

    # Context manager
    def __enter__(self): return self
    def __exit__(self, *args): pass

    # Hashing (if __eq__ defined, __hash__ must be explicit)
    def __hash__(self): return hash((self.x, self.y))

v1 = Vector(1, 2)
v2 = Vector(3, 4)
v1 + v2          # Vector(4, 6)
abs(v1)          # 2.236
3 * v1           # Vector(3, 6)
bool(Vector(0,0))  # False
```

| Dunder | Triggered by |
| :--- | :--- |
| `__init__` | `obj = Class(...)` |
| `__repr__` | `repr(obj)`, interactive shell |
| `__str__` | `str(obj)`, `print(obj)` |
| `__len__` | `len(obj)` |
| `__getitem__` | `obj[key]` |
| `__setitem__` | `obj[key] = val` |
| `__contains__` | `x in obj` |
| `__iter__` | `for x in obj`, `iter(obj)` |
| `__next__` | `next(obj)` |
| `__enter__/exit__` | `with obj:` |
| `__call__` | `obj(args)` |
| `__add__` | `obj + other` |
| `__eq__` | `obj == other` |
| `__hash__` | `hash(obj)`, dict key, set member |

---

## 9. Dataclasses (Python 3.7+)

```python
from dataclasses import dataclass, field

@dataclass
class Point:
    x: float
    y: float
    z: float = 0.0                     # default value
    tags: list = field(default_factory=list)  # mutable default

    def distance_from_origin(self):
        return (self.x**2 + self.y**2 + self.z**2) ** 0.5

@dataclass(frozen=True)   # immutable — like a namedtuple but with types
class ImmutablePoint:
    x: float
    y: float

@dataclass(order=True)    # auto-generates __lt__, __le__, __gt__, __ge__
class RankedItem:
    rank: int
    name: str

p = Point(1.0, 2.0)
p         # Point(x=1.0, y=2.0, z=0.0, tags=[])
```

> [!TIP]
> `@dataclass` auto-generates `__init__`, `__repr__`, `__eq__`. Add `frozen=True` for immutability (and hashability), `order=True` for comparison operators.

---

## 10. `__slots__`

Replaces `__dict__` with a fixed set of attributes — saves memory.

```python
class Point:
    __slots__ = ("x", "y")   # only these attrs allowed

    def __init__(self, x, y):
        self.x = x
        self.y = y

p = Point(1, 2)
# p.z = 3          # AttributeError — not in __slots__
# p.__dict__       # AttributeError — no __dict__
```

> [!NOTE]
> Use `__slots__` when creating **many** instances of the same class. Can reduce memory usage by 40–50% compared to the default `__dict__`.

---

## 11. Composition vs Inheritance

```python
# Inheritance (is-a)
class Car(Vehicle): ...

# Composition (has-a) — preferred for flexibility
class Engine:
    def start(self): print("Engine started")

class Car:
    def __init__(self):
        self.engine = Engine()   # Car HAS-A Engine

    def start(self):
        self.engine.start()
        print("Car moving")
```

> [!TIP]
> Prefer **composition over inheritance** in Python. Inheritance couples classes tightly; composition is more flexible and avoids deep inheritance hierarchies.

---

## 12. Summary

> [!IMPORTANT]
> **Key Interview Points**:
> 1. `__repr__` is for developers (should be unambiguous); `__str__` for users (readable). `print()` uses `__str__`, falling back to `__repr__`.
> 2. If you define `__eq__`, Python sets `__hash__ = None` → object becomes unhashable. Explicitly define `__hash__` if you need it in sets/dicts.
> 3. `@classmethod` receives the class (`cls`); `@staticmethod` receives nothing extra.
> 4. Multiple inheritance follows MRO (C3 linearization) — `super()` uses MRO, not just the direct parent.
> 5. `@dataclass(frozen=True)` makes instances immutable and hashable.
> 6. Name mangling (`__attr`) prevents accidental override in subclasses — it's **not** true private access control.
