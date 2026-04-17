# Request Handling in FastAPI

FastAPI makes it incredibly easy to extract and validate data from incoming requests.

## 1. Path Parameters
Variables declared in the path of the URL.
```python
@app.get("/items/{item_id}")
async def read_item(item_id: int):
    return {"item_id": item_id}
```

## 2. Query Parameters
Variables declared after the `?` in the URL.
```python
@app.get("/items/")
async def read_items(q: str = None, skip: int = 0, limit: int = 10):
    return {"q": q, "skip": skip, "limit": limit}
```

## 3. Request Body
Used for sending complex data, typically using Pydantic models.
```python
from pydantic import BaseModel

class Item(BaseModel):
    name: str
    price: float

@app.post("/items/")
async def create_item(item: Item):
    return item
```

## 4. Parameter Validation
FastAPI provides classes like `Path`, `Query`, and `Body` to add additional validation and metadata.
```python
from fastapi import Path, Query

@app.get("/items/{item_id}")
async def read_item(
    item_id: int = Path(..., title="The ID of the item", gt=0),
    q: str = Query(None, min_length=3, max_length=50)
):
    ...
```

## 5. Form Data and File Uploads
- **Form Data**: Use the `Form` class.
- **File Uploads**: Use `File` and `UploadFile`. `UploadFile` is preferred as it stores the file in memory only until a limit, then on disk, which is more memory-efficient for large files.

## 6. Request and Response objects
You can access the raw `Request` object and return a custom `Response` (or `JSONResponse`) if needed.

> [!IMPORTANT]
> **Path vs Query**:
> If a variable is declared in the path (e.g., `{item_id}`), FastAPI recognizes it as a **Path Parameter**. Otherwise, it's treated as a **Query Parameter**.
