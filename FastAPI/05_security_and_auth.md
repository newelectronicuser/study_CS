# Security and Authentication

FastAPI provides an integrated security system. It supports OAuth2, JWT tokens, and basic HTTP authentication out of the box.

## 1. Authentication vs. Authorization
- **Authentication**: Verifying who the user is (e.g., login with username/password).
- **Authorization**: Verifying what the user can do (e.g., access certain routes, delete certain records).

## 2. OAuth2 with Password and Bearer
This is the most common way to secure FastAPI applications.
1. The user sends a username and password.
2. The server validates the credentials and returns an **Access Token** (JWT).
3. For subsequent requests, the client sends this token in the `Authorization: Bearer <TOKEN>` header.

## 3. JWT (JSON Web Tokens)
JWTs are used to share identity information between the server and the client in a stateless way.
- **Header**: Algorithm and token type.
- **Payload**: User information (ID, roles, expiration).
- **Signature**: Used to verify that the token hasn't been tampered with.

## 4. Password Hashing
Never store passwords in plain text. Use a library like `passlib` with `bcrypt`.
```python
from passlib.context import CryptContext

pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")

def get_password_hash(password):
    return pwd_context.hash(password)

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)
```

## 5. Security Dependencies
FastAPI uses `OAuth2PasswordBearer` to extract the token from the header.
```python
from fastapi.security import OAuth2PasswordBearer

oauth2_scheme = OAuth2PasswordBearer(tokenUrl="token")

@app.get("/users/me")
async def read_users_me(token: str = Depends(oauth2_scheme)):
    return {"token": token}
```

## 6. Access Scopes
OAuth2 Scopes allow you to grant specific permissions to a token (e.g., `read:items`, `write:items`). FastAPI has full built-in support for verifying these scopes.

> [!IMPORTANT]
> **HTTPS is Required**: In production, your API MUST be served over HTTPS. JWT tokens transmitted over plain HTTP can be easily intercepted (Man-in-the-middle attack).
