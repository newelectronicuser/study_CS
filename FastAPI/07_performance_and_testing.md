# Performance and Testing

## 1. Testing with `TestClient`
FastAPI provides a `TestClient` based on `httpx` and `Starlette`. It allows you to test your application without running the server.

```python
from fastapi.testclient import TestClient
from .main import app

client = TestClient(app)

def test_read_main():
    response = client.get("/")
    assert response.status_code == 200
    assert response.json() == {"msg": "Hello World"}
```

### Why use `TestClient`?
- **Speed**: No overhead of starting a network server.
- **Consistency**: Use standard `assert` statements and `pytest`.
- **Easy dependency injection**: Use `app.dependency_overrides` to swap real DBs for test DBs.

## 2. ASGI Servers: Uvicorn and Gunicorn
- **Uvicorn**: A lightning-fast ASGI server implementation. It runs on a single process.
- **Gunicorn**: A WSGI HTTP Server. In production, we typically use Gunicorn with Uvicorn workers to gain multiprocessing capability.

```bash
gunicorn -w 4 -k uvicorn.workers.UvicornWorker main:app
```

## 3. Performance Optimization
- **Use `async def` correctly**: Ensure you are using `await` for all I/O operations.
- **Minimize CPU-bound tasks**: Offload heavy computations to background tasks or Celery.
- **Use `response_model`**: It ensures efficient serialization and filtering.
- **Database connection pooling**: Don't open/close connections for every request.

## 4. Concurrency vs. Parallelism
- **Concurrency**: Handled by the Event Loop (`async`/`await`). One process handling many requests.
- **Parallelism**: Handled by multiple worker processes (using Gunicorn).

## 5. Deployment Best Practices
- **Environment variables**: Use Pydantic's `BaseSettings`.
- **Dockerization**: Keep your environment consistent.
- **Monitoring**: Use tools like Prometheus or Jaeger for tracing.

> [!IMPORTANT]
> **Async is not a silver bullet**. While it handles I/O bound tasks efficiently, it can actually be *slower* for CPU-bound tasks because of the overhead of the event loop. Always benchmark your specific use case.
