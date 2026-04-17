# Tools and Decorators

Tools (function calling) allow agents to interact with the world by calling Python functions. PydanticAI makes this seamless using decorators.

## 1. The `@agent.tool` Decorator
Any Python function can be turned into a tool that the LLM can call.

```python
@agent.tool
def get_weather(city: str) -> str:
    """Get the weather for a city."""
    return f"The weather in {city} is sunny."
```

## 2. Automatic Schema Generation
When you decorate a function with `@agent.tool`, PydanticAI:
- Inspects the **function name**.
- Reads the **Docstring** (This is what the LLM uses to know when to call the tool).
- Analyzes the **Type Hints** for arguments.
- Automatically generates the JSON Schema required by models like GPT-4 or Claude.

## 3. Tool Arguments
The LLM will provide the arguments based on the type hints you've defined.

```python
@agent.tool
def calculate_area(width: float, height: float) -> float:
    return width * height
```

## 4. System Tools vs. Agent Tools
- **Agent Tools**: Defined on a specific agent instance. Only that agent can use them.
- **System Tools (Toolkit)**: You can define a `Toolkit` and share it across multiple agents.

## 5. Handling Tool Errors
If a tool raises an exception, PydanticAI catches it and:
- Sends the error message back to the LLM.
- The LLM can then try to fix its mistake (e.g., provide a different argument) or admit defeat.

## 6. Model Interaction with Tools
Example of a multi-turn tool interaction:
1. **User**: "What's the weather in London?"
2. **LLM**: (Sees `get_weather` tool) "Call `get_weather(city='London')`"
3. **PydanticAI**: Executes the function and returns "The weather in London is sunny."
4. **LLM**: "It is currently sunny in London."

> [!IMPORTANT]
> **Docstrings are Prompts**: 
> The docstring of a tool is the ONLY way the LLM knows what the tool does. A vague docstring like `def do_it(x: int)` will lead to the LLM calling the tool incorrectly or not at all. Be descriptive.

> [!TIP]
> **Async Tools**: 
> PydanticAI fully supports `async def` tools. This is essential for tools that call external APIs or perform database I/O to prevent blocking the entire agent.
