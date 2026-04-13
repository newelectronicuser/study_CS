# Python Modules, Packages & Standard Library — Interview Notes 🐍

## 1. Modules

Any `.py` file is a module. Importing it executes it once (then cached in `sys.modules`).

```python
# math_utils.py
PI = 3.14159

def circle_area(r):
    return PI * r ** 2

# In another file:
import math_utils
math_utils.circle_area(5)

# Import specific names
from math_utils import circle_area, PI

# Import with alias
import numpy as np
from collections import defaultdict as dd

# Import all (avoid in production — pollutes namespace)
from math_utils import *    # imports names not starting with _
```

### `__name__` guard

```python
# script.py
def main():
    print("Running main")

if __name__ == "__main__":
    main()
    # This block runs only when the file is executed directly,
    # NOT when imported as a module
```

---

## 2. Packages

A package is a **directory with `__init__.py`** (can be empty).

```
mypackage/
├── __init__.py          # makes it a package; can export names
├── utils.py
├── models/
│   ├── __init__.py
│   └── user.py
```

```python
# mypackage/__init__.py
from .utils import helper_func   # relative import
from .models.user import User    # make available at package level

# Importing
from mypackage import User
from mypackage.utils import helper_func
from mypackage.models import user
```

### Relative vs Absolute Imports

```python
# Absolute (preferred)
from mypackage.utils import func

# Relative (within package only)
from . import utils          # same package
from .. import sibling_pkg   # parent package
from .models import User     # sub-package
```

> [!WARNING]
> Relative imports only work **inside packages**. Running a file directly (`python mypackage/utils.py`) breaks relative imports. Always run packages as modules: `python -m mypackage.utils`.

---

## 3. `sys.path` — Module Search Order

```python
import sys
print(sys.path)
# 1. Current directory (or script directory)
# 2. PYTHONPATH env variable entries
# 3. Installation-dependent default (site-packages)

# Add custom path at runtime
sys.path.insert(0, "/path/to/my/modules")
```

---

## 4. `__all__` — Control Public API

```python
# mymodule.py
__all__ = ["public_func", "PublicClass"]  # only these are exported with import *

def public_func(): ...
class PublicClass: ...
def _private(): ...   # not exported even without __all__
```

---

## 5. Essential Standard Library Modules

### `os` — Operating System Interface

```python
import os

os.getcwd()                        # current working directory
os.listdir(".")                    # list directory
os.path.join("dir", "file.txt")   # platform-safe path
os.path.exists("/some/path")       # True/False
os.path.isfile("x.py")
os.path.isdir("mydir")
os.path.basename("/a/b/c.txt")    # 'c.txt'
os.path.dirname("/a/b/c.txt")     # '/a/b'
os.path.splitext("file.txt")      # ('file', '.txt')
os.makedirs("a/b/c", exist_ok=True)
os.rename("old.txt", "new.txt")
os.remove("file.txt")
os.environ.get("HOME", "/tmp")     # safe env var access
os.getenv("PATH")
```

### `pathlib` — Modern Path Handling (prefer over `os.path`)

```python
from pathlib import Path

p = Path("/home/user/docs/file.txt")
p.name          # 'file.txt'
p.stem          # 'file'
p.suffix        # '.txt'
p.parent        # Path('/home/user/docs')
p.parts         # ('/', 'home', 'user', 'docs', 'file.txt')

p.exists()
p.is_file()
p.is_dir()

# Build paths with / operator
base = Path("/home/user")
config = base / "config" / "settings.json"

# Read/write
config.read_text()
config.write_text("data")
config.read_bytes()

# Glob
list(Path(".").glob("**/*.py"))   # recursive

# Create directories
Path("a/b/c").mkdir(parents=True, exist_ok=True)
```

### `sys` Module

```python
import sys

sys.argv              # ['script.py', 'arg1', 'arg2']
sys.exit(0)           # exit with code
sys.version           # Python version string
sys.platform          # 'linux', 'darwin', 'win32'
sys.path              # module search paths
sys.modules           # dict of loaded modules
sys.stdin / stdout / stderr
sys.getsizeof(obj)    # bytes used by object
sys.getrecursionlimit()         # 1000 by default
sys.setrecursionlimit(5000)
```

### `collections` — High-Performance Containers

```python
from collections import (
    Counter, defaultdict, OrderedDict,
    namedtuple, deque, ChainMap
)

# Counter
c = Counter("abracadabra")    # {'a':5, 'b':2, 'r':2, 'c':1, 'd':1}
c.most_common(2)              # [('a',5), ('b',2)]
c + Counter("abc")            # add counts
c.elements()                  # iterator over elements repeated count times

# defaultdict — no KeyError for missing keys
dd = defaultdict(list)
dd["fruit"].append("apple")   # no KeyError
dd["fruit"].append("banana")
dd  # {'fruit': ['apple', 'banana']}

dd_int = defaultdict(int)
for char in "hello":
    dd_int[char] += 1         # counting without setdefault

# deque — O(1) append/pop from both ends
dq = deque([1, 2, 3], maxlen=5)
dq.appendleft(0)   # O(1)
dq.popleft()       # O(1) — unlike list.pop(0) which is O(n)
dq.rotate(2)       # shift right by 2

# namedtuple
Point = namedtuple("Point", ["x", "y"])
p = Point(3, 4)
p.x    # 3
p._asdict()           # OrderedDict
p._replace(x=10)      # new instance with x=10

# ChainMap — layered dict lookup
from collections import ChainMap
defaults = {"color": "red", "size": "M"}
user     = {"size": "XL"}
merged   = ChainMap(user, defaults)
merged["size"]   # 'XL' (user takes priority)
merged["color"]  # 'red' (falls through to defaults)
```

### `datetime`

```python
from datetime import datetime, date, time, timedelta, timezone

now = datetime.now()               # local time
utc = datetime.now(timezone.utc)   # timezone-aware

dt = datetime(2024, 1, 15, 10, 30)
dt.year   # 2024
dt.strftime("%Y-%m-%d %H:%M:%S")  # format to string
datetime.strptime("2024-01-15", "%Y-%m-%d")  # parse string

delta = timedelta(days=7, hours=3)
future = now + delta

# Date only
today = date.today()
yesterday = today - timedelta(days=1)

# Comparison
dt1 < dt2   # direct comparison works
```

### `json`

```python
import json

# Serialize Python → JSON string
json.dumps({"name": "Alice", "age": 30})     # '{"name": "Alice", "age": 30}'
json.dumps(obj, indent=2, sort_keys=True)    # pretty print

# Deserialize JSON string → Python
data = json.loads('{"name": "Alice"}')       # {'name': 'Alice'}

# File I/O
with open("data.json", "w") as f:
    json.dump(obj, f, indent=2)

with open("data.json") as f:
    data = json.load(f)
```

### `re` — Regular Expressions

```python
import re

# Match at start
m = re.match(r"\d+", "42abc")  # Match object or None

# Search anywhere in string
m = re.search(r"\d+", "abc42def")
m.group()    # '42'
m.start()    # 3
m.span()     # (3, 5)

# Find all matches
re.findall(r"\d+", "1a2b3c")  # ['1', '2', '3']

# Substitute
re.sub(r"\s+", "_", "hello world")  # 'hello_world'

# Compile for reuse
pattern = re.compile(r"(?P<year>\d{4})-(?P<month>\d{2})-(?P<day>\d{2})")
m = pattern.search("Date: 2024-01-15")
m.group("year")   # '2024'

# Flags
re.findall(r"hello", text, re.IGNORECASE)

# Common patterns
r"\d"   # digit
r"\w"   # word char [a-zA-Z0-9_]
r"\s"   # whitespace
r"."    # any char except newline
r"^"    # start of string
r"$"    # end of string
r"+"    # 1 or more
r"*"    # 0 or more
r"?"    # 0 or 1
r"{n,m}"# n to m times
```

### `logging`

```python
import logging

# Basic config
logging.basicConfig(
    level=logging.DEBUG,
    format="%(asctime)s [%(levelname)s] %(name)s: %(message)s",
    datefmt="%Y-%m-%d %H:%M:%S",
)

logger = logging.getLogger(__name__)   # use __name__ for module logger

logger.debug("Debug detail")
logger.info("General info")
logger.warning("Warning")
logger.error("Error occurred")
logger.critical("Critical failure")
logger.exception("Error with traceback", exc_info=True)  # logs traceback

# Log levels: DEBUG(10) < INFO(20) < WARNING(30) < ERROR(40) < CRITICAL(50)
```

---

## 6. Virtual Environments

```bash
# Create
python -m venv .venv

# Activate
source .venv/bin/activate         # Linux/macOS
.venv\Scripts\activate            # Windows

# Deactivate
deactivate

# Install packages
pip install requests
pip install -r requirements.txt
pip freeze > requirements.txt

# Modern: uv (faster)
uv venv
uv pip install requests
```

---

## 7. Summary

> [!IMPORTANT]
> **Key Interview Points**:
> 1. `if __name__ == "__main__"` prevents code from running on import.
> 2. Modules are **cached** in `sys.modules` after first import — importing again is free.
> 3. `from package import *` imports names in `__all__` (or all non-`_` names if `__all__` not defined).
> 4. Use `pathlib.Path` over `os.path` — more readable, OOP-style.
> 5. `collections.deque` is O(1) for both ends; `list.pop(0)` is O(n).
> 6. `Counter` is a subclass of `dict`.
> 7. `defaultdict(list)` is the standard grouping pattern; `defaultdict(int)` for counting.
> 8. Always use `logging` instead of `print()` in production code.
