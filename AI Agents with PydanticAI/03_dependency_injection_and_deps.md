# Dependency Injection and Deps

AI agents often need access to external systems like databases, API clients, or user-specific state. PydanticAI uses **Dependency Injection (DI)** to pass these cleanly.

## 1. What is `Deps`?
`Deps` (Dependencies) are objects that you pass to an agent's run method. These objects are then available to:
- System prompts
- Tool functions
- Result validators

## 2. Defining Dependencies
You define a class (usually a Pydantic model or a dataclass) to hold your dependencies.

```python
from dataclasses import dataclass
from pydantic_ai import Agent, RunContext

@dataclass
class MyDeps:
    api_key: str
    db_session: Any

agent = Agent('openai:gpt-4o', deps_type=MyDeps)
```

## 3. Using Dependencies in Tools
The `RunContext` object gives you access to the dependencies inside any tool.

```python
@agent.tool
def get_user_balance(ctx: RunContext[MyDeps], user_id: str) -> int:
    # Access the injected database session
    return ctx.deps.db_session.get_balance(user_id)
```

## 4. Running the Agent with Deps
When you call `run()`, you must provide the dependencies.

```python
deps = MyDeps(api_key="sk-...", db_session=session)
result = agent.run_sync("Check my balance", deps=deps)
```

## 5. Why use Dependency Injection?
- **Testability**: You can easily swap production dependencies (like a real database) with mocks in your unit tests.
- **Thread Safety**: Dependencies are scoped to the `run()` call, preventing state leakage between different users or threads.
- **Type Safety**: The `RunContext[MyDeps]` ensures that your tools know exactly what is in `ctx.deps`.

## 6. Global vs. Local State
- **Global**: Hardcoding a database connection in the file (Bad: hard to test, hard to scale).
- **Local (Deps)**: Passing the connection to the agent at runtime (Good: clean, testable, stateless).

> [!IMPORTANT]
> **Context Propagation**: 
> The `RunContext` also contains information about the current run, such as the `model` being used and the `retry` count, which can be useful for advanced tool logic.

> [!TIP]
> **Multiple Agents**: 
> You can pass the same `deps` object to multiple agents if they are working together as part of a larger workflow.
