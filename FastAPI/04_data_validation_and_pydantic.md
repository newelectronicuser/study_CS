# Data Validation and Pydantic

Pydantic is the engine that handles all data validation and parsing in FastAPI.

## 1. Pydantic Models
Models are classes that inherit from `pydantic.BaseModel`. They define the shape and types of your data.
```python
from pydantic import BaseModel

class User(BaseModel):
    id: int
    username: str
    email: str | None = None  # Optional field
```

## 2. Pydantic vs. Dictionary
- **Dictionary**: No type safety, no validation, hard to document.
- **Pydantic Model**: Enforces types, provides IntelliSense/auto-completion, and handles coercion (converting string "123" to int 123).

## 3. Data Serialization
FastAPI uses Pydantic to convert Python objects to JSON.
- **`response_model`**: Defined in the route decorator. It ensures that the output returned by the function matches the specified model (filtering out fields you don't want to expose, like passwords).

## 4. Custom Validators
You can add custom validation logic to your models using the `@validator` decorator.
```python
from pydantic import validator

class User(BaseModel):
    password: str

    @validator("password")
    def password_must_contain_digit(cls, v):
        if not any(char.isdigit() for char in v):
            raise ValueError("Password must contain at least one digit")
        return v
```

## 5. Pydantic Settings
`pydantic.BaseSettings` is an amazing tool for managing application configuration from environment variables.
```python
from pydantic import BaseSettings

class Settings(BaseSettings):
    app_name: str = "My FastAPI App"
    admin_email: str
    items_per_user: int = 50

    class Config:
        env_file = ".env"
```

## 6. ORM Compatibility
Pydantic models can be populated from ORM models (like SQLAlchemy) by setting `orm_mode = True` in the model's `Config` class.

> [!IMPORTANT]
> **Pydantic V1 vs V2**:
> Pydantic V2 (released in 2023) is significantly faster (written in Rust) and has some syntax changes (e.g., `@validator` is replaced by `@field_validator`). Ensure you know which version you are using.
