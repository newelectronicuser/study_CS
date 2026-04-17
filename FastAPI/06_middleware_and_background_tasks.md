# Middleware and Background Tasks

## 1. Middleware
Middleware is a function that runs before every request and after every response.

### Common Use Cases
- **Logging**: Log processing time or request metadata.
- **CORS (Cross-Origin Resource Sharing)**: Allow requests from different domains.
- **Trusted Hosts**: Restrict the hosts that can access your API.
- **Session management**.

### Example: Adding CORS
```python
from fastapi.middleware.cors import CORSMiddleware

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)
```

## 2. Background Tasks
Background tasks are used to perform operations that don't need to block the response to the user.
- **Example**: Sending an email after registration, processing an image, or updating a database record.

```python
from fastapi import BackgroundTasks

def write_notification(email: str, message=""):
    with open("log.txt", mode="a") as log:
        log.write(f"notification to {email}: {message}\n")

@app.post("/send-notification/{email}")
async def send_notification(email: str, background_tasks: BackgroundTasks):
    background_tasks.add_task(write_notification, email, message="some notification")
    return {"message": "Notification sent in the background"}
```

## 3. Background Tasks vs. Celery
- **FastAPI BackgroundTasks**: Best for simple, short tasks that share the same resources as your app. Fast to set up.
- **Celery / Redis Queue**: Best for long-running, CPU-intensive, or distributed tasks. Requires a message broker (like Redis or RabbitMQ).

## 4. `app.on_event` (Startup and Shutdown)
FastAPI allows you to run code during the startup and shutdown of the application.
```python
@app.on_event("startup")
async def startup_event():
    # Connect to DB
    pass

@app.on_event("shutdown")
async def shutdown_event():
    # Disconnect from DB
    pass
```
*Note: Lifespan events are now preferred over `on_event`.*

> [!TIP]
> Use **Background Tasks** for any operation that takes more than a few milliseconds and doesn't affect the response content. This keeps your API snappy.
