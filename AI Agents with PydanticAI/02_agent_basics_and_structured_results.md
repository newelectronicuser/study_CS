# Agent Basics and Structured Results

The core of PydanticAI is defining an agent and ensuring its output is exactly what you expect.

## 1. Defining an Agent
An agent is an instance of the `Agent` class. You specify the model and the expected return type.

```python
from pydantic_ai import Agent

# A simple agent returns a plain string
agent = Agent('openai:gpt-4o', system_prompt="You are a helpful assistant.")

# Running the agent
result = agent.run_sync("What is PydanticAI?")
print(result.data)
```

## 2. Structured Results
This is where PydanticAI shines. You can force the LLM to return a specific Pydantic model.

```python
from pydantic import BaseModel
from pydantic_ai import Agent

class UserDetails(BaseModel):
    name: str
    age: int
    occupation: str

# Define an agent with a result type
agent = Agent(
    'openai:gpt-4o',
    result_type=UserDetails,
    system_prompt="Extract user details from the text."
)

result = agent.run_sync("My name is Alice, I am 30 years old and I work as an engineer.")
# result.data is now a validated UserDetails object
print(result.data.name) # Alice
```

## 3. How Structured Results Work
1.  **Schema Injection**: PydanticAI takes your `UserDetails` model and automatically generates a JSON Schema.
2.  **Prompting**: It tells the LLM: "Return your answer matching this JSON Schema."
3.  **Validation**: When the LLM responds, PydanticAI parses the JSON and validates it against the Pydantic model.
4.  **Error Handling**: If the LLM returns invalid JSON or missing fields, PydanticAI can enter a "retry loop" where it shows the error to the LLM and asks it to try again.

## 4. System Prompts
System prompts set the persona and rules for the agent. They can be static strings or dynamic functions.

```python
@agent.system_prompt
def get_system_prompt():
    return f"Today's date is {date.today()}."
```

## 5. Capturing Context
Every `run()` call returns a result object that includes:
- `data`: The validated result.
- `all_messages()`: The entire conversation history (useful for multi-turn chats).
- `usage`: Token usage statistics (prompt tokens, completion tokens).

> [!IMPORTANT]
> **Static Type Checking**: 
> Because `result_type` is specified, your IDE knows that `result.data` is an instance of `UserDetails`. This prevents "AttributeErrors" common when parsing raw JSON.

> [!TIP]
> **Union Types**: 
> You can use `Union` in `result_type` if the agent might return different types based on the input (e.g., a `SuccessResponse` or an `ErrorResponse`).
