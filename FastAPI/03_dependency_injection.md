# Dependency Injection in FastAPI

Dependency Injection (DI) is a way for your code to declare what it needs to run, and the framework takes care of providing those requirements.

## 1. Why use Dependency Injection?
- **Shared logic**: Reuse database connections or authentication logic across multiple endpoints.
- **Code Decoupling**: View functions don't need to know how to create the objects they use.
- **Testability**: Easily swap real databases for mock ones during testing.

## 2. Basic Usage
Use the `Depends()` function in the parameters of your route function.
```python
from fastapi import Depends

async def common_parameters(q: str = None, skip: int = 0, limit: int = 10):
    return {"q": q, "skip": skip, "limit": limit}

@app.get("/items/")
async def read_items(commons: dict = Depends(common_parameters)):
    return commons
```

## 3. Sub-dependencies
Dependencies can depend on other dependencies. FastAPI will resolve the whole graph automatically.

## 4. Dependencies with `yield`
Sometimes you need to perform a cleanup step after the request is finished (e.g., closing a database session). Use `yield` instead of `return`.
```python
async def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
```

## 5. Middleware vs Dependencies
- **Middleware**: Runs for *every* request. Good for logging, CORS, etc.
- **Dependencies**: Can be specific to certain routes or groups of routes. They can have access to the same parameters as your route functions (path, query, etc.).

## 6. Security with Dependencies
FastAPI's security system is entirely built on top of dependencies. You "inject" the current user into your routes.

> [!TIP]
> Use **Class-based dependencies** when you need to store state or configure the dependency when it's initialized.
