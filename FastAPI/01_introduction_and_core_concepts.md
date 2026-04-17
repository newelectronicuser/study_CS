# Introduction and Core Concepts

## 1. Why FastAPI?
FastAPI is a modern, high-performance web framework for building APIs with Python 3.7+ based on standard Python type hints.

- **Fast**: Very high performance, on par with **NodeJS** and **Go** (thanks to Starlette and Pydantic).
- **Fast to code**: Increase the speed to develop features by about 200% to 300%.
- **Fewer bugs**: Reduce about 40% of human (developer) induced errors.
- **Intuitive**: Great editor support. Completion everywhere. Less time debugging.
- **Automatic Docs**: Interactive API documentation (Swagger UI and ReDoc).

## 2. The Core Stack
FastAPI stands on the shoulders of two giants:
- **Starlette**: For the web parts (routing, requests, websockets).
- **Pydantic**: For the data parts (validation, serialization).

## 3. Python Type Hints
FastAPI uses Python type hints extensively to define the shape of your data.
```python
def get_user(user_id: int):
    return {"user_id": user_id}
```
Because of these hints, FastAPI provides:
- **Validation**: If a string is passed as `user_id`, an error is automatically returned.
- **Serialization**: Converts Python types to JSON.
- **Auto-documentation**: Swagger knows `user_id` is an integer.

## 4. Async and Await
FastAPI supports asynchronous programming natively.
- **`async def`**: Used when you have I/O bound operations (DB queries, API calls).
- **Why?**: It allows the server to handle many concurrent requests on a single thread by "pausing" the execution of a request while waiting for I/O, allowing other requests to use the CPU.

## 5. ASGI vs WSGI
- **WSGI (Web Server Gateway Interface)**: Replaced by ASGI for modern async applications. Designed for synchronous communication.
- **ASGI (Asynchronous Server Gateway Interface)**: Supports async, websockets, and HTTP/2. FastAPI is an ASGI framework.

> [!TIP]
> If your code doesn't use `await` inside the function, you don't *have* to use `async def`. FastAPI will run regular `def` functions in a separate threadpool to avoid blocking the main event loop.
