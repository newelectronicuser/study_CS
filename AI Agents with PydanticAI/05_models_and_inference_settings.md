# Models and Inference Settings

PydanticAI is model-agnostic, supporting major LLM providers through a unified interface while allowing fine-grained control over inference.

## 1. Supported Model Providers
You can specify models using a simple string format:
- **OpenAI**: `openai:gpt-4o`, `openai:gpt-3.5-turbo`
- **Anthropic**: `anthropic:claude-3-5-sonnet-latest`
- **Gemini**: `google-gla:gemini-1.5-pro`
- **Groq**: `groq:llama-3-70b-versatile`
- **Ollama**: Local models via `ollama:llama3`

## 2. Model Instances
For more control (like setting API keys manually), you can use model classes.

```python
from pydantic_ai.models.openai import OpenAIModel

model = OpenAIModel('gpt-4o', api_key='my-key')
agent = Agent(model)
```

## 3. Inference Settings
You can pass settings to the `run()` call to control the LLM's behavior.

```python
from pydantic_ai.models import ModelSettings

settings = ModelSettings(
    temperature=0.7,
    max_tokens=1000,
    top_p=0.9
)

result = agent.run_sync("Write a poem", model_settings=settings)
```

## 4. Streaming Results
For a better user experience, especially with long responses, you can stream the result.

```python
async with agent.run_stream("Tell me a long story") as result:
    async for message in result.stream_text():
        print(message)
```

## 5. Structured Streaming
PydanticAI even supports streaming **structured data**. It will provide partial Pydantic models as they are being built from the LLM tokens.

```python
async with agent.run_stream("Extract menu items", result_type=Menu) as result:
    async for menu_item in result.stream_structured():
        # menu_item is a partially populated Pydantic model
        print(menu_item)
```

## 6. Token Usage and Cost
The result object tracks token usage precisely.
- `result.usage().request_tokens`
- `result.usage().response_tokens`
- `result.usage().total_tokens`

> [!IMPORTANT]
> **Environment Variables**: 
> Most providers are automatically configured if you have the standard environment variables set (e.g., `OPENAI_API_KEY`, `ANTHROPIC_API_KEY`).

> [!TIP]
> **Fallback Models**: 
> In production, you can implement a wrapper that switches to a different model (e.g., from GPT-4 to Claude) if the primary one returns a specialized error or is rate-limited.
